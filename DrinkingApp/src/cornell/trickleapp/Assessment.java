package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ListView;

public class Assessment extends Activity {
	ArrayList<String> surveys = new ArrayList<String>();
	ArrayList<String> surveys_classes = new ArrayList<String>();
	String surveyList[] = { 
			"DailySurvey2",
			"DailySurveyExercise", "DailySurveyProductivity"};
	String surveyNames[] = {"Overall Experience","Exercise","Productivity"};
	String placement;
	Boolean sleepSelected, exerciseSelected, productivitySelected,
			socialSelected;
	
	GridView assesssmentGridView;
	private DatabaseHandler db;

	private void drinkingDay(){
		for (int n = 0; n < surveyNames.length; n++) {
			surveys.add(surveyNames[n]);
			surveys_classes.add(surveyList[n]);
		}
	}
	
	private void nonDrinkingDay(){
		for (int n = 1; n < surveyNames.length; n++) {
			surveys.add(surveyNames[n]);
			surveys_classes.add(surveyList[n]);
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//get the Database
		db = new DatabaseHandler(this);
		Date date = new Date();
		ArrayList<DatabaseStore> drank = (ArrayList<DatabaseStore>)db.getVarValuesForDay("drank_last_night", date);
			if (drank != null){
				drinkingDay();
			}else{
				nonDrinkingDay();
			}
			db.updateOrAdd("numberofcategories", surveys_classes.size());
			
			

			// gets rid of the evaluation categories if users unchecked it.
			SharedPreferences getPreference = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			
			//Boolean sleepSelected = getPreference.getBoolean("sleepInEvaluation",
			//		true);
			Boolean exerciseSelected = getPreference.getBoolean(
					"exerciseInEvaluation", true);
			Boolean productivitySelected = getPreference.getBoolean(
					"productivityInEvaluation", true);
			Boolean socialSelected = getPreference.getBoolean("socialInEvaluation",
					true);
			/*
			if (!sleepSelected) {
				surveys_classes.remove("DailySurveySleep");
				surveys.remove("Sleep");
			}*/
			if (!exerciseSelected) {
				surveys_classes.remove("DailySurveyExercise");
				surveys.remove("Exercise");
			}
			if (!productivitySelected) {
				surveys_classes.remove("DailySurveyProductivity");
				surveys.remove("Productivity");
			}
			/*
			if (!socialSelected) {
				surveys_classes.remove("DailySurveySocial");
				surveys.remove("Social");
			}*/

			
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(false);
	/*
			setListAdapter(new ArrayAdapter<String>(Assessment.this,
					android.R.layout.simple_list_item_checked, surveys));
			*/
			setContentView(R.layout.assessmentvisualization);
			assesssmentGridView = (GridView) findViewById(R.id.gvAssessment);
			AssessmentAdapter adapter = new AssessmentAdapter(this, surveys,surveys_classes);
			
			assesssmentGridView.setAdapter(adapter);
			
			

	
	
	}
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent openPage;
		switch (item.getItemId()) {

		/*
		case R.id.tracking_menu:
			openPage = new Intent(this, DrinkCounter.class);
			startActivity(openPage);
			break;
		case R.id.assess_menu:
			openPage = new Intent(this, Assessment.class);
			startActivity(openPage);
			break;
		case R.id.visualize_menu:
			openPage = new Intent(this, VisualizeMenu.class);
			startActivity(openPage);
			break;
		case R.id.setting_menu:
			openPage = new Intent(this, Settings.class);
			startActivity(openPage);
			break;
		case android.R.id.home:
			openPage = new Intent(this, MainMenu.class);
			startActivity(openPage);
			break;
*/

		}
		return true;
	}

}
