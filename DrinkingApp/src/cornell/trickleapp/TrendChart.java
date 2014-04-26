package cornell.trickleapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class TrendChart extends View {
	private ArrayList<Float> degrees;
	private ArrayList<Integer> colors;
	private ArrayList<DatabaseStore> values; 
	
	private Context c;
	private Paint paint;
	private RectF rectf;
	private int start_chart;
	
	public static final float MINUTES_IN_24_HRS=1440;
	
	public TrendChart(Context context, ArrayList<DatabaseStore> bac_values){
		super(context);
		values = bac_values;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		start_chart = 0;
		degrees = new ArrayList<Float>();
		colors = new ArrayList<Integer>();
		rectf = new RectF(50, 50, 500,500);
		constructValues();
		c=context;
	}
	
	int last_time = 0;
	private void constructValues(){
		for (int i=0; i< values.size(); i++){
			DatabaseStore bac_ds = values.get(i);
			
			//calculate the degrees
			if(i==0){
				start_chart = (bac_ds.hour+1) * 60 + bac_ds.minute;
				last_time = start_chart;
			}else{
				int current = (bac_ds.hour+1) * 60 + bac_ds.minute;
				Float degree = 360 * ((current - last_time)/MINUTES_IN_24_HRS);
				if(degree == 0){
					degree = (float)1;
				}
				degrees.add(degree);

				
				last_time=current;
			}
			//Get and add the appropriate color
			colors.add(DrinkCounter.getBacColor(Double.parseDouble(bac_ds.value)));
		}
	}
	
	@Override 
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Toast t = Toast.makeText(c, 
				"Adding a degrees." + degrees.size() , Toast.LENGTH_SHORT);
		t.setGravity(Gravity.TOP, 0, 100);
		t.show();
		paint.setColor(Color.GRAY);
		canvas.drawArc(rectf, 0,start_chart, true, paint);
		for(int i=0; i< degrees.size(); i++){
			t = Toast.makeText(c, 
					"degrees." + String.valueOf(degrees.get(i)) , Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 100);
			t.show();
			paint.setColor(colors.get(i));
			canvas.drawArc(rectf, start_chart, degrees.get(i), true, paint);
			start_chart += degrees.get(i);
		}
	}
	
}
