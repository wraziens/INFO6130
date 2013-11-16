package com.example.drinkingapp;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

public class ExerciseGraphics extends View {

	Bitmap chicken, badCircle;
	public int drinkCount = 0;
	public int chickenCount = 0;
	public int month = 0, day = 0;
	// initiated at an offset to see the horizontal and verticle axis
	public float posX = 0, posY = 0, zoomVal = 0.8f;
	public float plusX, plusY;
	public float minusX, minusY;
	public Bitmap plus = BitmapFactory.decodeResource(getResources(),
			R.drawable.plus);
	public Bitmap minus = BitmapFactory.decodeResource(getResources(),
			R.drawable.minus);
	// Bitmap BufferBitmap = Bitmap.createBitmap(1000, 1000,
	// Bitmap.Config.ARGB_8888);
	// Canvas BufferCanvas = new Canvas(BufferBitmap);
	ArrayList<Integer> daysDrinkList = new ArrayList<Integer>();
	ArrayList<Integer> daysExercisedList = new ArrayList<Integer>();
	ArrayList<Double> averageExerciseQualityList = new ArrayList<Double>();

	public ExerciseGraphics(Context context, ArrayList<Integer> d,
			ArrayList<Integer> e,
			ArrayList<Double> q) {
		super(context);
		// TODO Auto-generated constructor stub
		daysDrinkList=d;
		daysExercisedList=e;
		averageExerciseQualityList=q;
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.save();
		canvas.scale(zoomVal, zoomVal, canvas.getWidth() / 2,
				canvas.getHeight() / 2);
		int yAxisMax = canvas.getHeight();
		int xAxisMax = canvas.getWidth();
		Paint axis = new Paint();
		axis.setColor(Color.BLACK);
		axis.setStrokeWidth(10);

		Paint text = new Paint();
		text.setTextSize(15);
		// draws the x-axis
		canvas.drawLine(20 + posX, yAxisMax + posY,
				xAxisMax * daysDrinkList.size() + posX, yAxisMax + posY, axis);

		int yScale = 7;
		float yAxisIncrement = yAxisMax / yScale;
		// draws the increments on Y axis
		Paint thinLines = new Paint();
		thinLines.setStrokeWidth(5);

		// draw legends
		Paint legendPaint = new Paint();
		Rect legendsRect = new Rect();
		// exercise legend
		legendsRect.set(canvas.getWidth() - 125, 50, canvas.getWidth() - 75,
				100);
		canvas.drawRect(legendsRect, legendPaint);
		legendPaint.setColor(Color.BLACK);
		legendPaint.setTextSize(20);
		canvas.drawText("Days Exercised", legendsRect.right,
				legendsRect.centerY(), legendPaint);

		// drink legend
		legendPaint.setColor(Color.rgb(255, 207, 17));
		legendsRect.set(canvas.getWidth() - 300, 50, canvas.getWidth() - 250,
				100);
		canvas.drawRect(legendsRect, legendPaint);
		legendPaint.setColor(Color.BLACK);
		legendPaint.setTextSize(20);
		canvas.drawText("Days Drank", legendsRect.right, legendsRect.centerY(),
				legendPaint);

		text.setTextSize(20);
		text.setColor(Color.BLACK);
		for (int n = 0; n < yScale; n++) {
			canvas.drawLine(10 + posX, n * yAxisIncrement + posY, 50 + posX, n
					* yAxisIncrement + posY, thinLines);

			Rect bounds = new Rect();
			String scaleData = "" + (yScale - n);
			text.getTextBounds(scaleData, 0, scaleData.length(), bounds);

			canvas.drawText("" + (yScale - n), -10 + posX, n * yAxisIncrement
					+ posY + (bounds.width() / 2), text);
		}
		canvas.rotate(90);
		canvas.drawText("Days", canvas.getHeight() / 2 + posY, 50 - posX, text);
		canvas.rotate(-90);

		axis.setColor(Color.RED);
		JSONArray tempList = null;
		float xPosition = 100;
		float widthOfBar = 50;
		float spaceBetweenWeeks = 50;
			// tempList=%%%%.getJSONArray("week");
			for (int n = 0; n < daysDrinkList.size(); n++) {
				String weekNumber = ""+(n+1);
				int daysDrink = daysDrinkList.get(n);
				int daysExercise = daysExercisedList.get(n);
				double rawGrade = averageExerciseQualityList.get(n);
				String grade="";
				if (rawGrade>=90)
					grade="A";
				else if (rawGrade>=80&&rawGrade<90)
					grade="B";
				else if (rawGrade>=70&&rawGrade<80)
					grade="C";
				else 
					grade="D";

				// draws days drank
				Rect alcBarRect = new Rect();
				alcBarRect.set((int) (xPosition + posX),
						(int) ((yScale - daysDrink) * yAxisIncrement + posY),
						(int) (xPosition + widthOfBar + posX),
						(int) (yAxisMax + posY));
				Bitmap alcoholBar = BitmapFactory.decodeResource(
						getResources(), R.drawable.exercisealcoholbar);
				// alcoholBar=Bitmap.createBitmap(alcoholBar, 0, 0,
				// (int)widthOfBar, (int)((yScale-daysDrink)*yAxisIncrement));
				// Bitmap
				// alcoholBarScaled=alcoholBar.createScaledBitmap(alcoholBar,
				// (int)widthOfBar, (int)((yScale-daysDrink)*yAxisIncrement),
				// false);
				canvas.drawBitmap(alcoholBar, null, alcBarRect, axis);

				// axis.setColor(Color.RED);

				// canvas.drawRect(xPosition + posX,
				// (yScale-daysDrink)*yAxisIncrement + posY,
				// xPosition+widthOfBar + posX,yAxisMax + posY,axis);

				// draws days exercised
				axis.setColor(Color.BLACK);
				// gets the bounds of the text for centering
				Rect bounds = new Rect();
				String weekText = "Week " + weekNumber;
				text.getTextBounds(weekText, 0, weekText.length(), bounds);
				canvas.drawRect(xPosition + widthOfBar + posX,
						(yScale - daysExercise) * yAxisIncrement + posY,
						xPosition + (widthOfBar * 2) + posX, yAxisMax + posY,
						axis);
				// draws week
				canvas.drawText(weekText, xPosition + widthOfBar + posX
						- (bounds.width() / 2), yAxisMax + 50 + posY, axis);
				// draws grade

				Rect textBound = new Rect();
				text.setColor(Color.BLACK);
				text.setTextSize(10);
				String quality = "Quality:";
				text.getTextBounds(quality, 0, quality.length(), textBound);
				canvas.drawText(quality, xPosition + widthOfBar + posX,
						(yScale - daysExercise) * yAxisIncrement + posY - 10,
						text);
				text.setColor(Color.BLUE);
				text.setTextSize(15);
				canvas.drawText(grade, xPosition + widthOfBar + posX
						+ textBound.width(), (yScale - daysExercise)
						* yAxisIncrement + posY - 10, text);
				xPosition += 200;
			}

		plusX = canvas.getWidth() - plus.getWidth() - 50;
		plusY = canvas.getHeight() - plus.getHeight();
		minusX = canvas.getWidth() - minus.getWidth();
		minusY = canvas.getHeight() - minus.getHeight();

		canvas.restore();
		canvas.drawBitmap(plus, plusX, plusY, null);
		canvas.drawBitmap(minus, minusX, minusY, null);
		// canvas.drawBitmap(BufferBitmap, (float) -posX, (float) -posY, null);
		// canvas.drawColor(Color.parseColor("#7b9aad"));

		requestLayout();

	}

}
