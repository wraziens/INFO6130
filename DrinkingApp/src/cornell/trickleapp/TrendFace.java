package cornell.trickleapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrendFace extends Activity {
	private DatabaseHandler db;
	
	private ImageView regret, dizzy, nausea, vomit, headache, memory, fatigue;
	private TextView regret_cnt, dizzy_cnt, nausea_cnt, vomit_cnt, headache_cnt, memory_cnt, fatigue_cnt;
	private TextView trendDate;
	private Button left_month, right_month;
	
	private ArrayList<DatabaseStore> month_regret, month_dizzy, month_nausea, month_vomit, month_headache, month_memory, month_fatigue;
	private HashMap<String, ArrayList<Date>> symptoms;
	
	private int total_drinking_days;
	
	private Date currentDate;
	private int dateOffset=0;
	
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
		
		Toast.makeText(TrendFace.this, "total drinking days" + total_drinking_days, Toast.LENGTH_SHORT).show();
		
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
		}else{
			boolean added = addToSymptomHash(month_regret,"regret");
			if(!added){
				regret.setImageResource(R.drawable.smiley_regret_blank);
			}else{
				regret.setImageResource(R.drawable.smiley_regret_filled);
				String ratio = symptoms.get("regret").size() + "/" + total_drinking_days;
				regret_cnt.setText(ratio);
			}
		}
		
		if(month_dizzy == null){
			dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
		}else{
			boolean added = addToSymptomHash(month_dizzy,"dizzy");
			if(!added){
				dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
			}else{
				dizzy.setImageResource(R.drawable.smiley_dizziness_filled);
				String ratio = symptoms.get("dizzy").size() + "/" + total_drinking_days;
				dizzy_cnt.setText(ratio);
			}
		}
		
		if(month_nausea == null){
			nausea.setImageResource(R.drawable.smiley_nausea_blank);
		}else{
			boolean added = addToSymptomHash(month_nausea,"nausea");
			if(!added){
				nausea.setImageResource(R.drawable.smiley_nausea_blank);
			}else{
				nausea.setImageResource(R.drawable.smiley_nausea_filled);
				String ratio = symptoms.get("nausea").size() + "/" + total_drinking_days;
				nausea_cnt.setText(ratio);
			}
		}
		if(month_vomit == null){
			vomit.setImageResource(R.drawable.smiley_vomit_blank);
		}else{
			boolean added = addToSymptomHash(month_vomit,"vomit");
			if(!added){
				vomit.setImageResource(R.drawable.smiley_vomit_blank);
			}else{
				vomit.setImageResource(R.drawable.smiley_vomit_filled);
				String ratio = symptoms.get("vomit").size() + "/" + total_drinking_days;
				vomit_cnt.setText(ratio);
			}
		}
		if(month_headache == null){
			headache.setImageResource(R.drawable.smiley_headache_blank);
		}else{
			boolean added = addToSymptomHash(month_headache,"headache");
			if(!added){
				headache.setImageResource(R.drawable.smiley_headache_blank);
			}else{
				headache.setImageResource(R.drawable.smiley_headache_filled);
				String ratio = symptoms.get("headache").size() + "/" + total_drinking_days;
				headache.setImageResource(R.drawable.smiley_headache_filled);
				headache_cnt.setText(ratio);
			}
		}
		if(month_memory == null){
			memory.setImageResource(R.drawable.smiley_memory_blank);
		}else{
			boolean added = addToSymptomHash(month_memory,"memory");
			if(!added){
				memory.setImageResource(R.drawable.smiley_memory_blank);
			}else{
				memory.setImageResource(R.drawable.smiley_memory_filled);
				String ratio = symptoms.get("memory").size() + "/" + total_drinking_days;
				memory_cnt.setText(ratio);
			}
		}
		if(month_fatigue == null){
			fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
			fatigue_cnt.setText("");
		}else{
			boolean added = addToSymptomHash(month_fatigue,"fatigue");
			if(!added){
				fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
				fatigue_cnt.setText("");
			}else{
				fatigue.setImageResource(R.drawable.smiley_fatigue_filled);
				String ratio = symptoms.get("fatigue").size() + "/" + total_drinking_days;
				fatigue_cnt.setText(ratio);
			}
		}
		
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
				Toast.makeText(TrendFace.this, "regret", Toast.LENGTH_SHORT).show();
				
			}
		});
		dizzy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TrendFace.this, "dizzy", Toast.LENGTH_SHORT).show();
				
			}
		});
		nausea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TrendFace.this, "nausea", Toast.LENGTH_SHORT).show();
				
			}
		});
		vomit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TrendFace.this, "vomit", Toast.LENGTH_SHORT).show();
				
			}
		});
		headache.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TrendFace.this, "headache", Toast.LENGTH_SHORT).show();
				/*final Dialog dialog = new Dialog(TrendFace.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dialog.setContentView(R.layout.trend_info);
				dialog.show();*/
			}
		});
		memory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TrendFace.this, "memory", Toast.LENGTH_SHORT).show();
				
			}
		});
		fatigue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(TrendFace.this, "fatigue", Toast.LENGTH_SHORT).show();
				
			}
		});
		
	}
}
