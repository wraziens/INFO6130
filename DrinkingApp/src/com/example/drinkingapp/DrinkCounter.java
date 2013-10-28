package com.example.drinkingapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//parse stuff
		
		Parse.initialize(this,  "pzrwzzF69gXSQrxr9gmfhWVQq3it1UrLFxCbPyUw", "8dZZu0sRje5F4K31FwAmYXbdSmkCOTZvUIfQo1N1");
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
		

		
		
		
		
		//end parse stuff
		setContentView(R.layout.drink_tracking);
		View view = findViewById(R.id.drink_layout);
		//view.setBackgroundColor(0xF2FA27);
		view.setBackgroundColor(start_color);
		setContentView(view);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	public void doneDrinking(View view){
		finish();
	}
	
	@SuppressLint("NewApi")
	public void hadDrink(View view){
		drink_count ++;
		ParseObject drinkCounter = new ParseObject("drinkCounter");
		Time timeNow=new Time();
		timeNow.setToNow();
		drinkCounter.put("counterType", "drink");
		drinkCounter.put("time",timeNow.toString());
		drinkCounter.put("time2445",timeNow.format2445());
		drinkCounter.put("time3339",timeNow.format3339(true));
		drinkCounter.put("hour", ""+timeNow.hour);
		drinkCounter.put("minute", ""+timeNow.minute);
		drinkCounter.put("second", ""+timeNow.second);
		drinkCounter.saveEventually();
		
		//Should actually get the color based on BAC which can be calculated using:
		//EBAC = ((0.806 * number_drinks * 1.2) / (gender_constant * weight_kilograms)) - (metabolism_constant * hours)
		//need to get stored weight and gender.
		/*//get the gender from the input survey
		float metabolism_constant = 0;
		float gender_constant = 0;
		switch(gender){
			case 'male':
				metabolism_constant = 0.015;
				gender_constant = 0.58; 
				break;
			case 'female':
				metabolism_constant = 0.017;
				gender_constant = 0.49;
				break;
		}
		bac = ((0.806 * number_drinks * 1.2) / (gender_constant * weight_kilograms)) - (metabolism_constant * hours)
		
		*/
		start_color = start_color - 0x00000900;
		if (start_color < 0xFF7b0000){
			start_color = 0xFF7b9aad;
		} 
		View parent_view = findViewById(R.id.drink_layout);
		parent_view.setBackgroundColor(start_color);
	}
}
