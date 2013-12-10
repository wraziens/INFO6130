package cornell.eickleapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class ExerciseVisualization extends Activity implements
		GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
		OnClickListener {

	private static final String DEBUG_TAG = "Gestures";
	private GestureDetectorCompat mDetector;
	ExerciseGraphics visual;
	float zoomVal = 1;
	
	private DatabaseHandler db;
	private ArrayList<Integer> daysDrinkList;
	private ArrayList<Integer> daysExercisedList;
	private ArrayList<Double> averageExerciseQualityList;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new DatabaseHandler(this);
		
		ArrayList<Date> date_list = new ArrayList<Date>();
		Date date =  new Date();
		date_list.add(date);
		//get the last 4 weeks
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		for(int i=0; i<4; i++){
			int value = -7 * (i+1);
			gc.add(Calendar.DAY_OF_YEAR, value);
			date_list.add(gc.getTime());
		}
		
		daysDrinkList = new ArrayList<Integer>();
		daysExercisedList = new ArrayList<Integer>();
		averageExerciseQualityList = new ArrayList<Double>();
		constructLists(date_list);
		
		
		//sample database
		/*
		
		daysDrinkList.add(2);
		daysDrinkList.add(4);
		daysDrinkList.add(5);
		daysExercisedList.add(1);
		daysExercisedList.add(3);
		daysExercisedList.add(2);
		averageExerciseQualityList.add(90.0);
		averageExerciseQualityList.add(60.0);
		averageExerciseQualityList.add(66.0);
*/
		
		visual = new ExerciseGraphics(this,daysDrinkList,daysExercisedList,averageExerciseQualityList);
		setContentView(visual);

		// Instantiate the gesture detector with the
		// application context and an implementation of
		// GestureDetector.OnGestureListener
		mDetector = new GestureDetectorCompat(this, this);
		// Set the gesture detector as the double tap
		// listener.
		mDetector.setOnDoubleTapListener(this);
		
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		if (checkSurveyed) {
			Intent openHint = new Intent(this, ExerciseVisualizationTutorial.class);
			startActivity(openHint);
		}
		
	}

	private void constructLists(ArrayList<Date> date_list){
		daysDrinkList.clear();
		daysExercisedList.clear();
		averageExerciseQualityList.clear();
		
		for (int i=0; i<4; i++){
			Date date = date_list.get(i);
			ArrayList<DatabaseStore> ex = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("exercise", date);
			ArrayList<DatabaseStore> quality = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("exercise_quality", date);
			ArrayList<DatabaseStore> drank = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("drank", date);
			if (ex!= null){
				daysExercisedList.add(ex.size());
			} else {
				daysExercisedList.add(0);
			}
			if (quality != null){
				int sum = 0;
				for (int j=0; j<quality.size(); j++){
					sum += Integer.parseInt(quality.get(j).value);
				}
				float val = (float)sum/(float)quality.size();
				averageExerciseQualityList.add(Double.valueOf(val));
			} else{
				averageExerciseQualityList.add(Double.valueOf(0));
			}
			if (drank != null){
				daysDrinkList.add(drank.size());
			} else{
				daysDrinkList.add(0);
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) {
		/*
		Log.d(DEBUG_TAG, "onDown: " + event.toString());
		float mouseX = event.getX();
		float mouseY = event.getY();
		float plusMaxX = visual.plusX + visual.plus.getWidth();
		float plusMinX = visual.plusX;
		float plusMaxY = visual.plusY + visual.plus.getHeight();
		float plusMinY = visual.plusY;
		if (mouseX <= (plusMaxX) && event.getX() >= plusMinX
				&& mouseY <= (plusMaxY) && event.getY() >= plusMinY) {
			visual.zoomVal = visual.zoomVal * 1.25f;
			setContentView(visual);
		}
		float minusMaxX = visual.minusX + visual.minus.getWidth();
		float minusMinX = visual.minusX;
		float minusMaxY = visual.minusY + visual.minus.getHeight();
		float minusMinY = visual.minusY;
		if (mouseX <= (minusMaxX) && event.getX() >= minusMinX
				&& mouseY <= (minusMaxY) && event.getY() >= minusMinY) {
			visual.zoomVal = visual.zoomVal / 1.25f;
			setContentView(visual);
		}
		*/

		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY) {
		Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		//out of bounds stop here.
		Log.d(DEBUG_TAG, "onScroll: " + e1.toString() + e2.toString());
		visual.setPosX(visual.posX - distanceX);

		setContentView(visual);

		return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event) {
		Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		Intent openPage;
		switch (item.getItemId()) {

		case R.id.tracking_menu:
			openPage = new Intent(this, DrinkCounter.class);
			startActivity(openPage);
			break;
		case R.id.assess_menu:
			openPage = new Intent(this, Assessment.class);
			startActivity(openPage);
			break;
		case R.id.visualize_menu:
			openPage = new Intent(this, VisualizeMenu.class);
			startActivity(openPage);
			break;
		case R.id.setting_menu:
			openPage = new Intent(this, Settings.class);
			startActivity(openPage);
			break;
		case android.R.id.home:
			openPage = new Intent(this, MainMenu.class);
			startActivity(openPage);
			break;

		}
		return true;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}

}
