package cornell.eickleapp;

import java.util.ArrayList;

import cornell.eickleapp.model.TrendsSliceItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SocialVisualization extends Activity implements OnClickListener {
	private SocialGraphics visual;
	private ArrayList<String> groupName = new ArrayList<String>();
	private ArrayList<Integer> hangoutCount = new ArrayList<Integer>();
	private ArrayList<Double> bacLevel = new ArrayList<Double>();
	private Button dateLeft, dateRight;
	private TextView dateMain;
	private Button beer, wine;
	private Button smileyNausia, smileyFatigue, smileyHeadache, smileyDizziness,
			smileyMemory, smileyVomit;
	private LinearLayout liquor;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private LinearLayout mainLayout;
	private ArrayList<TrendsSliceItem> sliceArray = new ArrayList<TrendsSliceItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// mock data
		ArrayList<Integer> drinkSecRaw = new ArrayList<Integer>();
		drinkSecRaw.add(62500);
		drinkSecRaw.add(72500);
		drinkSecRaw.add(75600);
		drinkSecRaw.add(77200);
		drinkSecRaw.add(79200);
		drinkSecRaw.add(4680);
		ArrayList<Double> drinkBAC = new ArrayList<Double>();
		drinkBAC.add(0.03);
		drinkBAC.add(0.05);
		drinkBAC.add(0.09);
		drinkBAC.add(0.15);
		drinkBAC.add(0.29);
		drinkBAC.add(0.40);
		// initializes the necessary values
		setupTrendSliceItem(drinkSecRaw, drinkBAC);

		visual = new SocialGraphics(this, sliceArray);
		setContentView(R.layout.trends);
		mainLayout = (LinearLayout) findViewById(R.id.llTesting);
		// get window width
		Display display = getWindowManager().getDefaultDisplay();
		windowWidth = display.getWidth(); // deprecated
		windowHeight = display.getHeight(); // deprecated
		mainLayout.addView(visual, windowWidth, windowHeight / 2);

		dateMain = (TextView) findViewById(R.id.tvTrendsDate);
		dateLeft = (Button) findViewById(R.id.bTrendsDateLeft);
		dateRight = (Button) findViewById(R.id.bTrendsDateRight);

		beer = (Button) findViewById(R.id.bTrendsBeer);
		wine = (Button) findViewById(R.id.bTrendsWine);
		liquor = (LinearLayout) findViewById(R.id.bTrendsLiquor);

		smileyNausia = (Button) findViewById(R.id.bSmileyNausea);
		smileyFatigue = (Button) findViewById(R.id.bSmileyFatigue);
		smileyHeadache = (Button) findViewById(R.id.bSmileyHeadache);
		smileyDizziness = (Button) findViewById(R.id.bSmileyDizziness);
		smileyMemory = (Button) findViewById(R.id.bSmileyMemory);
		smileyVomit = (Button) findViewById(R.id.bSmileyVomit);

		dateLeft.setOnClickListener(this);
		dateRight.setOnClickListener(this);
		// sets the drinks icons on and off.
		beerWineLiquorVis(true, true, true);
		NausiaFatigueHeadacheDizzinessMemoryVomitVis(false, true, false, true,
				false, true);
	}

	//given arrays of time (sec) when drinks were counted and corresponding array of bac level
	private void setupTrendSliceItem(ArrayList<Integer> drinkSecRaw,
			ArrayList<Double> drinkBAC) {
		// TODO Auto-generated method stub
		for (int i = 0; i < drinkSecRaw.size(); i++) {
			if (i + 1 == drinkSecRaw.size())
				sliceArray.add(new TrendsSliceItem(drinkSecRaw.get(i),
						drinkSecRaw.get(i + 1) + 3600, drinkBAC.get(i)));
			else
				sliceArray.add(new TrendsSliceItem(drinkSecRaw.get(i),
						drinkSecRaw.get(i + 1), drinkBAC.get(i)));
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.bTrendsDateLeft:
			// get date from database
			dateMain.setText("1/1/2014");
			Reboot();
			break;
		case R.id.bTrendsDateRight:
			dateMain.setText("1/1/2013");
			Reboot();
			break;
		}

	}

	private void beerWineLiquorVis(Boolean b, Boolean w, Boolean l) {
		if (!b)
			beer.setVisibility(View.GONE);
		if (!w)
			wine.setVisibility(View.GONE);
		if (!l)
			liquor.setVisibility(View.GONE);

	}

	private void NausiaFatigueHeadacheDizzinessMemoryVomitVis(Boolean n,
			Boolean f, Boolean h, Boolean d, Boolean m, Boolean v) {
		if (n)
			smileyNausia.setSelected(true);
		if (f)
			smileyFatigue.setSelected(true);
		if (h)
			smileyHeadache.setSelected(true);
		if (d)
			smileyDizziness.setSelected(true);
		if (m)
			smileyMemory.setSelected(true);
		if (v)
			smileyVomit.setSelected(true);

	}

	private void Reboot() {
		sliceArray = new ArrayList<TrendsSliceItem>();

		sliceArray.add(new TrendsSliceItem(65500, 72500, 0.03));
		sliceArray.add(new TrendsSliceItem(72500, 74600, 0.09));
		sliceArray.add(new TrendsSliceItem(74600, 76200, 0.03));
		sliceArray.add(new TrendsSliceItem(76200, 77200, 0.15));
		sliceArray.add(new TrendsSliceItem(77200, 4680, 0.05));
		sliceArray.add(new TrendsSliceItem(4680, 5400, 0.30));
		sliceArray.add(new TrendsSliceItem(5400, 6500, 0.10));
		sliceArray.add(new TrendsSliceItem(6500, 6501, 0.10));

		visual = new SocialGraphics(this, sliceArray);
		mainLayout = (LinearLayout) findViewById(R.id.llTesting);
		// get window width
		Display display = getWindowManager().getDefaultDisplay();
		windowWidth = display.getWidth(); // deprecated
		windowHeight = display.getHeight(); // deprecated
		mainLayout.removeAllViewsInLayout();
		mainLayout.addView(visual, windowWidth, windowHeight / 2);
		beerWineLiquorVis(false, true, true);
		NausiaFatigueHeadacheDizzinessMemoryVomitVis(true, true, true, true,
				true, true);
		mainLayout.invalidate();
	}

}
