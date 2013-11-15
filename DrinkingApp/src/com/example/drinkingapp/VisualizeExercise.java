package com.example.drinkingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class VisualizeExercise extends Activity implements
		GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
		OnClickListener {

	private static final String DEBUG_TAG = "Gestures";
	private GestureDetectorCompat mDetector;
	ExerciseGraphics visual;
	float zoomVal = 1;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		visual = new ExerciseGraphics(this);
		setContentView(visual);

		// Instantiate the gesture detector with the
		// application context and an implementation of
		// GestureDetector.OnGestureListener
		mDetector = new GestureDetectorCompat(this, this);
		// Set the gesture detector as the double tap
		// listener.
		mDetector.setOnDoubleTapListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) {
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

		Log.d(DEBUG_TAG, "onScroll: " + e1.toString() + e2.toString());
		visual.posX = visual.posX - distanceX;
		visual.posY = visual.posY - distanceY;

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
}
