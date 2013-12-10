package cornell.eickleapp;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FoodVisualization extends Activity {

	View visual;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// sample data lists
		int daysWithSkippedMeals = 4;
		int daysDidntSkipMeals = 2;
		double avgBacSkipMeal = 0.16;
		double avgBacNoSkipMeal = 0.09;
		int daysSkipMealHungOver = 3;
		int daysSkipMealSick = 2;
		int daysNoSkipHungOver = 1;
		int daysNoSkipMealSick = 0;
		

		visual = new FoodGraphics(this, daysWithSkippedMeals,
				daysDidntSkipMeals,avgBacSkipMeal, avgBacNoSkipMeal,daysSkipMealHungOver, daysSkipMealSick,
				daysNoSkipHungOver,daysNoSkipMealSick);
		
		setContentView(visual);
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

}
