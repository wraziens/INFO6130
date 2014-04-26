package cornell.trickleapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import cornell.trickleapp.R;
import cornell.trickleapp.R.drawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.View;

public class Graphics extends View{

	Bitmap chicken,badCircle;
	public int drinkCount=0;
	public int chickenCount=0;
	public int month=0,day=0;
	public Graphics(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		chicken = BitmapFactory.decodeResource(getResources(), R.drawable.chicken);
		badCircle = BitmapFactory.decodeResource(getResources(), R.drawable.badcircle);
		//font = Typeface.createFromAsset(context.getAssets(), "WW.TTF");
	}


	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawColor(Color.parseColor("#7b9aad"));
		Paint textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		canvas.drawBitmap(badCircle, canvas.getWidth()/2-badCircle.getWidth()/2, canvas.getHeight()/2-badCircle.getHeight()/2-200, null);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(50);
		canvas.drawText(String.valueOf(drinkCount), canvas.getWidth()/2, canvas.getHeight()/2-10-200, textPaint);
		canvas.drawText("Drinks", canvas.getWidth()/2, canvas.getHeight()/2+textPaint.getTextSize()-10-200, textPaint);
		canvas.drawText(month+"/"+day, canvas.getWidth()/2, 90, textPaint);

		for (int n=0;n<chickenCount;n++){
			Random randomPosition=new Random();
			canvas.drawBitmap(chicken, randomPosition.nextInt(canvas.getWidth())-chicken.getWidth()/2, randomPosition.nextInt(canvas.getHeight()/2)-chicken.getHeight()+canvas.getHeight()/2, null);
		}
		canvas.drawText(String.valueOf(drinkCount*120), canvas.getWidth()/2, canvas.getHeight()-75, textPaint);
		canvas.drawText("CALORIES", canvas.getWidth()/2, canvas.getHeight()-25, textPaint);
		requestLayout();
		
		
	}

}
