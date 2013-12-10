package cornell.eickleapp;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DailySurveyExercise extends Activity implements OnClickListener{

	Button finish;
	SeekBar exerciseQualityBar;
	RadioGroup exerGroup;
	String seekbarResult = null;
	private DatabaseHandler db;
	String exer_str;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailysurveyexercise);
		db = new DatabaseHandler(this);
		
		finish=(Button)findViewById(R.id.bDSExerciseFinish);
		
		exerciseQualityBar=(SeekBar)findViewById(R.id.sbDSExercise);
		
		finish.setOnClickListener(this);
		exerciseQualityBar.setProgress(0); //this is line 19
		exerciseQualityBar.setMax(100);
		
		initializeSeekBar();
		exerciseToggle();
	}

	private void exerciseToggle(){
		exerGroup = (RadioGroup) findViewById(R.id.rgDSExercise);
		exerGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rbDSExerciseYes:
							exer_str = "yes";
							findViewById(R.id.exer_qual_prompt).setVisibility(
									View.VISIBLE);
							findViewById(R.id.exer_seekbar).setVisibility(View.VISIBLE);
							break;
						case R.id.rbDSExerciseNo:
							exer_str = "no";
							findViewById(R.id.exer_qual_prompt).setVisibility(
									View.INVISIBLE);
							findViewById(R.id.exer_seekbar).setVisibility(View.INVISIBLE);
							break;
						default:
							throw new RuntimeException(
									"Unknown Button ID For Location Question.");
						}
					}

				});
}
	private void initializeSeekBar() {
		exerciseQualityBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				seekbarResult=String.valueOf(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
	}

	private void saveToDB(){
		if(exer_str != null){
			if (exer_str.equals("yes")){
				db.updateOrAdd("exercise", exer_str);
				if (seekbarResult != null){
					db.updateOrAdd("exercise_quality", seekbarResult);
				}
			}
		}
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.bDSExerciseFinish:
			saveToDB();
			db.updateOrAdd("DailySurveyExercise" + "CheckList",
					"done");
			finish();
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
