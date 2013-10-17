package com.example.drinkingapp;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Assessment extends ListActivity{

	String surveys[]={"DailySurvey1","DailySurvey2","DailySurvey3","DailySurvey4"};
	String placement;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setListAdapter(new ArrayAdapter<String>(Assessment.this, 
				android.R.layout.simple_list_item_checked, surveys));//have to create for list
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		placement=surveys[position];
		
		try{
		Class ourClass=Class.forName("com.example.drinkingapp."+placement);
		Intent goToSurvey=new Intent(this,ourClass);
		startActivity(goToSurvey);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
