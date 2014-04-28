package cornell.trickleapp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.Poptart;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class GoalsTracking extends BaseActivity implements OnClickListener,
		OnDrawerOpenListener {

	Poptart mPoptart;
	DatabaseHandler db;
	SlidingDrawer sdKiipRewards;
	TextView tvAchievementMessage;
	
	int DaysPerWeek_val, DrinksPerOuting_val, BAC_val, DaysPerMonth_val,
			DrinksPerMonth_val, SpendingPerMonth_val;

	Button bDaysPerWeek_star, bDrinksPerOuting_star, bBAC_star,
			bDaysPerMonth_star, bDrinksPerMonth_star, bSpendingPerMonth_star;
	Button bDaysPerWeek_clear, bDrinksPerOuting_clear, bBAC_clear,
			bDaysPerMonth_clear, bDrinksPerMonth_clear,
			bSpendingPerMonth_clear;
	Button track;

	LinearLayout llDaysPerWeekText, lDrinksPerOutingText, llBACText,
			llDaysPerMonthText, llDrinksPerMonthText, llSpendingPerMonthText;

	Boolean DaysPerWeek = false, DrinksPerOuting = false, BAC = false,
			DaysPerMonth = false, DrinksPerMonth = false,
			SpendingPerMonth = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(getBaseContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.goalstracking);
		// badges that show up when goal is tracked
		bDaysPerWeek_star = (Button) findViewById(R.id.bDaysPerWeek_star);
		bDrinksPerOuting_star = (Button) findViewById(R.id.bDrinksPerOuting_star);
		bBAC_star = (Button) findViewById(R.id.bBAC_star);
		bDaysPerMonth_star = (Button) findViewById(R.id.bDaysPerMonth_star);
		bDrinksPerMonth_star = (Button) findViewById(R.id.bDrinksPerMonth_star);
		bSpendingPerMonth_star = (Button) findViewById(R.id.bSpendingPerMonth_star);

		// badges that show up when goal is not tracked
		bDaysPerWeek_clear = (Button) findViewById(R.id.bDaysPerWeek_clear);
		bDrinksPerOuting_clear = (Button) findViewById(R.id.bDrinksPerOuting_clear);
		bBAC_clear = (Button) findViewById(R.id.bBAC_clear);
		bDaysPerMonth_clear = (Button) findViewById(R.id.bDaysPerMonth_clear);
		bDrinksPerMonth_clear = (Button) findViewById(R.id.bDrinksPerMonth_clear);
		bSpendingPerMonth_clear = (Button) findViewById(R.id.bSpendingPerMonth_clear);

		// texts underneath that show up when goal is not tracked
		llDaysPerWeekText = (LinearLayout) findViewById(R.id.llDaysPerWeekText);
		lDrinksPerOutingText = (LinearLayout) findViewById(R.id.lDrinksPerOutingText);
		llBACText = (LinearLayout) findViewById(R.id.llBACText);
		llDaysPerMonthText = (LinearLayout) findViewById(R.id.llDaysPerMonthText);
		llDrinksPerMonthText = (LinearLayout) findViewById(R.id.llDrinksPerMonthText);
		llSpendingPerMonthText = (LinearLayout) findViewById(R.id.llSpendingPerMonthText);

		// to go back to set goals page
		track = (Button) findViewById(R.id.bSet);
		track.setOnClickListener(this);

		sdKiipRewards = (SlidingDrawer) findViewById(R.id.sdKiipRewards);
		sdKiipRewards.setOnDrawerOpenListener(this);
		tvAchievementMessage= (TextView) findViewById(R.id.tvAchievementMessage);
		// bDaysPerWeek_star.setBackgroundResource(R.drawable.star_2);
		if (db.variableExistAll("reward_kiip")){
			sdKiipRewards.setVisibility(View.VISIBLE);
			achievementMesage();
		}
		//deletes home page notification after coming to this page
		if (db.variableExistAll("reward_kiip_home")){
			db.deleteAllVariables("reward_kiip_home");
		}
		
		initialize();
	}

	// pulls the correct badges from database
	private void initialize() {
		// TODO Auto-generated method stub

		// iterate through list of goals being tracked
		if (db.getAllVarValue("goal_checked") != null) {
			List<DatabaseStore> goals_list = db.getAllVarValue("goal_checked");
			for (int i = 0; i < goals_list.size(); i++) {
				if (goals_list.get(i).value.equals("1")) {
					DaysPerWeek = true;
				}
				if (goals_list.get(i).value.equals("2")) {
					DrinksPerOuting = true;
				}
				if (goals_list.get(i).value.equals("3")) {
					BAC = true;
				}
				if (goals_list.get(i).value.equals("4")) {
					DaysPerMonth = true;
				}
				if (goals_list.get(i).value.equals("5")) {
					DrinksPerMonth = true;
				}
				if (goals_list.get(i).value.equals("6")) {
					SpendingPerMonth = true;
				}
			}
		}

		// check if each of the badges are being tracked
		if (DaysPerWeek)
			badgeSetup(bDaysPerWeek_clear, bDaysPerWeek_star,
					llDaysPerWeekText, calcStars(1), true);
		else
			badgeSetup(bDaysPerWeek_clear, bDaysPerWeek_star,
					llDaysPerWeekText, 5, false);
		if (DrinksPerOuting)
			badgeSetup(bDrinksPerOuting_clear, bDrinksPerOuting_star,
					lDrinksPerOutingText, calcStars(2), true);
		else
			badgeSetup(bDrinksPerOuting_clear, bDrinksPerOuting_star,
					lDrinksPerOutingText, 5, false);
		if (BAC)
			badgeSetup(bBAC_clear, bBAC_star, llBACText, calcStars(3), true);
		else
			badgeSetup(bBAC_clear, bBAC_star, llBACText, 5, false);
		if (DaysPerMonth)
			badgeSetup(bDaysPerMonth_clear, bDaysPerMonth_star,
					llDaysPerMonthText, calcStars(4), true);
		else
			badgeSetup(bDaysPerMonth_clear, bDaysPerMonth_star,
					llDaysPerMonthText, 5, false);
		if (DrinksPerMonth)
			badgeSetup(bDrinksPerMonth_clear, bDrinksPerMonth_star,
					llDrinksPerMonthText, calcStars(5), true);
		else
			badgeSetup(bDrinksPerMonth_clear, bDrinksPerMonth_star,
					llDrinksPerMonthText, 5, false);
		if (SpendingPerMonth)
			badgeSetup(bSpendingPerMonth_clear, bSpendingPerMonth_star,
					llSpendingPerMonthText, calcStars(6), true);
		else
			badgeSetup(bSpendingPerMonth_clear, bSpendingPerMonth_star,
					llSpendingPerMonthText, 5, false);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	// makes badges/text appear when goal is tracked, and makes them
	// dissapear/replaced
	// by empty badge otherwise
	// number of stars vary from 1 to 8
	private void badgeSetup(Button emptyCover, Button stars,
			LinearLayout textBox, int star_num, boolean visibility) {
		if (visibility) {
			switch (star_num) {
			case 0:
				stars.setBackgroundResource(R.drawable.star_0);
				break;
			case 1:
				stars.setBackgroundResource(R.drawable.star_1);
				break;
			case 2:
				stars.setBackgroundResource(R.drawable.star_2);
				break;
			case 3:
				stars.setBackgroundResource(R.drawable.star_3);
				break;
			case 4:
				stars.setBackgroundResource(R.drawable.star_4);
				break;
			case 5:
				stars.setBackgroundResource(R.drawable.star_5);
				break;
			case 6:
				stars.setBackgroundResource(R.drawable.star_6);
				break;
			case 7:
				stars.setBackgroundResource(R.drawable.star_7);
				break;
			case 8:
				stars.setBackgroundResource(R.drawable.star_8);
				break;
			default:
				stars.setBackgroundResource(R.drawable.star_8);
			}
		} else {
			emptyCover.setVisibility(View.VISIBLE);
			textBox.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bSet:
			Intent goToThisPage = new Intent(GoalsTracking.this,
					GoalsLayout.class);
			startActivity(goToThisPage);
			break;
		case R.id.handle:
			sdKiipRewards.toggle();
			break;
		}

	}

	// takes goal variable name in database and outputs number of stars
	// associated with that goal
	private int calcStars(int goal) {
		String variable = "";

		switch (goal) {
		case 1:
			variable = "star_DaysPerWeek";
			break;
		case 2:
			variable = "star_DrinksPerOuting";
			break;
		case 3:
			variable = "star_BAC";
			break;
		case 4:
			variable = "star_DaysPerMonth";
			break;
		case 5:
			variable = "star_DrinksPerMonth";
			break;
		case 6:
			variable = "star_DollarsPerMonth";
			break;
		}

		List<DatabaseStore> list = db.getAllVarValue(variable);
		if (list == null)
			return 0;
		else
			return list.size();
	}

	@Override
	public void onDrawerOpened() {
		// TODO Auto-generated method stub
		sdKiipRewards.setVisibility(View.INVISIBLE);
		Kiip.getInstance()
				.saveMoment(
						"Goal Achievement!",
						5, new Kiip.Callback() {
							@Override
							public void onFailed(Kiip kiip, Exception exception) {
								// no-op
							}

							@Override
							public void onFinished(Kiip kiip, Poptart poptart) {
								if (poptart != null) {
									// Display the notification information in
									// your UI
									// showIntegratedNotification(poptart.getNotification());

									// Clear the notification in the poptart so
									// it doesn't display later
									// poptart.setNotification(null);
									// poptart.show(getBaseContext());
									onPoptart(poptart);
									// Save the poptart to display later
									// mPoptart = poptart;
									// showRewards.setVisibility(View.VISIBLE);
								}
							}
						});
	}
	//replaces the current message with correct one, then delete the database entry correlated to that value
	private void achievementMesage(){
		int category=Integer.parseInt(db.getAllVarValue("reward_kiip").get(0).value);
		String message="";
		switch(category){
		case 1:
			message="Congrats! You limited # of days you went drinking this week. Pull to unlock your rewards!";
			break;
		case 2:
			message="Congrats! You kept your # of Drinks per outing to a minimum! Pull to unlock your rewards!";
			break;
		case 3:
			message="Congrats! You kept your BAC in check!! Pull to unlock your rewards!";
			break;
		case 4:
			message="Congrats! You limited # of days you went drinking this month! Pull to unlock your rewards!";
			break;
		case 5:
			message="Congrats! You kept your # of Drinks per month to a minimum! Pull to unlock your rewards!";
			break;
		case 6:
			message="Congrats! You kept your $ spent this month to a minimum! Pull to unlock your rewards!";
			break;
		}
		
		
		tvAchievementMessage.setText(message);
		//delete entry from database
		db.deleteAllVariablesByValue("reward_kiip", ""+category);
		int stupid=1;
	}
}
