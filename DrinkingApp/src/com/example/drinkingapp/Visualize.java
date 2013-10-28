package com.example.drinkingapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Visualize extends Activity implements OnClickListener {
	ImageButton dataUpdate;
	TextView dataDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "pzrwzzF69gXSQrxr9gmfhWVQq3it1UrLFxCbPyUw",
				"8dZZu0sRje5F4K31FwAmYXbdSmkCOTZvUIfQo1N1");

		ParseUser.enableAutomaticUser();

		ParseACL defaultACL = new ParseACL();

		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
		setContentView(R.layout.visualization);

		dataUpdate = (ImageButton) findViewById(R.id.ibDataUpdate);
		dataDisplay = (TextView) findViewById(R.id.tvData);
		dataUpdate.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.ibDataUpdate:
			Time timeNow=new Time();
			timeNow.setToNow();
			String time3339=timeNow.format3339(true);
			ParseQuery<ParseObject> query = ParseQuery.getQuery("drinkCounter");
			query.whereEqualTo("counterType", "drink");
			query.whereEqualTo("time3339", time3339);
			query.orderByAscending("createdAt");
			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
			    public void done(List<ParseObject> drinkList, ParseException e) {
			        if (e == null) {
			        	String drinkResult="";
			        	int totalSec=0;
			        	int initialSec=0;
			        	for (int n=0;n<drinkList.size();n++){
			        		int tempSec=0;
			        		tempSec=tempSec+Integer.parseInt(drinkList.get(n).getString("hour"))*360;
			        		tempSec=tempSec+Integer.parseInt(drinkList.get(n).getString("minute"))*60;
			        		tempSec=tempSec+Integer.parseInt(drinkList.get(n).getString("second"));
			        		if (n==0){
			        			initialSec=tempSec;
			        			totalSec=tempSec;
			        		}
			        		else
			        			totalSec=tempSec-initialSec;

			        	}
			        	drinkResult=drinkList.size()+" drinks in "+totalSec/60+" Minutes";
			        	dataDisplay.setText(drinkResult);
			        	
			        	
			        } else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			    }
			});
			
			break;
		}
		
	}
}
