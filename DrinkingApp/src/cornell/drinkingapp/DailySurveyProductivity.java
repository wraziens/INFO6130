package cornell.drinkingapp;

import cornell.drinkingapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DailySurveyProductivity extends Activity implements OnClickListener{

	
	Button finish;
	SeekBar prodBar, stressBar, performBar;
	String seekbarResult;
	private DatabaseHandler db;
	String prod_str, stress_str, perf_str ;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dailysurveyproductivity);

		db = new DatabaseHandler(this);
		
		finish=(Button)findViewById(R.id.prod_finish);
		finish.setOnClickListener(this);
		
		//initialize the seek bars
		prodBar = (SeekBar)findViewById(R.id.productive_seek);
		prodBar.setMax(100);
		prodBar.setProgress(0);
		initializeProdSeekBar();
		
		performBar = (SeekBar)findViewById(R.id.perform_seek);
		performBar.setMax(100);
		performBar.setProgress(0);
		initializePerfSeekBar();
		
		stressBar = (SeekBar)findViewById(R.id.stressed_seek);
		stressBar.setMax(100);
		stressBar.setProgress(0);
		initializeStressSeekBar();
	}

	private void initializeProdSeekBar() {
		prodBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				prod_str=String.valueOf(progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {	
			}
		});
	}
	
	private void initializeStressSeekBar() {
		stressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				stress_str=String.valueOf(progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {	
			}
		});
	}
	
	private void initializePerfSeekBar() {
		performBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				perf_str=String.valueOf(progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {	
			}
		});
	}
	private void saveToDB(){
		if(prod_str != null){
			db.updateOrAdd("productive", prod_str);
		}
		if(perf_str != null){
			db.updateOrAdd("performance", perf_str);
		}
		if(stress_str != null){
			db.updateOrAdd("stress_level", stress_str);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.prod_finish:
			saveToDB();
			finish();
			break;	
		}	
	}
	
	protected void onPause() {
		super.onPause();
		finish();
	}
}
