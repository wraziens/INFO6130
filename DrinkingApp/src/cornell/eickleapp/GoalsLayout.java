package cornell.eickleapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

	Button greenBACButton, yellowBACButton, redBACButton, DaysPerWeek,
			DrinksPerOuting, DaysPerMonth, DrinksPerMonth, DollarsPerMonth,
			finish, track;

	CheckBox cbDaysPerWeek, cbDrinksPerOuting, cbBAC, cbDaysPerMonth,
			cbDrinksPerMonth, cbDollarsPerMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		if (true) {
			cbDaysPerWeek.setChecked(true);
			DaysPerWeek.setText("data");
		}
		if (true) {
			cbDrinksPerOuting.setChecked(true);
			// detect which square was clicked
			if (false)// green was clicked
				greenBACButton.setText("<0.06");

			if (false)// yellow was clicked
				yellowBACButton.setText("<0.15");

			if (false)// red was clicked
				redBACButton.setText("<0.24");
		}
		if (true) {
			cbBAC.setChecked(true);
			DaysPerWeek.setText("0");
		}
		if (true) {
			cbDaysPerMonth.setChecked(true);
			DaysPerMonth.setText("0");
		}
		if (true) {
			cbDrinksPerMonth.setChecked(true);
			DrinksPerMonth.setText("0");
		}
		if (true) {
			cbDollarsPerMonth.setChecked(true);
			DollarsPerMonth.setText("0");
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bTrack:
			Intent goToThisPage = new Intent(GoalsLayout.this, GoalsTracking.class);
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
			if (cbDaysPerWeek.isChecked()) {
				// store
				// =bDaysPerWeek.getText();
			}
			if (cbDrinksPerOuting.isChecked()) {
				// store
				// =bDrinksPerOuting.getText();
			}
			if (cbBAC.isChecked()) {
				if (greenBACButton.isSelected()) {
					// =bGreenBAC.getText();
					// limit bac variable selected as 1
				}
				if (yellowBACButton.isSelected()) {
					// =bYellowBAC.getText();
					// limit bac variable selected as 2
				}
				if (redBACButton.isSelected()) {
					// =bRedBAC.getText();
					// limit bac variable selected as 3
				}

			}
			if (cbDaysPerMonth.isChecked()) {
				// store
				// =bDaysPerMonth.getText();
			}
			if (cbDrinksPerMonth.isChecked()) {
				// store
				// =bDrinksPerMonth.getText();
			}
			if (cbDollarsPerMonth.isChecked()) {
				// store
				// =bDollarsPerMonth.getText();
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
