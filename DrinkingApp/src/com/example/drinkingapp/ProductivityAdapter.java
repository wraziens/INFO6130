package com.example.drinkingapp;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductivityAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Double> gradesAverageList = new ArrayList<Double>();
	ArrayList<Double> productivityAverageList = new ArrayList<Double>();
	ArrayList<Double> stressAverageList = new ArrayList<Double>();
	ArrayList<Integer> daysDrankList = new ArrayList<Integer>();
	ArrayList<Double> bacAverageList = new ArrayList<Double>();

	// constructer
	public ProductivityAdapter(Context context, ArrayList<Double> g,
			ArrayList<Double> p, ArrayList<Double> s, ArrayList<Integer> d,
			ArrayList<Double> b) {
		mContext = context;
		gradesAverageList = g;
		productivityAverageList = p;
		stressAverageList = s;
		daysDrankList = d;
		bacAverageList = b;
	}

	// at least 7 to start for week1.
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int test=6+6*gradesAverageList.size();
		return test;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView view = new TextView(mContext);
		view.setHeight(100);
		view.setTextSize(12);
		switch(position){
			case 0:
				view.setVisibility(8);
				break;
			case 1:
				view.setText("Academic");
				break;
			case 2:
				view.setText("Productivity");
				break;
			case 3:
				view.setText("Stress");
				view.setWidth(50);
				break;
			case 4:
				view.setText("No. of Drinking Days");
				break;
			case 5:
				view.setText("Avg BAC");
				break;
		}
		if (position==6){
			view.setText("Week 1");
		}
		if (position>6&&position%6==0)
			view.setText("Week "+position/6);
		
		
		if (position>6&&position%6==1){
			view.setText(gradesAverageList.get((position-1)/6-1).toString());
			
		}
		if (position>6&&position%6==2){
			view.setText(productivityAverageList.get((position-2)/6-1).toString());
		}
		if (position>6&&position%6==3){
			view.setText(stressAverageList.get((position-3)/6-1).toString());
		}
		if (position>6&&position%6==4){
			view.setText(daysDrankList.get((position-4)/6-1).toString());
		}
		if (position>6&&position%6==5){
			view.setText(bacAverageList.get((position-5)/6-1).toString());
		}
		return view;
			
	}

}
