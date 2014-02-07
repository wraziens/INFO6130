package cornell.eickleapp;

import me.kiip.sdk.Kiip;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import me.kiip.sdk.Poptart;
public class Badges extends BaseActivity implements OnClickListener{

	private DatabaseHandler db;
	Poptart mPoptart;
	Button showRewards;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.badges);
		showRewards=(Button)findViewById(R.id.bRewards);
		showRewards.setOnClickListener(this);
		String android_id = Secure.getString(getBaseContext()
				.getContentResolver(), Secure.ANDROID_ID);

		Toast.makeText(getBaseContext(), android_id, Toast.LENGTH_LONG).show();
	
		db = new DatabaseHandler(this);
		// adds 1 count to badge "counter" when user access this component of
		// the app
		db.addValue("badge_count", 1);
		if (db.getAllVarValue("badge_count").size() > 1) {
			Kiip.getInstance().saveMoment("something", 5, new Kiip.Callback() {
			    @Override
			    public void onFailed(Kiip kiip, Exception exception) {
			        // no-op
			    }

			    @Override
			    public void onFinished(Kiip kiip, Poptart poptart) {
			    	if (poptart != null) {
				    	// Display the notification information in your UI
				    	//showIntegratedNotification(poptart.getNotification());

				    	// Clear the notification in the poptart so it doesn't display later
				    	poptart.setNotification(null);

				    	// Save the poptart to display later
				    	mPoptart = poptart;
				    	showRewards.setVisibility(View.VISIBLE);
				    }
			    }
			});
		}

	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		mPoptart.show(this);
	}

}
