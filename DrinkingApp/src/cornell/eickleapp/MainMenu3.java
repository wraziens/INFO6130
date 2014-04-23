package cornell.eickleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainMenu3 extends Activity implements OnClickListener {

	Button drinkCounterMenu, surveyMenu, calendarMenu, trendsMenu, goalsMenu;

	FlyOutContainer root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// getActionBar().hide();

		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.menu3, null);

		this.setContentView(root);

		drinkCounterMenu = (Button) findViewById(R.id.bDrinkCounterMenu);
		surveyMenu = (Button) findViewById(R.id.bSurveyMenu);
		calendarMenu = (Button) findViewById(R.id.bCalendarMenu);
		trendsMenu = (Button) findViewById(R.id.bTrendsMenu);
		goalsMenu = (Button) findViewById(R.id.bGoalsMenu);
		drinkCounterMenu.setOnClickListener(this);
		surveyMenu.setOnClickListener(this);
		calendarMenu.setOnClickListener(this);
		trendsMenu.setOnClickListener(this);
		goalsMenu.setOnClickListener(this);

	}

	public void toggleMenu(View v) {
		this.root.toggleMenu();
	}

	@Override
	public void onClick(View v) {
		Intent goToThisPage;
		switch (v.getId()) {

		case R.id.bDrinkCounterMenu:
			goToThisPage = new Intent(MainMenu3.this, DrinkCounter.class);
			startActivity(goToThisPage);
			break;
		case R.id.bTrendsMenu:
			goToThisPage = new Intent(MainMenu3.this, SocialVisualization.class);
			startActivity(goToThisPage);
			break;
		case R.id.bCalendarMenu:
			goToThisPage = new Intent(MainMenu3.this, DrinkCalendar.class);
			startActivity(goToThisPage);
			break;
		case R.id.bSurveyMenu:
			goToThisPage = new Intent(MainMenu3.this, AfterDrinkSurvey.class);
			startActivity(goToThisPage);
			break;
		case R.id.bGoalsMenu:
			goToThisPage = new Intent(MainMenu3.this, GoalsLayout.class);
			startActivity(goToThisPage);
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.menu3, null);

		this.setContentView(root);

		drinkCounterMenu = (Button) findViewById(R.id.bDrinkCounterMenu);
		surveyMenu = (Button) findViewById(R.id.bSurveyMenu);
		calendarMenu = (Button) findViewById(R.id.bCalendarMenu);
		trendsMenu = (Button) findViewById(R.id.bTrendsMenu);
		goalsMenu = (Button) findViewById(R.id.bGoalsMenu);
		drinkCounterMenu.setOnClickListener(this);
		surveyMenu.setOnClickListener(this);
		calendarMenu.setOnClickListener(this);
		trendsMenu.setOnClickListener(this);
		goalsMenu.setOnClickListener(this);
	}
	
	
	
}
