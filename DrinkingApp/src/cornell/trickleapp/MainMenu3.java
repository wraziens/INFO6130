package cornell.trickleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class MainMenu3 extends Activity implements OnClickListener,
		OnDrawerOpenListener {

	Button drinkCounterMenu, surveyMenu, calendarMenu, trendsMenu, goalsMenu;
	DatabaseHandler db;
	SlidingDrawer sdKiipRewards;
	TextView tvAchievementMessage;
	FlyOutContainer root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//initializes the database
		db = new DatabaseHandler(getBaseContext());
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

		sdKiipRewards = (SlidingDrawer) findViewById(R.id.sdKiipRewards);
		sdKiipRewards.setOnDrawerOpenListener(this);
		tvAchievementMessage = (TextView) findViewById(R.id.tvAchievementMessage);
		// bDaysPerWeek_star.setBackgroundResource(R.drawable.star_2);
		if (db.variableExistAll("reward_kiip_home")) {
			sdKiipRewards.setVisibility(View.VISIBLE);
		}

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
		case R.id.handle:
			sdKiipRewards.toggle();
			break;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		db = new DatabaseHandler(getBaseContext());
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
		
		sdKiipRewards = (SlidingDrawer) findViewById(R.id.sdKiipRewards);
		sdKiipRewards.setOnDrawerOpenListener(this);
		tvAchievementMessage = (TextView) findViewById(R.id.tvAchievementMessage);
		if (db.variableExistAll("reward_kiip_home")) {
			sdKiipRewards.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDrawerOpened() {
		// TODO Auto-generated method stub
		sdKiipRewards.setVisibility(View.INVISIBLE);
		Intent goToThisPage = new Intent(MainMenu3.this, GoalsTracking.class);
		startActivity(goToThisPage);
	}

}
