package cornell.trickleapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
	private static FlyOutContainer root;
	private int drink_count = 0;
	private DatabaseHandler db;
	
	private double bac;
	
	private Vibrator click_vibe;
	
	private int face_color;
	private int face_icon;
	private boolean clicked;
	
	private Date start_date;

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
		
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		
		if (checkSurveyed) {
			Intent openTutorial = new Intent(this, DrinkCounterTutorial.class);
			startActivity(openTutorial);
		}
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


	private void start(){
		Date date = DatabaseStore.getDelayedDate();
		
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesDelay("drink_count", date);
		
		if(drink_count_vals != null){
			drink_count_vals = DatabaseStore.sortByTime(drink_count_vals);
			start_date =  drink_count_vals.get(0).date;
			drink_count = Integer.parseInt(drink_count_vals.get(
					drink_count_vals.size()-1).value);
		}else{
			start_date = date;
			bac = calculateBac(start_date, date, drink_count);
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
	
	private double calculateBac(Date start, Date end, int number_drinks) {
		
		if(number_drinks <= 0){
			return 0.0;
		}
		if(start == null){
			start();
		}
		
		long time_elapsed  =  (end.getTime()-start.getTime()) / (1000 * 60 * 60);
		
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
		double bac_update = ((0.806 * number_drinks * 1.2) / (gender_constant * weight_kilograms))
					- (metabolism_constant * time_elapsed);
		return bac_update;
	}

	@SuppressLint("NewApi")
	public void hadDrink(View view) {
		clicked=true;
		drink_count++;
		if (drink_count == 1) {
			db.addValueTomorrow("drank_last_night", "True");
			db.updateOrAdd("drank", "True");
		}
		
		db.addDelayValue("drink_count", drink_count);
		recalculateBac();
		start();
		updateFace();
		db.addDelayValue("bac", String.valueOf(bac));
		db.addDelayValue("bac_color", String.valueOf(face_color));
		
		// calculate number of hot dogs that equate the number of calories
		Double drink_cals = drink_count * CALORIES_PER_DRINK;

		int number_hot_dogs = (int) Math.ceil(drink_cals/ CALORIES_HOT_DOG);
		db.updateOrAdd("hot_dogs", number_hot_dogs);
	}
	
	private void injectDrink(int minutesDelay){
		drink_count++;
		Date date = new Date();
		
		//get the existing values from the DB
		ArrayList<DatabaseStore> counts_recent = (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"drink_count", date);
		ArrayList<DatabaseStore>  bac_recent = (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"bac", date);
		ArrayList<DatabaseStore> colors_recent =  (ArrayList<DatabaseStore>)db.getVarValuesDelay(
				"bac_color", date);
		
		
		//Construct the time where we want to inject 
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.HOUR_OF_DAY, -6);
		gc.add(Calendar.MINUTE, minutesDelay*-1);
		date = gc.getTime();
		
		int value_inject = 0;
		double bac_inject =0.0;
		int color_inject = 0;
		
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
			while (iterator.hasNext()){
				
				DatabaseStore ds = iterator.next();
				if(ds.date.after(date)){
					if (!placed){
						value_inject = Integer.parseInt(ds.value);
						bac_inject = calculateBac(start_date, date, Integer.parseInt(
								ds.value));
						color_inject = getBacColor(bac_inject);
						placed=true;
					}
					//update drink count
					Integer new_count_val = Integer.parseInt(ds.value) + 1;
					ds.value = new_count_val.toString();
					db.updateQuestion(ds);
					//update bac
					double new_bac = calculateBac(start_date, ds.date, new_count_val);
					DatabaseStore d = bac_recent.get(index_val);
					d.value = String.valueOf(new_bac);
					db.updateQuestion(d);
					//update Bac Color
					int new_bac_color = getBacColor(new_bac);
					d = colors_recent.get(index_val);
					d.value =String.valueOf(new_bac_color);
					db.updateQuestion(d);
				}
				index_val += 1;
			}
			
		}else{
			value_inject = 1;
			bac_inject = calculateBac(start_date, date, 1);
			color_inject = getBacColor(bac_inject);
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
		
		recalculateBac();
		updateFace();
	}
	
	
	public void addDrinkHandler(View view){
		click_vibe.vibrate(75);
		Toast t = Toast.makeText(getApplicationContext(), 
				"Adding a drink." , Toast.LENGTH_SHORT);
		t.setGravity(Gravity.TOP, 0, 100);
		t.show();
		updateFace();
		hadDrink(view);
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
}
