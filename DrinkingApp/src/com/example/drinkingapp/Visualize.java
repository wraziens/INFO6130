package com.example.drinkingapp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
/*
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
*/

public class Visualize extends Activity implements OnClickListener {
	ImageButton dataUpdate;
	TextView dataDisplay;
	Graphics ourView;
	private DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		db = new DatabaseHandler(this);
		
		

		/*
		Parse.initialize(this, "pzrwzzF69gXSQrxr9gmfhWVQq3it1UrLFxCbPyUw",
				"8dZZu0sRje5F4K31FwAmYXbdSmkCOTZvUIfQo1N1");

		ParseUser.enableAutomaticUser();

		ParseACL defaultACL = new ParseACL();

		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
		*/
		setContentView(R.layout.visualization);

		dataUpdate = (ImageButton) findViewById(R.id.ibDataUpdate);
		dataDisplay = (TextView) findViewById(R.id.tvData);
		dataUpdate.setOnClickListener(this);
		
		Date date = new Date();
		Date something = new Date();

		int drink_count=0;
		
		//get drink counts
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>)db.getVarValuesForDay("drink_count", date);
		if (drink_count_vals != null){
			drink_count = drink_count_vals.size();
		}
		
		//get chicken counts
		ArrayList<DatabaseStore> chicken_count_vals =(ArrayList<DatabaseStore>)db.getVarValuesForDay("number_chickens", date);
		Integer chicken_count=0;
		if (chicken_count_vals != null){
		            chicken_count_vals = DatabaseStore.sortByTime(chicken_count_vals);
		            chicken_count = Integer.parseInt(chicken_count_vals.get(chicken_count_vals.size()-1).value);
		}
		
		ourView = new Graphics(this);
		ourView.drinkCount=drink_count;
		ourView.chickenCount=chicken_count;
		ourView.month=date.getMonth();
		ourView.day=date.getDay();
		setContentView(ourView);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		/*
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
	*/	
	}
}
