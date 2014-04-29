package cornell.trickleapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DrinkCounter extends Activity {

	AlarmManager alarmManager;
	private static FlyOutContainer root;
	private int drink_count = 0;
	private DatabaseHandler db;
	
	private double bac;
	
	private Vibrator click_vibe;
	
	private int face_color;
	private int face_icon;
	private boolean clicked;
	
	private Date start_date;

	private ArrayList<Date> drinkDates;
	private ArrayList<DatabaseStore> startDates;
	
	private final Double CALORIES_PER_DRINK = 120.0;
	private final Double CALORIES_HOT_DOG = 250.0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.drink_tracker, null);

		this.setContentView(root);
		
		click_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		clicked=false;
		db = new DatabaseHandler(this);
		
		start();
		updateFace();

		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		start();
		recalculateBac();
		updateFace();
		clicked = false;
	}
	
	@Override
	protected void onStop() {
		db.close();
		super.onStop();
		finish();
	}


	private void start() {
		Date date = new Date();
		Date delayedDate = DatabaseStore.getDelayedDate();
		
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesDelay("drink_count", date);
		
		if(drink_count_vals != null){
			drink_count_vals = DatabaseStore.sortByTime(drink_count_vals);
			
			//Get the stored StartDates
			startDates = (ArrayList<DatabaseStore>)db.getVarValuesDelay("start_date", date);
			
			if(startDates!= null){
				startDates = DatabaseStore.sortByTime(startDates);
				start_date = DatabaseStore.retrieveDate(startDates.get(startDates.size()-1).value);
				if(start_date == null){
					start_date = delayedDate;
				}
			}else{
				start_date = delayedDate;
				db.addValueAtDate("start_date", start_date, start_date);
			}
			
			drink_count = Integer.parseInt(drink_count_vals.get(
					drink_count_vals.size()-1).value);
			recalculateBac();
		}else{
			//Check to see if residual BAC value from day Prior
			Date yesterday = DatabaseStore.getYesterday();
			
			ArrayList<DatabaseStore> yesterday_drink_count = (ArrayList<DatabaseStore>) db
					.getVarValuesDelay("drink_count", yesterday);
			
			if(yesterday_drink_count != null){
				yesterday_drink_count = DatabaseStore.sortByTime(yesterday_drink_count);
			
				//Get the stored StartDates for yesterday
				startDates = (ArrayList<DatabaseStore>)db.getVarValuesDelay("start_date", yesterday);
			
				if(startDates!= null){
					startDates = DatabaseStore.sortByTime(startDates);
					start_date = DatabaseStore.retrieveDate(startDates.get(startDates.size()-1).value);

					if(start_date != null){
						drink_count = Integer.parseInt(yesterday_drink_count.get(
							yesterday_drink_count.size()-1).value);
						double currentBAC = calculateBac(start_date, delayedDate, drink_count);

						if (currentBAC > 0){	
							//Add the start value to the db.
							db.addValueAtDate("start_date", start_date, delayedDate);
						
							db.addDelayValue("drink_count", drink_count);
							db.addDelayValue("bac", String.valueOf(currentBAC));
							updateFace();
							db.addDelayValue("bac_color", String.valueOf(face_color));
						}
					}else{
						//No remaining values from previous day - start fresh
						start_date = null;
						drink_count = 0;
						bac = 0;
					}
				}
			}else{
				//No remaining values from previous day - start fresh
				start_date = null;
				drink_count = 0;
				bac = 0;
			}
		}
	}

	
	public void removeLast() {
		drink_count--;
		Date date = new Date();
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesDelay("drink_count", date);

		drink_count_vals = DatabaseStore.sortByTime(drink_count_vals);

		ArrayList<String> variables = new ArrayList<String>();
		variables.add(drink_count_vals.get(
				drink_count_vals.size() - 1).variable);
		variables.add("bac");
		variables.add("bac_color");
		variables.add("hotdog");
		
		ArrayList<DatabaseStore> start_values = (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"start_date", date);
		if(start_values != null){
			start_values = DatabaseStore.sortByTime(start_values);
			Date date_val = drink_count_vals.get(drink_count_vals.size()-1).date;
			DatabaseStore ds = start_values.get(start_values.size()-1);
			Date last_start = DatabaseStore.retrieveDate(ds.value);
			if (date_val.equals(last_start)){
				db.deleteValue(ds);
				if(start_values.size() > 1){
					start_date = DatabaseStore.retrieveDate(start_values.get(start_values.size()-2).value);
				}else{
					start_date = null;
				}
			}
		}
		
		ArrayList<DatabaseStore> hd_val = ((ArrayList<DatabaseStore>)db
				.getVarValuesForDay("hotdog",date));
		if(hd_val!=null){
			if(hd_val.size() ==1){
					if(Integer.valueOf(hd_val.get(0).value) >0){
						db.updateOrAdd("hotdog", 
								Integer.valueOf(hd_val.get(0).value) - 1);
					}
			}
		}
		
		if (drink_count_vals.size() == 1) {
			ArrayList<String> vals = new ArrayList<String>();
			vals.add("drank_last_night");
			vals.add("tracked");
			db.deleteValuesTomorrow(vals);

			variables.add("drank");
		}

		db.deleteVaribles(variables,
				drink_count_vals.get(drink_count_vals.size() - 1));
		
		recalculateBac();
		updateFace();
		Toast.makeText(getApplicationContext(),
				"Your last drink has been removed", Toast.LENGTH_SHORT).show();
	}

	public static int getBacColor(double bac_value) {
		if (bac_value < 0.06) {
			return Color.rgb(112,191, 65);
		} else if (bac_value < 0.15) {
			return Color.rgb(245, 211,40);
		} else if (bac_value < 0.24) {
			return Color.rgb(236, 93, 87);
		} else {
			return Color.DKGRAY;
		}
	}
	
	public void setFaceIcon(double bac_value){
		if (bac_value < 0.06) {
			face_icon = R.drawable.ic_tracker_smile;
		} else if (bac_value < 0.15) {
			face_icon = R.drawable.ic_tracker_neutral;
		} else if (bac_value < 0.24) {
			face_icon = R.drawable.ic_tracker_frown;
		} else {
			face_icon = R.drawable.ic_tracker_dead;
		}
	}

	private void updateFace(){
		setContentView(R.layout.drink_tracker);
		face_color = getBacColor(bac);
		setFaceIcon(bac);
		
		
		ImageView face = (ImageView)findViewById(R.id.drink_smile);
		
		//Update the face color
		((GradientDrawable)((LayerDrawable) face.getDrawable()).getDrawable(0)
				).setColor(face_color);	
		
		//Update the face icon
		Drawable to_replace = getResources().getDrawable(face_icon);	
		((LayerDrawable) face.getDrawable()).setDrawableByLayerId(
				R.id.face_icon, to_replace);
		face.invalidate();
		face.refreshDrawableState();
		
	}

	private void recalculateBac(){
		Date date = DatabaseStore.getDelayedDate();
		bac = calculateBac(start_date, date, drink_count);
	}
	
	//Should NOT save anything to DB in this function!
	private double calculateBac(Date start, Date end, int number_drinks) {
		
		if(number_drinks <= 0){
			return 0.0;
		}
		
		// get the users gender
		ArrayList<DatabaseStore> stored_gender = (ArrayList<DatabaseStore>) db
				.getAllVarValue("gender");
		// If user did not set gender use "Female" as default
		String gender = "Female";
		if (stored_gender != null) {
			gender = stored_gender.get(0).value;
		}

		// fetch the users weight
		ArrayList<DatabaseStore> stored_weight = (ArrayList<DatabaseStore>) db
				.getAllVarValue("weight");
		Integer weight_lbs = 120;
		if (stored_weight != null) {
			weight_lbs = Integer.parseInt(stored_weight.get(0).value);
		}

		double metabolism_constant = 0;
		double gender_constant = 0;
		double weight_kilograms = weight_lbs * 0.453592;

		if (gender.equals("Male")) {
			metabolism_constant = 0.015;
			gender_constant = 0.58;
		} else {
			metabolism_constant = 0.017;
			gender_constant = 0.49;
		}
		
		//getTime returns in milliseconds. Divide by 1000 to convert to seconds, 60 to convert
				// to minutes and 60 to convert to hours.
		long time_elapsed  =  (end.getTime()-start.getTime()) / (1000 * 60 * 60);
		double bac_update = ((0.806 * number_drinks * 1.2) / (gender_constant * weight_kilograms))
					- (metabolism_constant * time_elapsed);
		return bac_update;
	}

	@SuppressLint("NewApi")
	public void hadDrink(View view) {
		clicked=true;
		Date delayedDate = DatabaseStore.getDelayedDate();
		
		//First drink of the session, add to db
		if(start_date == null){
			start_date = delayedDate;
			db.addDelayValue("start_date", start_date);
			
		}
		
		if(drink_count > 0){
			//Get last BAC at current Time
			double lastBAC = calculateBac(start_date, delayedDate, drink_count);
			
			//If BAC is <=0 then it is a new 'drinking session' so reset values
			if(lastBAC <= 0){
				drink_count = 0;
				start_date = delayedDate;
				db.addValueAtDate("start_date", start_date, start_date);
			}
		}
		
		drink_count++;
		if (drink_count == 1 ) {
			db.addValueTomorrow("drank_last_night", "True");
			db.updateOrAdd("drank", "True");
		}
		
		//Add the drink_count to the DB
		db.addDelayValue("drink_count", drink_count);
		
		recalculateBac();

		updateFace();

		//Add BAC value and BAC color to the database
		db.addDelayValue("bac", String.valueOf(bac));
		db.addDelayValue("bac_color", String.valueOf(face_color));
		
		// calculate number of hot dogs that equate the number of calories
		Double drink_cals = drink_count * CALORIES_PER_DRINK;

		int number_hot_dogs = (int) Math.ceil(drink_cals/ CALORIES_HOT_DOG);
		db.updateOrAdd("hot_dogs", number_hot_dogs);
	}
	
	private void injectDrink(int minutesDelay){
		Date delayedDate = DatabaseStore.getDelayedDate();
		
		//First drink of the session, add to db
		if(start_date == null){
			start_date = delayedDate;
			db.addDelayValue("start_date", start_date);			
		}
		
		//drink_count++;
		Date date = new Date();
		
		//get the existing values from the DB
		ArrayList<DatabaseStore> counts_recent = (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"drink_count", date);
		ArrayList<DatabaseStore>  bac_recent = (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"bac", date);
		ArrayList<DatabaseStore> colors_recent =  (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"bac_color", date);
		ArrayList<DatabaseStore> start_values = (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"start_date", date);
		
		
		
		//Construct the time where we want to inject 
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.HOUR_OF_DAY, -6);
		gc.add(Calendar.MINUTE, minutesDelay*-1);
		date = gc.getTime();
		
		int value_inject = 0;
		double bac_inject =0.0;
		int color_inject = 0;
		
		if (start_values != null){
			start_values = DatabaseStore.sortByTime(start_values);
			ArrayList<Integer> afterIndexes = setStartDate(start_values, date);
			//Check to see if we are inserting before the current startDate
			if(afterIndexes.contains(0)){
				start_date = date;
				db.injectDelayValueDate("start_date", date, minutesDelay);
			}
			
			//Delete all startValues after the insert Date
			for(int i=0; i<afterIndexes.size(); i++){
				db.deleteValue(start_values.get(afterIndexes.get(i)));
			}
		}else{
			start_date = date;
			db.injectDelayValueDate("start_date", date, minutesDelay);
		}
		
		int last_value = -1;
		if(counts_recent != null){
			
			//Sort all values by date
			counts_recent = DatabaseStore.sortByTime(counts_recent);
			bac_recent = DatabaseStore.sortByTime(bac_recent);
			colors_recent = DatabaseStore.sortByTime(colors_recent);
			
			assert(bac_recent.size()== counts_recent.size());
			assert(colors_recent.size() == counts_recent.size());
			
			boolean placed = false;
			
			Iterator<DatabaseStore> iterator = counts_recent.iterator();
			Date start_date = counts_recent.get(0).date;
			int index_val = 0;
			DatabaseStore last = null;
			
			
			double last_count=0;
			while (iterator.hasNext()){
				
				DatabaseStore ds = iterator.next();
				if(ds.date.after(date)){
					if (!placed){
						value_inject = Integer.parseInt(ds.value);
						int recent_drink_count = Integer.parseInt(ds.value);
						if(recent_drink_count > 1){
							double test_bac = calculateBac(start_date, date, Integer.parseInt(
								ds.value)-1);
							if(test_bac <=0){
								start_date = date; 
								db.injectValueWithDate("start_date", start_date, date);
								bac_inject = calculateBac(start_date, date, 1);
							}else{
								bac_inject = calculateBac(start_date, date, Integer.parseInt(
										ds.value)-1);
							}
							
						}else if(last!=null){
							double test_bac = calculateBac(start_date, date, Integer.parseInt(
									last.value));
							if(test_bac <=0){
								start_date = date; 
								db.injectValueWithDate("start_date", start_date, date);
								bac_inject = calculateBac(start_date, date, 1);
							}else{
								calculateBac(start_date, date, Integer.parseInt(
										last.value)+1);
							}
						}else{
							bac_inject = calculateBac(start_date, date, Integer.parseInt(
									ds.value));
						}
						color_inject = getBacColor(bac_inject);
						placed=true;
						
					}
					
					if (last_value == -1){
						last_value = Integer.parseInt(ds.value);
					}
					//check to see if BAC <=0 to indicate new 'session'
					double test_bac = calculateBac(start_date, ds.date, last_value);
					double bac_new = 0.0;
					if(test_bac <=0){
						start_date = ds.date; 
						db.injectValueWithDate("start_date", start_date, ds.date);
						bac_new = calculateBac(start_date, ds.date, 1);
						last_value = 1;
					}else{
						bac_new = calculateBac(start_date, ds.date, last_value + 1 );
						last_value += 1;
					}
					
					//update drink count
					ds.value = String.valueOf(last_value);
					db.updateQuestion(ds);
					
					//update bac
					DatabaseStore d = bac_recent.get(index_val);
					d.value = String.valueOf(bac_new);
					db.updateQuestion(d);
					//update Bac Color
					int new_bac_color = getBacColor(bac_new);
					d = colors_recent.get(index_val);
					d.value =String.valueOf(new_bac_color);
					db.updateQuestion(d);
				}
				last = ds;
				index_val += 1;
			}
			
		}else{
			value_inject = 1;
			start_date = date;
			bac_inject = calculateBac(start_date, date, 1);
			db.injectValueWithDate("start_date", start_date, date);
			color_inject = getBacColor(bac_inject);
			last_value = 1;
		}
	
		//Add the injected question
		DatabaseStore data_store = DatabaseStore.DatabaseIntegerStore("drink_count",
				String.valueOf(value_inject), date);
		db.addQuestion(data_store);
		data_store = DatabaseStore.DatabaseIntegerStore("bac",
				String.valueOf(bac_inject), date);
		db.addQuestion(data_store);
		data_store = DatabaseStore.DatabaseIntegerStore("bac_color",
				String.valueOf(color_inject), date);
		db.addQuestion(data_store);
		drink_count = last_value;
		recalculateBac();
		updateFace();
	}
	
	/**
	 *  Sets the start date for an inject operation and returns the index integers of all 
	 *  start dates that are after the inject date. These values should be checked to 
	 *  see if they need to be updated elsewhere
	 * 
	 * @param start_dates
	 * 		A list of DatabaseStore objects containing the start_dates for the inject
	 * @param itemDate
	 * 		The date at which we are trying to inject the drink value
	 */
	private ArrayList<Integer> setStartDate(ArrayList<DatabaseStore> start_dates, Date itemDate){
		
		ArrayList<Integer> afterIndexes = new ArrayList<Integer>();
		
		for(int i=0; i<start_dates.size(); i++){
			Date startVal = DatabaseStore.retrieveDate(start_dates.get(i).value);
			if(itemDate.after(startVal)){
				start_date = startVal;
			}else{
				afterIndexes.add(i);
			}
		}
		return afterIndexes;
	}
	
	
	//adds a drink and also sets an alarm for morning after-survey 12 hours after
	public void addDrinkHandler(View view){
		click_vibe.vibrate(75);
		Toast t = Toast.makeText(getApplicationContext(), 
				"Adding a drink." , Toast.LENGTH_SHORT);
		t.setGravity(Gravity.TOP, 0, 100);
		t.show();
		updateFace();
		hadDrink(view);
		setAlarm(43200000L,666);//666=unique notification id for morning after survey
	}
	
	public void removeLastHandler(View view){
		click_vibe.vibrate(20);
		if (clicked == true){
			new AlertDialog.Builder(this)
				.setTitle("Remove Last Drink")
				.setMessage("Are you sure you would like to remove your last recorded drink?")
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clicked=false;
						removeLast();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();	
		}else{
			new AlertDialog.Builder(this)
				.setTitle("Remove Last Drink")
				.setMessage("Your last drink was already removed or you did not record any drinks recently.")
				.setPositiveButton(android.R.string.yes, null).show();
		}
	}
	

	
	public void injectDrinkHandler(View view){
		click_vibe.vibrate(20);
		
		final String options[] = new String[] {"3 Hours Ago", "2.5 Hours Ago", 
				"2 Hours Ago", "1.5 Hours Ago", "1 Hour Ago", "45 Minutes Ago",
				"30 Minutes Ago", "15 Minutes Ago"};
		final int values[] = new int[] {180, 150, 120, 90, 60, 45, 30, 15};
		final Context context = this;
		new AlertDialog.Builder(this)
		.setTitle("Record a Drink I had: ")
		.setItems(options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final int v = values[which];
	
				new AlertDialog.Builder(context)
				.setTitle("Add Drink in Past")
				.setMessage("Are you sure you would like to record a drink you had "+options[which] +"?")
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						injectDrink(v);
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();	
				
				
				
			}
		})
		.setNegativeButton(android.R.string.no, null).show();
	}
	
	public void trackMoney(View view){
		click_vibe.vibrate(20);
		
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		new AlertDialog.Builder(this).setTitle("Enter Amount you spent on Alcohol").setView(
				input).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						double value = Double.parseDouble(input.getText().toString());
						DecimalFormat formatter = new DecimalFormat("#.##");
						db.addDelayValue("money", formatter.format(value));
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
		
		
	}
	public void toggleMenu(View v) {
		this.root.toggleMenu();
	}
	private void setAlarm(long miliSec, int id) {

		Intent intent = new Intent(this, ReminderAlarm.class);
		intent.putExtra("id", id);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id,
				intent, 0);

		Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeValue));

		// calendar.setTimeInMillis(calendar.getTimeInMillis() + 86400000);

		long todayMili = calendar.getTimeInMillis();

		long miliForAlarm = todayMili + miliSec;

		alarmManager.cancel(pendingIntent);
		alarmManager.set(AlarmManager.RTC_WAKEUP, miliForAlarm, pendingIntent);
	}
}
