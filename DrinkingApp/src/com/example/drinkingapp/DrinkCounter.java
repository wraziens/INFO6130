package com.example.drinkingapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.parse.ParseUser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;


public class DrinkCounter extends Activity {
	private int drink_count = 0;
	int start_color = 0xFF7b9aad;
	int offset = 10;
	private DatabaseHandler db;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drink_tracking);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		db = new DatabaseHandler(this);
		//get the current date
		Date date = new Date();
		SimpleDateFormat year_fmt = new SimpleDateFormat("yyyy", Locale.US);
		SimpleDateFormat month_fmt = new SimpleDateFormat("MM", Locale.US);
		SimpleDateFormat day_fmt = new SimpleDateFormat("dd", Locale.US);
		
		int year = Integer.parseInt(year_fmt.format(date));
		int month = Integer.parseInt(month_fmt.format(date));
		int day = Integer.parseInt(day_fmt.format(date));
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>)db.getVarValuesForDay("drink_count", month , day, year);
		
		if (drink_count_vals != null){
			drink_count = drink_count_vals.size();
		}
		
		int color = start_color - (0x00000900 * drink_count);
		if (color < 0xFF7b0000){
			color = 0xFF7b0000;
		} 
		View view = findViewById(R.id.drink_layout);
		//view.setBackgroundColor(0xF2FA27);
		view.setBackgroundColor(color);
		setContentView(view);

	}
	
	public void doneDrinking(View view){
		finish();
	}
	
	@SuppressLint("NewApi")
	public void hadDrink(View view){
		drink_count ++;
		db.addValue("drink_count", drink_count);
		//Should actually get the color based on BAC which can be calculated using:
		//EBAC = ((0.806 * number_drinks * 1.2) / (gender_constant * weight_kilograms)) - (metabolism_constant * hours)
		//need to get stored weight and gender.
		//get the gender from the input survey
		/*
		double metabolism_constant = 0;
		double gender_constant = 0;
		double weight_lbs = 130;
		double weight_kilograms = weight_lbs * 0.453592;

		String gender = "FEMALE";
		if (gender == "MALE"){
			metabolism_constant = 0.015;
			gender_constant = 0.58; 
		} else {
			metabolism_constant = 0.017;
			gender_constant = 0.49;
		}
		
		//float bac = ((0.806 * drink_count * 1.2) / (gender_constant * weight_kilograms)) - (metabolism_constant * 1);//hours);
		*/
		int color = start_color - (0x00000900 * drink_count);
		if (color < 0xFF7b0000){
			color = 0xFF7b0000;
		} 
		View parent_view = findViewById(R.id.drink_layout);
		parent_view.setBackgroundColor(color);
	}
}
