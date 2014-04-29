package cornell.trickleapp.model;

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

	public TrendsSliceItem(int t, int n, double b) {
		//shorten it to 12 hr times
		if (t>43200)
			t=t-43200;
		timeStart = t;
		if (n>43200)
			n=n-43200;
		timeNextDrink = n;
		bac = b;
	}

	public void setTimeNextDrink(int inputInSec) {
		timeNextDrink = inputInSec;
	}

	public void setTimeStart(int inputInSec) {
		timeStart = inputInSec;
	}

	public void setBAC(int inputBAC) {
		bac = inputBAC;
	}

	public int getDuration() {
		if (timeNextDrink >= timeStart) {
			return timeNextDrink - timeStart;
		} else {
			return timeNextDrink + 43200 - timeStart;
		}
	}

	public float getDegree() {
		float something = getDuration();
		float something2 = something / 43200;
		float testing = something2 * 360f;
		return testing;
	}

	public float getStartDegree() {
		float test = getTimeStart() / 43200f * 360f - 90f;
		return test;
	}

	public int getTimeStart() {

		return timeStart;
	}

	public String getTimeStartInString() {
		int day = (int) TimeUnit.SECONDS.toDays(timeStart);
		long hours = TimeUnit.SECONDS.toHours(timeStart) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(timeStart)
				- (TimeUnit.SECONDS.toHours(timeStart) * 60);
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		Calendar time=new GregorianCalendar(2013,1,28,(int)hours,(int)minute,0);
		String result=df.format(time.getTime())+" PM";
		return result;
	}

	public int getTimeNextDrink() {

		return timeNextDrink;
	}

	public double getBAC() {

		return bac;
	}
}
