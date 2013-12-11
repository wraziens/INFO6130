package cornell.eickleapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductivityAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<Double> gradesAverageList = new ArrayList<Double>();
	ArrayList<Double> productivityAverageList = new ArrayList<Double>();
	ArrayList<Double> stressAverageList = new ArrayList<Double>();
	ArrayList<Integer> daysDrankList = new ArrayList<Integer>();
	ArrayList<Double> bacAverageList = new ArrayList<Double>();

	// constructer
	public ProductivityAdapter(Context context, ArrayList<Double> g,
			ArrayList<Double> p, ArrayList<Double> s, ArrayList<Integer> d,
			ArrayList<Double> b) {
		mContext = context;
		gradesAverageList = g;
		productivityAverageList = p;
		stressAverageList = s;
		daysDrankList = d;
		bacAverageList = b;
	}

	// at least 7 to start for week1.
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int test = 4 + 4 * gradesAverageList.size();
		return test;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView view = new TextView(mContext);

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(),
				"fonts/dust.ttf");
		view.setTypeface(tf);
		view.setGravity(17);
		view.setHeight(150);
		view.setTextSize(16);
		switch (position) {
		case 0:
			view.setVisibility(8);
			break;
		case 1:

			view.setBackgroundResource(R.drawable.academicsicon);
			break;
		case 2:
			view.setBackgroundResource(R.drawable.productivityicon);
			break;
		case 3:
			view.setBackgroundResource(R.drawable.stressicon);
			break;
		/*
		 * case 4: view.setText("No. of Days Drinking"); break; case 5:
		 * view.setText("Avg. BAC");
		 * 
		 * break;
		 */
		}
		if (position == 4) {
			view.setText("Week 1");
			view.setGravity(17);

			view.setBackgroundColor(Color.GRAY);
			view.setTextColor(Color.WHITE);
		}
		if (position > 4 && position % 4 == 0) {
			view.setText("Week " + position / 4);
			view.setGravity(17);
		}
		if (position > 4 && position % 4 == 1) {
			view.setText(getGradeFromDouble(
					gradesAverageList.get((position - 1) / 4 - 1), false));
			if (position == 5) {
				view.setBackgroundColor(Color.GRAY);
				view.setTextColor(Color.WHITE);
			}

		}
		if (position > 4 && position % 4 == 2) {
			view.setText(getGradeFromDouble(
					productivityAverageList.get((position - 2) / 4 - 1), false));
			if (position == 6) {
				view.setBackgroundColor(Color.GRAY);
				view.setTextColor(Color.WHITE);
			}
		}
		if (position > 4 && position % 4 == 3) {
			view.setText(getGradeFromDouble(
					stressAverageList.get((position - 3) / 4 - 1), true));
			if (position == 7) {
				view.setBackgroundColor(Color.GRAY);
				view.setTextColor(Color.WHITE);
			}
		}

		if (position > 7) {
			int val = (position / 4) % 2;
			if (val == 0)
				view.setBackgroundColor(Color.WHITE);
			else {
				view.setBackgroundColor(Color.GRAY);
				view.setTextColor(Color.WHITE);

			}
		}

		return view;

	}

	private String getGradeFromDouble(Double rawGrade, Boolean reverse) {
		String grade = "N/A";
		if (!reverse) {
			if (rawGrade >= 90)
				grade = "A";
			else if (rawGrade >= 80 && rawGrade < 90)
				grade = "B";
			else if (rawGrade >= 70 && rawGrade < 80)
				grade = "C";
			else if (rawGrade >= 60 && rawGrade < 70)
				grade = "D";
			else if (rawGrade < 60)
				grade = "F";

		} else {
			if (rawGrade >= 80)
				grade = "F";
			else if (rawGrade >= 60 && rawGrade < 80)
				grade = "D";
			else if (rawGrade >= 40 && rawGrade < 60)
				grade = "C";
			else if (rawGrade >= 20 && rawGrade < 40)
				grade = "B";
			else if (rawGrade <= 20)
				grade = "A";
			else
				grade = "-";
		}

		return grade;
	}

}
