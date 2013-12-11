package cornell.eickleapp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import cornell.eickleapp.R;
import cornell.eickleapp.R.id;
import cornell.eickleapp.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class DrinkReview extends Activity implements OnClickListener {
	private DatabaseHandler db;
	private TextView drinks, bac;
	private Button finish;
	
	String drinkCount;
	Double maxBac;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHandler(this);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.drinkreview);
		
		drinks = (TextView)findViewById(R.id.review_drinks);
		bac = (TextView)findViewById(R.id.review_bac);
		getDrinkCount();
		getMaxBac();
		
		drinks.setText("You recorderd having " + drinkCount + " drinks last night.");
		DecimalFormat formatter = new DecimalFormat("#.###");
		
		bac.setText("Your maximum Estimated Blood Alcohol Level for the night was " + formatter.format(maxBac));
		
		finish = (Button) findViewById(R.id.review_finish);
		finish.setOnClickListener(this);
	}
	
	private void getMaxBac(){
		Date date = new Date();
		ArrayList<DatabaseStore> bacs = (ArrayList<DatabaseStore>)db.getVarValuesForYesterday("bac", date);
		maxBac = Double.valueOf(0);
		for (int i=0; i<bacs.size(); i++){
			Double cur = Double.valueOf(bacs.get(i).value);
			if(cur > maxBac){
				maxBac = cur;
			}
		}
	}
	
	//returns the number of drinks for the previous night
	private void getDrinkCount(){
		Date date = new Date();
		ArrayList<DatabaseStore> drink_vals = (ArrayList<DatabaseStore>)db.getVarValuesForYesterday("drink_count", date);
		drink_vals = DatabaseStore.sortByTime(drink_vals);
		drinkCount = String.valueOf(drink_vals.size());
	}
	
	private void saveToDB(){
		db.addValueYesterday("max_bac", maxBac.toString());
	}
	
	@Override
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.review_finish:
			Intent intent = new Intent();
			setResult(3, intent);
			saveToDB();
			finish();
			break;
		}
	}
}
