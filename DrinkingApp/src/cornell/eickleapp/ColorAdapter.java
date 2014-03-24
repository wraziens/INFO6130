package cornell.eickleapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

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
	static ArrayList<Integer> estimateStore = new ArrayList<Integer>();
	static List<DatabaseStore> estRelavantMonthList = new ArrayList<DatabaseStore>();
	
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

		sdk = android.os.Build.VERSION.SDK_INT;
		
		List<DatabaseStore> everyEstList = db.getAllVarValue("drink_guess");

		if (everyEstList != null) {
			for (int i = 0; i < everyEstList.size(); i++) {
				if ((everyEstList.get(i).month) - 1 == monthSelected
						&& everyEstList.get(i).year == yearSelected)
					estRelavantMonthList.add(everyEstList.get(i));
			}
		}
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
		Button view = new Button(mContext);

		LayerDrawable circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
		((GradientDrawable)circle.getDrawable(1)).setColor(Color.TRANSPARENT);
		view = setBackground(view, circle);
		view.setEnabled(false);
		view.setTextColor(Color.BLACK);
		
		double bacLevel;

		final int dayOfMonth = position - daysSetBack - 7 + 1;
		
		if (estRelavantMonthList != null) {
			if (position > (7 + daysSetBack) && estRelavantMonthList.size() > 0) {
				for (int i = 0; i < estRelavantMonthList.size(); i++) {
					if (estRelavantMonthList.get(i).day == dayOfMonth
							&& estRelavantMonthList.get(i).month - 1 == monthSelected
							&& estRelavantMonthList.get(i).year == yearSelected) {
						estimateStore.add(position);
					}
				}
			}
		}	
		
		if (!drinkingDays.contains(position - SHIFT - daysSetBack)) {
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((Button)v).setTextSize(14f);
					((DrinkCalendar) mContext).changeBottomDisplay("", 0, -1);
					if (focused > 0) {
						Button child = (Button)parent.getChildAt(focused);
						if (drinkingDays.contains((Integer) focused - SHIFT - daysSetBack)) {
							//Set the color of the circle background for the drinking days
							int i = drinkingDays.indexOf(focused - SHIFT - daysSetBack);
							LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
							((GradientDrawable)color_circle.getDrawable(1)).setColor(bacColors.get(i));
							child = setBackground(child, color_circle);
							//child.setBackground(color_circle);
							((Button)child).setTextColor(Color.BLACK);
									
						}else if (estimateStore.contains(focused)) {
							((Button)child).setTextColor(Color.WHITE);
							((Button)child).setEnabled(true);
						}else{
							//set background of circle with no data
							LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
							((GradientDrawable)color_circle.getDrawable(1)).setColor(Color.TRANSPARENT);
							child = setBackground(child, color_circle);
							//child.setBackground(color_circle);
							((Button)child).setTextColor(Color.BLACK);
						}
					}
					focused = position;
					v.setSelected(true);
					if (estimateStore.contains(focused)) {
						int estNo = 0;
						int s = focused - daysSetBack - 7 + 1;
						for (int i = 0; i < estRelavantMonthList.size(); i++) {
							if (estRelavantMonthList.get(i).day == s) {
								estNo = Integer.parseInt(estRelavantMonthList.get(i).value);
							}
						}
						((DrinkCalendar) mContext).changeBottomDisplay("", 0, -1);
					}
					((Button)v).setTextColor(Color.WHITE);
					
				}

			});
		}
		
		for (int n = 0; n < drinkingDays.size(); n++) {
			if (drinkingDays.get(n) + daysSetBack + 7 == position + 1) {
				LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
				((GradientDrawable)color_circle.getDrawable(1)).setColor(bacColors.get(n));
				view = setBackground(view, color_circle);
				//view.setBackground(color_circle);
				view.setEnabled(true);
				bacLevel = maxbac.get(n);
				final double bac_lev = bacLevel;
				final int i = n;
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						DecimalFormat formatter = new DecimalFormat("#.###");
						if (focused > 0) {
							Button child = (Button)parent.getChildAt(focused);
							if (drinkingDays.contains((Integer) focused - SHIFT - daysSetBack)) {
								int i = drinkingDays.indexOf(focused - SHIFT - daysSetBack);
								LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
								((GradientDrawable)color_circle.getDrawable(1)).setColor(bacColors.get(i));
								//child.setBackground(color_circle);
								child = setBackground(child, color_circle);
								((Button)child).setTextColor(Color.BLACK);
							}else if (estimateStore.contains(focused)) {
								LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
								((GradientDrawable)color_circle.getDrawable(1)).setColor(bacColors.get(i));
								//child.setBackground(color_circle);
								child = setBackground(child, color_circle);
								((Button)child).setTextColor(Color.WHITE);
								
							}else {
								LayerDrawable color_circle = (LayerDrawable)parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
								((GradientDrawable)color_circle.getDrawable(1)).setColor(Color.TRANSPARENT);
								//child.setBackground(color_circle);
								child = setBackground(child, color_circle);
								((Button)child).setTextColor(Color.BLACK);
							}
						}
						focused = position;
						v.setSelected(true);
						((Button)v).setTextColor(Color.WHITE);
						
					}
				});
			}
		}
		
		if (position > 6 && position < 7 + daysSetBack) {
			view.setVisibility(8);
		} else {

			view.setText("" + (position + 1 - daysSetBack - 7));
			view.setTextSize(14f);
		}
		//Set the Week Titles for the Calendar
		switch (position) {
		case 0:
			view.setText("Sun");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(16f);
			break;
		case 1:
			view.setText("Mon");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(16f);
			break;
		case 2:
			view.setText("Tue");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(16f);
			break;
		case 3:
			view.setText("Wed");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(16f);
			break;
		case 4:
			view.setText("Thu");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(16f);
			break;
		case 5:
			view.setText("Fri");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(16f);
			break;
		case 6:
			view.setText("Sat");
			view = setBackground(view, null);
			view.setTextColor(Color.rgb(81, 167, 249));	
			view.setEnabled(false);
			view.setTextSize(16f);
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
