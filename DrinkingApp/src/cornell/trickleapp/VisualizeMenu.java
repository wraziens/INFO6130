package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cornell.trickleapp.R;
import cornell.trickleapp.R.id;
import cornell.trickleapp.R.layout;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class VisualizeMenu extends Activity implements OnClickListener {

	ImageButton exercise, drink, social, food, mood, productivity;
	Intent goToThisPage;
	TextView drinkScore, exerciseScore, productivityScore, moodScore;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		db = new DatabaseHandler(this);
		setContentView(R.layout.vmenu);
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		exercise = (ImageButton) findViewById(R.id.bExercise);
		drink = (ImageButton) findViewById(R.id.bDrink);
		// social=(Button)findViewById(R.id.bSocialization);
		// food=(Button)findViewById(R.id.bFood);
		productivity = (ImageButton) findViewById(R.id.bProductivity);
		mood = (ImageButton) findViewById(R.id.bMood);
		Typeface tf = Typeface.createFromAsset(getBaseContext().getAssets(),
				"fonts/dust.ttf");
		
		
		drinkScore = (TextView) findViewById(R.id.tvVMenuDrink);
		exerciseScore = (TextView) findViewById(R.id.tvVMenuExercise);
		productivityScore = (TextView) findViewById(R.id.tvVMenuProductivity);
		moodScore = (TextView) findViewById(R.id.tvVMenuMood);
		
		drinkScore.setTypeface(tf);
		exerciseScore.setTypeface(tf);
		productivityScore.setTypeface(tf);
		moodScore.setTypeface(tf);
		
		
		// set up the scores for each category

		// exercise
		setAverage(1);
		setAverage(4);

		Boolean checkExercise = getPrefs.getBoolean("exerciseInEvaluation",
				true);
		if (!checkExercise) {
			Intent openHint = new Intent(this, VisualizationMenuTutorial.class);
			exercise.setAlpha(50);
			exerciseScore.setText("OFF");
			exercise.setEnabled(false);
		} else if (checkExercise) {
			setAverage(2);

		}
		Boolean checkProductivity = getPrefs.getBoolean(
				"productivityInEvaluation", true);
		if (!checkProductivity) {
			productivity.setAlpha(50);
			productivityScore.setText("OFF");
			productivity.setEnabled(false);
		} else if (checkProductivity) {
			setAverage(3);
		}
		exercise.setOnClickListener(this);
		drink.setOnClickListener(this);
		// social.setOnClickListener(this);
		// food.setOnClickListener(this);
		productivity.setOnClickListener(this);
		mood.setOnClickListener(this);

		if (checkSurveyed) {
			Intent openHint = new Intent(this, VisualizationMenuTutorial.class);
			startActivity(openHint);
		}

	}

	private void setAverage(int category) {
		// TODO Auto-generated method stub

		List<DatabaseStore> firstTimeList = db.getAllVarValue("firstTime");
		Date firstDate = firstTimeList.get(0).date;
		Date currentDate = new Date();
		int daysDifference = (int) ((currentDate.getTime() - firstDate
				.getTime()) / 86400000);

		Double totalScore = 0.0;

		switch (category) {
		case 1:
			// list of total drinks in a day
			ArrayList<DatabaseStore> clickDrinkCountList = db.getDrinkCountForAllDays();
			List<DatabaseStore> estimateDrinkCountList = db
					.getAllVarValue("drink_guess");
			SharedPreferences getPrefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			int goal = Integer.parseInt(getPrefs.getString("goal", "2"));
			int total=0;
			if (clickDrinkCountList!=null)
				total=total+clickDrinkCountList.size();
			if (estimateDrinkCountList!=null)
				total=total+estimateDrinkCountList.size();
			if (total < 1)
				total = 0;
			double score = 0;

			if (estimateDrinkCountList != null) {
				for (int i = 0; i < estimateDrinkCountList.size(); i++) {
					if (Integer.parseInt(estimateDrinkCountList.get(i).value) <= goal)
						score=score+1;
				}
			}
			if (estimateDrinkCountList != null) {
				for (int i = 0; i < estimateDrinkCountList.size(); i++) {
					if (Integer.parseInt(estimateDrinkCountList.get(i).value) <= goal)
						score=score+1;
				}
			}
			drinkScore.setText(getGradeFromDouble((Double)((score / total)*100)));

			break;
		case 2:
			// gets 1st time, figure out number of days from 1st time until now,
			// and
			// divide score by that number
			List<DatabaseStore> exerciseList = db
					.getAllVarValue("exercise_quality");
			if (exerciseList != null) {
				for (int i = 0; i < exerciseList.size(); i++) {
					String s = exerciseList.get(i).value;
					totalScore = totalScore + Integer.parseInt(s);
				}
				exerciseScore
						.setText(getGradeFromDouble((Double) (totalScore / (daysDifference + 1)))
								);

			}
			else
				exerciseScore.setText("N/A");
			break;
		case 3:
			List<DatabaseStore> productiveList = db
					.getAllVarValue("productive");
			List<DatabaseStore> performanceList = db
					.getAllVarValue("performance");
			List<DatabaseStore> stressLevelList = db
					.getAllVarValue("stress_level");

			if (productiveList != null) {
				for (int i = 0; i < productiveList.size(); i++) {
					int productivityVal = Integer.parseInt(productiveList
							.get(i).value);
					int performanceVal = Integer.parseInt(performanceList
							.get(i).value);
					int stressVal = 100 - Integer.parseInt(stressLevelList
							.get(i).value);

					totalScore = totalScore
							+ ((productivityVal + productivityVal + stressVal) / 3);
				}
				productivityScore
						.setText(getGradeFromDouble((Double) (totalScore / (daysDifference + 1)))
								);

			}
			else
				productivityScore.setText("N/A");
			break;
		case 4:
			List<DatabaseStore> moodList = db.getAllVarValue("moodScore");
			int numberOfMoods = 0;
			if (db.getAllVarValue("wordle") != null)
				numberOfMoods = db.getAllVarValue("wordle").size();

			if (moodList != null) {
				for (int i = 0; i < moodList.size(); i++) {
					int moodVal = Integer.parseInt(moodList.get(i).value);

					totalScore = totalScore + moodVal;
				}
				if (totalScore < 0)
					totalScore = 0.0;
				moodScore
						.setText(getGradeFromDouble(((Double) (totalScore / (numberOfMoods) * 100))
								));

			}
			else
				moodScore.setText("N/A");
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bExercise:
			goToThisPage = new Intent(this, ExerciseVisualization.class);
			startActivity(goToThisPage);
			break;
		case R.id.bDrink:
			goToThisPage = new Intent(this, DrinkCalendar.class);
			startActivity(goToThisPage);
			break;
		/*
		 * case R.id.bSocialization: goToThisPage = new Intent(this,
		 * SocialVisualization.class); startActivity(goToThisPage); break;
		 */
		/*
		 * case R.id.bFood: goToThisPage = new Intent(this,
		 * FoodVisualization.class); startActivity(goToThisPage); break;
		 */
		case R.id.bProductivity:
			goToThisPage = new Intent(this, ProductivityVisualization.class);
			startActivity(goToThisPage);
			break;

		case R.id.bMood:
			goToThisPage = new Intent(this, MoodVisualization.class);
			startActivity(goToThisPage);
			break;

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
		/*
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
*/

		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	private String getGradeFromDouble(Double rawGrade) {
		String grade = "N/A";
		
		
			if (rawGrade >= 90)
				grade = "A";
			else if (rawGrade >= 80 && rawGrade < 90)
				grade = "B";
			else if (rawGrade >= 70 && rawGrade < 80)
				grade = "C";
			else if (rawGrade >= 60 && rawGrade < 70)
				grade = "D";
			else if (rawGrade < 60)
				grade = "F";


		return grade;
	}


}
