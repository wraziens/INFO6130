package cornell.eickleapp;

import cornell.eickleapp.R;
import cornell.eickleapp.R.xml;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Settings extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
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
		case android.R.id.home:
			openPage = new Intent(this, MainMenu.class);
			startActivity(openPage);
			break;

		}
		return true;
	}
}
