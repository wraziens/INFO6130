package cornell.eickleapp;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DrinkCalendar extends Activity implements OnClickListener {

	int selectedMonth, selectedYear;
	Calendar calendar = Calendar.getInstance();
	GridView drinkCalendar;
	TextView monthDisplay, yearDisplay, bottomDisplay, infoDisplay, drinkCount,drinkTime,drinkBac,
			dogCount;
	RelativeLayout drink_img, dog_img;

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
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.drinkcalendar);

		db = new DatabaseHandler(this);
		dc = this;
		drinkCalendar = (GridView) findViewById(R.id.gvDrinkCalendar);
		monthDisplay = (TextView) findViewById(R.id.tvMonth);
		yearDisplay = (TextView) findViewById(R.id.tvYear);
		bottomDisplay = (TextView) findViewById(R.id.tvCalendarBottomDisplay);
		infoDisplay = (TextView) findViewById(R.id.tvInfoDisplay);
		drinkCount = (TextView) findViewById(R.id.drink_count);
		drinkTime = (TextView) findViewById(R.id.drink_time);
		drinkBac = (TextView) findViewById(R.id.month_bac);
		
		click = (LinearLayout) findViewById(R.id.clickAppear);

		drink_img = (RelativeLayout) findViewById(R.id.drink_img);
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		goal = Integer.parseInt(getPrefs.getString("goal", "2"));

		drinkingDays = new ArrayList<Integer>();
		maxBac = new ArrayList<Double>();
		bacColors = new ArrayList<Integer>();

		hotdogs = new ArrayList<DatabaseStore>();

		//aggregate values for the month
		month_max_bac=0.0;
		month_total_drink=0;
		month_total_time=0.0;
		month_max_color = DrinkCounter.getBacColor(0);
		
		// Get the values from the DB
		Date date = DatabaseStore.getDelayedDate();
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
		setCalendarBottom();
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
		
		Date start = null;
		Date end = null;
		if (values != null) {
			int cnt = 0;
			for (int i = 0; i < values.size(); i++) {
				cnt += 1;
				DatabaseStore s = values.get(i);
				
				if(start == null){
					start = s.date; 
				}
				
				if (max_day == null) {
					max_day = s;
					max_color = colors.get(i);
					
				} else {
					if (max_day.day < s.day) {
						day_colors.add(max_color);
						day_values.add(max_day);
						day_counts.add(cnt);
						
						//monthly aggregate values
						month_total_drink += cnt;
						end = max_day.date;
						month_total_time += (end.getTime() - start.getTime()) / (1000 * 60 * 60) + 1;
						cnt = 1;
						max_day = s;
						max_color = colors.get(i);
						if(Double.valueOf(max_day.value) > month_max_bac){
							month_max_bac = Double.valueOf(max_day.value);
							month_max_color = Integer.parseInt(max_color.value);
						}
						start = s.date;
					} else if (Double.valueOf(max_day.value) < Double
							.valueOf(s.value)) {
						max_day = s;
						max_color = colors.get(i);
					}
				}
			}
			end = values.get(values.size()-1).date;
			// add values for each day
			day_values.add(max_day);
			day_colors.add(max_color);
			day_counts.add(cnt);
			month_total_drink += cnt;
			month_total_time += (end.getTime() - start.getTime())/(1000 * 60 * 60) + 1;
			
			if(Double.valueOf(max_day.value) > month_max_bac){
				month_max_bac = Double.valueOf(max_day.value);
				month_max_color = Integer.parseInt(max_color.value);
			}
			
			
		}
	}

	private void calculateValues(Date date) {
		maxBac.clear();
		bacColors.clear();
		drinkingDays.clear();
		
		month_total_drink = 0;
		month_total_time = 0.0;
		month_max_bac = 0.0;
		month_max_color = DrinkCounter.getBacColor(month_max_bac);
		
		ArrayList<DatabaseStore> month_bac = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("bac", date);
		ArrayList<DatabaseStore> month_drinks = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("drink_count", date);
		ArrayList<DatabaseStore> month_colors = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("bac_color", date);

		if (month_bac!=null && month_drinks!=null && month_colors != null) {
			month_colors = DatabaseStore.sortByTime(month_colors);
			month_bac = DatabaseStore.sortByTime(month_bac);
			month_drinks = DatabaseStore.sortByTime(month_drinks);
			
			getMaxForDays(month_colors, month_bac);
			
			convertToLists(day_colors, day_values);
		}
	}

	@Override
	public void onClick(View v) {
	}

	// inputs the int value of month and outputs its corresponding name
	private void setMonthFromInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		month = months[num];
		monthDisplay.setText(month);
	}

	public void changeBottomDisplay(String entry, double bac, int index) {
			// bottomDisplay.setText(entry);
			int info_color = 0;
			String info_txt = "";
			String est = "";
			String cnt = "";
			String dogs = "";
			if (bac == 0) {
				info_color = 0xFF0099CC;
				info_txt = "Click on a colored date for more information." + month_total_time;
				cnt = "";
				dogs = "";
				click.setVisibility(View.VISIBLE);

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
				}
				info_txt = "BAC: " + entry + "\n\n";
				if (bac < 0.06) {
					info_color = 0x884D944D;
					if (bac < 0.02) {
						info_txt += "-Begin to feel relaxed.\n-Reaction time slows.\n"+ month_total_time;
					} else {
						info_txt += "-Euphoria, \"the buzz\"\n-Sociability.\n-Decrease in judgement and reasoning.\n"+ month_total_time;
					}
				} else if (bac < 0.15) {
					info_color = 0X88E68A2E;
					if (bac <= 0.08) {
						info_txt += "-Legally Intoxicated.\n-Balance and Coordination impaired.\n-Less self-control."+ month_total_time;
					} else {
						info_txt += "-Clear deterioration of cognitive judgement and motor coordination.\n-Speech may be slurred.\n"+ month_total_time;
					}
				} else if (bac < 0.24) {
					info_color = 0X88A30000;
					info_txt += "-At risk for blackout.\n-Nausea.\n-Risk of stumbling and falling.\n"+ month_total_time;
				} else {
					if (bac < .35) {
						info_txt += "-May be unable to walk.\n-May pass out or lose conciousness.\n-Seek medical attention.\n";
					} else {
						info_txt += "-High risk for coma or death.\n"+ month_total_time;
					}
					info_color = 0XCC000000;
				}
			}
			infoDisplay.setText(info_txt);
			infoDisplay.setBackgroundColor(info_color);
			drinkCount.setText(cnt);

	
	}
	
	public int getFaceIcon(double bac_value){
		if (bac_value < 0.06) {
			return R.drawable.ic_tracker_smile;
		} else if (bac_value < 0.15) {
			return R.drawable.ic_tracker_neutral;
		} else if (bac_value < 0.24) {
			return R.drawable.ic_tracker_frown;
		} else {
			return R.drawable.ic_tracker_dead;
		}
	}


	
	protected void setCalendarBottom(){
		String drink_text = month_total_drink + " total drinks recorded";
		drinkCount.setText(drink_text);
		drinkCount.setVisibility(View.VISIBLE);
		
		String time_text = month_total_time + " hours spent drinking";
		drinkTime.setText(time_text);
		drinkTime.setVisibility(View.VISIBLE);
		
		int icon_face = getFaceIcon(month_max_bac);
		
		ImageView face = (ImageView)findViewById(R.id.drink_smile_calendar);
		
		
		//Update the face color
		((GradientDrawable)((LayerDrawable) face.getDrawable()).getDrawable(0)
				).setColor(month_max_color);	
		
		//Update the face icon
		Drawable to_replace = getResources().getDrawable(icon_face);	
		((LayerDrawable) face.getDrawable()).setDrawableByLayerId(
				R.id.face_icon, to_replace);
		face.invalidate();
		face.refreshDrawableState();
		
		DecimalFormat formatter = new DecimalFormat("#.###");
		
		
		String bac_text = formatter.format(month_max_bac) + " max BAC value";
		drinkBac.setText(bac_text);
		drinkBac.setVisibility(View.VISIBLE);
		
		
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
		private static final int SWIPE_MIN_DISTANCE = 75;
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
				setCalendarBottom();
			}
			setCalendarBottom();
			return true;
		}

	}
	
}
