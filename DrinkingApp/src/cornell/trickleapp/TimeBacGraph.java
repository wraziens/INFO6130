package cornell.trickleapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

public class TimeBacGraph extends View {

	private ArrayList<TrendsSliceItem> slicedArray;
	private float startingDegree;
	private Bitmap bitmap;
	private ArrayList<Integer> highestBacSlot = new ArrayList<Integer>();
	private ArrayList<Integer> highestDrinkingSpeedSlot = new ArrayList<Integer>();
	private ArrayList<Double> drinkingSpeedArray = new ArrayList<Double>(); 
	private int density;
	private BacTime bacTime;
	private Context context;

	private ArrayList<DatabaseStore> drinkCounts;
	private ArrayList<TrendsSliceItem> morningCounts;
	private ArrayList<TrendsSliceItem> eveningCounts;
	private ArrayList<TrendsSliceItem> drawCounts;
 	private ArrayList<PieSlice> slices;
	private ArrayList<PieSlice> after;
	private ArrayList<PieSlice> morningSlices;
	private ArrayList<PieSlice> eveningSlices;
	String[] eveningLabels;
	String[] morningLabels;
	String[] afterLabels;
	
	
	private ArrayList<PieSlice> drawSlices;
	private float drawStart = -1;
	private String[] drawLabels;
	
	private float startDegreeEvening = -1;
	private float startDegreeMorning = -1;
	private float start = -1;
	private Canvas canvasPie;
	private RectF oval;
	private Paint paint;
	
	public TimeBacGraph(Context context, ArrayList<TrendsSliceItem> eveningCounts, ArrayList<TrendsSliceItem> morningCounts) {
		super(context);
		
		drawCounts = new ArrayList<TrendsSliceItem>();
		
		this.context = context;
		this.morningCounts = morningCounts;
		this.eveningCounts = eveningCounts;
		this.slices = new ArrayList<PieSlice>();
		this.morningSlices = new ArrayList<PieSlice>();
		this.after = new ArrayList<PieSlice>();
		this.drawSlices = new ArrayList<PieSlice>();
		
		if(eveningCounts.size() > 0){
			start = this.eveningCounts.get(0).getStartDegree();
			startDegreeEvening = this.eveningCounts.get(0).getStartDegree();
		}
		if(morningCounts.size() > 0){
			startDegreeMorning = this.morningCounts.get(0).getStartDegree();
		}
		
		bacTime = new BacTime(context);
		paint = new Paint();
		paint.setAntiAlias(true);
		oval = new RectF();
		this.startingDegree = this.startDegreeMorning;
		for(int j =0 ;j < morningCounts.size();j++){
			TrendsSliceItem current = morningCounts.get(j);
			processColors(current, paint);	
		}
		
		morningSlices = new ArrayList<PieSlice>(slices);
		slices.clear();
		//slices = new ArrayList<PieSlice>(after);
		after.clear();
		
		this.startingDegree = this.startDegreeEvening;
		for(int i = 0; i < eveningCounts.size(); i++){
			TrendsSliceItem current = eveningCounts.get(i);
			processColors(current, paint);	
		}
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		density = metrics.densityDpi;
		
		eveningLabels = new String[]{ "9PM","12AM","3AM" ,"6PM"};
		morningLabels = new String[]{ "9AM", "12PM", "3PM","6AM"};
		afterLabels = new String[]{ "9AM", "12PM", "3PM","6AM"};
		
		drawSlices = slices;
		drawStart = this.startDegreeEvening;
		drawLabels = eveningLabels;
		
		if (eveningCounts != null){
			drawCounts = eveningCounts;
		} else{
			drawCounts.clear();
		}
	}
	
	public void setEvening(){
		drawSlices = slices;
		drawStart = this.startDegreeEvening;
		if (eveningCounts != null){
			drawCounts = eveningCounts;
		} else{
			drawCounts.clear();
		}
		drawLabels = eveningLabels;
		this.invalidate();
		
	}
	public void setMorning(){
		drawSlices = morningSlices;
		drawStart = this.startDegreeMorning;
		if (morningCounts != null){
			drawCounts = morningCounts;
		} else{
			drawCounts.clear();
		}
		drawLabels = morningLabels;
		this.invalidate();
		
	}
	public void setAfter(){
		drawSlices = after;
		drawStart = this.startDegreeEvening;
		if (eveningCounts != null){
			drawCounts = eveningCounts;
		} else{
			drawCounts.clear();
		}
		drawLabels = afterLabels;
		this.invalidate();
		
	}
	
		/*
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
*/
	private ArrayList<Integer> highestValue(ArrayList<Double> valArray) {
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

	private void processColors(TrendsSliceItem item, Paint paint){
		int colorTime = -1;
		double bac = item.getBAC();
		//Bac is currently 'black' 
		if(bac >= 0.24){
			colorTime = bacTime.getRedDate(item.getDrinkCount(), item.getTimeStart());
			if( colorTime != -1){
				if(item.getTimeNextDrink() > colorTime || item.getTimeNextDrink() == -1) {
					float degree = item.getDegreeVal(colorTime);
					PieSlice p = new PieSlice(degree, this.startingDegree, 0.25, paint);
					if(startingDegree >  450 && startingDegree + degree < 810){
						after.add(p);
					}else if(this.startingDegree + degree < 810){
						if (this.startingDegree + degree > 450){
							float new_degree = 450 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.25, paint);
							 PieSlice p2 = new PieSlice(degree - new_degree, this.startingDegree + new_degree, 0.25, paint);
							 after.add(p2);
						}
						slices.add(p);
					}else{
						if (this.startingDegree + degree > 810){
							float new_degree = 810 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.25, paint);
							 after.add(p);
						}
					}
					this.startingDegree = this.startingDegree + degree;
					item.setTimeStart(colorTime);
				}
			}
		}	
		//Bac is currently red or higher
		if(bac >= 0.15){
			colorTime = bacTime.getYellowDate(item.getDrinkCount(), item.getTimeStart());
			if( colorTime != -1){
				if(item.getTimeNextDrink() > colorTime || item.getTimeNextDrink() == -1) {
					float degree = getDegree(item.getTimeStart(), colorTime);
					PieSlice p = new PieSlice(degree, this.startingDegree, 0.16, paint);
					if(startingDegree > 450 && startingDegree + degree < 810){
						after.add(p);
					}else if(this.startingDegree + degree < 810){
						if (this.startingDegree + degree > 450){
							float new_degree = 450 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.16, paint);
							 PieSlice p2 = new PieSlice(degree - new_degree, this.startingDegree + new_degree, 0.16, paint);
							 after.add(p2);
						}
						slices.add(p);
					}else{
						if (this.startingDegree + degree > 810){
							float new_degree = 810 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.16, paint);
							 after.add(p);
						}
					}
				
					this.startingDegree = this.startingDegree + degree;
					item.setTimeStart(colorTime);
				}
			}
		}
		
		//Bac is currently yellow or higher
		if(bac >= 0.06){
			colorTime = bacTime.getGreenDate(item.getDrinkCount(), item.getTimeStart());
			if( colorTime != -1){
				if(item.getTimeNextDrink() > colorTime || item.getTimeNextDrink() == -1) {
					float degree = item.getDegreeVal(colorTime);
					PieSlice p = new PieSlice(degree, this.startingDegree, 0.07, paint);
					if(startingDegree > 450 && startingDegree + degree < 810){
						after.add(p);
					}else if(this.startingDegree + degree < 810){
						if (this.startingDegree + degree > 450){
							float new_degree = 450 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.07, paint);
							 PieSlice p2 = new PieSlice(degree - new_degree, this.startingDegree + new_degree, 0.07, paint);
							 after.add(p2);
						}
		
						slices.add(p);
					}else{
						if (this.startingDegree + degree > 810){
							float new_degree = 810 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.07, paint);
							 after.add(p);
						}
					}
					this.startingDegree = this.startingDegree + degree;
					item.setTimeStart(colorTime);
					
				} 
			}
		}
		//Bac is currently green or higher
		if(bac > 0){

			colorTime = bacTime.getClearDate(item.getDrinkCount(), item.getTimeStart());
			if( colorTime != -1){
		
				if(item.getTimeNextDrink() > colorTime || item.getTimeNextDrink() == -1) {
					float degree = item.getDegreeVal(colorTime);
					PieSlice p = new PieSlice(degree, this.startingDegree, 0.01, paint);
					if(startingDegree > 450 && startingDegree + degree < 810){
						after.add(p);
					}else if(this.startingDegree + degree < 810){
						
						if (this.startingDegree + degree > 450){
							float new_degree = 450 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.01, paint);
							 PieSlice p2 = new PieSlice(degree - new_degree, this.startingDegree + new_degree, 0.01, paint);
							 after.add(p2);
						}
						slices.add(p);
					}else{
						if (this.startingDegree + degree > 810){
							float new_degree = 810 - this.startingDegree;
							 p = new PieSlice(new_degree, this.startingDegree, 0.01, paint);
							 after.add(p);
						}
					}
					this.startingDegree = this.startingDegree + degree;
					item.setTimeStart(colorTime);
					
					if(item.getTimeNextDrink() > -1){
						degree = item.getDegree();
						p = new PieSlice(degree, this.startingDegree, 0.00, paint);
						if(startingDegree > 450 && startingDegree + degree  < 810){
							after.add(p);
						}else{
							if (this.startingDegree + degree > 450){
								float new_degree = 450 - this.startingDegree;
								 p = new PieSlice(new_degree, this.startingDegree, 0.00, paint);
								 PieSlice p2 = new PieSlice(degree - new_degree, this.startingDegree + new_degree, 0.00, paint);
								 after.add(p2);
							}
							slices.add(p);
						}
						/*else{
							if (this.startingDegree + degree > 810){
								float new_degree = 810 - this.startingDegree;
								 p = new PieSlice(new_degree, this.startingDegree, 0.00, paint);
								 after.add(p);
							}
						}*/
						this.startingDegree = this.startingDegree + degree;
						item.setTimeStart(colorTime);
					}
				}
			}
		}
		if(item.getTimeNextDrink() != -1){
			float degree = item.getDegree();
			PieSlice p = new PieSlice(degree, this.startingDegree, item.getBAC(), paint);
			if(startingDegree >  450){
				after.add(p);
			}else{
				if (this.startingDegree + degree > 450){
					float new_degree = 450 - this.startingDegree;
					 p = new PieSlice(new_degree, this.startingDegree,item.getBAC(), paint);
					 PieSlice p2 = new PieSlice(degree - new_degree, this.startingDegree + new_degree, item.getBAC(), paint);
					 after.add(p2);
				}
				slices.add(p);
			}
			this.startingDegree = this.startingDegree + degree;
		}
	}
	
	private float getDegree(int start,int end) {
		if (start>43200){
			start -= 43200;
		}
		if(end > 43200){
			end -= 43200;
		}
		float timeElapsed;
		if (end > start){
			timeElapsed = (end - start);
		}else{
			timeElapsed = (end + 43200 - start);
		}
		
		return (timeElapsed / 43200f) * 360f;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvasPie = canvas;
		canvasPie.setDensity(density);

		//canvasPie.drawColor(Color.parseColor("#f7f3f7"));
		canvasPie.drawColor(Color.parseColor("#DCDEE0"));
		float canvasWidth = canvasPie.getWidth();
		float canvasHeight = canvasPie.getHeight();
		float zoom = 0;
		if (density > 240) {
			zoom = density / 240;
			canvasPie.scale(zoom, zoom, canvasWidth / 2, canvasHeight / 2);
		}
		
		paint.setAntiAlias(true);
		
		float[] resultingRectCord = translateLTRBfromPoint(
				canvasPie.getWidth() / 2, canvasPie.getHeight() / 2, 300, 300);
		float left = resultingRectCord[0];
		float top = resultingRectCord[1];
		
		float right = resultingRectCord[2];
		float bottom = resultingRectCord[3];
		if(density<=240){
			float diff = top - 70;
			top = 70;
			bottom = bottom -diff;
		}
		float ovalRadius = (left - right) / 2 ;
		oval.set(left, top, right, bottom);
		paint.setColor(Color.parseColor("#f0f0f0"));
		canvasPie.drawArc(oval, 0, 360, true, paint);

		int countSize = eveningCounts.size();
		
		this.startingDegree = this.drawStart;
		
		for(int j=0; j<drawSlices.size(); j++){
			PieSlice s = drawSlices.get(j);
			s.setPaint();
			canvasPie.drawArc(oval, s.startingDegree, s.sweepingDegree, true, s.paint);
		}
		paint.setColor(Color.parseColor("#BBBDBE"));

		// draws white lines to seperate the times.
		paint.setStrokeWidth(1);
		for (int i = 0; i < 12; i++) {
			float[] startEndXY = trigMeasurement(i * 30, oval, ovalRadius);
			if(i==9){
				paint.setColor(Color.parseColor("#222222"));
				paint.setStrokeWidth(2);
			}
			
			canvasPie.drawLine(startEndXY[0], startEndXY[1], startEndXY[2],
					startEndXY[3], paint);
			paint.setColor(Color.parseColor("#BBBDBE"));
			paint.setStrokeWidth(1);
		}
		
		int circle=0;
		//Add labels to the Image
		Rect labelRect = new Rect();
		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.parseColor("#85888D"));
		for(int i=0; i<4; i++){
			paint.getTextBounds(drawLabels[i], 0, drawLabels[i].length(), labelRect);	
			int paintSize=18;
			paint.setTextSize(paintSize);
			//paint.setTextAlign(Align.RIGHT);
			float[] startEndXY = trigMeasurement(circle, oval,
					ovalRadius * 1.15f);
			float timeFinalPositionX = startEndXY[2];
			float timeFinalPositionY = startEndXY[3];
			if (i==1)
			canvasPie.drawText(drawLabels[i], timeFinalPositionX, timeFinalPositionY+(paintSize/2),
					paint);
			else
				canvasPie.drawText(drawLabels[i], timeFinalPositionX, timeFinalPositionY,
						paint);
			circle+=90;
		}
		
		
		
		
		
		
		// points out the highest val here
		// takes the midpoint of the
		paint.setColor(Color.BLACK);
		oval.set(left, top, right, bottom);

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
		/*
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
		}*/
		// draws the starting time for drinking
		/*
		paint.setColor(Color.BLACK);
		Boolean AlignLeft=textAndUnderlineOrientation(startDegreeEvening);
		if(drawCounts.size() >0){
			String startTime = drawCounts.get(0).getTimeStartInString();
			Rect timeStringRect = new Rect();
			paint.getTextBounds(startTime, 0, startTime.length(), timeStringRect);
			float[] startEndXY = trigMeasurement(startDegreeEvening, oval,
					ovalRadius * 1.1f);
			if(AlignLeft){
				paint.setTextAlign(Align.LEFT);
			}else{
				paint.setTextAlign(Align.RIGHT);
			}
			float timeFinalPositionX = startEndXY[2];
			float timeFinalPositionY = startEndXY[3];
			canvasPie.drawText(startTime, timeFinalPositionX, timeFinalPositionY,
					paint);
		}*/
		forceLayout();
		
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
		float halfHeight = (float)height / 2;
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
