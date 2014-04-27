package cornell.eickleapp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class GoalsTracking extends Activity implements OnClickListener {

	DatabaseHandler db;

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

	Boolean DaysPerWeek, DrinksPerOuting, BAC, DaysPerMonth, DrinksPerMonth,
			SpendingPerMonth;

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

		// bDaysPerWeek_star.setBackgroundResource(R.drawable.star_2);

		initialize();
	}

	// pulls the correct badges from database
	private void initialize() {
		// TODO Auto-generated method stub

		// iterate through list of goals being tracked
		List<DatabaseStore> goals_list = db.getAllVarValue("goals_checked");
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

		// check if each of the badges are being tracked
		if (DaysPerWeek)
			badgeSetup(bDaysPerWeek_clear, bDaysPerWeek_star,
					llDaysPerWeekText, 1, true);
		else
			badgeSetup(bDaysPerWeek_clear, bDaysPerWeek_star,
					llDaysPerWeekText, 5, false);
		if (DrinksPerOuting)
			badgeSetup(bDrinksPerOuting_clear, bDrinksPerOuting_star,
					lDrinksPerOutingText, 2, true);
		else
			badgeSetup(bDrinksPerOuting_clear, bDrinksPerOuting_star,
					lDrinksPerOutingText, 5, false);
		if (BAC)
			badgeSetup(bBAC_clear, bBAC_star, llBACText, 3, true);
		else
			badgeSetup(bBAC_clear, bBAC_star, llBACText, 5, false);
		if (DaysPerMonth)
			badgeSetup(bDaysPerMonth_clear, bDaysPerMonth_star,
					llDaysPerMonthText, 4, true);
		else
			badgeSetup(bDaysPerMonth_clear, bDaysPerMonth_star,
					llDaysPerMonthText, 5, false);
		if (DrinksPerMonth)
			badgeSetup(bDrinksPerMonth_clear, bDrinksPerMonth_star,
					llDrinksPerMonthText, 6, true);
		else
			badgeSetup(bDrinksPerMonth_clear, bDrinksPerMonth_star,
					llDrinksPerMonthText, 5, false);
		if (SpendingPerMonth)
			badgeSetup(bSpendingPerMonth_clear, bSpendingPerMonth_star,
					llSpendingPerMonthText, 8, true);
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
		}
	}
}
