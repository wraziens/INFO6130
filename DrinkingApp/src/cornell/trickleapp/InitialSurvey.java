package cornell.trickleapp;

import java.util.Calendar;
import java.util.List;
import java.util.prefs.Preferences;

import cornell.trickleapp.R;
import cornell.trickleapp.R.id;
import cornell.trickleapp.R.layout;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class InitialSurvey extends Activity implements OnClickListener {

	static FlyOutContainer root;
	Button save, male, female, kg, lb;
	EditText weight;
	int weightResult = 100;
	String sexResult = "Female", weightUnitResult = "lb";
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.settings, null);

		this.setContentView(root);

		db = new DatabaseHandler(this);
		male = (Button) findViewById(R.id.bMale);
		female = (Button) findViewById(R.id.bFemale);
		save = (Button) findViewById(R.id.bSettingsSave);
		lb = (Button) findViewById(R.id.bLb);
		kg = (Button) findViewById(R.id.bKg);
		weight = (EditText) findViewById(R.id.etWeight);
		male.setOnClickListener(this);
		female.setOnClickListener(this);
		save.setOnClickListener(this);
		lb.setOnClickListener(this);
		kg.setOnClickListener(this);
		
		//dont populate field when it's the 1st time setting up
		if (db.variableExistAll("firstTime"))
			initialize();
	}

	// pulls data from database and populates the field based on that.
	private void initialize() {
		// TODO Auto-generated method stub
		if (db.variableExistAll("gender")) {
			sexResult = db.getAllVarValue("gender").get(0).value;
		}
		if (db.variableExistAll("weight")) {
			weightResult = Integer
					.parseInt(db.getAllVarValue("weight").get(0).value);
		}
		if (db.variableExistAll("weight_unit")) {
			List<DatabaseStore> dummy=db.getAllVarValue("weight_unit");
			weightUnitResult = db.getAllVarValue("weight_unit").get(0).value;
		}
		//Selects options that are previously recorded in the database
		if (sexResult.equals("Male")){
			male.setSelected(true);
		}
		if (sexResult.equals("Female")){
			male.setSelected(true);
		}
		if (sexResult.equals("Male")){
			female.setSelected(true);
		}
		if (weightUnitResult.equals("lb")){
			lb.setSelected(true);
		}
		if (weightUnitResult.equals("kg")){
			kg.setSelected(true);
		}
		weight.setText(""+weightResult);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bMale:
			sexResult = "Male";
			male.setSelected(true);
			female.setSelected(false);
			break;
		case R.id.bFemale:
			sexResult = "Female";
			female.setSelected(true);
			male.setSelected(false);
			break;
		case R.id.bLb:
			weightUnitResult = "lb";
			lb.setSelected(true);
			kg.setSelected(false);
			break;
		case R.id.bKg:
			weightUnitResult = "kg";
			kg.setSelected(true);
			lb.setSelected(false);
			break;
		case R.id.bSettingsSave:
			weightResult = Integer.parseInt(weight.getText().toString());
			// adds the gender
			if (db.variableExistAll("gender")) {
				db.deleteAllVariables("gender");
			}
			db.addValue("gender", sexResult);

			// sets the unit for weight to kg BUT stores value (in lb)
			if (weightUnitResult.equals("kg")) {
				if (db.variableExistAll("weight")) {
					db.deleteAllVariables("weight");
				}
				// converts from kg to lb and rounds to the nearest integer and
				// stores it
				db.addValue("weight", (int) (0.453592 * weightResult));
				if (db.variableExistAll("weight_unit")) {
					db.deleteAllVariables("weight_unit");
				}
				db.addValue("weight_unit", "kg");
			}
			// sets the unit for weight to lb and stores value (in lb)
			if (weightUnitResult.equals("lb")) {
				if (db.variableExistAll("weight")) {
					db.deleteAllVariables("weight");
				}
				db.addValue("weight", weightResult);
				if (db.variableExistAll("weight_unit")) {
					db.deleteAllVariables("weight_unit");
				}
				db.addValue("weight_unit", "lb");
			}
			// sets the mark for 1st time use proceed to home page afterwards
			if (!db.variableExistAll("firstTime")){
				db.addValue("firstTime", "1");
				Intent goToThisPage=new Intent(this,MainMenu3.class);
				startActivity(goToThisPage);
			}
			break;

		}

	}

	// check if the categories for evaluations are checked, if yes: check it in
	// settings.

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		finish();
	}


}
