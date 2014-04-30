package cornell.trickleapp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TrendFace extends Activity {
	private DatabaseHandler db;
	
	private ImageView regret, dizzy, nausea, vomit, headache, memory, fatigue;
	private TextView regret_cnt, dizzy_cnt, nausea_cnt, vomit_cnt, headache_cnt, memory_cnt, fatigue_cnt;
	private TextView trendDate;
	
	private ArrayList<DatabaseStore> month_regret, month_dizzy, month_nausea, month_vomit, month_headache, month_memory, month_fatigue;
	private HashMap<String, ArrayList<Date>> symptoms;
	
	private int total_drinking_days;
	
	private Date currentDate;
	private int dateOffset=0;
	private int currentInfo = 0;
	
	private Dialog dialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.trendfaces);

		db = new DatabaseHandler(this);
		symptoms = new HashMap<String, ArrayList<Date>>();
		currentDate = new Date();
			
		renderMonth(currentDate);

	}
	
	private void renderMonth(Date date){
		setupFaces();
		getDbValues(date);
	}
	
	private boolean addToSymptomHash(ArrayList<DatabaseStore> sym, String symptom_name){
		boolean added = false;
		sym = DatabaseStore.sortByTime(sym);
		for(int i=0; i<sym.size(); i++){
			DatabaseStore d = sym.get(i);
			if(Integer.parseInt(d.value)==1){
				if(symptoms.containsKey(symptom_name)){
					symptoms.get(symptom_name).add(d.date);
					added=true;
				}else{
					ArrayList<Date> dates = new ArrayList<Date>(); 
					dates.add(d.date);
					symptoms.put(symptom_name,dates);
					added=true;
				}
			}
		}
		return added;
	}
	
	public void nextMonth(View v){
		dateOffset = 1;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(currentDate);
		gc.add(Calendar.MONTH, dateOffset);
		currentDate = gc.getTime();
		
		
		renderMonth(currentDate);
	}
	
	public void previousMonth(View v){
		dateOffset = -1;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(currentDate);
		gc.add(Calendar.MONTH, dateOffset);
		currentDate = gc.getTime();
		renderMonth(currentDate);
	}
	
	private void getDbValues(Date date){
		symptoms.clear();
		month_regret = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("regret", date);
		month_dizzy = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_dizzy", date);
		month_nausea = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_nausea", date);
		month_vomit = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("vomit", date);
		month_headache = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_headache", date);
		month_memory = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("memory", date);
		month_fatigue = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_fatigue", date);
		
		ArrayList<DatabaseStore> drinking_days = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("drank", date);
		total_drinking_days = 0;
		if (drinking_days !=  null){
			total_drinking_days = drinking_days.size();
		}
		
		//reset all text values
		fatigue_cnt.setText("");
		memory_cnt.setText("");
		headache_cnt.setText("");
		vomit_cnt.setText("");
		nausea_cnt.setText("");
		dizzy_cnt.setText("");
		regret_cnt.setText("");
		
		if(month_regret == null){
			regret.setImageResource(R.drawable.smiley_regret_blank);
			regret.setClickable(false);
		}else{
			boolean added = addToSymptomHash(month_regret,"regret");
			if(!added){
				regret.setImageResource(R.drawable.smiley_regret_blank);
				regret.setClickable(false);
			}else{
				regret.setImageResource(R.drawable.smiley_regret_filled);
				String ratio = symptoms.get("regret").size() + "/" + total_drinking_days;
				regret_cnt.setText(ratio);
				regret.setClickable(true);
			}
		}
		
		if(month_dizzy == null){
			dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
			dizzy.setClickable(false);
		}else{
			boolean added = addToSymptomHash(month_dizzy,"dizzy");
			if(!added){
				dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
				dizzy.setClickable(false);
			}else{
				dizzy.setImageResource(R.drawable.smiley_dizziness_filled);
				String ratio = symptoms.get("dizzy").size() + "/" + total_drinking_days;
				dizzy_cnt.setText(ratio);
				dizzy.setClickable(true);
			}
		}
		
		if(month_nausea == null){
			nausea.setImageResource(R.drawable.smiley_nausea_blank);
			nausea.setClickable(false);
		}else{
			boolean added = addToSymptomHash(month_nausea,"nausea");
			if(!added){
				nausea.setImageResource(R.drawable.smiley_nausea_blank);
				nausea.setClickable(false);
			}else{
				nausea.setImageResource(R.drawable.smiley_nausea_filled);
				String ratio = symptoms.get("nausea").size() + "/" + total_drinking_days;
				nausea_cnt.setText(ratio);
				nausea.setClickable(true);
			}
		}
		if(month_vomit == null){
			vomit.setImageResource(R.drawable.smiley_vomit_blank);
			vomit.setClickable(false);
		}else{
			boolean added = addToSymptomHash(month_vomit,"vomit");
			if(!added){
				vomit.setImageResource(R.drawable.smiley_vomit_blank);
				vomit.setClickable(false);
			}else{
				vomit.setImageResource(R.drawable.smiley_vomit_filled);
				String ratio = symptoms.get("vomit").size() + "/" + total_drinking_days;
				vomit_cnt.setText(ratio);
				vomit.setClickable(true);
			}
		}
		if(month_headache == null){
			headache.setImageResource(R.drawable.smiley_headache_blank);
			headache.setClickable(false);
		}else{
			boolean added = addToSymptomHash(month_headache,"headache");
			if(!added){
				headache.setImageResource(R.drawable.smiley_headache_blank);
				headache.setClickable(false);
			}else{
				headache.setImageResource(R.drawable.smiley_headache_filled);
				String ratio = symptoms.get("headache").size() + "/" + total_drinking_days;
				headache_cnt.setText(ratio);
				headache.setClickable(true);
			}
		}
		if(month_memory == null){
			memory.setImageResource(R.drawable.smiley_memory_blank);
			memory.setClickable(false);
		}else{
			boolean added = addToSymptomHash(month_memory,"memory");
			if(!added){
				memory.setImageResource(R.drawable.smiley_memory_blank);
				memory.setClickable(false);
			}else{
				memory.setImageResource(R.drawable.smiley_memory_filled);
				String ratio = symptoms.get("memory").size() + "/" + total_drinking_days;
				memory_cnt.setText(ratio);
				memory.setClickable(true);
			}
		}
		if(month_fatigue == null){
			fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
			fatigue.setClickable(false);
		}else{
			boolean added = addToSymptomHash(month_fatigue,"fatigue");
			if(!added){
				fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
				fatigue.setClickable(false);
			}else{
				fatigue.setImageResource(R.drawable.smiley_fatigue_filled);
				String ratio = symptoms.get("fatigue").size() + "/" + total_drinking_days;
				fatigue_cnt.setText(ratio);
				fatigue.setClickable(true);
			}
		}
		
	}
	
	public static double getMax(ArrayList<DatabaseStore> bacVals){
		double max = 0.0;
		if (bacVals!=null){
			for(int i=0; i<bacVals.size(); i++){
				if(max < Double.parseDouble(bacVals.get(i).value)){
					max = Double.parseDouble(bacVals.get(i).value);
				}
			}
		}
		return max;
	}
	
	public static double getHours(ArrayList<DatabaseStore> drinkVals){
		double time=0.0;
		Date startTime = null;
		if(drinkVals!=null && drinkVals.size()>1){
			if(Integer.parseInt(drinkVals.get(0).value) == 1) {
				startTime = drinkVals.get(0).date;
				 time = (drinkVals.get(drinkVals.size()-1).date.getTime() - startTime.getTime())/(1000 * 60 * 60.0);
			}else{
				startTime = drinkVals.get(1).date;
				if (drinkVals.size() > 2){
					time = (drinkVals.get(drinkVals.size()-1).date.getTime() - startTime.getTime())/(1000 * 60 * 60.0);
				}
			}
		}		
		return time;
	}
	
	public static int getCount(ArrayList<DatabaseStore> drinkVals){
		int count = 0;
		if(drinkVals!=null){
			count = drinkVals.size();
			if ((Integer.parseInt(drinkVals.get(0).value)) > 1){
				count--;
			}
		}
		return count;
	}
	
	private void updateDialog(Date date){
		TextView infoDate = (TextView) dialog.findViewById(R.id.trendsDate);
		ImageView beer_icon = (ImageView)dialog.findViewById(R.id.sym_beer_icon);
		ImageView wine_icon = (ImageView) dialog.findViewById(R.id.sym_wine_icon);
		ImageView liquor_icon = (ImageView) dialog.findViewById(R.id.sym_liquor_icon);
		TextView bac_text = (TextView) dialog.findViewById(R.id.day_bac);
		TextView rate_text = (TextView) dialog.findViewById(R.id.day_rate);
		TextView time_text = (TextView) dialog.findViewById(R.id.day_drink_time);
		TextView count_text = (TextView) dialog.findViewById(R.id.day_cal_drink_count);
		ImageView sym_regret = (ImageView) dialog.findViewById(R.id.sym_regret_icon);
		ImageView sym_dizzy = (ImageView) dialog.findViewById(R.id.sym_dizzy_icon);
		ImageView sym_nausea =(ImageView) dialog.findViewById(R.id.sym_nausea_icon);
		ImageView sym_vomit =  (ImageView) dialog.findViewById(R.id.sym_vomit_icon);
		ImageView sym_headache =  (ImageView) dialog.findViewById(R.id.sym_headache_icon);
		ImageView sym_memory =  (ImageView) dialog.findViewById(R.id.sym_memory_icon);
		ImageView sym_fatigue =  (ImageView) dialog.findViewById(R.id.sym_fatigue_icon);
		
		
		ArrayList<DatabaseStore> beer_result = (ArrayList<DatabaseStore>) db.getVarValuesForDay("type_beer", date);
		ArrayList<DatabaseStore> wine_result = (ArrayList<DatabaseStore>) db.getVarValuesForDay("type_wine", date);
		ArrayList<DatabaseStore> liquor_result = (ArrayList<DatabaseStore>) db.getVarValuesForDay("type_liquor", date);
		ArrayList<DatabaseStore> bacVals = (ArrayList<DatabaseStore>)db.getVarValuesForDay("bac", date);
		ArrayList<DatabaseStore> drinkVals = (ArrayList<DatabaseStore>)db.getVarValuesForDay("drink_count", date);
		ArrayList<DatabaseStore> day_regret = (ArrayList<DatabaseStore>)db.getVarValuesForDay("regret", date);
		ArrayList<DatabaseStore> day_dizzy = (ArrayList<DatabaseStore>)db.getVarValuesForDay("symptom_dizzy", date);
		ArrayList<DatabaseStore> day_nausea = (ArrayList<DatabaseStore>)db.getVarValuesForDay("symptom_nausea", date);
		ArrayList<DatabaseStore> day_vomit = (ArrayList<DatabaseStore>)db.getVarValuesForDay("vomit", date);
		ArrayList<DatabaseStore> day_headache = (ArrayList<DatabaseStore>)db.getVarValuesForDay("symptom_headache", date);
		ArrayList<DatabaseStore> day_memory = (ArrayList<DatabaseStore>)db.getVarValuesForDay("memory", date);
		ArrayList<DatabaseStore> day_fatigue = (ArrayList<DatabaseStore>)db.getVarValuesForDay("symptom_fatigue", date);
		
		SimpleDateFormat date_ft = new SimpleDateFormat("EEE MM/dd/yy", Locale.US);
		infoDate.setText(date_ft.format(date));
		
		DecimalFormat formatter = new DecimalFormat("#.###");
		
		double maxBac = getMax(bacVals);
		bac_text.setText(formatter.format(maxBac) + " max BAC");
		
		double hrs = getHours(drinkVals);
		int count = getCount(drinkVals);
		
		count_text.setText(count + " drinks recorded");
		rate_text.setText(formatter.format(count/hrs) + " drinks / hour");		
		time_text.setText(formatter.format(hrs)+ " hours elapsed drinking");
		
		if(beer_result != null){
			int val = Integer.parseInt(beer_result.get(0).value);
			if(val == 1){
				beer_icon.setImageResource(R.drawable.ic_calendar_beer);
			}else{
				beer_icon.setImageResource(R.drawable.white_beer);
			}
		}
		if(wine_result != null){
			int val = Integer.parseInt(wine_result.get(0).value);
			if(val == 1){
				wine_icon.setImageResource(R.drawable.ic_calendar_wine);
			}else{
				wine_icon.setImageResource(R.drawable.white_wine);
			}
		}
		if(liquor_result != null){
			int val = Integer.parseInt(liquor_result.get(0).value);
			if(val == 1){
				liquor_icon.setImageResource(R.drawable.ic_calendar_liquor);
			}else{
				liquor_icon.setImageResource(R.drawable.white_liquor);
			}
		}
		
		if(day_dizzy == null){
			sym_dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
		}else{
			if(Integer.parseInt(day_dizzy.get(0).value)==0){
				sym_dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
			}else{
				sym_dizzy.setImageResource(R.drawable.smiley_dizziness_filled);
			}
		}
		
		if(day_nausea == null){
			sym_nausea.setImageResource(R.drawable.smiley_nausea_blank);
		}else{
			if(Integer.parseInt(day_nausea.get(0).value)==0){
				sym_nausea.setImageResource(R.drawable.smiley_nausea_blank);
			}else{
				sym_nausea.setImageResource(R.drawable.smiley_nausea_filled);
			}
		}
		if(day_vomit == null){
			sym_vomit.setImageResource(R.drawable.smiley_vomit_blank);
		}else{
			if(Integer.parseInt(day_vomit.get(0).value)==0){
				sym_vomit.setImageResource(R.drawable.smiley_vomit_blank);
			}else{
				sym_vomit.setImageResource(R.drawable.smiley_vomit_filled);
			}
		}
		if(day_headache == null){
			sym_headache.setImageResource(R.drawable.smiley_headache_blank);
		}else{
			if(Integer.parseInt(day_headache.get(0).value)==0){
				sym_headache.setImageResource(R.drawable.smiley_headache_blank);
			}else{
				sym_headache.setImageResource(R.drawable.smiley_headache_filled);
			}
		}
		if(day_memory == null){
			sym_memory.setImageResource(R.drawable.smiley_memory_blank);
		}else{
			if(Integer.parseInt(day_memory.get(0).value)==0){
				sym_memory.setImageResource(R.drawable.smiley_memory_blank);
			}else{
				sym_memory.setImageResource(R.drawable.smiley_memory_filled);
			}
		}
		if(day_fatigue == null){
			sym_fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
		}else{
			if(Integer.parseInt(day_fatigue.get(0).value)==0){
				sym_fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
			}else{
				sym_fatigue.setImageResource(R.drawable.smiley_fatigue_filled);
			}
		}
		if(day_regret == null){
			sym_regret.setImageResource(R.drawable.smiley_regret_blank);
		}else{
			if(Integer.parseInt(day_regret.get(0).value)==0){
				sym_regret.setImageResource(R.drawable.smiley_regret_blank);
			}else{
				sym_regret.setImageResource(R.drawable.smiley_regret_filled);
			}
		}
		
		
		ImageView face = (ImageView)dialog.findViewById(R.id.drink_calendar_day);
		//Update the face color
		((GradientDrawable)((LayerDrawable) face.getDrawable()).getDrawable(0)
				).setColor(DrinkCounter.getBacColor(maxBac));	
		
		int icon_face = DrinkCalendar.getFaceIcon(maxBac);
		//Update the face icon
		Drawable to_replace = getResources().getDrawable(icon_face);	
		((LayerDrawable) face.getDrawable()).setDrawableByLayerId(
				R.id.face_icon, to_replace);
		face.invalidate();
		face.refreshDrawableState();
	}
	
	private void showInfo(String key, String label){
		dialog = new Dialog(TrendFace.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.trend_info);
		
		TextView symLabel = (TextView) dialog.findViewById(R.id.symptom_label);
		symLabel.setText(label);
		
		Button right = (Button) dialog.findViewById(R.id.trends_left_arrow);
		Button left = (Button) dialog.findViewById(R.id.trends_right_arrow);
		Button close = (Button)dialog.findViewById(R.id.close_trend_info);
		
	
		
		int dateIndex =0;
		
		final ArrayList<Date> dateVals = symptoms.get(key);
		currentInfo=0;
		right.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentInfo>0){
					currentInfo--;
					updateDialog(dateVals.get(currentInfo));
				}
			}
		});
		left.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentInfo < dateVals.size()-1){
					currentInfo++;
					updateDialog(dateVals.get(currentInfo));
				}
			}
		});
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				dialog.dismiss();
			}
		});

		updateDialog(dateVals.get(0));
		
		dialog.show();
	}
	
	private void setupFaces(){
		regret = (ImageView) findViewById(R.id.regret);
		dizzy = (ImageView) findViewById(R.id.dizzy);
		nausea =(ImageView) findViewById(R.id.nausea);
		vomit =  (ImageView) findViewById(R.id.vomit);
		headache =  (ImageView) findViewById(R.id.headache);
		memory =  (ImageView) findViewById(R.id.memory);
		fatigue =  (ImageView) findViewById(R.id.fatigue);
		
		trendDate = (TextView)findViewById(R.id.trendMonth);
		//left_month = (Button) findViewById(R.id.month_left_arrow);
		SimpleDateFormat month_ft = new SimpleDateFormat("MMMM yyyy", Locale.US);
		trendDate.setText(month_ft.format(currentDate));
		
		regret_cnt = (TextView) findViewById(R.id.regret_count);
		dizzy_cnt = (TextView) findViewById(R.id.dizzy_count);
		nausea_cnt =(TextView) findViewById(R.id.nausea_count);
		vomit_cnt =  (TextView) findViewById(R.id.vomit_count);
		headache_cnt =  (TextView) findViewById(R.id.headache_count);
		memory_cnt =  (TextView) findViewById(R.id.memory_count);
		fatigue_cnt =  (TextView) findViewById(R.id.fatigue_count);
		
		//setup onclick listeners
		regret.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo("regret", "Regret " + regret_cnt.getText());
				
			}
		});
		dizzy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo("dizzy", "Dizziness " + dizzy_cnt.getText());
			}
		});
		nausea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo("nausea", "Nausea " + nausea_cnt.getText());
				
			}
		});
		vomit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo("vomit", "Vomitting " + vomit_cnt.getText());
				
			}
		});
		headache.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo("headache", "Headache " + headache_cnt.getText());
			}
		});
		memory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showInfo("memory", "Memory Loss " + memory_cnt.getText());
			}
		});
		fatigue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showInfo("fatigue", "Fatigue " + fatigue_cnt.getText());
			}
		});
		
	}
}
