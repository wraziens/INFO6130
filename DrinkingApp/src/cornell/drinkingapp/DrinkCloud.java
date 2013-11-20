package cornell.drinkingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

public class DrinkCloud extends Activity{
	WordleGraphics visual;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		//sample data;
		//nested arraylist/array, inner array: 0-name 1-count
		ArrayList<String[]> test=new ArrayList<String[]>();
		String[] testStringArray1 = {"Vomit","4"};
		test.add(testStringArray1);
		String[] testStringArray2 = {"Puke","2"};
		test.add(testStringArray2);
		String[] testStringArray3 = {"Hungry","3"};
		test.add(testStringArray3);
		String[] testStringArray4 = {"Kiss","5"};
		test.add(testStringArray4);
		String[] testStringArray5 = {"Trip","3"};
		test.add(testStringArray5);
		String[] testStringArray6 = {"Hurt","2"};
		test.add(testStringArray6);
		String[] testStringArray7 = {"Dance","1"};
		test.add(testStringArray7);
		String[] testStringArray8 = {"Buzzed","3"};
		test.add(testStringArray8);
		String[] testStringArray9 = {"Fun","10"};
		test.add(testStringArray9);
		String[] testStringArray10 = {"Shots","6"};
		test.add(testStringArray10);
		
	
		//visual = new WordleGraphics(this,test,"Drinking");
		
		setContentView(visual);
	}

}
