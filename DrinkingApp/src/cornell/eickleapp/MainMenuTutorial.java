package cornell.eickleapp;

import java.util.ArrayList;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MainMenuTutorial extends Activity implements OnCheckedChangeListener,OnClickListener{

	CheckBox hint;
	Button next, previous;
	TextView background1,background2;
	ArrayList<TextView> backgroundList=new ArrayList<TextView>();
	int currentBackgroundCount=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainmenututorial);
		hint=(CheckBox)findViewById(R.id.cbMainMenuTutorial);
		previous=(Button)findViewById(R.id.bMainMenuPrevious);
		next=(Button)findViewById(R.id.bMainMenuNext);
		background1=(TextView)findViewById(R.id.tvMainMenuTutorial1);
		background2=(TextView)findViewById(R.id.tvMainMenuTutorial2);
		backgroundList.add(background1);
		backgroundList.add(background2);
		
		hint.setOnCheckedChangeListener(this);
		previous.setOnClickListener(this);
		next.setOnClickListener(this);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView.getId()==R.id.cbMainMenuTutorial){
	        SharedPreferences getPrefs=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	        if (isChecked){
	        	SharedPreferences.Editor editer=getPrefs.edit().putBoolean("hints", true);
	        	editer.commit();
	        }
	        else{
	        	SharedPreferences.Editor editer=getPrefs.edit().putBoolean("hints", false);
	        	editer.commit();
	        	finish();
	        }
			
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.bMainMenuPrevious:
			if (currentBackgroundCount-1>-1){

				currentBackgroundCount--;
			}
			break;
		case R.id.bMainMenuNext:
			if (backgroundList.size()>currentBackgroundCount+1){

				currentBackgroundCount++;
			}
			break;

		}
		
	}

}
