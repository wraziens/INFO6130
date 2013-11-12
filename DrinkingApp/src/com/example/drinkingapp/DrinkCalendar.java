package com.example.drinkingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.GridView;

public class DrinkCalendar extends Activity{
	
	 
	GridView drinkCalendar;
	int daysInThisMonth=31;
	
	ArrayList<String> numbers = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.drinkcalendar);
	drinkCalendar = (GridView) findViewById(R.id.gvDrinkCalendar);
	
	for (int n=1;n<=daysInThisMonth;n++){
		numbers.add(Integer.toString(n));
	}
	
	ArrayList<Integer>drinkingDays=new ArrayList<Integer>();
	drinkingDays.add(5);
	drinkingDays.add(14);
	
	ArrayList<Double>maxBac=new ArrayList<Double>();
	maxBac.add(0.25);
	maxBac.add(0.1);
	
	int month=11;
	drinkCalendar.setAdapter(new ColorAdapter(this,month,drinkingDays,maxBac));
	
	 
	}
	
	
}