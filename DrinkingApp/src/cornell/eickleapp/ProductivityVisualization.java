package cornell.eickleapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

public class ProductivityVisualization extends Activity {

	GridView productivityGridView;

	
	private DatabaseHandler db;
	private ArrayList<Double> gradesAverageList;
	private ArrayList<Double> productivityAverageList;
	private ArrayList<Double> stressAverageList;
	private ArrayList<Integer> daysDrankList;
	private ArrayList<Double> bacAverageList;
	
	private ArrayList<DatabaseStore> day_values;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productivityvisualization);

		db = new DatabaseHandler(this);

		gradesAverageList = new ArrayList<Double>();
		productivityAverageList = new ArrayList<Double>();
		stressAverageList = new ArrayList<Double>();
		daysDrankList = new ArrayList<Integer>();
		bacAverageList = new ArrayList<Double>();

		ArrayList<Date> date_list = new ArrayList<Date>();
		Date date =  new Date();
		date_list.add(date);
		//get the last 4 weeks
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		for(int i=0; i<4; i++){
			int value = -7 * (i+1);
			gc.add(Calendar.DAY_OF_YEAR, value);
			date_list.add(gc.getTime());
		}
		constructLists(date_list);
		
		productivityGridView = (GridView) findViewById(R.id.gvProductivity);
		ProductivityAdapter adapter = new ProductivityAdapter(this, gradesAverageList,
				productivityAverageList, stressAverageList, daysDrankList, bacAverageList);
		
		productivityGridView.setAdapter(adapter);
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		if (checkSurveyed) {
		}
	}

	private void constructLists(ArrayList<Date> date_list){
		gradesAverageList.clear();
		productivityAverageList.clear();
		stressAverageList.clear();
		daysDrankList.clear();
		bacAverageList.clear();
		
		for (int i=0; i<4; i++){
			Date date = date_list.get(i);
			ArrayList<DatabaseStore> stress = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("stress_level", date);
			ArrayList<DatabaseStore> perf = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("performance", date);
			ArrayList<DatabaseStore> prod = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("productive", date);
			ArrayList<DatabaseStore> drank = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("drank", date);
			ArrayList<DatabaseStore> bac = (ArrayList<DatabaseStore>)db.getVarValuesForWeek("bac", date);
		
			if (stress != null){
				stressAverageList.add(getAverage(stress));
			} else {
				//stressAverageList.add(Double.valueOf(0));
			}
			if(perf != null){
				gradesAverageList.add(getAverage(perf));
			}else{
				//gradesAverageList.add(Double.valueOf(0));
			}
			if(prod != null){
				productivityAverageList.add(getAverage(prod));
			}else{
				//productivityAverageList.add(Double.valueOf(0));
			}
			if (drank != null){
				daysDrankList.add(drank.size());
			} else{
				daysDrankList.add(0);
			}
			if (bac != null){
				bac = DatabaseStore.sortByTime(bac);
				getMaxForDays(bac);
				bacAverageList.add(getAverage(day_values));
			}else{
				bacAverageList.add(Double.valueOf(0));
			}
		}
	}
	
	/*
	 * Must sort values by time before calling.
	 */
	private void getMaxForDays(ArrayList<DatabaseStore> values){
		day_values = new ArrayList<DatabaseStore>();
		DatabaseStore max_day=null;
		for(int i=0; i< values.size(); i++){
			DatabaseStore s = values.get(i);
			if(max_day == null){
				max_day = s;
			}else{
				if(max_day.day < s.day){
					day_values.add(max_day);
					max_day = s;
				} else if(Double.valueOf(max_day.value)< Double.valueOf(s.value)){
					max_day = s;
				}
			}
			day_values.add(max_day);
		}
	}
	
	private Double getAverage(ArrayList<DatabaseStore> lst){
		Double sum = Double.valueOf(0);
		for(int i=0; i < lst.size(); i++){
				sum += Double.parseDouble(lst.get(i).value);
		}
		Double val = sum/lst.size();
		return val;
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
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
	

	
}
