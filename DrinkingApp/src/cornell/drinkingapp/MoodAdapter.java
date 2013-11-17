package cornell.drinkingapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ToggleButton;

public class MoodAdapter extends BaseAdapter implements OnClickListener{
	
	private Context mContext;
	private ArrayList<String> stringInputs;
	//constructer
	public MoodAdapter(Context c,ArrayList<String> s) {
		mContext = c;
		stringInputs = s;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return stringInputs.size();
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
		String word=stringInputs.get(position);
		final ToggleButton toggleView=new ToggleButton(mContext);
		toggleView.setText(stringInputs.get(position));
		toggleView.setTextOn(word);
		toggleView.setTextOff(word);
		toggleView.setTextSize(10);
		toggleView.setOnClickListener(new OnClickListener(){

			@Override
            public void onClick(View v) {
				if (toggleView.isChecked())
					((MoodSelection)mContext).addToResultList(toggleView.getTextOn().toString());
				else
					((MoodSelection)mContext).removeFromResultList(toggleView.getTextOff().toString());
            }
			
		});
		return toggleView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
