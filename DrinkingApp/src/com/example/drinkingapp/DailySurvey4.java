package com.example.drinkingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;


public class DailySurvey4 extends Activity implements OnClickListener{

	private Button finish;
	private RadioGroup radioGroup;
	private EditText other_ans;
	private String locationResult = null;
	private DatabaseHandler db;
	
	private void addDrinkLocation(){
		db = new DatabaseHandler(this);
		finish=(Button)findViewById(R.id.location_finish);
		finish.setOnClickListener(this);
		
		radioGroup=(RadioGroup)findViewById(R.id.location_group);
		radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.bar_radio:
						locationResult = "Bar";
						break;
					case R.id.restaurant_radio:
						locationResult = "Restaurant";
						break;
					case R.id.myhome_radio:
						locationResult = "Home";
						break;
					case R.id.friend_radio:
						locationResult = "Friend";
						break;
					case R.id.school_radio:
						locationResult = "School";
						break;	
					case R.id.frat_radio:
						locationResult = "Fraternity";
						break;
					case R.id.sport_radio:
						locationResult = "Public";
						break;
					case R.id.other_radio:
						locationResult = other_ans.getText().toString();
						break;
					default:
						throw new RuntimeException("Unknown Button ID For Location Question.");
				}		
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.dailysurvey4);
		
		//add questions to the survey
		addDrinkLocation();
	}

	private void saveToDB(){
		if (locationResult != null){
			db.addValue("location", locationResult);
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.location_finish:
				saveToDB();
				finish();
				break;
		}
	}

	protected void onPause() {
		super.onPause();
		finish();
	}

}
