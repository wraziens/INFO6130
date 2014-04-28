package cornell.trickleapp;

import java.util.Calendar;
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

	FlyOutContainer root;
	Button save, male, female, kg, lb;
	EditText weight;
	int weightResult=100;
	String sexResult="f", weightUnitResult="lb";
	AlarmManager alarmManager;
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
		weight=(EditText)findViewById(R.id.etWeight);
		male.setOnClickListener(this);
		female.setOnClickListener(this);
		save.setOnClickListener(this);
		lb.setOnClickListener(this);
		kg.setOnClickListener(this);

		// sets the mark for 1st time use
		db.updateOrAdd("firstTime", "here");
	}

	// first time reset. delete before launching of app.
	private void initiate() {
		// TODO Auto-generated method stub
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		SharedPreferences getPreference = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor preferenceEditor = getPreference.edit();
		// preferenceEditor.putBoolean("sleepInEvaluation", false);
		preferenceEditor.putBoolean("exerciseInEvaluation", false);
		preferenceEditor.putBoolean("productivityInEvaluation", false);
		// preferenceEditor.putBoolean("socialInEvaluation", false);
		preferenceEditor.commit();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bMale:
			sexResult="m";
			male.setSelected(true);
			female.setSelected(false);
			break;
		case R.id.bFemale:
			sexResult="f";
			female.setSelected(true);
			male.setSelected(false);
			break;
		case R.id.bLb:
			weightUnitResult="kg";
			lb.setSelected(true);
			kg.setSelected(false);
			break;
		case R.id.bKg:
			weightUnitResult="lb";
			kg.setSelected(true);
			lb.setSelected(false);
			break;
		case R.id.bSettingsSave:
			weightResult=Integer.getInteger(weight.getText().toString());
			if (sexResult=="m"){
				
			}
			if (sexResult=="f"){
				
			}
			if (weightUnitResult=="kg"){
				
			}
			if (weightUnitResult=="lb"){
				
			}
			/** test.setText("TESTED");
			SharedPreferences getPrefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			SharedPreferences.Editor editPrefs = getPrefs.edit();
			editPrefs.putBoolean("initialSurvey", true);
			editPrefs.putString("goal", "" + goal);
			editPrefs.commit();
			preferenceCheck();
			Intent goToMenu = new Intent(this, MainMenu.class);
			startActivity(goToMenu);
			**/
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

	// currently reminds the user 24 hours since 1st use
	public void setAlarm() {
		Intent intent = new Intent(this, ReminderAlarm.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), (86400000), pendingIntent);
		// System.currentTimeMillis(), (5000), pendingIntent);
	}

}