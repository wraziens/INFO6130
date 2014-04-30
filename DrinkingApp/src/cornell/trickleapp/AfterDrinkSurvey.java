package cornell.trickleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AfterDrinkSurvey extends Activity implements OnClickListener{

	FlyOutContainer root;
	private CheckBox beer_chk, wine_chk, liquor_chk; 
	private SeekBar recall_bar, regret_bar;
	private RadioGroup vomit_group, memory_group, regret_group;
	private CheckBox sym_fatigue, sym_nausea, sym_headache, sym_dizzy;
	private Button save_btn;
	
	private String regret_result, vomit_result, memory_result;
	
	private DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.after_drinking_survey, null);

		this.setContentView(root);
		
		db = new DatabaseHandler(this);
		
		save_btn = (Button)findViewById(R.id.save_survey);
		
		beer_chk = (CheckBox) findViewById(R.id.type_beer);
		wine_chk = (CheckBox) findViewById(R.id.type_wine);
		liquor_chk = (CheckBox) findViewById(R.id.type_liquor);
		
		vomit_group = (RadioGroup) findViewById(R.id.vomit_group);
		regret_group = (RadioGroup) findViewById(R.id.regret_group);
		memory_group = (RadioGroup) findViewById(R.id.memory_group);
		
		sym_fatigue = (CheckBox) findViewById(R.id.symptom_fatigue);
		sym_headache = (CheckBox) findViewById(R.id.symptom_headache);
		sym_nausea = (CheckBox) findViewById(R.id.symptom_nausea);
		sym_dizzy = (CheckBox) findViewById(R.id.symptom_dizzy);
		
		vomit_result = "";
		memory_result = "";
		regret_result="";
		
		save_btn.setOnClickListener(this);
	
		initializeVomitGroup();
		initializeMemoryGroup();
		initializeRegretGroup();
		
	}

	private void initializeVomitGroup(){
		vomit_group.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.yes_vomit:
							vomit_result= "1";
							break;
					case R.id.no_vomit:
							vomit_result = "0";
							break;
					default:
						throw new RuntimeException(
									"Unknown Button ID For Location Question.");
					}
				}

			});
	}
	private void initializeMemoryGroup(){
		memory_group.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.yes_memory_loss:
							memory_result= "1";
							break;
					case R.id.no_memory_loss:
							memory_result = "0";
							break;
					default:
						throw new RuntimeException(
									"Unknown Button ID For Location Question.");
					}
				}

			});
	}
	private void initializeRegretGroup(){
		regret_group.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.yes_regret:
							regret_result= "1";
							break;
					case R.id.no_regret:
							regret_result = "0";
							break;
					default:
						throw new RuntimeException(
									"Unknown Button ID For Location Question.");
					}
				}

			});
	}
	
	private void saveToDB(){
		if(!memory_result.equals("")){
			db.updateOrAddYesterday("memory", memory_result);
		}
		if(!regret_result.equals("")){
			db.updateOrAddYesterday("regret", regret_result);
		}
		if(!vomit_result.equals("")){
			db.updateOrAddYesterday("vomit", vomit_result);
		}
		saveCheckBoxes();
		
	}
	
	private void saveCheckBoxes(){
		db.updateOrAddYesterday("type_beer", getCheckValue(beer_chk));
		db.updateOrAddYesterday("type_wine", getCheckValue(wine_chk));
		db.updateOrAddYesterday("type_liquor", getCheckValue(liquor_chk));
		db.updateOrAddYesterday("symptom_fatigue", getCheckValue(sym_fatigue));
		db.updateOrAddYesterday("symptom_headache", getCheckValue(sym_headache));
		db.updateOrAddYesterday("symptom_nausea", getCheckValue(sym_nausea));
		db.updateOrAddYesterday("symptom_dizzy", getCheckValue(sym_dizzy));
	}
	
	public int getCheckValue(CheckBox chk){
		if(chk.isChecked()){
			
			Toast.makeText(this, "checked" , Toast.LENGTH_SHORT).show();
			return 1;
		}else{
			Toast.makeText(this, "not checked" , Toast.LENGTH_SHORT).show();
			return 0;
		}
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.save_survey:
			saveToDB();
			db.close();
			finish();
			break;
		}
		
	}
	
	protected void onPause() {
		super.onPause();
		finish();
	}

}