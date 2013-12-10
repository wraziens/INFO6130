package cornell.eickleapp;

import java.util.ArrayList;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

public class DailySurvey2 extends Activity implements OnClickListener {

	private Button finish;
	
	private ArrayList<CheckValue> checkboxes;
	private ArrayList<Integer> moodValue=new ArrayList<Integer>();
	private ArrayList<String> selected_values;
	private DatabaseHandler db;
	private int totalVal=0;

	private void addCheckboxValues(){
		//add all the checkbox values
		checkboxes.add(new CheckValue("happy", (CheckBox)findViewById(R.id.wordle_happy)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("productive",(CheckBox)findViewById(R.id.wordle_productive)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("lazy", (CheckBox)findViewById(R.id.wordle_lazy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("foggy",  (CheckBox)findViewById(R.id.wordle_foggy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("sick", (CheckBox)findViewById(R.id.wordle_sick)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("strong", (CheckBox)findViewById(R.id.wordle_strong)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("excited", (CheckBox)findViewById(R.id.wordle_excited)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("angry", (CheckBox)findViewById(R.id.wordle_angry)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("unhappy", (CheckBox)findViewById(R.id.wordle_unhappy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("sad", (CheckBox)findViewById(R.id.wordle_sad)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("defeated", (CheckBox)findViewById(R.id.wordle_defeated)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("social", (CheckBox)findViewById(R.id.wordle_social)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("fun", (CheckBox)findViewById(R.id.wordle_fun)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("sad", (CheckBox)findViewById(R.id.wordle_sad)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("energetic", (CheckBox)findViewById(R.id.wordle_energetic)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("healthy", (CheckBox)findViewById(R.id.wordle_healthy)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("lonely", (CheckBox)findViewById(R.id.wordle_lonely)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("successful", (CheckBox)findViewById(R.id.wordle_successful)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("apathetic", (CheckBox)findViewById(R.id.wordle_apathetic)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("optimistic", (CheckBox)findViewById(R.id.wordle_optimistic)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("loved", (CheckBox)findViewById(R.id.wordle_loved)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("embarrassed", (CheckBox)findViewById(R.id.wordle_embarrassed)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("sloppy", (CheckBox)findViewById(R.id.wordle_sloppy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("out of control", (CheckBox)findViewById(R.id.wordle_out_control)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("relaxed", (CheckBox)findViewById(R.id.wordle_relaxed)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("uncomfortable", (CheckBox)findViewById(R.id.wordle_uncomfortable)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("advernturous", (CheckBox)findViewById(R.id.wordle_adventurous)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("stressed", (CheckBox)findViewById(R.id.wordle_stressed)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("anxious", (CheckBox)findViewById(R.id.wordle_anxious)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("depressed", (CheckBox)findViewById(R.id.wordle_depressed)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("humorous", (CheckBox)findViewById(R.id.wordle_humorous)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("regretful", (CheckBox)findViewById(R.id.wordle_regretful)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("hopeful", (CheckBox)findViewById(R.id.wordle_hopeful)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("outgoing", (CheckBox)findViewById(R.id.wordle_outgoing)));
		moodValue.add(1);
		checkboxes.add(new CheckValue("busy", (CheckBox)findViewById(R.id.wordle_busy)));
		moodValue.add(0);
		checkboxes.add(new CheckValue("tired", (CheckBox)findViewById(R.id.wordle_tired)));
		moodValue.add(0);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordlesurvey);
		db = new DatabaseHandler(this);
		
		checkboxes = new ArrayList<CheckValue>();
		addCheckboxValues();
		
		selected_values = new ArrayList<String>();
		
		finish = (Button) findViewById(R.id.wordle_finish);
		finish.setOnClickListener(this);
	}

	/*
	 * Clears the current values in selected_values and 
	 * Checks to see which of the checkboxes are checked. Adds
	 * the corresponding string values to selected_values for the options
	 * that are checked.
	 */
	private void checkCheckBoxes(){
		selected_values.clear();
		for (int i=0; i<checkboxes.size(); i++){
			CheckValue c = checkboxes.get(i);
			if(c.view.isChecked()){
				selected_values.add(c.value);
				totalVal=totalVal+moodValue.get(i);
			}
		}
	}
	
	private void saveToDb(){
		db.clearAllValuesDay("wordle");
		for(int i=0; i<selected_values.size(); i++){
			db.addValue("wordle", selected_values.get(i));
		}
		db.updateOrAdd("moodScore", totalVal);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.wordle_finish:
				checkCheckBoxes();
				saveToDb();
				db.updateOrAdd("DailySurvey2" + "CheckList",
						"done");
				finish();
				break;
		}
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

		}
		return true;
	}
	protected void onPause() {
		super.onPause();
		finish();
	}

}
