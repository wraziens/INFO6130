package cornell.eickleapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MoodVisualization extends Activity {
	
	WordleGraphics visual;
	private DatabaseHandler db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this);
		
		//get data from the database
		ArrayList<String[]> drink = db.getWordleDrink();
		ArrayList<String[]> no_drink = db.getWordleNoDrink();

		
		//sample data;
		//nested arraylist/array, inner array: 0-name 1-count
		ArrayList<String[]> drinking = new ArrayList<String[]>();
		ArrayList<String[]> noDrinking = new ArrayList<String[]>();
		String[] testStringArray1 = {"Sad","4"};
		drinking.add(testStringArray1);
		String[] testStringArray2 = {"Exciting","2"};
		drinking.add(testStringArray2);
		String[] testStringArray3 = {"Bashful","3"};
		drinking.add(testStringArray3);
		String[] testStringArray4 = {"Stupid","5"};
		drinking.add(testStringArray4);
		String[] testStringArray5 = {"horrific","3"};
		noDrinking.add(testStringArray5);
		String[] testStringArray6 = {"Wonderful","2"};
		noDrinking.add(testStringArray6);
		String[] testStringArray7 = {"Harsh","1"};
		noDrinking.add(testStringArray7);
		String[] testStringArray8 = {"lol","3"};
		noDrinking.add(testStringArray8);
		String[] testStringArray9 = {"nice","10"};
		noDrinking.add(testStringArray9);
		String[] testStringArray10 = {"great","6"};
		noDrinking.add(testStringArray10);
		
	
		visual = new WordleGraphics(this,drinking,noDrinking,"Mood");
		
		setContentView(visual);
	
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		if (checkSurveyed) {
			Intent openHint = new Intent(this, MoodVisualizationTutorial.class);
			startActivity(openHint);
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
	

}
