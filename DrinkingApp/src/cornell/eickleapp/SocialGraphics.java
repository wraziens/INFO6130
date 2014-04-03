package cornell.eickleapp;

import java.util.ArrayList;

import cornell.eickleapp.R;
import cornell.eickleapp.R.drawable;
import cornell.eickleapp.model.TrendsSliceItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

public class SocialGraphics extends View {

	private ArrayList<TrendsSliceItem> slicedArray;
	private float startingDegree;
	private Bitmap bitmap;
	private ArrayList<Integer> highestBacSlot = new ArrayList<Integer>();
	private ArrayList<Integer> highestDrinkingSpeedSlot = new ArrayList<Integer>();
	private ArrayList<Double> drinkingSpeedArray = new ArrayList<Double>();
	private int density;

	// constructor takes in social group name, its count, and bac level
	// associated with that group.
	public SocialGraphics(Context context, ArrayList<TrendsSliceItem> s) {
		super(context);
		// TODO Auto-generated constructor stub
		slicedArray = s;
		ArrayList<Double> inputArray = new ArrayList<Double>();
		// parse each bac val into array
		for (int i = 0; i < slicedArray.size(); i++)
			inputArray.add(slicedArray.get(i).getBAC());
		highestBacSlot = highestValue(inputArray);
		// parse drink speed val (per hour) into array
		int count = 0;
		double val = 0;
		double totalSec = 0;
		for (int i = 0; i < slicedArray.size(); i++) {
			if (count == 0) {
				val = (i + 1) / (double) slicedArray.get(i).getDuration();
				totalSec = slicedArray.get(i).getDuration();
			}
			// else number of drinks consumed divided by sum of all the time
			// spent drinking up until now
			else
				val = (i + 1)
						/ ((double) slicedArray.get(i).getDuration() + drinkingSpeedArray
								.get(i - count) * totalSec);
			drinkingSpeedArray.add(val);
			totalSec = totalSec + (double) slicedArray.get(i).getDuration();
			count++;
		}
		for (int i = 0; i < drinkingSpeedArray.size(); i++)
			drinkingSpeedArray.set(i, drinkingSpeedArray.get(i));
		highestDrinkingSpeedSlot = highestValueGeneral(drinkingSpeedArray);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		density = metrics.densityDpi;
	}

	private ArrayList<Integer> highestValue(ArrayList<Double> valArray) {
		// TODO Auto-generated method stub
		ArrayList<Double> dummyArray = new ArrayList<Double>();
		ArrayList<Integer> highestValSlot = new ArrayList<Integer>();
		// gets the max value of the ArrayList
		double highestVal = 0;
		for (int i = 0; i < slicedArray.size(); i++) {
			if (highestVal < slicedArray.get(i).getBAC()) {
				highestVal = slicedArray.get(i).getBAC();
			}
			dummyArray.add(slicedArray.get(i).getBAC());
		}
		for (int i = 0; i < dummyArray.size(); i++) {
			if (dummyArray.get(i) == highestVal)
				highestValSlot.add(i);
		}

		return highestValSlot;

	}

	private ArrayList<Integer> highestValueGeneral(ArrayList<Double> valArray) {
		// TODO Auto-generated method stub
		ArrayList<Double> dummyArray = new ArrayList<Double>();
		ArrayList<Integer> highestValSlot = new ArrayList<Integer>();
		// gets the max value of the ArrayList
		double highestVal = 0;
		for (int i = 0; i < valArray.size(); i++) {
			if (highestVal < valArray.get(i)) {
				highestVal = valArray.get(i);
			}
			dummyArray.add(valArray.get(i));
		}
		for (int i = 0; i < dummyArray.size(); i++) {
			if (dummyArray.get(i) == highestVal)
				highestValSlot.add(i);
		}

		return highestValSlot;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.setDensity(density);

		canvas.drawColor(Color.parseColor("#f7f3f7"));
		float canvasWidth = canvas.getWidth();
		float canvasHeight = canvas.getHeight();
		float zoom = 0;
		if (density > 240) {
			zoom = density / 240;
			canvas.scale(zoom, zoom, canvasWidth / 2, canvasHeight / 2);
		}
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		// transition the color depending on bac
		RectF oval = new RectF();
		float[] resultingRectCord = translateLTRBfromPoint(
				canvas.getWidth() / 2, canvas.getHeight() / 2, 300, 300);
		float left = resultingRectCord[0];
		float top = resultingRectCord[1];
		float right = resultingRectCord[2];
		float bottom = resultingRectCord[3];
		float ovalRadius = (left - right) / 2;
		oval.set(left, top, right, bottom);
		startingDegree = slicedArray.get(0).getStartDegree();
		paint.setColor(Color.parseColor("#dedfe7"));
		canvas.drawArc(oval, 0, 360, true, paint);

		for (int i = 0; i < slicedArray.size(); i++) {
			float sweepAngle = slicedArray.get(i).getDegree();
			paint = setColor(paint, slicedArray.get(i).getBAC());
			canvas.drawArc(oval, startingDegree, sweepAngle, true, paint);
			startingDegree = startingDegree + sweepAngle;
			// sets up the labels here:

		}
		paint.setColor(Color.parseColor("#ecedf5"));
		// draws white lines to seperate the times.
		paint.setStrokeWidth(0);
		for (int i = 0; i < 12; i++) {
			float[] startEndXY = trigMeasurement(i * 30, oval, ovalRadius);
			canvas.drawLine(startEndXY[0], startEndXY[1], startEndXY[2],
					startEndXY[3], paint);
		}
		// points out the highest val here
		// takes the midpoint of the
		paint.setColor(Color.BLACK);
		// oval.set(left, top, right, bottom);

		paint.setStrokeWidth(1);
		/*
		 * for (int i = 0; i < highestBacSlot.size(); i++) { TrendsSliceItem
		 * highestBAC = slicedArray.get(highestBacSlot.get(i)); float degree =
		 * highestBAC.getStartDegree() + (highestBAC.getDegree() / 2); float[]
		 * startEndXY = trigMeasurement(degree, oval, ovalRadius * 1.1f); float
		 * startX = (startEndXY[0] + startEndXY[2]) / 2; float startY =
		 * (startEndXY[1] + startEndXY[3]) / 2; canvas.drawLine(startX, startY,
		 * startEndXY[2], startEndXY[3], paint); // draws text
		 * paint.setTextSize(15); String highestText = "" + highestBAC.getBAC();
		 * Rect highestTextRect = new Rect(); paint.getTextBounds(highestText,
		 * 0, highestText.length(), highestTextRect); // draws text and make (y
		 * position) bottom of text align to ending // pt of the pointer float
		 * bottomOfText = (float) startEndXY[3] - highestTextRect.height() / 2;
		 * float rightOfText = (float) startEndXY[2] + highestTextRect.width();
		 * 
		 * // draws line underlining the text canvas.drawLine((float)
		 * startEndXY[2], (float) startEndXY[3], rightOfText, (float)
		 * startEndXY[3], paint); paint = setColor(paint, highestBAC.getBAC());
		 * canvas.drawText(highestText, (float) startEndXY[2], bottomOfText,
		 * paint); }
		 */
		// draws the highest drinking speed
		for (int i = 0; i < highestDrinkingSpeedSlot.size(); i++) {
			TrendsSliceItem highestBAC = slicedArray
					.get(highestDrinkingSpeedSlot.get(i));
			float degree = highestBAC.getStartDegree()
					+ (highestBAC.getDegree() / 2);
			float[] startEndXY = trigMeasurement(degree, oval,
					ovalRadius * 1.1f);
			// shortens the path by 1/4
			float startX = (startEndXY[0] + startEndXY[2]) / 2;
			startX = (startX + startEndXY[2]) / 2;
			float startY = (startEndXY[1] + startEndXY[3]) / 2;
			startY = (startY + startEndXY[3]) / 2;
			canvas.drawLine(startX, startY, startEndXY[2], startEndXY[3], paint);
			// draws text
			paint.setTextSize(15);
			Boolean alignLeft = textAndUnderlineOrientation(degree);
			if (alignLeft)
				paint.setTextAlign(Align.LEFT);
			if (!alignLeft)
				paint.setTextAlign(Align.RIGHT);
			// currently drinks per sec so it needs to be converted to
			// drinks/hours
			String highestText = ""
					+ drinkingSpeedArray.get((highestDrinkingSpeedSlot.get(i)))
					* 3600;
			// cuts off the decimal places and trailing numbers
			int placementOfDecimal = 0;
			placementOfDecimal = highestText.indexOf(".");
			highestText = highestText.substring(0, placementOfDecimal)
					+ " drinks/hr";
			Rect highestTextRect = new Rect();
			paint.getTextBounds(highestText, 0, highestText.length(),
					highestTextRect);
			// draws text and make (y position) bottom of text align to ending
			// pt of the pointer
			float bottomOfText = (float) startEndXY[3]
					- highestTextRect.height() / 2;
			float rightOfText=0;
			if (alignLeft)
				rightOfText = (float) startEndXY[2] + highestTextRect.width();
			if (!alignLeft)
				rightOfText = (float) startEndXY[2] - highestTextRect.width();
			// draws line underlining the text
			canvas.drawLine((float) startEndXY[2], (float) startEndXY[3],
					rightOfText, (float) startEndXY[3], paint);
			paint = setColor(paint, highestBAC.getBAC());
			canvas.drawText(highestText, (float) startEndXY[2], bottomOfText,
					paint);
		}
		// draws the starting time for drinking
		paint.setColor(Color.BLACK);
		float startDegree = slicedArray.get(0).getStartDegree();
		Boolean AlignLeft=textAndUnderlineOrientation(startDegree);
		String startTime = slicedArray.get(0).getTimeStartInString();
		Rect timeStringRect = new Rect();
		paint.getTextBounds(startTime, 0, startTime.length(), timeStringRect);
		float[] startEndXY = trigMeasurement(startDegree, oval,
				ovalRadius * 1.1f);
		if(AlignLeft)
			paint.setTextAlign(Align.LEFT);
		if(!AlignLeft)
			paint.setTextAlign(Align.RIGHT);
		float timeFinalPositionX = startEndXY[2];
		float timeFinalPositionY = startEndXY[3];
		canvas.drawText(startTime, timeFinalPositionX, timeFinalPositionY,
				paint);

		forceLayout();
	}

	private Paint setColor(Paint paint, double bac) {
		// TODO Auto-generated method stub
		if (bac < 0.05)
			paint.setColor(Color.parseColor("#6bbe42"));
		if (bac < 0.08 && bac >= 0.05)
			paint.setColor(Color.parseColor("#f7d321"));
		if (bac < 0.1 && bac >= 0.08)
			paint.setColor(Color.parseColor("#f78e18"));
		if (bac < 0.35 && bac >= 0.1)
			paint.setColor(Color.parseColor("#ef5d52"));
		if (bac >= 0.35)
			paint.setColor(Color.parseColor("#707070"));
		return paint;
	}

	private float[] trigMeasurement(float degree, RectF oval, float rad) {

		if (degree < 0)
			degree = 360 + degree;
		float circleCenterX = (oval.right - oval.left) / 2 + oval.left;
		float circleCenterY = (oval.bottom - oval.top) / 2 + oval.top;
		float textDegree = degree;
		float radius = rad;
		double z = 0;
		double y = 0;
		double x = 0;
		float endingX = 0;
		float endingY = 0;
		// gets text's width
		Rect textBounds = new Rect();
		Paint text = new Paint();
		float[] startXstartYendXendY = new float[4];
		if (textDegree == 0) {
			x = radius;
			endingX = circleCenterX + radius;
			endingY = circleCenterY;
		}
		if (textDegree > 0 && textDegree < 90) {
			x = radius * Math.cos(Math.toRadians(textDegree));
			y = radius * Math.sin(Math.toRadians(textDegree));
			endingX = circleCenterX + (float) Math.abs(x);
			endingY = circleCenterY + (float) Math.abs(y);
		}
		if (textDegree == 90) {
			y = radius;
			endingX = circleCenterX;
			endingY = circleCenterY + radius;
		}
		if (textDegree > 90 && textDegree < 180) {
			x = radius * Math.cos(Math.toRadians(textDegree));
			y = radius * Math.sin(Math.toRadians(textDegree));
			endingX = circleCenterX - ((float) Math.abs(x));
			endingY = circleCenterY + (float) Math.abs(y);
		}
		if (textDegree == 180) {
			x = radius;
			endingX = circleCenterX - radius;
			endingY = circleCenterY;
		}
		if (textDegree > 180 && textDegree < 270) {
			x = radius * Math.cos(Math.toRadians(textDegree));
			y = radius * Math.sin(Math.toRadians(textDegree));
			endingX = circleCenterX - ((float) Math.abs(x));
			endingY = circleCenterY - ((float) Math.abs(y));
		}
		if (textDegree == 270) {
			y = radius;
			endingX = circleCenterX;
			endingY = circleCenterY - radius;
		}
		if (textDegree > 270 && textDegree < 360) {
			x = radius * Math.cos(Math.toRadians(textDegree));
			y = radius * Math.sin(Math.toRadians(textDegree));
			endingX = circleCenterX + (float) Math.abs(x);
			endingY = circleCenterY - ((float) Math.abs(y));
		}
		startXstartYendXendY[0] = circleCenterX;
		startXstartYendXendY[1] = circleCenterY;
		startXstartYendXendY[2] = endingX;
		startXstartYendXendY[3] = endingY;
		return startXstartYendXendY;
	}

	private float[] translateLTRBfromPoint(float x, float y, float width,
			float height) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float[] result = new float[4];
		// left
		result[0] = x - halfWidth;
		// top
		result[1] = y - halfHeight;
		// right
		result[2] = x + halfWidth;
		// bottom
		result[3] = y + halfHeight;
		return result;
	}

	//Alignment of the text depending on degree
	private Boolean textAndUnderlineOrientation(float degree) {
		if (degree <= 90)
			return true;
		if (degree <= 270 && degree > 90)
			return false;
		else
			return true;
	}
}
