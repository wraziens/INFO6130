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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DailySurveyProductivity extends Activity implements OnClickListener{

	
	Button finish;
	SeekBar prodBar, stressBar, performBar;
	String seekbarResult;
	private DatabaseHandler db;
	String prod_str="0", stress_str="100", perf_str="0";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dailysurveyproductivity);

		db = new DatabaseHandler(this);
		
		finish=(Button)findViewById(R.id.prod_finish);
		finish.setOnClickListener(this);
		
		//initialize the seek bars
		prodBar = (SeekBar)findViewById(R.id.productive_seek);
		prodBar.setMax(100);
		prodBar.setProgress(0);
		initializeProdSeekBar();
		
		performBar = (SeekBar)findViewById(R.id.perform_seek);
		performBar.setMax(100);
		performBar.setProgress(0);
		initializePerfSeekBar();
		
		stressBar = (SeekBar)findViewById(R.id.stressed_seek);
		stressBar.setMax(100);
		stressBar.setProgress(0);
		initializeStressSeekBar();
	}

	private void initializeProdSeekBar() {
		prodBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				prod_str=String.valueOf(progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {	
			}
		});
	}
	
	private void initializeStressSeekBar() {
		stressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				stress_str=String.valueOf(progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {	
			}
		});
	}
	
	private void initializePerfSeekBar() {
		performBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				perf_str=String.valueOf(progress);
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
		if(prod_str != null){
			db.updateOrAdd("productive", prod_str);
		}
		if(perf_str != null){
			db.updateOrAdd("performance", perf_str);
		}
		if(stress_str != null){
			db.updateOrAdd("stress_level", stress_str);
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
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.prod_finish:
			saveToDB();
			db.updateOrAdd("DailySurveyProductivity" + "CheckList",
					"done");
			finish();
			break;	
		}	
	}
	
	protected void onPause() {
		super.onPause();
		finish();
	}
}
