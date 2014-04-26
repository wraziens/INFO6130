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

	
	MediaPlayer ourSong;
	@Override
	protected void onCreate(Bundle AlexLoveBacon) {
		// TODO Auto-generated method stub
		super.onCreate(AlexLoveBacon);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);

		Thread timer=new Thread(){
			public void run(){
				try{
					sleep(1500);
				} catch (InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openStartingPoint=new Intent(getBaseContext(),MainActivity.class);
					startActivity(openStartingPoint);//then goes to onPause method tutorial 16
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
