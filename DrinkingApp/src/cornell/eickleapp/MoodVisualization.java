package cornell.eickleapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MoodVisualization extends Activity {
	
	WordleGraphics visual;
	private DatabaseHandler db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this);
		
		//get data from the database
		ArrayList<String[]> drink = db.getWordleDrink();
		ArrayList<String[]> no_drink = db.getWordleNoDrink();
	
		visual = new WordleGraphics(this,drink,no_drink,"Mood");
		
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
		super.onStop();
		finish();
	}
	

}
