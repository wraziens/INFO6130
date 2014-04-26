package cornell.trickleapp;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AssessmentAdapter extends BaseAdapter implements OnClickListener{

	private DatabaseHandler db;
	Context context;
	ArrayList<String> nameList = new ArrayList<String>();
	ArrayList<String> classList = new ArrayList<String>();

	public AssessmentAdapter(Context c, ArrayList<String> n,
			ArrayList<String> cl) {
		context = c;
		nameList = n;
		classList = cl;
		db = new DatabaseHandler(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nameList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		// checks the list items depending on whether the user has given input
		// from before.

		// TODO Auto-generated method stub
		final Button view = new Button(context);
		// view.setGravity(17);
		view.setHeight(20);
		String classTarget=classList.get(position);
		Boolean checked = db.variableExist(classList.get(position) + "CheckList");
		String className=classList.get(position);
		
		if (className.equals("DailySurvey2")){
			if (checked) {
				view.setBackgroundResource(R.drawable.overallassessmentbuttonchecked);
			}
			else{
				view.setBackgroundResource(R.drawable.overallassessmentbutton);
			}
	}
		if (className.equals("DailySurveyExercise")){
			if (checked) {
				view.setBackgroundResource(R.drawable.exerciseassessmentbuttonchecked);
			}
			else{
				view.setBackgroundResource(R.drawable.exerciseassessmentbutton);
			}
		}
		if (className.equals("DailySurveyProductivity")){
			if (checked) {
				view.setBackgroundResource(R.drawable.productivityassessmentbuttonchecked);
			}
			else{
				view.setBackgroundResource(R.drawable.productivityassessmentbutton);
			}
		}
			
			
			
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (position){
				
				//context
				case 0:

						view.setBackgroundResource(R.drawable.overallassessmentbuttonchecked);
					break;
				case 1:

						view.setBackgroundResource(R.drawable.exerciseassessmentbuttonchecked);
					break;
				case 2:

						view.setBackgroundResource(R.drawable.productivityassessmentbuttonchecked);
					break;
					
				}
				

				try {
					//db.updateOrAdd(classList.get(position) + "CheckList","done");
					Class ourClass;
					ourClass = Class.forName("cornell.trickleapp."
							+ classList.get(position));
					Intent goToSurvey = new Intent(context, ourClass);
					context.startActivity(goToSurvey);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
