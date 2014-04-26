package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Trends extends Activity{
	private DatabaseHandler db;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trends);
		
		db = new DatabaseHandler(this);
		Date date = new Date();
		ArrayList<DatabaseStore> bac_values = (ArrayList<DatabaseStore>)db.getVarValuesDelay("bac", date);
		bac_values = DatabaseStore.sortByTime(bac_values);
		
		Toast t = Toast.makeText(getApplicationContext(), 
				"bac_vals = " + bac_values.size() , Toast.LENGTH_SHORT);
		t.setGravity(Gravity.TOP, 0, 100);
		t.show();
		LinearLayout linear = (LinearLayout) findViewById(R.id.linear);	
		
		TrendChart trends = new TrendChart(this,bac_values);
		linear.addView(trends);
	
	}
	
	
}