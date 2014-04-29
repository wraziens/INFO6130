package cornell.trickleapp;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TrendsSliceItem {

	private int timeStart;
	private int timeNextDrink;
	private double bac;
	private int drinkCount;
	private int origStart;
	
	public TrendsSliceItem(int t, int n, int count, double b) {
		//shorten it to 12 hr times
		if (t>43200)
			t=t-43200;
		timeStart = t;
		origStart = t;
		if (n>43200)
			n=n-43200;
		timeNextDrink = n;
		drinkCount = count;
		bac = b;
	}

	public void setTimeNextDrink(int inputInSec) {
		timeNextDrink = inputInSec;
	}

	public void setTimeStart(int inputInSec) {
		timeStart = inputInSec;
	}

	public void setDrinkCount(int count){
		drinkCount = count;
	}
	
	public void setBAC(int inputBAC) {
		bac = inputBAC;
	}

	public int getDuration() {
		return getDurationVal(timeNextDrink);
	}

	public int getDurationVal(int endValue){
		if (endValue >= timeStart) {
			return endValue - timeStart;
		} else {
			return endValue + 43200 - timeStart;
		}
	}
	
	public float getDegreeVal(int time_seconds){
		if(time_seconds > 43200){
			time_seconds -= 43200;
		}
		float duration = getDurationVal(time_seconds);
		float percent = duration / 43200;
		float circle = percent * 360f;
		return circle;
		
	}
	
	public float getDegree() {
		return getDegreeVal(timeNextDrink);
	}

	public float getStartDegree() {
		float test = getTimeStart() / 43200f * 360f - 90f;
		return test;
	}

	public int getTimeStart() {

		return timeStart;
	}

	public String getTimeStartInString() {
		int day = (int) TimeUnit.SECONDS.toDays(origStart);
		long hours = TimeUnit.SECONDS.toHours(origStart) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(origStart)
				- (TimeUnit.SECONDS.toHours(origStart) * 60);
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		Calendar time=new GregorianCalendar(2013,1,28,(int)hours,(int)minute,0);
		String result=df.format(time.getTime())+" PM";
		return result;
	}

	public boolean isEndTime() {
		if (timeNextDrink == -1){
			return true;
		}else{
			return false;
		}
	}
	
	public int getTimeNextDrink() {

		return timeNextDrink;
	}

	public double getBAC() {

		return bac;
	}
	
	public int getDrinkCount() {
		return drinkCount;
	}
}
