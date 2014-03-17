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
import android.widget.SeekBar;

public class DailySurvey1 extends Activity implements OnClickListener {

	Button yes, no;
	String result;
	Intent goToAssessment;
	private DatabaseHandler db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this);
		setContentView(R.layout.dailysurvey1);
		yes = (Button) findViewById(R.id.bDS1Yes);
		no = (Button) findViewById(R.id.bDS1No);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.bDS1Yes:
			result = "yes";
			db.addValue("drank_last_night", "True");
			db.addValueYesterday("drank", "True");
			setResult(2, intent);
			finish();
			break;
		case R.id.bDS1No:
			result = "no";
			db.addValue("drank_last_night", "False");
			setResult(2, intent);
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
		finish();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
