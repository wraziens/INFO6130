package cornell.eickleapp.fragments;

import cornell.eickleapp.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {
	TextView health_top, health_bot, health_fill_top, health_fill_bot;

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		health_top = (TextView) getView().findViewById(R.id.health_top);
		health_bot = (TextView) getView().findViewById(R.id.health_bot);
		health_fill_top = (TextView) getView().findViewById(
				R.id.health_fill_top);
		health_fill_bot = (TextView) getView().findViewById(
				R.id.health_fill_bot);

		// health = (TextView) findViewById(R.id.health_layout);
		// health.setOnClickListener(this);

		// sets the % bar according to % of the

		setBar(0.9f, 0.85f, health_top, health_bot, health_fill_top,
				health_fill_bot);

		// health=(LinearLayout)findViewById(R.id.health_whole);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	private void setBar(float average, float fill, TextView top, TextView bot,
			TextView topFill, TextView botFill) {
		// TODO Auto-generated method stub

		float remain = fill - average;
		float topSlice = 0;
		float botSlice = 0;
		// above average
		if (remain > 0) {
			topSlice =1f-average - (remain);
		}
		if (remain == 0) {
			topSlice = 0.98f * average;
		}
		// if number is negative
		if (remain < 0) {
			topSlice = 0.98f * (1-average);
			botSlice = remain*-1;
		}

		float newBot = 0.98f * (average - botSlice);
		float newTop = 0.98f * (1f - average) - topSlice;

		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) top
				.getLayoutParams();
		params.weight = newTop;
		top.setLayoutParams(params);

		params = (LinearLayout.LayoutParams) bot.getLayoutParams();

		params.weight = newBot;
		bot.setLayoutParams(params);

		params = (LinearLayout.LayoutParams) topFill.getLayoutParams();
		params.weight = topSlice;
		topFill.setLayoutParams(params);

		params = (LinearLayout.LayoutParams) botFill.getLayoutParams();
		params.weight = botSlice;
		botFill.setLayoutParams(params);

	}

	private TextView findViewById(int healthTop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
