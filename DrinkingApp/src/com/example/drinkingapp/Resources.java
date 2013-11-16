package com.example.drinkingapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Resources extends Activity implements OnClickListener{


	Button home, tracking, assessment, visualize, settings, resources,gannett;
	Intent goToThisPage,call;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.resources);
		home=(Button)findViewById(R.id.bMenuBottomHome);
		tracking=(Button)findViewById(R.id.bMenuBottomTracking);
		assessment=(Button)findViewById(R.id.bMenuBottomAssessment);
		visualize=(Button)findViewById(R.id.bMenuBottomVisualize);
		settings=(Button)findViewById(R.id.bMenuBottomSettings);
		gannett=(Button)findViewById(R.id.bCallGannett);
		
		home.setOnClickListener(this);
		tracking.setOnClickListener(this);
		assessment.setOnClickListener(this);
		visualize.setOnClickListener(this);
		settings.setOnClickListener(this);
		gannett.setOnClickListener(this);
		//resources.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bMenuBottomHome:
			finish();
			break;
		case R.id.bMenuBottomTracking:
			finish();
			goToThisPage = new Intent(this,DrinkCounter.class);
			startActivity(goToThisPage);
			break;
		case R.id.bMenuBottomAssessment:
			finish();
			goToThisPage = new Intent(this,Assessment.class);
			startActivity(goToThisPage);
			break;
		case R.id.bMenuBottomVisualize:
			finish();
			goToThisPage = new Intent(this, VisualizeMenu.class);
			startActivity(goToThisPage);
			break;
		case R.id.bMenuBottomSettings:
			finish();
			goToThisPage = new Intent(this, Settings.class);
			startActivity(goToThisPage);
			break;
		case R.id.bCallGannett:
			 try {
                  call = new Intent(Intent.ACTION_CALL);
                  call.setData(Uri.parse("tel:6072550269"));
                 startActivity(call);
             } catch (ActivityNotFoundException activityException) {
                 Log.e("Calling a Phone Number", "Call failed", activityException);
             }
			break;
		}

	}

}
