package cornell.drinkingapp;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	
	//start the app, checks if the preference for firstTime is true, if yes display the survey
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//full screen
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		SharedPreferences getPrefs=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		boolean checkSurveyed=getPrefs.getBoolean("initialSurvey", false);
		
		if (checkSurveyed==false){
			Intent openStartingPoint=new Intent(this,InitialSurvey.class);
			startActivity(openStartingPoint);//start activity initialsurvey
		
		}
		else{
		
			Intent goToMenu=new Intent(this,MainMenu.class);
			startActivity(goToMenu);
		
		//Intent openStartingPoint=new Intent("cornell.drinkingapp.MENU");
		//startActivity(openStartingPoint);//then goes to onPause method tutorial 16
		}
		//Intent goToMenu=new Intent("com.example.drinkingapp.MENU");
		//startActivity(goToMenu);
	
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	


}
