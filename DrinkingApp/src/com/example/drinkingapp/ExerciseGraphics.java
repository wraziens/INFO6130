package com.example.drinkingapp;

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

public class ExerciseGraphics extends View{

	Bitmap chicken, badCircle;
	public int drinkCount = 0;
	public int chickenCount = 0;
	public int month = 0, day = 0;
	//initiated at an offset to see the horizontal and verticle axis
	public float posX=0, posY=0,zoomVal=0.8f;
	JSONObject weekData = new JSONObject();
	public float plusX,plusY;
	public float minusX,minusY;
	public Bitmap plus=BitmapFactory.decodeResource(getResources(), R.drawable.plus);
	public Bitmap minus=BitmapFactory.decodeResource(getResources(), R.drawable.minus);
	// Bitmap BufferBitmap = Bitmap.createBitmap(1000, 1000,
	// Bitmap.Config.ARGB_8888);
	// Canvas BufferCanvas = new Canvas(BufferBitmap);

	public ExerciseGraphics(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		

		//testing JSON Data
		
		try {
			JSONObject data = new JSONObject();
			data.put("number", "1");
			data.put("daysDrink", "2");
			data.put("daysExercise", "5");
			data.put("grade", "A+");
			weekData.put("1", data);

			JSONObject data2 = new JSONObject();
			data2.put("number", "2");
			data2.put("daysDrink", "3");
			data2.put("daysExercise", "4");
			data2.put("grade", "B+");
			weekData.put("2", data2);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		
		
		canvas.save();
		canvas.scale(zoomVal, zoomVal,canvas.getWidth()/2,canvas.getHeight()/2);
		int yAxisMax = canvas.getHeight();
		int xAxisMax = canvas.getWidth();
		Paint axis = new Paint();
		axis.setColor(Color.BLACK);
		axis.setStrokeWidth(10);

		Paint text = new Paint();
		text.setTextSize(15);
		//draws the x-axis
		canvas.drawLine(0+posX, yAxisMax + posY, xAxisMax*weekData.length()+posX, yAxisMax + posY, axis);

		int yScale = 7;
		float yAxisIncrement = yAxisMax / yScale;
		//draws the increments on Y axis
		Paint thinLines=new Paint();
		thinLines.setStrokeWidth(5);
		
		text.setTextSize(20);
		text.setColor(Color.BLACK);
		for (int n = 0; n < yScale; n++) {
			canvas.drawLine(10 + posX, n * yAxisIncrement + posY, 50 + posX, n
					* yAxisIncrement + posY, thinLines);
			
			Rect bounds=new Rect();
			String scaleData=""+(yScale - n);
			text.getTextBounds(scaleData, 0, scaleData.length(), bounds);
			
			canvas.drawText("" + (yScale - n), -10 + posX, n * yAxisIncrement
					+ posY+(bounds.width()/2), text);
		}
		canvas.rotate(90);
		canvas.drawText("Days", canvas.getHeight()/2, 50, text);
		canvas.rotate(-90);

		
		
		axis.setColor(Color.RED);
		JSONArray tempList = null;
		float xPosition=100;
		float widthOfBar=50;
		float spaceBetweenWeeks=50;
		try {
			//tempList=weekData.getJSONArray("week");
			for (int n = 0; n < weekData.length(); n++) {
				JSONObject tempjSon= weekData.getJSONObject(""+(n+1));
				String weekNumber=tempjSon.getString("number");
				int daysDrink=Integer.valueOf(tempjSon.getString("daysDrink"));
				int daysExercise=Integer.valueOf(tempjSon.getString("daysExercise"));
				String grade=tempjSon.getString("grade");
				//draws days drank
				axis.setColor(Color.RED);
				canvas.drawRect(xPosition + posX, (yScale-daysDrink)*yAxisIncrement + posY, xPosition+widthOfBar + posX, 
						yAxisMax + posY,axis);
				//draws days exercised
				axis.setColor(Color.BLACK);
				//gets the bounds of the text for centering
				Rect bounds=new Rect();
				String weekData="Week "+weekNumber;
				text.getTextBounds(weekData, 0, weekData.length(), bounds);
				canvas.drawRect(xPosition+widthOfBar + posX, (yScale-daysExercise)*yAxisIncrement + posY, xPosition+(widthOfBar*2) + posX, 
						yAxisMax + posY,axis);
				//draws week
				canvas.drawText(weekData, xPosition+widthOfBar+ posX-(bounds.width()/2), yAxisMax+50+posY, axis);
				//draws grade
				text.setColor(Color.BLUE);
				canvas.drawText(grade, xPosition+widthOfBar+widthOfBar/2 + posX, (yScale-daysExercise)*yAxisIncrement + posY-10, text);
				xPosition+=200;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		plusX=0;
		plusY=0;
		minusX=50;
		minusY=0;
		
		canvas.restore();
		canvas.drawBitmap(plus, plusX, plusY, null);
		canvas.drawBitmap(minus, minusX, minusY, null);
		// canvas.drawBitmap(BufferBitmap, (float) -posX, (float) -posY, null);
		// canvas.drawColor(Color.parseColor("#7b9aad"));

		requestLayout();

	}


}
