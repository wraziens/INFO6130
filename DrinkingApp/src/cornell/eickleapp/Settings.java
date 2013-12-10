package cornell.eickleapp;

import java.util.Calendar;

import cornell.eickleapp.R;
import cornell.eickleapp.R.xml;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class Settings extends PreferenceActivity {
	AlarmManager alarmManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		findPreference("notificationTime").setOnPreferenceChangeListener(
				new Preference.OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						// TODO Auto-generated method stub
						setAlarm();
						

						return true;
					}
				});

	}

	protected void setAlarm() {
		
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String timeValue = getPrefs.getString("notificationTime", "12");

		Intent intent = new Intent(this, ReminderAlarm.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		
		Calendar calendar=Calendar.getInstance();
		//calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeValue));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.setTimeInMillis(calendar.getTimeInMillis()+86400000);
		
		
		long miliUntilNextDay=calendar.getTimeInMillis();
		
		long miliForAlarm=miliUntilNextDay+(3600000*Integer.parseInt(timeValue));
		
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				miliForAlarm, (86400000), pendingIntent);
		// System.currentTimeMillis(), (5000), pendingIntent);
		
		SharedPreferences.Editor editPrefs=getPrefs.edit();
		editPrefs.putString("notificationTime", timeValue);
		editPrefs.commit();

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
