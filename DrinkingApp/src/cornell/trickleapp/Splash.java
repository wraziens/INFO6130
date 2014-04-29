package cornell.trickleapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity{


	DatabaseHandler db;
	MediaPlayer ourSong;
	@Override
	protected void onCreate(Bundle AlexLoveBacon) {
		// TODO Auto-generated method stub
		super.onCreate(AlexLoveBacon);

		db = new DatabaseHandler(getBaseContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);

		Thread timer=new Thread(){
			public void run(){
				try{
					sleep(500);
				} catch (InterruptedException e){
					e.printStackTrace();
				}finally{
					if (!db.variableExistAll("firstTime")){
					Intent openStartingPoint=new Intent(getBaseContext(),InitialSurvey.class);
					startActivity(openStartingPoint);
					}
					else{
						Intent openStartingPoint=new Intent(getBaseContext(),MainMenu3.class);
						startActivity(openStartingPoint);
					}
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();//destroys iteself, goes to onDestroy
	}
	
	
	

}
