package cornell.eickleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainMenu3 extends Activity implements OnClickListener {

	Button drinkCounterMenu, surveyMenu, dataMenu;

	FlyOutContainer root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		this.root = (FlyOutContainer) this.getLayoutInflater().inflate(
				R.layout.menu3, null);

		this.setContentView(root);

		drinkCounterMenu = (Button) findViewById(R.id.bDrinkCounterMenu);
		surveyMenu = (Button) findViewById(R.id.bSurveyMenu);
		dataMenu = (Button) findViewById(R.id.bDataMenu);
		drinkCounterMenu.setOnClickListener(this);
		surveyMenu.setOnClickListener(this);
		dataMenu.setOnClickListener(this);

	}

	public void toggleMenu(View v) {
		this.root.toggleMenu();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent goToThisPage;
		switch(v.getId()){
		
		case R.id.bDrinkCounterMenu:
			goToThisPage = new Intent(MainMenu3.this, DrinkCounter.class);
			startActivity(goToThisPage);
			
			break;
		case R.id.bSurveyMenu:
			/*
			goToThisPage = new Intent(MainMenu3.this, SurveyMenu.class);
			startActivity(goToThisPage);
			*/
			break;
		case R.id.bDataMenu:
			/*
			goToThisPage = new Intent(MainMenu3.this, DataMenu.class);
			startActivity(goToThisPage);
			*/
		break;
		}
		
	}
}
