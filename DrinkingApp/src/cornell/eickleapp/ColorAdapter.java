package cornell.eickleapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cornell.eickleapp.R;
import cornell.eickleapp.R.drawable;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ColorAdapter extends BaseAdapter implements OnClickListener {
	private Context mContext;
	int monthSelected, yearSelected;
	ArrayList<Integer> drinkingDays;
	private ArrayList<Integer> bacColors;
	ArrayList<Double> maxbac;
	int daysSetBack = 0;
	static int daysInMonth = 0;
	static ArrayList<Button> buttonStore = new ArrayList<Button>();
	static int focused = 0;
	TextView test;
	DatabaseHandler db;
	static ArrayList<Integer> estimateStore = new ArrayList<Integer>();
	static int timesTouched=0;
	static List<DatabaseStore> estRelavantMonthList = new ArrayList<DatabaseStore>();

	public ColorAdapter(Context c, int m, int y, ArrayList<Integer> d,
			ArrayList<Double> mb, ArrayList<Integer> colors) {
		mContext = c;
		monthSelected = m;
		yearSelected = y;
		drinkingDays = d;
		maxbac = mb;
		bacColors = colors;
		db = new DatabaseHandler(mContext);

		List<DatabaseStore> everyEstList = db.getAllVarValue("drink_guess");

		if (everyEstList != null) {
			for (int i = 0; i < everyEstList.size(); i++) {
				int something = everyEstList.get(i).month;
				if ((everyEstList.get(i).month) - 1 == monthSelected
						&& everyEstList.get(i).year == yearSelected)
					estRelavantMonthList.add(everyEstList.get(i));
			}
		}
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

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		final Drawable circle = parent.getContext().getResources().getDrawable(R.drawable.calendar_day); 
		
		final Button view = new Button(mContext);
		view.setBackground(circle);
		double bacLevel;

		final int dayOfMonth = position - daysSetBack - 7 + 1;
		Boolean specialPaint = false;
		
		if (estRelavantMonthList != null) {
			if (position > (7 + daysSetBack)
					&& estRelavantMonthList.size() > 0) {
				for (int i = 0; i < estRelavantMonthList.size(); i++) {
					if (estRelavantMonthList.get(i).day == dayOfMonth
							&& estRelavantMonthList.get(i).month - 1 == monthSelected
							&& estRelavantMonthList.get(i).year == yearSelected) {
						specialPaint = true;
						view.setBackgroundColor(Color.rgb(255, 251, 188));
						view.setBackground(circle);
						estimateStore.add(position);
					}
				}
			}
		}

			if (!drinkingDays.contains(position - 6 - daysSetBack)) {
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						timesTouched++;
						((DrinkCalendar) mContext).changeBottomDisplay("", 0,
								-1, 0);
						if (focused > 0) {
							View child = parent.getChildAt(focused);
							if (drinkingDays.contains((Integer) focused - 6
									- daysSetBack)) {
								int i = drinkingDays.indexOf(focused - 6
										- daysSetBack);
								child.setBackgroundColor(bacColors.get(i));
								child.setBackground(circle);
							} else if (estimateStore.contains(focused)) {
								child.setBackgroundColor(Color.rgb(255, 251,
										188));
								child.setBackground(circle);
							} else {

								child.setBackground(circle);
							}
						}
						focused = position;
						v.setSelected(true);
						if (estimateStore.contains(focused)) {
							int estNo = 0;
							int s = focused - daysSetBack - 7 + 1;
							for (int i = 0; i < estRelavantMonthList.size(); i++) {
								if (estRelavantMonthList.get(i).day == s) {
									estNo = Integer
											.parseInt(estRelavantMonthList
													.get(i).value);
									int dummy = 0;
								}
							}
							((DrinkCalendar) mContext).changeBottomDisplay("",
									0, -1, estNo);
						}
						view.setBackgroundResource(R.drawable.border);

					}

				});
			}
			for (int n = 0; n < drinkingDays.size(); n++) {
				if (drinkingDays.get(n) + daysSetBack + 7 == position + 1) {
					view.setBackgroundColor(bacColors.get(n));
					view.setBackground(circle);
					bacLevel = maxbac.get(n);
					final double bac_lev = bacLevel;
					final int i = n;
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							timesTouched++;

							DecimalFormat formatter = new DecimalFormat("#.###");
							((DrinkCalendar) mContext).changeBottomDisplay(
									formatter.format(bac_lev), bac_lev, i, 0);
							if (focused > 0) {
								View child = parent.getChildAt(focused);
								if (drinkingDays.contains((Integer) focused - 6
										- daysSetBack)) {
									int i = drinkingDays.indexOf(focused - 6
											- daysSetBack);
									child.setBackgroundColor(bacColors.get(i));
								} else if (estimateStore.contains(focused)) {
									child.setBackgroundColor(Color.rgb(255,
											251, 188));
								} else {

									child.setBackground(circle);
								}
							}
							focused = position;
							v.setSelected(true);
							view.setBackgroundResource(R.drawable.border);
						}

					});
				}

			}
		
		if (position > 6 && position < 7 + daysSetBack) {
			view.setVisibility(8);
		} else {

			view.setText("" + (position + 1 - daysSetBack - 7));
			view.setTextSize(10f);
			// sets up the Days of the week display
		}
		switch (position) {
		case 0:
			view.setBackgroundColor(Color.WHITE);
			view.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_bright));
			view.setText("Su");
			view.setEnabled(false);
			break;
		case 1:
			view.setBackgroundColor(Color.WHITE);
			view.setText("M");
			view.setEnabled(false);
			break;
		case 2:
			view.setBackgroundColor(Color.WHITE);
			view.setText("T");
			view.setEnabled(false);
			break;
		case 3:
			view.setBackgroundColor(Color.WHITE);
			view.setText("W");
			view.setEnabled(false);
			break;
		case 4:
			view.setBackgroundColor(Color.WHITE);
			view.setText("Th");
			view.setEnabled(false);
			break;
		case 5:
			view.setBackgroundColor(Color.WHITE);
			view.setText("F");
			view.setEnabled(false);
			break;
		case 6:
			view.setBackgroundColor(Color.WHITE);
			view.setText("Sa");
			view.setEnabled(false);
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
