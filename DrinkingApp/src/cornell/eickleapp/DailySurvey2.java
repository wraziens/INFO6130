package cornell.eickleapp;

import java.util.ArrayList;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

public class DailySurvey2 extends Activity implements OnClickListener {

	private Button finish;
	
	private ArrayList<CheckValue> checkboxes;
	private ArrayList<String> selected_values;
	private DatabaseHandler db;

	private void addCheckboxValues(){
		//add all the checkbox values
		checkboxes.add(new CheckValue("happy", (CheckBox)findViewById(R.id.wordle_happy)));
		checkboxes.add(new CheckValue("productive",(CheckBox)findViewById(R.id.wordle_productive)));
		checkboxes.add(new CheckValue("lazy", (CheckBox)findViewById(R.id.wordle_lazy)));
		checkboxes.add(new CheckValue("foggy",  (CheckBox)findViewById(R.id.wordle_foggy)));
		checkboxes.add(new CheckValue("sick", (CheckBox)findViewById(R.id.wordle_sick)));
		checkboxes.add(new CheckValue("strong", (CheckBox)findViewById(R.id.wordle_strong)));
		checkboxes.add(new CheckValue("excited", (CheckBox)findViewById(R.id.wordle_excited)));
		checkboxes.add(new CheckValue("angry", (CheckBox)findViewById(R.id.wordle_angry)));
		checkboxes.add(new CheckValue("unhappy", (CheckBox)findViewById(R.id.wordle_unhappy)));
		checkboxes.add(new CheckValue("sad", (CheckBox)findViewById(R.id.wordle_sad)));
		checkboxes.add(new CheckValue("defeated", (CheckBox)findViewById(R.id.wordle_defeated)));
		checkboxes.add(new CheckValue("social", (CheckBox)findViewById(R.id.wordle_social)));
		checkboxes.add(new CheckValue("fun", (CheckBox)findViewById(R.id.wordle_fun)));
		checkboxes.add(new CheckValue("sad", (CheckBox)findViewById(R.id.wordle_sad)));
		checkboxes.add(new CheckValue("energetic", (CheckBox)findViewById(R.id.wordle_energetic)));
		checkboxes.add(new CheckValue("healthy", (CheckBox)findViewById(R.id.wordle_healthy)));
		checkboxes.add(new CheckValue("lonely", (CheckBox)findViewById(R.id.wordle_lonely)));
		checkboxes.add(new CheckValue("successful", (CheckBox)findViewById(R.id.wordle_successful)));
		checkboxes.add(new CheckValue("apathetic", (CheckBox)findViewById(R.id.wordle_apathetic)));
		checkboxes.add(new CheckValue("optimistic", (CheckBox)findViewById(R.id.wordle_optimistic)));
		checkboxes.add(new CheckValue("loved", (CheckBox)findViewById(R.id.wordle_loved)));
		checkboxes.add(new CheckValue("embarrassed", (CheckBox)findViewById(R.id.wordle_embarrassed)));
		checkboxes.add(new CheckValue("sloppy", (CheckBox)findViewById(R.id.wordle_sloppy)));
		checkboxes.add(new CheckValue("out of control", (CheckBox)findViewById(R.id.wordle_out_control)));
		checkboxes.add(new CheckValue("relaxed", (CheckBox)findViewById(R.id.wordle_relaxed)));
		checkboxes.add(new CheckValue("uncomfortable", (CheckBox)findViewById(R.id.wordle_uncomfortable)));
		checkboxes.add(new CheckValue("advernturous", (CheckBox)findViewById(R.id.wordle_adventurous)));
		checkboxes.add(new CheckValue("stressed", (CheckBox)findViewById(R.id.wordle_stressed)));
		checkboxes.add(new CheckValue("anxious", (CheckBox)findViewById(R.id.wordle_anxious)));
		checkboxes.add(new CheckValue("depressed", (CheckBox)findViewById(R.id.wordle_depressed)));
		checkboxes.add(new CheckValue("humorous", (CheckBox)findViewById(R.id.wordle_humorous)));
		checkboxes.add(new CheckValue("regretful", (CheckBox)findViewById(R.id.wordle_regretful)));
		checkboxes.add(new CheckValue("hopeful", (CheckBox)findViewById(R.id.wordle_hopeful)));
		checkboxes.add(new CheckValue("outgoing", (CheckBox)findViewById(R.id.wordle_outgoing)));
		checkboxes.add(new CheckValue("busy", (CheckBox)findViewById(R.id.wordle_busy)));
		checkboxes.add(new CheckValue("tired", (CheckBox)findViewById(R.id.wordle_tired)));
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
			}
		}
	}
	
	private void saveToDb(){
		db.clearAllValuesDay("wordle");
		for(int i=0; i<selected_values.size(); i++){
			db.addValue("wordle", selected_values.get(i));
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.wordle_finish:
				checkCheckBoxes();
				saveToDb();
				finish();
				break;
		}
	}

	protected void onPause() {
		super.onPause();
		finish();
	}

}
