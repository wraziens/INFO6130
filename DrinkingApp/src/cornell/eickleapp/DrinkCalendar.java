package cornell.eickleapp;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkCalendar extends Activity implements OnClickListener {

	int selectedMonth, selectedYear;
	Calendar calendar = Calendar.getInstance();
	GridView drinkCalendar;
	TextView monthDisplay, yearDisplay, bottomDisplay, infoDisplay, drinkCount,
			drinkEst, dogCount, goalText;
	RelativeLayout drink_img, dog_img;
	ImageButton back, next;
	ArrayList<Button> drinkBacButtons = new ArrayList<Button>();
	ArrayList<String> numbers = new ArrayList<String>();
	int goal;
	LinearLayout click;
	static int achievement=0;
	private ArrayList<Integer> drinkingDays;
	private ArrayList<Double> maxBac;
	private ArrayList<Integer> bacColors;
	private DatabaseHandler db;
	private ArrayList<DatabaseStore> day_colors;
	private ArrayList<DatabaseStore> day_values;
	private ArrayList<DatabaseStore> day_guess;
	private ArrayList<Integer> day_counts;
	private ArrayList<DatabaseStore> hotdogs;
	
	private Double month_max_bac; //the max BAC for the month
	private Integer month_total_drink; //the total number of drinks the user had in a month
	private Double month_total_time; //the total time the user spent drinking in the month
	private Integer month_max_color; //the color that corresponds to the month max bac value
	
	private GestureDetectorCompat mDetector;
	private DrinkCalendar dc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinkcalendar);

		db = new DatabaseHandler(this);
		dc = this;
		drinkCalendar = (GridView) findViewById(R.id.gvDrinkCalendar);
		monthDisplay = (TextView) findViewById(R.id.tvMonth);
		yearDisplay = (TextView) findViewById(R.id.tvYear);
		bottomDisplay = (TextView) findViewById(R.id.tvCalendarBottomDisplay);
		infoDisplay = (TextView) findViewById(R.id.tvInfoDisplay);
		drinkCount = (TextView) findViewById(R.id.drink_count);
		click = (LinearLayout) findViewById(R.id.clickAppear);

		goalText = (TextView) findViewById(R.id.goalStatement);
		back = (ImageButton) findViewById(R.id.bPreviousMonth);
		next = (ImageButton) findViewById(R.id.bNextMonth);
		drink_img = (RelativeLayout) findViewById(R.id.drink_img);
		dog_img = (RelativeLayout) findViewById(R.id.hot_dog_img);
		dogCount = (TextView) findViewById(R.id.hot_dog_count);
		back.setOnClickListener(this);
		next.setOnClickListener(this);
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		goal = Integer.parseInt(getPrefs.getString("goal", "2"));

		drinkingDays = new ArrayList<Integer>();
		maxBac = new ArrayList<Double>();
		bacColors = new ArrayList<Integer>();

		day_guess = new ArrayList<DatabaseStore>();
		hotdogs = new ArrayList<DatabaseStore>();

		//aggregate values for the month
		month_max_bac=0.0;
		month_total_drink=0;
		month_total_time=0.0;
		
		// Get the values from the DB
		Date date = new Date();
		calculateValues(date);

		selectedMonth = calendar.get(Calendar.MONTH);
		selectedYear = calendar.get(Calendar.YEAR);
		setMonthFromInt(selectedMonth);
		yearDisplay.setText(Integer.toString(selectedYear));
		ColorAdapter adapter = new ColorAdapter(this, selectedMonth,
				selectedYear, drinkingDays, maxBac, bacColors);
		drinkCalendar.setAdapter(adapter);
		drinkBacButtons = adapter.getButtonView();
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	

	}

	@Override
	public boolean onTouchEvent(MotionEvent event){ 
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	public boolean dispatchTouchEvent(MotionEvent event){
		super.dispatchTouchEvent(event);
		return this.mDetector.onTouchEvent(event);
		
	}
	
	/*
	 * Construct necessary Lists for the DB
	 */
	private void convertToLists(ArrayList<DatabaseStore> color,
			ArrayList<DatabaseStore> values) {
		for (int i = 0; i < values.size(); i++) {
			DatabaseStore ds = values.get(i);
			drinkingDays.add(ds.day);
			maxBac.add(Double.valueOf(ds.value));
			bacColors.add(Integer.parseInt(color.get(i).value));
		}
	}

	/*
	 * Must sort both color and values by time before calling.
	 */
	private void getMaxForDays(ArrayList<DatabaseStore> colors,
			ArrayList<DatabaseStore> values) {
		assert (colors.size() == values.size());

		day_colors = new ArrayList<DatabaseStore>();
		day_values = new ArrayList<DatabaseStore>();
		day_counts = new ArrayList<Integer>();
		
		
		
		DatabaseStore max_day = null;
		DatabaseStore max_color = null;
		if (values != null) {
			int cnt = 0;
			for (int i = 0; i < values.size(); i++) {
				cnt += 1;
				DatabaseStore s = values.get(i);
				if (max_day == null) {
					max_day = s;
					max_color = colors.get(i);
					
				} else {
					if (max_day.day < s.day) {
						day_colors.add(max_color);
						day_values.add(max_day);
						day_counts.add(cnt);
						cnt = 1;
						max_day = s;
						max_color = colors.get(i);
					} else if (Double.valueOf(max_day.value) < Double
							.valueOf(s.value)) {
						max_day = s;
						max_color = colors.get(i);
					}
				}
				if(Double.valueOf(max_day.value)>month_max_bac){
					month_max_bac = Double.valueOf(max_day.value);
					month_max_color = Integer.parseInt(max_color.value);
				}
			}
			// add final values
			day_values.add(max_day);
			day_colors.add(max_color);
			day_counts.add(cnt);
		}
	}

	private void calculateValues(Date date) {
		maxBac.clear();
		bacColors.clear();
		drinkingDays.clear();

		ArrayList<DatabaseStore> bac_colors = (ArrayList<DatabaseStore>) db
				.getVarValuesForMonth("bac_color", date);
		ArrayList<DatabaseStore> bac_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesForMonth("bac", date);
		day_guess = (ArrayList<DatabaseStore>) db.getVarValuesForMonth(
				"drink_guess", date);
		hotdogs = (ArrayList<DatabaseStore>) db.getVarValuesForMonth(
				"hot_dogs", date);

		if (bac_colors != null && bac_vals != null && hotdogs != null) {
			bac_colors = DatabaseStore.sortByTime(bac_colors);
			bac_vals = DatabaseStore.sortByTime(bac_vals);
			hotdogs = DatabaseStore.sortByTime(hotdogs);
			if (day_guess != null) {
				day_guess = DatabaseStore.sortByTime(day_guess);
			}
			getMaxForDays(bac_colors, bac_vals);
			convertToLists(day_colors, day_values);
		}
	}

	@Override
	public void onClick(View v) {

		GregorianCalendar gc = new GregorianCalendar(selectedYear,
				selectedMonth, 1);
		Date date = new Date();
		switch (v.getId()) {
		case R.id.bNextMonth:

			gc.add(Calendar.MONTH, 1);
			date = gc.getTime();

			if (selectedMonth + 1 > 11) {
				selectedMonth = 0;
				selectedYear++;
				yearDisplay.setText(Integer.toString(selectedYear));
			} else
				selectedMonth++;
			setMonthFromInt(selectedMonth);
			break;
		case R.id.bPreviousMonth:

			gc.add(Calendar.MONTH, -1);
			date = gc.getTime();

			if (selectedMonth - 1 < 0) {
				selectedMonth = 11;
				selectedYear--;
				yearDisplay.setText(Integer.toString(selectedYear));
			} else
				selectedMonth--;
			setMonthFromInt(selectedMonth);
			break;
		}
		calculateValues(date);
		ColorAdapter adapter = new ColorAdapter(this, selectedMonth,
				selectedYear, drinkingDays, maxBac, bacColors);
		drinkCalendar.setAdapter(adapter);
		drinkBacButtons = adapter.getButtonView();
	}

	// inputs the int value of month and outputs its corresponding name
	private void setMonthFromInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		month = months[num];
		monthDisplay.setText(month);
	}

	public void changeBottomDisplay(String entry, double bac, int index,
			int estimatedDrinks) {

		if (estimatedDrinks <= 0) {
			// bottomDisplay.setText(entry);
			int info_color = 0;
			String info_txt = "";
			String est = "";
			String cnt = "";
			String dogs = "";
			if (bac == 0) {
				info_color = 0xFF0099CC;
				info_txt = "Click on a colored date for more information.";
				est = "";
				cnt = "";
				dogs = "";
				goalText.setText("");
				click.setVisibility(View.GONE);

			} else {
				if (index != -1) {
					if (day_counts.get(index) == 1) {
						cnt = String.valueOf(day_counts.get(index))
								+ " Drink Tracked.";
						click.setVisibility(View.VISIBLE);
					} else {
						cnt = String.valueOf(day_counts.get(index))
								+ " Drinks Tracked.";
						click.setVisibility(View.VISIBLE);
					}
					int num_dogs = Integer.parseInt(hotdogs.get(index).value);
					if (num_dogs == 1) {
						dogs = "Which is calorically equivalent to 1 hot dog.";
					} else {
						dogs = "Which is calorically equivalent to " + num_dogs
								+ " hot dogs.";
					}
				}
				info_txt = "BAC: " + entry + "\n\n";
				if (bac < 0.06) {
					info_color = 0x884D944D;
					if (bac < 0.02) {
						info_txt += "-Begin to feel relaxed.\n-Reaction time slows.\n";
					} else {
						info_txt += "-Euphoria, \"the buzz\"\n-Sociability.\n-Decrease in judgement and reasoning.\n";
					}
				} else if (bac < 0.15) {
					info_color = 0X88E68A2E;
					if (bac <= 0.08) {
						info_txt += "-Legally Intoxicated.\n-Balance and Coordination impaired.\n-Less self-control.";
					} else {
						info_txt += "-Clear deterioration of cognitive judgement and motor coordination.\n-Speech may be slurred.\n";
					}
				} else if (bac < 0.24) {
					info_color = 0X88A30000;
					info_txt += "-At risk for blackout.\n-Nausea.\n-Risk of stumbling and falling.\n";
				} else {
					if (bac < .35) {
						info_txt += "-May be unable to walk.\n-May pass out or lose conciousness.\n-Seek medical attention.\n";
					} else {
						info_txt += "-High risk for coma or death.\n";
					}
					info_color = 0XCC000000;
				}
			}
			infoDisplay.setText(info_txt);
			infoDisplay.setBackgroundColor(info_color);
			drinkCount.setText(cnt);
			dogCount.setText(dogs);

			int count = 0;
			if (index != -1) {
				int num_dogs = Integer.parseInt(hotdogs.get(index).value);
				dog_img.removeAllViews();
				drink_img.removeAllViews();
				for (int i = 0; i <= num_dogs; i++) {
					count += 1;
					ImageView iv = new ImageView(this);
					iv.setBackgroundResource(R.drawable.hot_dog);
					iv.setId(i);
					RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
							80, 80);
					if (i > 0) {
						ImageView last = (ImageView) dog_img
								.findViewById(i - 1);
						if (count > 10) {
							p.addRule(RelativeLayout.BELOW, i - 10);
							p.addRule(RelativeLayout.ALIGN_START, i - 10);
						} else {
							p.addRule(RelativeLayout.RIGHT_OF, last.getId());
						}
					}

					iv.setLayoutParams(p);
					dog_img.addView(iv, p);
				}
				count = 0;
				for (int i = 0; i <= day_counts.get(index); i++) {
					count += 1;
					ImageView iv = new ImageView(this);
					iv.setBackgroundResource(R.drawable.beer_icon);
					iv.setId(i);
					RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
							60, 60);
					if (i > 0) {
						ImageView last = (ImageView) drink_img
								.findViewById(i - 1);
						if (count > 10) {
							p.addRule(RelativeLayout.BELOW, i - 10);
							p.addRule(RelativeLayout.ALIGN_START, i - 10);
						} else {
							p.addRule(RelativeLayout.RIGHT_OF, last.getId());
						}
					}

					iv.setLayoutParams(p);
					drink_img.addView(iv, p);
				}
			} else if (index == -1) {
				drink_img.removeAllViews();
				dog_img.removeAllViews();
			}

		} else {
		
			if (estimatedDrinks == 1)
				drinkCount.setText("One Drink Estimated");
			else
				drinkCount.setText(estimatedDrinks + " Drinks Estimated");
		}
	}
	/*
	private void displayInfo(double bac, int index){
		
	}
	*/
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
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 50;
		private static final int SWIPE_MAX_OFF_PATH = 150;
		private static final int SWIPE_THRESHOLD_VELOCITY = 80;
		
		@Override 
		public boolean onFling(MotionEvent event1, MotionEvent event2, 
				float velocityX, float velocityY){
			float dx = event2.getX() - event1.getX();
			float dy = event1.getY() - event2.getY();
			//Toast.makeText(getApplicationContext(), "FLING!",
			//		Toast.LENGTH_SHORT).show();
			
			GregorianCalendar gc = new GregorianCalendar(selectedYear,
					selectedMonth, 1);
			Date date = new Date();
			
			if(Math.abs(dy) < SWIPE_MAX_OFF_PATH && 
					Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY &&
					Math.abs(dx) >= SWIPE_MIN_DISTANCE){
				
				if(dx >0){
					Toast.makeText(getApplicationContext(), "RightSwipe",
							Toast.LENGTH_SHORT).show();
					
					//Previous Month
					gc.add(Calendar.MONTH, -1);
					date = gc.getTime();

					if (selectedMonth - 1 < 0) {
						selectedMonth = 11;
						selectedYear--;
						yearDisplay.setText(Integer.toString(selectedYear));
					} else
						selectedMonth--;
					setMonthFromInt(selectedMonth);
				}else{
					Toast.makeText(getApplicationContext(), "LeftSwipe",
					Toast.LENGTH_SHORT).show();
					gc.add(Calendar.MONTH, 1);
					date = gc.getTime();
					//Next Month
					if (selectedMonth + 1 > 11) {
						selectedMonth = 0;
						selectedYear++;
						yearDisplay.setText(Integer.toString(selectedYear));
					} else
						selectedMonth++;
					setMonthFromInt(selectedMonth);
				}
				calculateValues(date);
				ColorAdapter adapter = new ColorAdapter(dc, selectedMonth,
						selectedYear, drinkingDays, maxBac, bacColors);
				drinkCalendar.setAdapter(adapter);
				drinkBacButtons = adapter.getButtonView();
				
				return true;
			}
			return true;
		}

	}
	
}
