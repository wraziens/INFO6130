package cornell.eickleapp;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DrinkCounterTutorial extends Activity implements OnCheckedChangeListener{

	CheckBox hint;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.drinktrackertutorial);
		hint=(CheckBox)findViewById(R.id.cbCounterTutorial);
		hint.setOnCheckedChangeListener(this);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView.getId()==R.id.cbCounterTutorial){
	        SharedPreferences getPrefs=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	        if (isChecked){
	        	SharedPreferences.Editor editer=getPrefs.edit().putBoolean("hints", true);
	        	editer.commit();
	        }
	        else{
	        	SharedPreferences.Editor editer=getPrefs.edit().putBoolean("hints", false);
	        	editer.commit();
	        }
			
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
