package cornell.eickleapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class DrinkCounter extends Activity {
	private int drink_count = 0;
	private DatabaseHandler db;
	
	private double hours;
	private double bac;
	
	private Vibrator click_vibe;
	
	private int face_color;
	private int face_icon;
	private boolean clicked;

	private final Double CALORIES_PER_DRINK = 120.0;
	private final Double CALORIES_HOT_DOG = 250.0;
	private final int START_COLOR = Color.rgb(112,191, 65);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		click_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		setContentView(R.layout.drink_tracker);
		clicked=false;
		db = new DatabaseHandler(this);
		calculateBac();
		calculateColor();
		
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		
		if (checkSurveyed) {
			Intent openTutorial = new Intent(this, DrinkCounterTutorial.class);
			startActivity(openTutorial);
		}
	}

	
	private void updateFace(){
		ImageView face = (ImageView)findViewById(R.id.drink_smile);
		
		//Update the face color
		((GradientDrawable)((LayerDrawable) face.getDrawable()).getDrawable(0)
				).setColor(face_color);	
		
		//Update the face icon
		((LayerDrawable) face.getDrawable()).setDrawableByLayerId(
				R.id.face_icon, getResources().getDrawable(face_icon));
		face.invalidate();	
	}
		
	
	@Override
	protected void onResume() {
		super.onResume();
		calculateBac();
		calculateColor();
		updateFace();
		clicked=false;
	}


	private void calculateHours() {
		Date date = new Date();
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesDelay("drink_count", date);

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.HOUR_OF_DAY, -6);
		date = gc.getTime();
		DatabaseStore current = new DatabaseStore("", "", date, "Integer");

		face_color = START_COLOR;
		
		if (drink_count_vals != null) {
			drink_count = drink_count_vals.size();
			drink_count_vals = DatabaseStore.sortByTime(drink_count_vals);
 			
			// calculate the hours drinking
			if (drink_count_vals.size() > 0) {
				DatabaseStore start = drink_count_vals.get(0);
				Integer start_time = start.hour * 60 + start.minute;
				Integer last_time = current.hour * 60 + current.minute;
				hours = (last_time - start_time) / 60.0;
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
		calculateBac();
		calculateColor();

		updateFace();
		Toast.makeText(getApplicationContext(),
				"Your last drink has been removed", Toast.LENGTH_SHORT).show();
	}

	public void calculateColor() {
		if (bac < 0.06) {
			face_color = START_COLOR;
			face_icon = R.drawable.ic_tracker_smile;
		} else if (bac < 0.15) {
			face_color = Color.rgb(245, 211,40);
			face_icon = R.drawable.ic_tracker_neutral;
		} else if (bac < 0.24) {
			face_color = Color.rgb(236, 93, 87);
			face_icon = R.drawable.ic_tracker_frown;
		} else {
			face_color = Color.DKGRAY;
			face_icon = R.drawable.ic_tracker_dead;
		}
	}

	private void calculateBac() {
		Date date = new Date();
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesDelay("drink_count", date);
		if (drink_count_vals != null) {
			calculateHours();

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

			bac = ((0.806 * drink_count * 1.2) / (gender_constant * weight_kilograms))
					- (metabolism_constant * hours);
		} else {
			bac = 0;
		}
	}

	@SuppressLint("NewApi")
	public void hadDrink(View view) {
		clicked=true;
		drink_count++;
		if (drink_count == 1) {
			db.addValueTomorrow("drank_last_night", "True");
			db.addValueTomorrow("tracked", "True");
			db.updateOrAdd("drank", "True");
		}
		db.addDelayValue("drink_count", drink_count);
		calculateBac();
		db.addDelayValue("bac", String.valueOf(bac));
		calculateColor();
		db.addDelayValue("bac_color", String.valueOf(face_color));
		updateFace();

		// calculate number of hot dogs that equate the number of calories
		Double drink_cals = drink_count * CALORIES_PER_DRINK;

		int number_hot_dogs = (int) Math.ceil(drink_cals/ CALORIES_HOT_DOG);
		db.updateOrAdd("hot_dogs", number_hot_dogs);
	}
	
	public void addDrinkHandler(View view){
		click_vibe.vibrate(75);
		Toast t = Toast.makeText(getApplicationContext(), 
				"Adding a drink. Count=" + drink_count, Toast.LENGTH_SHORT);
		t.show();
		
		face_icon = R.drawable.ic_tracker_dead;
		face_color = Color.GRAY;
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
		/*
		final Map<String,Integer> options = new HashMap<String, Integer>();
		options.put("2.5 Hours Ago", 150);
		options.put("2 Hours Ago", 120);
		options.put("1.5 Hours Ago", 90);
		options.put("1 Hour Ago", 60);
		options.put("45 Minutes Ago", 45);
		options.put("30 Minutes Ago", 30);
		options.put("15 Minutes Ago", 15);
		*/
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
						drink_count++;
						db.injectDelayValue("drink_count", drink_count, v);
						calculateHours();
						calculateBac();
						calculateColor();
						updateFace();
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
		input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		new AlertDialog.Builder(this).setTitle("Enter Amount you spent on Alcohol").setView(
				input).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						double value = Double.parseDouble(input.getText().toString());
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
		
		
	}
	
		/*
		 * 
		 * TextView check = new TextView(this);
		 * check.setText(String.valueOf(drink_count));
		 * check.setTextColor(Color.parseColor("#FFFFFF"));
		 * ((FrameLayout)parent_view).addView(check);
		 */
		/*
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent openPage;
		switch (item.getItemId()) {

		case R.id.tracking_menu:
			openPage = new Intent(this, DrinkCounter.class);
			startActivity(openPage);
			break;
		case R.id.assess_menu:
			openPage = new Intent(this, Assessment.class);
			startActivity(openPage);
			break;
		case R.id.visualize_menu:
			openPage = new Intent(this, VisualizeMenu.class);
			startActivity(openPage);
			break;
		case R.id.setting_menu:
			openPage = new Intent(this, Settings.class);
			startActivity(openPage);
			break;
		case android.R.id.home:
			openPage = new Intent(this, MainMenu.class);
			startActivity(openPage);
			break;

		}
		return true;
	}
	*/
}
