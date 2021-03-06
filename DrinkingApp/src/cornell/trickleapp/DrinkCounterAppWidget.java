package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cornell.trickleapp.R;
import cornell.trickleapp.R.id;
import cornell.trickleapp.R.layout;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

public class DrinkCounterAppWidget extends AppWidgetProvider{

	private int drink_count = 0;
	int start_color = 0x884D944D;
	int offset = 10;
	private DatabaseHandler db;
	private double hours;
	private int color;
	static private double bac;

	// TODO:Temporary, move to a class that makes more sense
	private final Double CALORIES_PER_DRINK = 120.0;
	private final Double CALORIES_PER_CHICKEN = 264.0;
	private final Double CALORIES_PER_PIZZA = 285.0;
	private final Double CALORIES_HOT_DOG = 250.0;
	DrinkCounter counter=new DrinkCounter();
    public final String counterAction = "Add Drink";
    Intent intent;
	static boolean checkSurveyed;
	RelativeLayout widget;
	static int[] neededIds;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		neededIds=appWidgetIds;
		


		final int N = appWidgetIds.length;
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            SharedPreferences getPrefs=PreferenceManager.getDefaultSharedPreferences(context);
            checkSurveyed=getPrefs.getBoolean("initialSurvey", false);
    			 // Create an Intent to launch ExampleActivity
                intent = new Intent(context, DrinkCounterAppWidget.class);
                intent.setAction(counterAction);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
               
                
                // Get the layout for the App Widget and attach an on-click listener
                // to the button
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counterwidget_layout);
                views.setOnClickPendingIntent(R.id.bTestClick, pendingIntent);

                
                intent = new Intent(context, MainActivity.class);
                intent.setAction("Home");
                PendingIntent homeIntent = PendingIntent.getActivity(context, 0, intent, 0);
                views.setOnClickPendingIntent(R.id.bWidget, homeIntent);
                views.setOnClickPendingIntent(R.id.ibWidgetWarning, homeIntent);
                
                
                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            
        
        }
		
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		String action=intent.getAction();
		if (action.equals(counterAction)) {
			// do some really cool stuff here
			SharedPreferences getPrefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			checkSurveyed = getPrefs.getBoolean("initialSurvey", false);
			if (checkSurveyed == false) {
				Intent i = new Intent();
				i.setClassName("cornell.trickleapp", "cornell.trickleapp.InitialSurvey");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);

			} else {
				

				
				db = new DatabaseHandler(context);
				hadDrink();
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counterwidget_layout);
				//calculateColor();
				if (bac>0.15){
					views.setInt(R.id.ibWidgetWarning, "setBackgroundColor", color);
					//views.setInt(R.id.ibWidgetWarning, "setColorFilter", color);
					views.setInt(R.id.tvWidget, "setVisibility", View.VISIBLE);
					views.setInt(R.id.tvWidgetHomeImage, "setVisibility", View.INVISIBLE);
				}
				else{
					views.setInt(R.id.ibWidgetWarning, "setBackgroundColor", color);
					views.setInt(R.id.tvWidget, "setVisibility", View.INVISIBLE);
					views.setInt(R.id.tvWidgetHomeImage, "setVisibility", View.VISIBLE);
				}
				appWidgetManager.updateAppWidget(neededIds, views);
			}
		}
		else if (action.equals("Home")){
			
			Intent i = new Intent();
			i.setClassName("cornell.trickleapp", "cornell.trickleapp.MainActivity");
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}

	}
	public void hadDrink() {
		drink_count++;
		if (drink_count == 1){
			db.addValueTomorrow("drank_last_night", "True");
			db.addValueTomorrow("tracked", "True");
			db.updateOrAdd("drank", "True");
		}
		db.addDelayValue("drink_count", drink_count);
		calculateBac();
		db.addDelayValue("bac", String.valueOf(bac));
		calculateColor();
		db.addDelayValue("bac_color", String.valueOf(color));

		//calculate number of chickens that equate the number of calories
		Double drink_cals = drink_count * CALORIES_PER_DRINK;
		int number_chickens = (int) Math.ceil(drink_cals / CALORIES_PER_CHICKEN);
		db.updateOrAdd("number_chickens", number_chickens);

		//calculate the number of slices of pizza that equate to the 
		//number of drinks consumed that day.
		int number_pizza = (int) Math.ceil(drink_cals / CALORIES_PER_PIZZA);
		db.updateOrAdd("number_pizza", number_pizza);
		
		int number_hot_dogs = (int)Math.ceil(drink_cals/ CALORIES_HOT_DOG);
		db.updateOrAdd("hot_dogs", number_hot_dogs);
		
		/*
		TextView check = new TextView(this);
		check.setText(String.valueOf(bac));
		check.setTextColor(Color.parseColor("#FFFFFF"));
		((FrameLayout)parent_view).addView(check);
		*/
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
	private void calculateHours() {
		Date date = new Date();
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesDelay("drink_count", date);
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.HOUR_OF_DAY, -6);
		date = gc.getTime();
		DatabaseStore current = new DatabaseStore("","",date, "Integer");
		
		color = start_color;
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
	public void calculateColor() {
		if (bac < 0.06) {
			color = start_color;
		} else if (bac < 0.15) {
			color = 0X88E68A2E;
		} else if (bac < 0.24) {
			color = 0X88A30000;
		} else {
			color = 0XCC000000;
		}
	}

}
