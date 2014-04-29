package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrendFace extends Activity {
	private DatabaseHandler db;
	
	private ImageView regret, dizzy, nausea, vomit, headache, memory, fatigue;
	private TextView trendDate;
	
	private ArrayList<DatabaseStore> month_regret, month_dizzy, month_nausea, month_vomit, month_headache, month_memory, month_fatigue;
	private HashMap<String, ArrayList<Date>> symptoms;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.trendfaces);

		db = new DatabaseHandler(this);
		symptoms = new HashMap<String, ArrayList<Date>>();
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		Boolean checkSurveyed = getPrefs.getBoolean("hints", true);
		
		if (checkSurveyed) {
			Intent openTutorial = new Intent(this, DrinkCounterTutorial.class);
			startActivity(openTutorial);
		}
		setupFaces();
		getDbValues();

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
	
	
	private void getDbValues(){
		Date date = new Date();
		month_regret = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("regret", date);
		month_dizzy = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_dizzy", date);
		month_nausea = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_nausea", date);
		month_vomit = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_vomit", date);
		month_headache = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_headache", date);
		month_memory = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_memory", date);
		month_fatigue = (ArrayList<DatabaseStore>)db.getVarValuesForMonth("symptom_fatigue", date);
		
		
		
		if(month_regret == null){
			regret.setImageResource(R.drawable.smiley_regret_blank);
		}else{
			boolean added = addToSymptomHash(month_regret,"regret");
			if(!added){
				regret.setImageResource(R.drawable.smiley_regret_blank);
			}
		}
		
		if(month_dizzy == null){
			dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
		}else{
			boolean added = addToSymptomHash(month_dizzy,"dizzy");
			if(!added){
				dizzy.setImageResource(R.drawable.smiley_dizziness_blank);
			}
		}
		
		if(month_nausea == null){
			nausea.setImageResource(R.drawable.smiley_nausea_blank);
		}else{
			boolean added = addToSymptomHash(month_nausea,"nausea");
			if(!added){
				nausea.setImageResource(R.drawable.smiley_nausea_blank);
			}
		}
		if(month_vomit == null){
			vomit.setImageResource(R.drawable.smiley_vomit_blank);
		}else{
			boolean added = addToSymptomHash(month_vomit,"vomit");
			if(!added){
				vomit.setImageResource(R.drawable.smiley_vomit_blank);
			}
		}
		if(month_headache == null){
			headache.setImageResource(R.drawable.smiley_headache_blank);
		}else{
			boolean added = addToSymptomHash(month_headache,"headache");
			if(!added){
				headache.setImageResource(R.drawable.smiley_headache_blank);
			}
		}
		if(month_memory == null){
			memory.setImageResource(R.drawable.smiley_memory_blank);
		}else{
			boolean added = addToSymptomHash(month_memory,"memory");
			if(!added){
				memory.setImageResource(R.drawable.smiley_memory_blank);
			}
		}
		if(month_fatigue == null){
			fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
		}else{
			boolean added = addToSymptomHash(month_fatigue,"fatigue");
			if(!added){
				fatigue.setImageResource(R.drawable.smiley_fatigue_blank);
			}
		}
		
	}
	
	//removes any negative values from the lists
	private void cleanList(){
		
	}
	private void setupFaces(){
		regret = (ImageView) findViewById(R.id.regret);
		dizzy = (ImageView) findViewById(R.id.dizzy);
		nausea =(ImageView) findViewById(R.id.nausea);
		vomit =  (ImageView) findViewById(R.id.vomit);
		headache =  (ImageView) findViewById(R.id.headache);
		memory =  (ImageView) findViewById(R.id.memory);
		fatigue =  (ImageView) findViewById(R.id.fatigue);
		trendDate = (TextView)findViewById(R.id.trendDate);
		
		
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
