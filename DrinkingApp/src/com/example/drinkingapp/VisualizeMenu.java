package com.example.drinkingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VisualizeMenu extends Activity implements OnClickListener{

	Button exercise,drink,social;
	Intent goToThisPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vmenu);
		exercise=(Button)findViewById(R.id.bExercise);
		drink=(Button)findViewById(R.id.bDrink);
		social=(Button)findViewById(R.id.bSocialization);
		exercise.setOnClickListener(this);
		drink.setOnClickListener(this);
		social.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
			case R.id.bExercise:
				goToThisPage = new Intent(this, VisualizeExercise.class);
				startActivity(goToThisPage);
				break;
			case R.id.bDrink:
				goToThisPage = new Intent(this, DrinkSubmenu.class);
				startActivity(goToThisPage);
				break;
			case R.id.bSocialization:
				goToThisPage = new Intent(this, SocialChart.class);
				startActivity(goToThisPage);
				break;
		
		}
		
	}

}
