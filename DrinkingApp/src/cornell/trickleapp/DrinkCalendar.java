package cornell.trickleapp;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Display;
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
import android.widget.Toast;
import android.widget.ToggleButton;

public class DrinkCalendar extends Activity implements OnClickListener{
	FlyOutContainer root;
	int selectedMonth, selectedYear;
	Calendar calendar = Calendar.getInstance();
	GridView drinkCalendar;
	TextView monthDisplay, yearDisplay, bottomDisplay, infoDisplay, drinkCount,drinkTime,drinkBac,monthOverview, monthMoney,
			dogCount;
	ImageView wine_icon, beer_icon, liquor_icon;
	RelativeLayout drink_img, dog_img;
	

	ArrayList<Button> drinkBacButtons = new ArrayList<Button>();
	ArrayList<String> numbers = new ArrayList<String>();
	LinearLayout click;
	private ArrayList<Integer> drinkingDays;
	private ArrayList<Double> maxBac;
	private ArrayList<Integer> bacColors;
	private DatabaseHandler db;
	private ArrayList<DatabaseStore> day_colors;
	private ArrayList<DatabaseStore> day_values;
	private ArrayList<Double> day_times;
	private ArrayList<Double> day_time_counts;
	private ArrayList<Double> day_money;
	private ArrayList<Integer> day_counts;
	
	private Double month_max_bac; //the max BAC for the month
	private Integer month_total_drink; //the total number of drinks the user had in a month
	private Double month_total_time; //the total time the user spent drinking in the month
	private Integer month_max_color; //the color that corresponds to the month max bac value
	private Double month_money;//the total amount of money the user spent on drinks
	
	private GestureDetectorCompat mDetector;
	private DrinkCalendar dc;

	private int windowWidth = 0;
	private int windowHeight = 0;
	private LinearLayout mainLayout;
	private Context context;
	private ArrayList<DatabaseStore> drinkCounts;
	private ArrayList<Double> bacVals;
	private ArrayList<TrendsSliceItem> morningCounts;
	private ArrayList<TrendsSliceItem> eveningCounts;
	private BacTime bacTime;
	private TimeBacGraph visual;
	ToggleButton morning, evening, after;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.drinkcalendar, null);

		this.setContentView(root);
		context = this; 
		db = new DatabaseHandler(this);
		dc = this;
		drinkCalendar = (GridView) findViewById(R.id.gvDrinkCalendar);
		monthDisplay = (TextView) findViewById(R.id.tvMonth);
		bottomDisplay = (TextView) findViewById(R.id.tvCalendarBottomDisplay);
		infoDisplay = (TextView) findViewById(R.id.tvInfoDisplay);
		drinkCount = (TextView) findViewById(R.id.drink_count);
		drinkTime = (TextView) findViewById(R.id.drink_time);
		drinkBac = (TextView) findViewById(R.id.month_bac);
		monthOverview = (TextView) findViewById(R.id.month_overview);
		monthMoney = (TextView) findViewById(R.id.month_money);

		
		click = (LinearLayout) findViewById(R.id.clickAppear);

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		drinkingDays = new ArrayList<Integer>();
		maxBac = new ArrayList<Double>();
		bacColors = new ArrayList<Integer>();
		day_money = new ArrayList<Double>();
		day_times = new ArrayList<Double>();

		//aggregate values for the month
		month_max_bac=0.0;
		month_total_drink=0;
		month_total_time=0.0;
		month_max_color = DrinkCounter.getBacColor(0);
		month_money = 0.0;
		
		// Get the values from the DB
		Date date = DatabaseStore.getDelayedDate();
		calculateValues(date);

		selectedMonth = calendar.get(Calendar.MONTH);
		selectedYear = calendar.get(Calendar.YEAR);
		String month_text =  getMonthFromInt(selectedMonth) + " " + selectedYear;
		monthDisplay.setText(month_text);
		
		ColorAdapter adapter = new ColorAdapter(this, selectedMonth,
				selectedYear, drinkingDays, maxBac, bacColors);
		drinkCalendar.setAdapter(adapter);
		drinkBacButtons = adapter.getButtonView();
		
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

	private int getMonthlyTotal(ArrayList<DatabaseStore> drinks){
		
		int month_drink = 0;
		DatabaseStore last = null;
		if(drinks!=null){
			for(int i=0; i<drinks.size();i++){
				DatabaseStore ds = drinks.get(i);
				if(last != null){
					if(ds.day > last.day){
						if(Integer.parseInt(ds.value) == 1){
							month_drink++;
						}
					}else{
						month_drink++;
					}
				}
				last = ds;
			}
			month_drink++;
		}
		return month_drink;
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
		day_time_counts = new ArrayList<Double>();
		
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

						end = max_day.date;
						double time = (end.getTime() - start.getTime()) / (1000 * 60 * 60) + 1;
						
						//monthly aggregate values
						day_time_counts.add(time);
						cnt = 1;
						
						if(Double.valueOf(max_day.value) > month_max_bac){
							month_max_bac = Double.valueOf(max_day.value);
							month_max_color = Integer.parseInt(max_color.value);
						}
						max_day = s;
						max_color = colors.get(i);
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
			
			double time = (end.getTime() - start.getTime())/(1000 * 60 * 60) + 1;
			day_time_counts.add(time);

			
			if(Double.valueOf(max_day.value) > month_max_bac){
				month_max_bac = Double.valueOf(max_day.value);
				month_max_color = Integer.parseInt(max_color.value);
			}
		}
	}
	
	//Calculate the amount of time the person is intoxicated
	private void calculate_times(){		
		day_times.clear();
		month_total_time=0.0;
		if(day_counts == null){
			return;
		}else{
			for(int i=0; i<day_counts.size(); i++){
				int number_drinks = day_counts.get(i);
			
				// get the users gender
				ArrayList<DatabaseStore> stored_gender = (ArrayList<DatabaseStore>) db
						.getAllVarValue("gender");
				// If user did not set gender use "Female" as default
				String gender = "Female";
				if (stored_gender != null) {
					gender = stored_gender.get(0).value;
				}
		
				// fetch the users weight
				ArrayList<DatabaseStore> stored_weight = (ArrayList<DatabaseStore>) db
						.getAllVarValue("weight");
				Integer weight_lbs = 120;
				if (stored_weight != null) {
					weight_lbs = Integer.parseInt(stored_weight.get(0).value);
				}
		
				double metabolism_constant = 0;
				double gender_constant = 0;
				double weight_kilograms = weight_lbs * 0.453592;
		
				if (gender.equals("Male")) {
					metabolism_constant = 0.015;
					gender_constant = 0.58;
				} else {
					metabolism_constant = 0.017;
					gender_constant = 0.49;
				}
				
				double time = ((0.806 * number_drinks * 1.2) / (gender_constant * weight_kilograms))/(metabolism_constant);
				day_times.add(time); 
				month_total_time += time;
			}
		}
	}
	

	private void calculateValues(Date date) {
		maxBac.clear();
		bacColors.clear();
		drinkingDays.clear();
		day_money.clear();
		day_times.clear();
		
		month_total_drink = 0;
		month_total_time = 0.0;
		month_max_bac = 0.0;
		month_max_color = DrinkCounter.getBacColor(month_max_bac);
		month_money =0.0;
		
		ArrayList<DatabaseStore> month_bac = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("bac", date);
		ArrayList<DatabaseStore> month_drinks = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("drink_count", date);
		ArrayList<DatabaseStore> month_colors = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("bac_color", date);
		ArrayList<DatabaseStore> month_money = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("money", date);

		month_total_drink = getMonthlyTotal(month_drinks);
		
		if (month_bac!=null && month_drinks!=null && month_colors != null) {
			month_colors = DatabaseStore.sortByTime(month_colors);
			month_bac = DatabaseStore.sortByTime(month_bac);
			month_drinks = DatabaseStore.sortByTime(month_drinks);
			if (month_money!= null){
				month_money = DatabaseStore.sortByTime(month_money);
			}
			getMaxForDays(month_colors, month_bac);
			
			convertToLists(day_colors, day_values);
			setMoneyValues(month_money);
			calculate_times();
		}
		
	}

	public void setMoneyValues(ArrayList<DatabaseStore> money_list){
		if (money_list == null){
			return;
		}else{
			
			DatabaseStore first_date = money_list.get(0);
			double count = 0;
			month_money = 0.0;
			
			for (int i=0; i<money_list.size(); i++){
				DatabaseStore current = money_list.get(i);
				if(first_date.day<current.day){
					day_money.add(count);
					month_money += count; 
					count = Double.parseDouble(current.value);
				}else{
					count += Double.parseDouble(current.value);
				}
			}
			day_money.add(count);
			month_money += count;
		}	
	}

	private String getMonthFromInt(int num){
		String month = "";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		month = months[num];
		return month;
	}
	
	private void constructLists(){
		morningCounts.clear();
		eveningCounts.clear();
		DatabaseStore lastEvening = null;
		int lastEveningIndex = -1;
		DatabaseStore lastMorning = null;
		int lastMorningIndex = -1;
		//int lastRemovedEvening = 0;
		//int lastRemovedMorning = 0;
		int lastRemoved = 0;

		
		for(int i=0; i<drinkCounts.size(); i++) {
			DatabaseStore value = drinkCounts.get(i);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(value.date);
			gc.add(Calendar.HOUR_OF_DAY, 6);
			
			
			if(value.time_seconds >= 43200){
				value.setDate(gc.getTime());
				if (lastEvening!=null){
					TrendsSliceItem item = new TrendsSliceItem(lastEvening.time_seconds, value.time_seconds, i ,Double.parseDouble(lastEvening.value));
					if (lastRemoved> 0){
						item.setDrinkCount(item.getDrinkCount() -lastRemoved);

					}
					int removed = bacTime.adjustDrinkCount(item);

					lastRemoved += Math.min(removed, i);
					eveningCounts.add(item);
				} else if(lastMorning != null){
					TrendsSliceItem item = new TrendsSliceItem(lastMorning.time_seconds, value.time_seconds, i,Double.parseDouble(lastMorning.value));
					if (lastRemoved> 0){
						item.setDrinkCount(item.getDrinkCount() -lastRemoved);
					}
					int removed = bacTime.adjustDrinkCount(item);
					morningCounts.add(item);
					lastMorning = null;
				}
				lastEvening = value;
				lastEveningIndex = i;
			}else{
				value.setDate(gc.getTime());
				if (lastMorning!=null){
					TrendsSliceItem item = new TrendsSliceItem(lastMorning.time_seconds, value.time_seconds, i, Double.parseDouble(lastMorning.value));
					if (lastRemoved > 0){
						item.setDrinkCount(item.getDrinkCount() -lastRemoved);
					}
					int removed = bacTime.adjustDrinkCount(item);
					lastRemoved +=Math.min(removed, i);
					morningCounts.add(item);
				}
				lastMorning = value;
				lastMorningIndex = i;
			}
		}
		if (lastEvening != null){
			TrendsSliceItem item = new TrendsSliceItem(lastEvening.time_seconds, -1, drinkCounts.size(), Double.parseDouble(lastEvening.value));
			if (lastRemoved> 0){
				item.setDrinkCount(drinkCounts.size() -lastRemoved);
				
			}
			eveningCounts.add(item);
		}
		if(lastMorning != null){
			TrendsSliceItem item = new TrendsSliceItem(lastMorning.time_seconds, -1, drinkCounts.size(), Double.parseDouble(lastMorning.value));
			if (lastRemoved > 0){
				item.setDrinkCount(item.getDrinkCount() -lastRemoved);
			}
			morningCounts.add(item);
		}
	}
	
	public void showDayInfo(double bac, int index){
		String day_day = day_values.get(index).day_week;
		Date date_val = day_values.get(index).date; 
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.calendar_day_info);
		
		bacTime = new BacTime(this);
		db = new DatabaseHandler(this);
		context = this;
		Date date = new Date();
		drinkCounts = (ArrayList<DatabaseStore>)db.getVarValuesForDay("bac", date_val);
		ArrayList<DatabaseStore> drinks = (ArrayList<DatabaseStore>)db.getVarValuesForDay("drink_count", date_val);
		drinks = DatabaseStore.sortByTime(drinks);
		
		int rec_drinks=0;
		if(Integer.parseInt(drinks.get(0).value)>1){
			rec_drinks = drinks.size()-1;
		}else{
			rec_drinks = drinks.size();
		}
		
		
		morningCounts = new ArrayList<TrendsSliceItem>();
		eveningCounts = new ArrayList<TrendsSliceItem>();
		if (drinkCounts != null){
			drinkCounts = DatabaseStore.sortByTime(drinkCounts);
			constructLists();
		}
		
		beer_icon = (ImageView)dialog.findViewById(R.id.beer_icon);
		wine_icon = (ImageView) dialog.findViewById(R.id.wine_icon);
		liquor_icon = (ImageView) dialog.findViewById(R.id.liquor_icon);
		// initializes the necessary values
		//setupTrendSliceItem(drinkSecRaw, drinkBAC);

		visual = new TimeBacGraph(this, eveningCounts, morningCounts);
		mainLayout = (LinearLayout) dialog.findViewById(R.id.pieGraph);
		evening = (ToggleButton) dialog.findViewById(R.id.evening_button);
		evening.setPressed(true);
		morning = (ToggleButton) dialog.findViewById(R.id.morning_button);
		after = (ToggleButton) dialog.findViewById(R.id.after_button);
		evening.setOnClickListener(this);
		morning.setOnClickListener(this);
		after.setOnClickListener(this);
		evening.setChecked(true);
		// get window width
		Display display = getWindowManager().getDefaultDisplay();
		windowWidth = display.getWidth(); // deprecated
		windowHeight = display.getHeight(); // deprecated
		visual.bringToFront();
		
		mainLayout.addView(visual, windowWidth, windowHeight/2);
		
		//-----------
		
		
		
		//dialog.setTitle(title);
		
		Map<String, String> week_days = new HashMap<String, String>();
		week_days.put("Sun", "Sunday");
		week_days.put("Mon", "Monday");
		week_days.put("Tue", "Tuesday");
		week_days.put("Wed", "Wednesday");
		week_days.put("Thu", "Thursday");
		week_days.put("Fri", "Friday");
		week_days.put("Sat", "Saturday");
		
		DecimalFormat formatter = new DecimalFormat("#.###");
		
		TextView day_text = (TextView) dialog.findViewById(R.id.day_of_week);
		day_text.setText(week_days.get(day_day)+ " "  + (selectedMonth+1) +"/" + day_values.get(index).day);
		//date_text.setText(date_string);
		
		// set the custom dialog components - text, image and button
		TextView bac_text = (TextView) dialog.findViewById(R.id.day_bac);
		bac_text.setText(formatter.format(bac) + " max BAC");
		TextView count_text = (TextView) dialog.findViewById(R.id.day_cal_drink_count);
		count_text.setText(String.valueOf(rec_drinks) + " drinks recorded");
		
		
		TextView rate_text = (TextView) dialog.findViewById(R.id.day_rate);
		rate_text.setText(formatter.format(day_counts.get(index)/day_time_counts.get(index)) + " drinks / hour");
		
		DecimalFormat time_formatter = new DecimalFormat("#.##");
		TextView time_text = (TextView) dialog.findViewById(R.id.day_drink_time);
		time_text.setText(time_formatter.format(day_times.get(index)) + " hours intoxicated");
		
		Button close = (Button)dialog.findViewById(R.id.close_info);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				dialog.dismiss();
			}
		});
		
		ArrayList<DatabaseStore> beer_result = (ArrayList<DatabaseStore>) db.getVarValuesForDay("type_beer", date_val);
		ArrayList<DatabaseStore> wine_result = (ArrayList<DatabaseStore>) db.getVarValuesForDay("type_wine", date_val);
		ArrayList<DatabaseStore> liquor_result = (ArrayList<DatabaseStore>) db.getVarValuesForDay("type_liquor", date_val);
		
		if(beer_result != null){
			int val = Integer.parseInt(beer_result.get(0).value);
			if(val == 1){
				beer_icon.setImageResource(R.drawable.ic_calendar_beer);
			}
		}
		if(wine_result != null){
			int val = Integer.parseInt(wine_result.get(0).value);
			if(val == 1){
				wine_icon.setImageResource(R.drawable.ic_calendar_wine);
			}
		}
		if(liquor_result != null){
			int val = Integer.parseInt(liquor_result.get(0).value);
			if(val == 1){
				liquor_icon.setImageResource(R.drawable.ic_calendar_liquor);
			}
		}
		
		ImageView face = (ImageView)dialog.findViewById(R.id.drink_calendar_day);
		//Update the face color
		((GradientDrawable)((LayerDrawable) face.getDrawable()).getDrawable(0)
				).setColor(DrinkCounter.getBacColor(bac));	
		
		int icon_face = getFaceIcon(bac);
		//Update the face icon
		Drawable to_replace = getResources().getDrawable(icon_face);	
		((LayerDrawable) face.getDrawable()).setDrawableByLayerId(
				R.id.face_icon, to_replace);
		face.invalidate();
		face.refreshDrawableState();
		
		dialog.show();
	}
	
	public void changeBottomDisplay(String entry, double bac, int index){
			showDayInfo(bac, index);
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
		String drink_text = month_total_drink + " drinks";
		drinkCount.setText(drink_text);
		drinkCount.setVisibility(View.VISIBLE);
		
		DecimalFormat time_formatter = new DecimalFormat("#.##");
		String time_text = time_formatter.format(month_total_time) + " hours intoxicated";
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
		
		
		String bac_text = formatter.format(month_max_bac) + " max BAC";
		drinkBac.setText(bac_text);
		drinkBac.setVisibility(View.VISIBLE);
		
		DecimalFormat money_formatter = new DecimalFormat("#.##");
		
		String overview_text = "Summary";
		monthOverview.setText(overview_text);
		
		
		String money_text = "$" + money_formatter.format(month_money);
		monthMoney.setText(money_text);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		db.close();
		super.onStop();
		finish();
	}

	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	    case R.id.evening_button:
	    	morning.setChecked(false);
			evening.setChecked(true);
			after.setChecked(false);
			
			visual.setEvening();
	        break;
	    case R.id.morning_button:
	    	morning.setChecked(true);
			evening.setChecked(false);
			after.setChecked(false);
			visual.setMorning();
	       break;
	    case R.id.after_button:
	    	morning.setChecked(false);
			evening.setChecked(false);
			after.setChecked(true);
			visual.setAfter();
			break;
	    }   
	}
	
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 30;
		private static final int SWIPE_MAX_OFF_PATH = 150;
		private static final int SWIPE_THRESHOLD_VELOCITY = 20;
		
		@Override 
		public boolean onFling(MotionEvent event1, MotionEvent event2, 
				float velocityX, float velocityY){
			float dx = event2.getX() - event1.getX();
			float dy = event1.getY() - event2.getY();

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
					}else{
						selectedMonth--;
					}
					String month_text = getMonthFromInt(selectedMonth) + " " + selectedYear;
					monthDisplay.setText(month_text);
				}else{
					gc.add(Calendar.MONTH, 1);
					date = gc.getTime();
					//Next Month
					if (selectedMonth + 1 > 11) {
						selectedMonth = 0;
						selectedYear++;
					}else{
						selectedMonth++;
					}
					String month_text = getMonthFromInt(selectedMonth) + " " + selectedYear;
					monthDisplay.setText(month_text);
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
