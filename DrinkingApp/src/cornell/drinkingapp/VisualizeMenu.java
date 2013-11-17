package cornell.drinkingapp;

import cornell.drinkingapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VisualizeMenu extends Activity implements OnClickListener{

	Button exercise,drink,social,food,mood,productivity;
	Intent goToThisPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vmenu);
		exercise=(Button)findViewById(R.id.bExercise);
		drink=(Button)findViewById(R.id.bDrink);
		//social=(Button)findViewById(R.id.bSocialization);
		//food=(Button)findViewById(R.id.bFood);
		productivity=(Button)findViewById(R.id.bProductivity);
		//mood=(Button)findViewById(R.id.bMood);
		exercise.setOnClickListener(this);
		drink.setOnClickListener(this);
		//social.setOnClickListener(this);
		//food.setOnClickListener(this);
		productivity.setOnClickListener(this);
		//mood.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			case R.id.bExercise:
				goToThisPage = new Intent(this, ExerciseVisualization.class);
				startActivity(goToThisPage);
				break;
			case R.id.bDrink:
				goToThisPage = new Intent(this, DrinkCalendar.class);
				startActivity(goToThisPage);
				break;
				/*
			case R.id.bSocialization:
				goToThisPage = new Intent(this, SocialVisualization.class);
				startActivity(goToThisPage);
				break;
					*/
				/*
			case R.id.bFood:
				goToThisPage = new Intent(this, FoodVisualization.class);
				startActivity(goToThisPage);
				break;
						*/
			case R.id.bProductivity:
				goToThisPage = new Intent(this, ProductivityVisualization.class);
				startActivity(goToThisPage);
				break;
				/*
			case R.id.bMood:
				goToThisPage = new Intent(this, MoodVisualization.class);
				startActivity(goToThisPage);
				break;
				*/
		}
		
	}

}
