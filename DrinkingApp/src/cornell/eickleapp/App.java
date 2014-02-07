package cornell.eickleapp;

import me.kiip.sdk.Kiip;
import android.app.Application;

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		 Kiip kiip = Kiip.init(this, "55da72ccf43139eb03c430bccd5e0fdf", "dd93cd6266436421e4f7ccdb076679fa");
		    Kiip.setInstance(kiip);
	}
}
