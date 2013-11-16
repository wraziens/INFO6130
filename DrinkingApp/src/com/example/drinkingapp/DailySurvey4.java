package com.example.drinkingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DailySurvey4 extends Activity implements OnClickListener {

	private Button finish;
	private RadioGroup radioGroup, companyGroup, objectiveGroup;
	private String locationResult = null;
	private String companyResult = null;
	private String objectiveResult = null;
	private DatabaseHandler db;

	private void addDrinkLocation() {

		finish = (Button) findViewById(R.id.location_finish);
		finish.setOnClickListener(this);

		radioGroup = (RadioGroup) findViewById(R.id.location_group);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.bar_radio:
							locationResult = "Bar";
							findViewById(R.id.other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.restaurant_radio:
							locationResult = "Restaurant";
							findViewById(R.id.other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.myhome_radio:
							locationResult = "Home";
							findViewById(R.id.other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.friend_radio:
							locationResult = "Friend";
							findViewById(R.id.other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.school_radio:
							locationResult = "School";
							findViewById(R.id.other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.frat_radio:
							locationResult = "Fraternity";
							findViewById(R.id.other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.sport_radio:
							locationResult = "Public";
							findViewById(R.id.other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.other_radio:
							locationResult = "Other";
							findViewById(R.id.other_ans).setVisibility(
									View.VISIBLE);
							break;
						default:
							throw new RuntimeException(
									"Unknown Button ID For Location Question.");
						}
					}
				});
	}

	private void addDrinkCompany() {
		companyGroup = (RadioGroup) findViewById(R.id.company_group);
		companyGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.myself_radio:
							companyResult = "Alone";
							findViewById(R.id.company_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.family_radio:
							companyResult = "Family";
							findViewById(R.id.company_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.male_radio:
							companyResult = "Male";
							findViewById(R.id.company_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.female_radio:
							companyResult = "Female";
							findViewById(R.id.company_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.mixed_radio:
							companyResult = "Mixed";
							findViewById(R.id.company_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.strangers_radio:
							companyResult = "Strangers";
							findViewById(R.id.company_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.so_radio:
							companyResult = "Significant Other";
							findViewById(R.id.company_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.company_other:
							companyResult = "Other";
							findViewById(R.id.company_other_ans).setVisibility(
									View.VISIBLE);
							break;
						default:
							throw new RuntimeException(
									"Unknown Button ID For Location Question.");
						}
					}

				});
	}

	private void addDrinkObjective() {
		objectiveGroup = (RadioGroup) findViewById(R.id.objective_group);
		objectiveGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.relax_radio:
							objectiveResult = "Relax";
							findViewById(R.id.objective_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.celebrate_radio:
							objectiveResult = "Celebrate";
							findViewById(R.id.objective_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.social_obj_radio:
							objectiveResult = "Social";
							findViewById(R.id.objective_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.forget_radio:
							objectiveResult = "Forget";
							findViewById(R.id.objective_other_ans).setVisibility(
									View.INVISIBLE);
							break;
						case R.id.objective_other:
							objectiveResult = "Other";
							findViewById(R.id.objective_other_ans).setVisibility(
									View.VISIBLE);
							break;
						default:
							throw new RuntimeException(
									"Unknown Button ID For Location Question.");
						}
					}

				});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the database
		db = new DatabaseHandler(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.dailysurvey4);

		// add questions to the survey
		addDrinkLocation();
		addDrinkCompany();
		addDrinkObjective();
	}

	private void saveToDB() {
		if (locationResult != null) {
			if (locationResult.equals("Other")) {
				locationResult = ((EditText) findViewById(R.id.other_ans))
						.getText().toString();
			}
			db.updateOrAdd("location", locationResult);
		}
		if (companyResult != null) {
			if (companyResult.equals("Other")) {
				companyResult = ((EditText) findViewById(R.id.company_other_ans))
						.getText().toString();
			}
			db.updateOrAdd("company", companyResult);
		}
		if (objectiveResult != null) {
			if (objectiveResult.equals("Other")) {
				objectiveResult = ((EditText) findViewById(R.id.objective_other_ans))
						.getText().toString();
			}
			db.updateOrAdd("objective", objectiveResult);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.location_finish:
			saveToDB();
			finish();
			break;
		}
	}

	protected void onPause() {
		super.onPause();
		finish();
	}

}
