package com.example.drinkingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class MoodSelection extends Activity implements OnClickListener{

	TextView test;
	GridView moodGrid;
	Button finish;
	ArrayList<String> stringInputs=new ArrayList<String>();
	ArrayList<String> inputResult=new ArrayList<String>();//stores items in this list to database
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moodselection);
		//sample items in arrayList
		stringInputs.add("happy");
		stringInputs.add("sad");
		stringInputs.add("bashful");
		stringInputs.add("Drunk");
		stringInputs.add("Vomit");
		stringInputs.add("Hate");
		stringInputs.add("Fight");
		
		moodGrid=(GridView)findViewById(R.id.gvMood);
		test=(TextView)findViewById(R.id.tvMoodTest);
		//takes an arraylist and populates items in the string arraylist into the view
		MoodAdapter adapter=new MoodAdapter(this,stringInputs);
		moodGrid.setAdapter(adapter);
		//drinkBacButtons=adapter.getButtonView();
		
		
		finish=(Button)findViewById(R.id.bMoodFinish);
		finish.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			case R.id.bMoodFinish:
				for (int n=0;n<inputResult.size();n++)
					test.setText(test.getText()+inputResult.get(n));
				//do something here
				break;
		}
		
	}
	public void addToResultList(String s){
		test.setText(s);
		inputResult.add(s);
	}
	public void removeFromResultList(String s){
		test.setText(s);
		inputResult.remove(s);
	}

}
