package cornell.trickleapp;

import android.graphics.Color;
import android.graphics.Paint;

public class PieSlice {
	public float startingDegree;
	public float sweepingDegree;
	public double bac;
	public Paint paint;
	
	public PieSlice(float sweep, float start, double bacVal, Paint paint){
		this.startingDegree = start;
		this.sweepingDegree = sweep;
		this.bac = bacVal;
		this.paint = paint;
	}
	
	public void setPaint(){
		if (bac<=0){
			paint.setColor(Color.parseColor("#ecedf5"));
		}else{
			paint.setColor(DrinkCounter.getBacColor(bac));
		}
	}

}
