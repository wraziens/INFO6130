package cornell.trickleapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ColorAdapter extends BaseAdapter implements OnClickListener {
	private Context mContext;
	int monthSelected, yearSelected;
	private ArrayList<Integer> drinkingDays;
	private ArrayList<Integer> bacColors;
	private ArrayList<Double> maxbac;
	int daysSetBack = 0;
	static int daysInMonth = 0;
	static ArrayList<Button> buttonStore = new ArrayList<Button>();
	static int focused = 0;
	private DatabaseHandler db;
	private LayoutInflater inflater;
	
	private int sdk;
	
	private static final Integer SHIFT=6;

	public ColorAdapter(Context c, int m, int y, ArrayList<Integer> d,
			ArrayList<Double> mb, ArrayList<Integer> colors) {
		mContext = c;
		monthSelected = m;
		yearSelected = y;
		drinkingDays = d;
		maxbac = mb;
		bacColors = colors;
		db = new DatabaseHandler(mContext);
		inflater = LayoutInflater.from(c);

		sdk = android.os.Build.VERSION.SDK_INT;

		db.close();
	}

	// given month and year, it detects the 1st day of the month and see what
	// day it is, days in that month
	// and shift the calender accordingly.
	public int getCount() {
		Calendar selectedCalendar = Calendar.getInstance();
		selectedCalendar.set(yearSelected, monthSelected, 1);
		int daysInSelectedMonth = selectedCalendar
				.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayOfTheWeekSelected = selectedCalendar.get(Calendar.DAY_OF_WEEK);
		daysSetBack = dayOfTheWeekSelected - 1;
		return daysInSelectedMonth + daysSetBack + 7;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	private Button setBackground(Button view, Drawable drawable){
		if(this.sdk < android.os.Build.VERSION_CODES.JELLY_BEAN){
			view.setBackgroundDrawable(drawable);
		}else{
			view.setBackground(drawable);
		}
		return view;
	}
	
	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		
		Button view = (Button)inflater.inflate(R.layout.calendar_day_button, parent, false);

		//LayerDrawable circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
		//((GradientDrawable)circle.getDrawable(0)).setColor(Color.TRANSPARENT);
		GradientDrawable circle = (GradientDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
		circle.setColor(Color.TRANSPARENT);
		circle.setSize(10, 10);
		view = setBackground(view, circle);
		view.setEnabled(false);
		view.bringToFront();
		view.setHeight(20);
		view.setWidth(20);
		view.setTextColor(Color.BLACK);
		
		double bacLevel;

		final int dayOfMonth = position - daysSetBack - 7 + 1;	
		
		if (!drinkingDays.contains(position - SHIFT - daysSetBack)) {
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((Button)v).setTextSize(14);
					((DrinkCalendar) mContext).changeBottomDisplay("", 0, -1);
					if (focused > 0) {
						Button child = (Button)parent.getChildAt(focused);
						if (drinkingDays.contains((Integer) focused - SHIFT - daysSetBack)) {
							//Set the color of the circle background for the drinking days
							int i = drinkingDays.indexOf(focused - SHIFT - daysSetBack);
							GradientDrawable color_circle = (GradientDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
							color_circle.setColor(bacColors.get(i));
							color_circle.setSize(10, 10);
							//LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
							//((GradientDrawable)color_circle.getDrawable(0)).setColor(bacColors.get(i));
							child = setBackground(child, color_circle);
						}else{
							//set background of circle with no data
							GradientDrawable color_circle = (GradientDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
							color_circle.setColor(Color.TRANSPARENT);
							color_circle.setSize(20, 20);
							//LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
							//((GradientDrawable)color_circle.getDrawable(0)).setColor(Color.TRANSPARENT);
							child = setBackground(child, color_circle);
						}
					}
					focused = position;
					v.setSelected(true);
					((Button)v).setTextColor(Color.WHITE);
					
				}

			});
		}
		
		for (int n = 0; n < drinkingDays.size(); n++) {
			if (drinkingDays.get(n) + daysSetBack + 7 == position + 1) {
				
				GradientDrawable color_circle = (GradientDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
				color_circle.setColor(bacColors.get(n));
				color_circle.setSize(10, 10);
		
				
				view = setBackground(view, color_circle);
				view.setEnabled(true);
				view.setTextColor(Color.WHITE);
				bacLevel = maxbac.get(n);
				final double bac_lev = bacLevel;
				final int i = n;
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						DecimalFormat formatter = new DecimalFormat("#.###");
						((DrinkCalendar) mContext).changeBottomDisplay(
								formatter.format(bac_lev), bac_lev, i);
						if (focused > 0) {
							Button child = (Button)parent.getChildAt(focused);
							if (drinkingDays.contains((Integer) focused - SHIFT - daysSetBack)) {
								int i = drinkingDays.indexOf(focused - SHIFT - daysSetBack);
								GradientDrawable color_circle = (GradientDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
								color_circle.setColor(bacColors.get(i));
								color_circle.setSize(20, 20);
								child = setBackground(child, color_circle);
							}else {
								if(child!=null){
									GradientDrawable color_circle = (GradientDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
									color_circle.setColor(Color.TRANSPARENT);
									color_circle.setSize(20, 20);
									child = setBackground(child, color_circle);
								}
							}
						}
						focused = position;
						v.setSelected(true);
						
					}
				});
			}
		}
		
		if (position > 6 && position < 7 + daysSetBack) {
			view.setVisibility(8);
		} else {

			view.setText("" + (position + 1 - daysSetBack - 7));
			view.setTextSize(14);
		}
		//Set the Week Titles for the Calendar
		switch (position) {
		case 0:
			view.setText("Sun");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(14);
			break;
		case 1:
			view.setText("Mon");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(14);
			break;
		case 2:
			view.setText("Tue");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(14);
			break;
		case 3:
			view.setText("Wed");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(14);
			break;
		case 4:
			view.setText("Thu");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(14);
			break;
		case 5:
			view.setText("Fri");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(14);
			break;
		case 6:
			view.setText("Sat");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(14);
			break;
		}

		return view;
	}

	
	public ArrayList<Button> getButtonView() {
		return buttonStore;

	}

	
	
	@Override
	public void onClick(View v) {
	}
}
