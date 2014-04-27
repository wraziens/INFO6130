package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class GoalsLayout extends Activity implements OnClickListener {

	AlarmManager alarmManager;

	DatabaseHandler db;

	Button greenBACButton, yellowBACButton, redBACButton, DaysPerWeek,
			DrinksPerOuting, DaysPerMonth, DrinksPerMonth, DollarsPerMonth,
			finish, track;

	CheckBox cbDaysPerWeek, cbDrinksPerOuting, cbBAC, cbDaysPerMonth,
			cbDrinksPerMonth, cbDollarsPerMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(getBaseContext());
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.goalslayout);
		greenBACButton = (Button) findViewById(R.id.bGreenBAC);
		yellowBACButton = (Button) findViewById(R.id.bYellowBAC);
		redBACButton = (Button) findViewById(R.id.bRedBAC);
		DaysPerWeek = (Button) findViewById(R.id.bDaysPerWeek);
		DrinksPerOuting = (Button) findViewById(R.id.bDrinksPerOuting);
		DaysPerMonth = (Button) findViewById(R.id.bDaysPerMonth);
		DrinksPerMonth = (Button) findViewById(R.id.bDrinksPerMonth);
		DollarsPerMonth = (Button) findViewById(R.id.bDollarsPerMonth);
		finish = (Button) findViewById(R.id.bFinish);
		track = (Button) findViewById(R.id.bTrack);

		cbDaysPerWeek = (CheckBox) findViewById(R.id.cbDaysPerWeek);
		cbDrinksPerOuting = (CheckBox) findViewById(R.id.cbDrinksPerOuting);
		cbBAC = (CheckBox) findViewById(R.id.cbBAC);
		cbDaysPerMonth = (CheckBox) findViewById(R.id.cbDaysPerMonth);
		cbDrinksPerMonth = (CheckBox) findViewById(R.id.cbDrinksPerMonth);
		cbDollarsPerMonth = (CheckBox) findViewById(R.id.cbDollarsPerMonth);

		greenBACButton.setOnClickListener(this);
		// initialize with green BAC level selected
		greenBACButton.setSelected(true);
		yellowBACButton.setOnClickListener(this);
		redBACButton.setOnClickListener(this);
		DaysPerWeek.setOnClickListener(this);
		DrinksPerOuting.setOnClickListener(this);
		DaysPerMonth.setOnClickListener(this);
		DrinksPerMonth.setOnClickListener(this);
		DollarsPerMonth.setOnClickListener(this);
		finish.setOnClickListener(this);
		track.setOnClickListener(this);

		initialize();

	}

	// grabs data from database and populate the checkboxes, and value for
	private void initialize() {
		// TODO Auto-generated method stub
		// iterates through list of goals checked and generate the information
		// accordingly
		// to show the checked static page for goals setting from previous
		// sessions
		if (db.variableExistAll("goal_checked")) {
			List<DatabaseStore> goals_checked = db
					.getAllVarValue("goal_checked");
			if (!goals_checked.isEmpty()) {
				for (int i = 0; i < goals_checked.size(); i++) {
					String dummy = goals_checked.get(i).value;
					if (goals_checked.get(i).value.equals("1")) {
						cbDaysPerWeek.setChecked(true);
						DaysPerWeek.setText(db.getAllVarValue(
								"goal_DaysPerWeek").get(0).value);
					}
					if (goals_checked.get(i).value.equals("2")) {
						cbDrinksPerOuting.setChecked(true);
						DrinksPerOuting.setText(db.getAllVarValue(
								"goal_DrinksPerOuting").get(0).value);
					}
					if (goals_checked.get(i).value.equals("3")) {
						cbBAC.setChecked(true);

						String colorSelected = db.getAllVarValue(
								"goal_BAC_color").get(0).value;
						// detect which square was clicked and select+change
						// value
						// for that button
						// green was clicked
						if (colorSelected.equals("1")) {
							greenBACButton.setSelected(true);
							greenBACButton.setText(db.getAllVarValue(
									"goal_BAC_val").get(0).value);
						}
						// yellow was clicked
						if (colorSelected.equals("2")) {
							yellowBACButton.setSelected(true);
							greenBACButton.setSelected(false);
							yellowBACButton.setText(db.getAllVarValue(
									"goal_BAC_val").get(0).value);
						}
						// red was clicked
						if (colorSelected.equals("3")) {
							redBACButton.setSelected(true);
							greenBACButton.setSelected(false);
							redBACButton.setText(db.getAllVarValue(
									"goal_BAC_val").get(0).value);
						}
					}
					if (goals_checked.get(i).value.equals("4")) {
						cbDaysPerMonth.setChecked(true);
						DaysPerMonth.setText(db.getAllVarValue(
								"goal_DaysPerMonth").get(0).value);
					}
					if (goals_checked.get(i).value.equals("5")) {
						cbDrinksPerMonth.setChecked(true);
						DrinksPerMonth.setText(db.getAllVarValue(
								"goal_DrinksPerMonth").get(0).value);
					}
					if (goals_checked.get(i).value.equals("6")) {
						cbDollarsPerMonth.setChecked(true);
						DollarsPerMonth.setText(db.getAllVarValue(
								"goal_DollarsPerMonth").get(0).value);
					}
				}
			}
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bTrack:
			Intent goToThisPage = new Intent(GoalsLayout.this,
					GoalsTracking.class);
			startActivity(goToThisPage);
			break;
		case R.id.bGreenBAC:
			progressBarBAC(1);
			greenBACButton.setSelected(true);
			yellowBACButton.setSelected(false);
			redBACButton.setSelected(false);
			break;

		case R.id.bYellowBAC:
			progressBarBAC(2);
			greenBACButton.setSelected(false);
			yellowBACButton.setSelected(true);
			redBACButton.setSelected(false);
			break;
		case R.id.bRedBAC:
			progressBarBAC(3);
			greenBACButton.setSelected(false);
			yellowBACButton.setSelected(false);
			redBACButton.setSelected(true);
			break;
		case R.id.bDaysPerWeek:
			progressBarGeneral(0, DaysPerWeek);
			break;
		case R.id.bDrinksPerOuting:
			progressBarGeneral(1, DrinksPerOuting);
			break;
		case R.id.bDaysPerMonth:
			progressBarGeneral(2, DaysPerMonth);
			break;
		case R.id.bDrinksPerMonth:
			progressBarGeneral(3, DrinksPerMonth);
			break;
		case R.id.bDollarsPerMonth:
			progressBarGeneral(4, DollarsPerMonth);
			break;
		case R.id.bFinish:
			// if these are checked store them into the database
			// the following array will have values of 1 to 6
			// corresponding
			// to whether or not checkboxes are checked
			// resets all values in goals checklist entry to clear for new entry
			if (db.variableExistAll("goal_checked"))
				db.deleteAllVariables("goal_checked");

			if (cbDaysPerWeek.isChecked()) {
				// store
				db.updateOrAdd("goal_DaysPerWeek", DaysPerWeek.getText()
						.toString());
				db.addValue("goal_checked", 1);
				setAlarm(604800000,1);// 7 days
			}
			if (cbDrinksPerOuting.isChecked()) {
				// store
				db.updateOrAdd("goal_DrinksPerOuting", DrinksPerOuting
						.getText().toString());

				db.addValue("goal_checked", 2);
			}
			// check if checkbox is checked, if yes store both color of
			// bac+Value (1=green 2=yellow 3=red)
			if (cbBAC.isChecked()) {
				if (greenBACButton.isSelected()) {
					// =bGreenBAC.getText();

					db.updateOrAdd("goal_BAC_color", 1);
					db.updateOrAdd("goal_BAC_val", greenBACButton.getText()
							.toString());

					db.addValue("goal_checked", 3);
				}
				if (yellowBACButton.isSelected()) {

					db.updateOrAdd("goal_BAC_color", 2);
					db.updateOrAdd("goal_BAC_val", yellowBACButton.getText()
							.toString());
					// limit bac variable selected as 2

					db.addValue("goal_checked", 3);
				}
				if (redBACButton.isSelected()) {

					db.updateOrAdd("goal_BAC_color", 3);
					db.updateOrAdd("goal_BAC_val", redBACButton.getText()
							.toString());
					// limit bac variable selected as 3

					db.addValue("goal_checked", 3);
				}

			}
			if (cbDaysPerMonth.isChecked()) {
				// store
				db.updateOrAdd("goal_DaysPerMonth", DaysPerMonth.getText()
						.toString());

				db.addValue("goal_checked", 4);
			}
			if (cbDrinksPerMonth.isChecked()) {
				// store
				db.updateOrAdd("goal_DrinksPerMonth", DrinksPerMonth.getText()
						.toString());

				db.addValue("goal_checked", 5);
			}
			if (cbDollarsPerMonth.isChecked()) {
				// store
				db.updateOrAdd("goal_DollarsPerMonth", DollarsPerMonth
						.getText().toString());
				db.addValue("goal_checked", 6);
			}
			break;
		}

	}

	private void progressBarBAC(final int typeBAC) {
		// TODO Auto-generated method stub
		double base = 0.00;

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.goals_bac_selector);
		Button close = (Button) dialog.findViewById(R.id.bSave);
		final TextView bacDisplay = (TextView) dialog
				.findViewById(R.id.tvBACChosen);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		SeekBar sk = (SeekBar) dialog.findViewById(R.id.sbBAC);
		switch (typeBAC) {
		case 1:
			sk.setMax(6);
			bacDisplay.setText(greenBACButton.getText().subSequence(1, 5));
			break;
		case 2:
			base = 0.06;
			sk.setMax(9);
			bacDisplay.setText(yellowBACButton.getText().subSequence(1, 5));
			break;
		case 3:
			base = 0.15;
			sk.setMax(9);
			bacDisplay.setText(redBACButton.getText().subSequence(1, 5));
			break;
		}
		final double baseFinal = base;
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				// since seekbar only allows 0-100 this manipulates it to 0.08
				double progressDouble = progress;
				double finalResult = progressDouble / 100 + baseFinal;
				String result = "" + (String.format("%.2f", finalResult));
				bacDisplay.setText(result);
			}
		});

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				switch (typeBAC) {
				case 1:
					greenBACButton.setText("<" + bacDisplay.getText());
					break;
				case 2:
					yellowBACButton.setText("<" + bacDisplay.getText());
					break;
				case 3:
					redBACButton.setText("<" + bacDisplay.getText());
					break;
				}
			}

		});
		dialog.show();

	}

	private void progressBarGeneral(final int type, final Button button) {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.goals_bac_selector);
		Button close = (Button) dialog.findViewById(R.id.bSave);
		final TextView valueDisplay = (TextView) dialog
				.findViewById(R.id.tvBACChosen);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		SeekBar sk = (SeekBar) dialog.findViewById(R.id.sbBAC);
		switch (type) {
		case 0:
			sk.setMax(5);
			valueDisplay.setText(button.getText());
			break;
		case 1:
			sk.setMax(15);
			valueDisplay.setText(button.getText());
			break;
		case 2:
			sk.setMax(20);
			valueDisplay.setText(button.getText());
			break;
		case 3:
			sk.setMax(40);
			valueDisplay.setText(button.getText());
			break;
		case 4:
			sk.setMax(2000);
			valueDisplay.setText(button.getText());
			break;
		}
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				valueDisplay.setText("" + progress);
			}
		});

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				button.setText(valueDisplay.getText());
			}

		});
		dialog.show();

	}

	// if the checkbox is confirmed, set the alarm designated for that specific
	// period
	private void setAlarm(int miliSec, int id) {

		Intent intent = new Intent(this, ReminderAlarm.class);
		intent.putExtra("id", id);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);

		Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeValue));

		// calendar.setTimeInMillis(calendar.getTimeInMillis() + 86400000);

		long todayMili = calendar.getTimeInMillis();

		long miliForAlarm = todayMili + miliSec;

		alarmManager.cancel(pendingIntent);
		alarmManager.set(AlarmManager.RTC_WAKEUP, miliForAlarm, pendingIntent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
