package com.example.drinkingapp;

import java.sql.Date;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.GridView;
import android.widget.TextView;

public class DrinkCalendar extends Activity implements OnClickListener{
	
	int selectedMonth,selectedYear;
	Calendar calendar=	Calendar.getInstance();
	GridView drinkCalendar;
	TextView monthDisplay,yearDisplay,bottomDisplay;
	Button back,next;
	ArrayList<Button>drinkBacButtons=new ArrayList<Button>();
	
	ArrayList<String> numbers = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.drinkcalendar);

	drinkCalendar = (GridView) findViewById(R.id.gvDrinkCalendar);
	monthDisplay = (TextView) findViewById(R.id.tvMonth);
	yearDisplay = (TextView) findViewById(R.id.tvYear);
	bottomDisplay = (TextView) findViewById(R.id.tvCalendarBottomDisplay);
	back = (Button) findViewById(R.id.bPreviousMonth);
	next = (Button) findViewById(R.id.bNextMonth);
	back.setOnClickListener(this);
	next.setOnClickListener(this);
	
	//sample numbers to be added
	ArrayList<Integer>drinkingDays=new ArrayList<Integer>();
	drinkingDays.add(5);
	drinkingDays.add(14);
	drinkingDays.add(16);
	
	ArrayList<Double>maxBac=new ArrayList<Double>();
	maxBac.add(0.25);
	maxBac.add(0.1);
	maxBac.add(0.7);
	
	selectedMonth=calendar.get(Calendar.MONTH);
	selectedYear=calendar.get(Calendar.YEAR);
	setMonthFromInt(selectedMonth);
	yearDisplay.setText(Integer.toString(selectedYear));
	ColorAdapter adapter=new ColorAdapter(this,selectedMonth,selectedYear,drinkingDays,maxBac);
	drinkCalendar.setAdapter(adapter);
	drinkBacButtons=adapter.getButtonView();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		//finds the data in the database
		ArrayList<Integer>drinkingDays=new ArrayList<Integer>();
		drinkingDays.add(5);
		drinkingDays.add(14);
		drinkingDays.add(16);
		
		ArrayList<Double>maxBac=new ArrayList<Double>();
		maxBac.add(0.25);
		maxBac.add(0.1);
		maxBac.add(0.7);
		
		switch(v.getId()){
		case R.id.bNextMonth:

			if (selectedMonth+1>12){
				selectedMonth=1;
				selectedYear++;
				yearDisplay.setText(Integer.toString(selectedYear));
			}
			else
				selectedMonth++;
			setMonthFromInt(selectedMonth);
			break;
		case R.id.bPreviousMonth:
			if (selectedMonth-1<1){
				selectedMonth=12;
				selectedYear--;
				yearDisplay.setText(Integer.toString(selectedYear));
			}
			else
				selectedMonth--;
			setMonthFromInt(selectedMonth);
			break;
		}
		ColorAdapter adapter=new ColorAdapter(this,selectedMonth,selectedYear,drinkingDays,maxBac);
		drinkCalendar.setAdapter(adapter);
		drinkBacButtons=adapter.getButtonView();
	}
	//inputs the int value of month and outputs its corresponding name
	private void setMonthFromInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        month = months[num-1];
		monthDisplay.setText(month);
    }
	public void changeBottomDisplay(String entry){
		bottomDisplay.setText(entry);
	}
	
}