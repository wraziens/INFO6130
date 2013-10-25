package com.example.drinkingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InitialSurvey extends Activity implements OnClickListener{

	TextView test;
	Button finish;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initialsurvey);
		test=(TextView)findViewById(R.id.test);
		finish=(Button)findViewById(R.id.bISFinish);
		finish.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
			case R.id.bISFinish:
				test.setText("TESTED");
				SharedPreferences getPrefs=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor editPrefs=getPrefs.edit();
				editPrefs.putBoolean("initialSurvey", true);
				editPrefs.commit();
				
				Intent goToMenu=new Intent(this,Menu.class);
				startActivity(goToMenu);
				break;
			
		}
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
