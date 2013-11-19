package cornell.drinkingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class FoodVisualization extends Activity {

	View visual;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// sample data lists
		int daysWithSkippedMeals = 4;
		int daysDidntSkipMeals = 2;
		double avgBacSkipMeal = 0.16;
		double avgBacNoSkipMeal = 0.09;
		int daysSkipMealHungOver = 3;
		int daysSkipMealSick = 2;
		int daysNoSkipHungOver = 1;
		int daysNoSkipMealSick = 0;
		

		visual = new FoodGraphics(this, daysWithSkippedMeals,
				daysDidntSkipMeals,avgBacSkipMeal, avgBacNoSkipMeal,daysSkipMealHungOver, daysSkipMealSick,
				daysNoSkipHungOver,daysNoSkipMealSick);
		
		setContentView(visual);
	}

}
