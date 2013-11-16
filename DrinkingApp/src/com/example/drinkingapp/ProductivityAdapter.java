package com.example.drinkingapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductivityAdapter extends BaseAdapter{

	Context mContext;
	
	//constructer
	public ProductivityAdapter(Context context){
		mContext=context;
	}
	
	
	//at least 7 to start for week1.
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
		TextView view=new TextView(mContext);
		
		
		
		return null;
	}

}
