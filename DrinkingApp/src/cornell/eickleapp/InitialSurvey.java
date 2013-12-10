package cornell.eickleapp;

import java.util.Calendar;
import java.util.prefs.Preferences;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class InitialSurvey extends Activity implements OnClickListener,OnCheckedChangeListener {

	TextView ageTV,weightTV;
	Button finish,plusAge,plusWeight,minusAge,minusWeight;
	CheckBox sleep, exercise, productivity, social;
	int age,weight;
	RadioGroup sex,alcoholFrequency,alcoholQuantity;
	String sexResult,alcoholFrequencyResult,alcoholQuantityResult;
	AlarmManager alarmManager;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this);
		initiate();
		setAlarm();
		setContentView(R.layout.initialsurvey);
		//test = (TextView) findViewById(R.id.test);
		ageTV=(TextView) findViewById(R.id.tvISAge);
		weightTV=(TextView) findViewById(R.id.tvISWeight);
		age=Integer.parseInt(ageTV.getText().toString());
		weight=Integer.parseInt(weightTV.getText().toString());
		//sleep = (CheckBox) findViewById(R.id.cbISSleep);
		exercise = (CheckBox) findViewById(R.id.cbISExercise);
		productivity = (CheckBox) findViewById(R.id.cbISProductivity);
		//social = (CheckBox) findViewById(R.id.cbISSocial);
		finish = (Button) findViewById(R.id.bISFinish);
		plusAge = (Button) findViewById(R.id.bISPlus1);
		plusWeight = (Button) findViewById(R.id.bISPlus2);
		minusAge = (Button) findViewById(R.id.bISMinus1);
		minusWeight = (Button) findViewById(R.id.bISMinus2);
		sex = (RadioGroup) findViewById(R.id.rgISSex);
		alcoholFrequency=(RadioGroup) findViewById(R.id.rgISAlcoholFrequency);
		alcoholQuantity=(RadioGroup) findViewById(R.id.rgISAlcoholQuantity);
		finish.setOnClickListener(this);
		plusAge.setOnClickListener(this);
		plusWeight.setOnClickListener(this);
		minusAge.setOnClickListener(this);
		minusWeight.setOnClickListener(this);
		sex.setOnCheckedChangeListener(this);
		alcoholFrequency.setOnCheckedChangeListener(this);
		alcoholQuantity.setOnCheckedChangeListener(this);
		
	}

	// first time reset. delete before launching of app.
	private void initiate() {
		// TODO Auto-generated method stub
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		SharedPreferences getPreference = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor preferenceEditor = getPreference.edit();
		//preferenceEditor.putBoolean("sleepInEvaluation", false);
		preferenceEditor.putBoolean("exerciseInEvaluation", false);
		preferenceEditor.putBoolean("productivityInEvaluation", false);
		//preferenceEditor.putBoolean("socialInEvaluation", false);
		preferenceEditor.commit();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
			case R.id.bISFinish:
				//test.setText("TESTED");
				SharedPreferences getPrefs=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor editPrefs=getPrefs.edit();
				editPrefs.putBoolean("initialSurvey", true);
				editPrefs.commit();
				preferenceCheck();
				Intent goToMenu=new Intent(this,MainMenu.class);
				startActivity(goToMenu);
				break;
			case R.id.bISPlus1:
				age++;
				ageTV.setText(""+age);
				break;
			case R.id.bISMinus1:
				age--;
				ageTV.setText(""+age);
				break;
			case R.id.bISPlus2:
				weight+=5;
				weightTV.setText(""+weight);
				break;
			case R.id.bISMinus2:
				weight-=5;
				weightTV.setText(""+weight);
				break;
		}
		
		
	}
	//check if the categories for evaluations are checked, if yes: check it in settings.
	private void preferenceCheck() {
		// TODO Auto-generated method stub
		SharedPreferences getPreference=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor preferenceEditor=getPreference.edit();
		//if (sleep.isChecked())
		//	preferenceEditor.putBoolean("sleepInEvaluation", true);
		//if (!sleep.isChecked())
			//preferenceEditor.putBoolean("sleepInEvaluation", false);
		if (exercise.isChecked())
			preferenceEditor.putBoolean("exerciseInEvaluation", true);
		if (!exercise.isChecked())
			preferenceEditor.putBoolean("exerciseInEvaluation", false);
		if (productivity.isChecked())
			preferenceEditor.putBoolean("productivityInEvaluation", true);
		if (!productivity.isChecked())
			preferenceEditor.putBoolean("productivityInEvaluation", false);
		//if (social.isChecked())
		//	preferenceEditor.putBoolean("socialInEvaluation", true);
		//if (!social.isChecked())
		//	preferenceEditor.putBoolean("socialInEvaluation", false);
		preferenceEditor.commit();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		finish();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (group.getId()==R.id.rgISSex){//don't need the if statement, but inserted for coder's visual structure
			switch(checkedId){
				case R.id.rbISMale:
					//test.setText("Male");
					sexResult=((RadioButton)findViewById(sex.getCheckedRadioButtonId())).getText().toString();
					break;
				case R.id.rbISFemale:
					//test.setText("Female");
					sexResult=((RadioButton)findViewById(sex.getCheckedRadioButtonId())).getText().toString();
					break;
			}
		}
		if (group.getId()==R.id.rgISAlcoholFrequency){//don't need the if statement, but inserted for coder's visual structure
			switch(checkedId){
				case R.id.rbISAlcoholF1:
					//test.setText("F1");
					alcoholFrequencyResult=((RadioButton)findViewById(alcoholFrequency.getCheckedRadioButtonId())).getText().toString();
					break;
				case R.id.rbISAlcoholF2:
					//test.setText("F2");
					alcoholFrequencyResult=((RadioButton)findViewById(alcoholFrequency.getCheckedRadioButtonId())).getText().toString();
					break;
				case R.id.rbISAlcoholF3:
					//test.setText("F3");
					alcoholFrequencyResult=((RadioButton)findViewById(alcoholFrequency.getCheckedRadioButtonId())).getText().toString();
					break;
			}
		}
		if (group.getId()==R.id.rgISAlcoholQuantity){//don't need the if statement, but inserted for coder's visual structure
			switch(checkedId){
				case R.id.rbISAlcoholQ1:
					//test.setText("Q1");
					alcoholQuantityResult=((RadioButton)findViewById(alcoholQuantity.getCheckedRadioButtonId())).getText().toString();
					break;
				case R.id.rbISAlcoholQ2:
					//test.setText("Q2");
					alcoholQuantityResult=((RadioButton)findViewById(alcoholQuantity.getCheckedRadioButtonId())).getText().toString();
					break;
				case R.id.rbISAlcoholQ3:
					//test.setText("Q3");
					alcoholQuantityResult=((RadioButton)findViewById(alcoholQuantity.getCheckedRadioButtonId())).getText().toString();
					break;
				case R.id.rbISAlcoholQ4:
					//test.setText("Q4");
					alcoholQuantityResult=((RadioButton)findViewById(alcoholQuantity.getCheckedRadioButtonId())).getText().toString();
					break;
					
					
			}
		}
		
	}
	//currently reminds the user 24 hours since 1st use
	public void setAlarm() {
		  Intent intent = new Intent(this, ReminderAlarm.class);
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
		    intent, 0);
		  
		  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				  System.currentTimeMillis(), (5000), pendingIntent);  
		   // System.currentTimeMillis() + (86400000), pendingIntent);
	}

}
