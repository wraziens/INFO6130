package com.example.drinkingapp;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorAdapter extends BaseAdapter implements OnClickListener{
	private Context mContext;
	int monthSelected,yearSelected;
	ArrayList<Integer> drinkingDays;
	ArrayList<Double>  maxbac;
	int daysSetBack=0;
	int daysInMonth=0;
	static ArrayList<Button> buttonStore=new ArrayList<Button>();
	static int focused=0;
	TextView test;
	
	public ColorAdapter(Context c,int m, int y, ArrayList<Integer> d,ArrayList<Double> mb) {
		mContext = c;
		monthSelected = m;
		yearSelected = y;
		drinkingDays = d;
		maxbac= mb;

	}
	//given month and year, it detects the 1st day of the month and see what day it is, days in that month
	//and shift the calender accordingly.
	public int getCount() {
		Calendar selectedCalendar=Calendar.getInstance();
		selectedCalendar.set(yearSelected, monthSelected, 1);
		int daysInSelectedMonth=selectedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayOfTheWeekSelected=selectedCalendar.get(Calendar.DAY_OF_WEEK);
		daysSetBack=dayOfTheWeekSelected-1;
		return daysInSelectedMonth+daysSetBack+7;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final Button view=new Button(mContext);

			//view.setPadding(0, 0, 0, 0);
		double bacLevel;
		
		
		
			for (int n=0;n<drinkingDays.size();n++){
				if (drinkingDays.get(n)+daysSetBack+7==position+1){
					view.setBackgroundColor(Color.RED);
					bacLevel=maxbac.get(n);
					final String bac=""+bacLevel;
					view.setOnClickListener(new OnClickListener(){

						@Override
	                    public void onClick(View v) {
							((DrinkCalendar)mContext).changeBottomDisplay("Max Bac: "+bac);
							if (focused>0){
								int something=parent.getChildCount();
								View child=parent.getChildAt(focused);
								child.setBackgroundColor(Color.RED);
							}
							focused=position;
							v.setSelected(true);
							view.setBackgroundResource(R.drawable.border);
	                    }
						
					});
				}
			}
			if (position>6&&position<7+daysSetBack){
				view.setVisibility(8);
			}
			else{
			
			    view.setText(""+(position+1-daysSetBack-7));
			    view.setTextSize(10f);
			    //sets up the Days of the week display
			}
			    switch(position){
				case 0:
					view.setBackgroundColor(Color.WHITE);
					view.setText("M");
					break;
				case 1:
					view.setBackgroundColor(Color.WHITE);
					view.setText("T");
					break;
				case 2:
					view.setBackgroundColor(Color.WHITE);
					view.setText("W");
					break;
				case 3:
					view.setBackgroundColor(Color.WHITE);
					view.setText("Th");
					break;
				case 4:
					view.setBackgroundColor(Color.WHITE);
					view.setText("F");
					break;
				case 5:
					view.setBackgroundColor(Color.WHITE);
					view.setText("Sa");
					break;
				case 6:
					view.setBackgroundColor(Color.WHITE);
					view.setText("Su");
					break;
				}
		    

		return view;
	}
	
	public ArrayList<Button> getButtonView(){
		return buttonStore;
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
