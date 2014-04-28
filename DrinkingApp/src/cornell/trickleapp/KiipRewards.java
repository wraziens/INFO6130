package cornell.trickleapp;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.KiipFragmentCompat;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import me.kiip.sdk.Poptart;

public class KiipRewards extends FragmentActivity {
	
	KiipFragmentCompat mKiipFragment;
	Poptart mPoptart;
	Button showRewards;
	Context context;
	private final static String KIIP_TAG = "kiip_fragment_tag";

	public KiipRewards(Context c) {
		context = c;
		initialize();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
		    mKiipFragment = (KiipFragmentCompat) getSupportFragmentManager().findFragmentByTag(KIIP_TAG);
		} else {
		    mKiipFragment = new KiipFragmentCompat();
		    getSupportFragmentManager().beginTransaction().add(mKiipFragment, KIIP_TAG).commit();
		}
	}

	private void initialize() {

		Kiip kiip = Kiip.init((Application) context,
				"55da72ccf43139eb03c430bccd5e0fdf",
				"dd93cd6266436421e4f7ccdb076679fa");
		// TODO Auto-generated method stub
		Kiip.getInstance().saveMoment("something", 5, new Kiip.Callback() {
			@Override
			public void onFailed(Kiip kiip, Exception exception) {
				// no-op
			}

			@Override
			public void onFinished(Kiip kiip, Poptart poptart) {
				if (poptart != null) {
					// Display the notification information in your UI
					// showIntegratedNotification(poptart.getNotification());

					// Clear the notification in the poptart so it doesn't
					// display later
					// poptart.setNotification(null);
					onPoptart(poptart);
					// Save the poptart to display later
					// mPoptart = poptart;
					// showRewards.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Kiip.getInstance().startSession(new Kiip.Callback() {
			@Override
			public void onFailed(Kiip kiip, Exception exception) {
				// handle failure
			}

			@Override
			public void onFinished(Kiip kiip, Poptart poptart) {
				onPoptart(poptart);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		Kiip.getInstance().endSession(new Kiip.Callback() {
			@Override
			public void onFailed(Kiip kiip, Exception exception) {
				// handle failure
			}

			@Override
			public void onFinished(Kiip kiip, Poptart poptart) {
				onPoptart(poptart);
			}
		});
	}

	public void onPoptart(Poptart poptart) {
		mKiipFragment.showPoptart(poptart);
	}

}
