package cornell.drinkingapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

public class ChickenCompare extends Activity {
	private Integer drink_count;
	private final Double CALORIES_PER_DRINK = 120.0;
	private final Double CALORIES_PER_CHICKEN = 264.0;
	private DatabaseHandler db;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		db = new DatabaseHandler(this);
		// each fried chicken leg has 264 calories
		// each drink has approximately 120 calories.
		Date date = new Date();
		drink_count = 0;
		ArrayList<DatabaseStore> drink_count_vals = (ArrayList<DatabaseStore>) db
				.getVarValuesForDay("drink_count", date);
		if (drink_count_vals != null) {
			drink_count = drink_count_vals.size();
		}

		Double drink_cals = drink_count.doubleValue() * CALORIES_PER_DRINK;
		int number_chickens = (int) Math
				.ceil(drink_cals / CALORIES_PER_CHICKEN);

	}
}
