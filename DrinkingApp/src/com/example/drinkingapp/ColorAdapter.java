package com.example.drinkingapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorAdapter extends BaseAdapter {
	private Context mContext;
	int month;
	ArrayList<Integer> drinkingDays;
	ArrayList<Double>  maxbac;
	int daysSetBack=0;
	int daysInMonth=0;
	
	public ColorAdapter(Context c,int m,ArrayList<Integer> d,ArrayList<Double> mb) {
		mContext = c;
		month = m;
		drinkingDays = d;
		maxbac= mb;
	}

	public int getCount() {

		if (month==11){
			daysSetBack=4;
			daysInMonth=30;
			return daysInMonth+daysSetBack+7;
		}
		else return 0;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Button view=new Button(mContext);
			//view.setPadding(0, 0, 0, 0);
		double bacLevel;
		
		
		
			for (int n=0;n<drinkingDays.size();n++){
				if (drinkingDays.get(n)+daysSetBack+7==position+1){
					view.setBackgroundColor(Color.RED);
					
					bacLevel=maxbac.get(n);
					
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
}
