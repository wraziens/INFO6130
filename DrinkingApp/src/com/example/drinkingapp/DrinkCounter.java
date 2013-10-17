package com.example.drinkingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class DrinkCounter extends Activity {
	private int drink_count = 0;
	private String color_string = "#F2FA27";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drink_tracking);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		//possibly get drink count from the database when starting instead of assuming 
		//it starts at 0.
	}
	
	public void doneDrinking(View view){
	
	}
	public void hadDrink(View view){
		drink_count ++;
		view.getBackground().setColorFilter(Color.parseColor("#F2FA27"), PorterDuff.Mode.DARKEN);
	}
}
