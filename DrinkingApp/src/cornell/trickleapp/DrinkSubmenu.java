package cornell.trickleapp;

import cornell.trickleapp.R;
import cornell.trickleapp.R.id;
import cornell.trickleapp.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DrinkSubmenu extends Activity implements OnClickListener{

	Button cloud,calendar;
	Intent goToPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drinksubmenu);
		
		cloud=(Button)findViewById(R.id.bWordCloudVisualization);
		calendar=(Button)findViewById(R.id.bCalendarVisualization);
		cloud.setOnClickListener(this);
		calendar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.bWordCloudVisualization:
				goToPage=new Intent(this,DrinkCloud.class);
				startActivity(goToPage);
				break;
			case R.id.bCalendarVisualization:
				goToPage=new Intent(this,DrinkCalendar.class);
				startActivity(goToPage);
				break;
		}
		
	}

}
