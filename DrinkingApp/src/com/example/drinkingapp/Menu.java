package com.example.drinkingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Menu extends Activity implements OnClickListener {

	Button tracking, assessment, visualize, settings, resources;
	Intent goToThisPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu);
		tracking = (Button) findViewById(R.id.bMenuTracking);
		assessment = (Button) findViewById(R.id.bMenuAssessment);
		visualize = (Button) findViewById(R.id.bMenuVisualize);
		settings = (Button) findViewById(R.id.bMenuSettings);
		resources = (Button) findViewById(R.id.bMenuResources);

		tracking.setOnClickListener(this);
		assessment.setOnClickListener(this);
		visualize.setOnClickListener(this);
		settings.setOnClickListener(this);
		resources.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bMenuTracking:
			//goToThisPage = new Intent(Menu.this,Class.Tracking);
			goToThisPage = new Intent(Menu.this, DrinkCounter.class);
			startActivity(goToThisPage);
			break;
		case R.id.bMenuAssessment:
			goToThisPage = new Intent(Menu.this, Assessment.class);
			startActivity(goToThisPage);
			break;
		case R.id.bMenuVisualize:
			goToThisPage = new Intent(Menu.this,Visualize.class);
			startActivity(goToThisPage);
			break;
		case R.id.bMenuSettings:
			goToThisPage = new Intent(Menu.this,Settings.class);
			startActivity(goToThisPage);
			break;
		case R.id.bMenuResources:
			goToThisPage = new Intent(Menu.this, Resources.class);
			startActivity(goToThisPage);
			break;
		}

	}

}
