package com.example.drinkingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MoodVisualization extends Activity {
	
	WordleGraphics visual;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//sample data;
		//nested arraylist/array, inner array: 0-name 1-count
		ArrayList<String[]> test=new ArrayList<String[]>();
		String[] testStringArray1 = {"Sad","4"};
		test.add(testStringArray1);
		String[] testStringArray2 = {"Exciting","2"};
		test.add(testStringArray2);
		String[] testStringArray3 = {"Bashful","3"};
		test.add(testStringArray3);
		String[] testStringArray4 = {"Stupid","5"};
		test.add(testStringArray4);
		String[] testStringArray5 = {"Bashful","3"};
		test.add(testStringArray5);
		String[] testStringArray6 = {"Wonderful","2"};
		test.add(testStringArray6);
		String[] testStringArray7 = {"Harsh","1"};
		test.add(testStringArray7);
		String[] testStringArray8 = {"lol","3"};
		test.add(testStringArray8);
		String[] testStringArray9 = {"GOD","10"};
		test.add(testStringArray9);
		String[] testStringArray10 = {"Painful","6"};
		test.add(testStringArray10);
		
	
		visual = new WordleGraphics(this,test,"Mood");
		
		setContentView(visual);
	
	
	}

}
