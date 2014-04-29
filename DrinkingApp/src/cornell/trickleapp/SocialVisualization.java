package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import cornell.trickleapp.model.TrendsSliceItem;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SocialVisualization extends Activity implements OnClickListener {
	private TimeBacGraph visual;

	private static FlyOutContainer root;
	private ArrayList<String> groupName = new ArrayList<String>();
	private ArrayList<Integer> hangoutCount = new ArrayList<Integer>();
	private ArrayList<Double> bacLevel = new ArrayList<Double>();
	private Button dateLeft, dateRight;
	private TextView dateMain;
	private Button beer, wine;
	private Button smileyNausia, smileyFatigue, smileyHeadache,
			smileyDizziness, smileyMemory, smileyVomit;
	private LinearLayout liquor;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private LinearLayout mainLayout;
	private ArrayList<TrendsSliceItem> sliceArray = new ArrayList<TrendsSliceItem>();
	
	private DatabaseHandler db;
	private Context context;
	private ArrayList<DatabaseStore> drinkCounts;
	private ArrayList<Double> bacVals;
	private ArrayList<TrendsSliceItem> morningCounts;
	private ArrayList<TrendsSliceItem> eveningCounts;
	private BacTime bacTime;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		
		bacTime = new BacTime(this);
		db = new DatabaseHandler(this);
		context = this;
		Date date = new Date();
		drinkCounts = (ArrayList<DatabaseStore>)db.getVarValuesDelay("bac", date);
		if (drinkCounts != null){
			drinkCounts = DatabaseStore.sortByTime(drinkCounts);
			//bacVals = bacTime.getBacValues(drinkCounts);
		
			morningCounts = new ArrayList<TrendsSliceItem>();
			eveningCounts = new ArrayList<TrendsSliceItem>();
			constructLists();
		}
		
		// initializes the necessary values
		//setupTrendSliceItem(drinkSecRaw, drinkBAC);

		visual = new TimeBacGraph(this, eveningCounts, morningCounts);
		setContentView(R.layout.trends);
		visual = new SocialGraphics(this, sliceArray);
		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.trends, null);

		this.setContentView(root);
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
	
	private void constructLists(){
		morningCounts.clear();
		eveningCounts.clear();
		DatabaseStore lastEvening = null;
		int lastEveningIndex = -1;
		DatabaseStore lastMorning = null;
		int lastMorningIndex = -1;
		//int lastRemovedEvening = 0;
		//int lastRemovedMorning = 0;
		int lastRemoved = 0;

		
		for(int i=0; i<drinkCounts.size(); i++) {
			DatabaseStore value = drinkCounts.get(i);
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(value.date);
			gc.add(Calendar.HOUR_OF_DAY, 6);
			
			
			if(value.time_seconds >= 43200){
				value.setDate(gc.getTime());
				Toast.makeText(context,
						"time  " + value.hour, Toast.LENGTH_SHORT).show();
				if (lastEvening!=null){
					TrendsSliceItem item = new TrendsSliceItem(lastEvening.time_seconds, value.time_seconds, i ,Double.parseDouble(lastEvening.value));
					if (lastRemoved> 0){
						item.setDrinkCount(item.getDrinkCount() -lastRemoved);
						Toast.makeText(context,
								"drinkCOunt " + String.valueOf(item.getDrinkCount() - lastRemoved), Toast.LENGTH_SHORT).show();
					}
					int removed = bacTime.adjustDrinkCount(item);
					Toast.makeText(context,
							"removed  " + removed, Toast.LENGTH_SHORT).show();
					lastRemoved += Math.min(removed, i);
					eveningCounts.add(item);
				} else if(lastMorning != null){
					TrendsSliceItem item = new TrendsSliceItem(lastMorning.time_seconds, value.time_seconds, i,Double.parseDouble(lastMorning.value));
					if (lastRemoved> 0){
						item.setDrinkCount(item.getDrinkCount() -lastRemoved);
					}
					int removed = bacTime.adjustDrinkCount(item);
					Toast.makeText(context,
							"removed22  " + removed, Toast.LENGTH_SHORT).show();
					Toast.makeText(context,
							"removed22  " + removed, Toast.LENGTH_SHORT).show();
					lastRemoved += Math.min(removed, i);
					Toast.makeText(context,
							"removed21  " + lastRemoved, Toast.LENGTH_SHORT).show();
					//eveningCounts.add(item);
					morningCounts.add(item);
					lastMorning = null;
				}
				lastEvening = value;
				lastEveningIndex = i;
			}else{
				value.setDate(gc.getTime());
				if (lastMorning!=null){
					TrendsSliceItem item = new TrendsSliceItem(lastMorning.time_seconds, value.time_seconds, i, Double.parseDouble(lastMorning.value));
					if (lastRemoved > 0){
						item.setDrinkCount(item.getDrinkCount() -lastRemoved);
					}
					int removed = bacTime.adjustDrinkCount(item);
					lastRemoved +=Math.min(removed, i);
					morningCounts.add(item);
				}
				lastMorning = value;
				lastMorningIndex = i;
			}
		}
		if (lastEvening != null){
			TrendsSliceItem item = new TrendsSliceItem(lastEvening.time_seconds, -1, drinkCounts.size(), Double.parseDouble(lastEvening.value));
			if (lastRemoved> 0){
				item.setDrinkCount(drinkCounts.size() -lastRemoved);
				Toast.makeText(context,
						"drinkCountRaw " + String.valueOf(drinkCounts.size()), Toast.LENGTH_SHORT).show();
				Toast.makeText(context,
						"lastRemoved" + String.valueOf(lastRemoved), Toast.LENGTH_SHORT).show();
				Toast.makeText(context,
						"drinkCOunt " + String.valueOf(item.getDrinkCount()), Toast.LENGTH_SHORT).show();
				Toast.makeText(context,
						"drinkBAC " +lastEvening.value, Toast.LENGTH_SHORT).show();
				
			}
			eveningCounts.add(item);
		}
		if(lastMorning != null){
			TrendsSliceItem item = new TrendsSliceItem(lastMorning.time_seconds, -1, drinkCounts.size(), Double.parseDouble(lastMorning.value));
			if (lastRemoved > 0){
				item.setDrinkCount(item.getDrinkCount() -lastRemoved);
			}
			morningCounts.add(item);
		}
	}

	/*
=======
	// given arrays of time (sec) when drinks were counted and corresponding
	// array of bac level
	private void setupTrendSliceItem(ArrayList<Integer> drinkSecRaw,
			ArrayList<Double> drinkBAC) {
		// TODO Auto-generated method stub
		for (int i = 0; i < drinkSecRaw.size(); i++) {
			if (i + 1 == drinkSecRaw.size()) {
				int one = drinkSecRaw.get(i);
				int two = drinkSecRaw.get(i) + 3600;
				double three = drinkBAC.get(i);
				sliceArray.add(new TrendsSliceItem(one, two, three));
				int n = 0 + 1;
			} else
				sliceArray.add(new TrendsSliceItem(drinkSecRaw.get(i),
						drinkSecRaw.get(i + 1), drinkBAC.get(i)));
		}
	}
	*/
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

	@SuppressLint("NewApi")
	private void Reboot() {
		Date date = new Date();
		drinkCounts = (ArrayList<DatabaseStore>)db.getVarValuesDelay("drink_count", date);
		bacVals = bacTime.getBacValues(drinkCounts);
		morningCounts = new ArrayList<TrendsSliceItem>();
		eveningCounts = new ArrayList<TrendsSliceItem>();
		constructLists();
		//ArrayList<DatabaseStore> drinkBAC = (ArrayList<DatabaseStore>)db.getVarValuesDelay("drink_count", date);
		visual = new TimeBacGraph(this, eveningCounts, morningCounts);
		mainLayout = (LinearLayout) findViewById(R.id.llTesting);
		// get window width
		Display display = getWindowManager().getDefaultDisplay();
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
			windowWidth = display.getWidth(); // deprecated
			windowHeight = display.getHeight(); // deprecated
		}else{
			Point size = new Point();
			display.getSize(size);
			windowWidth = size.x;
			windowHeight = size.y;
		}
		mainLayout.removeAllViewsInLayout();
		mainLayout.addView(visual, windowWidth, windowHeight / 2);
		beerWineLiquorVis(false, true, true);
		NausiaFatigueHeadacheDizzinessMemoryVomitVis(true, true, true, true,
				true, true);
		mainLayout.invalidate();
	}

}
