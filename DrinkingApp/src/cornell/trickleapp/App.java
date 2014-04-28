package cornell.trickleapp;


import java.util.Date;

import me.kiip.sdk.Kiip;
import android.app.Application;

public class App extends Application {
	//NEEDED FOR KIIP DONT DELETE
	@Override
	public void onCreate() {
		super.onCreate();
		 Kiip kiip = Kiip.init(this, "8a47cb76acc8db9e54a82c27f0c8be46", "4d84fcaf0220a27e8c987a8c6533ac1d");
		    Kiip.setInstance(kiip);
	}
}
