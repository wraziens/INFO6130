package com.example.drinkingapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class SocialGraphics extends View{

	
	ArrayList<String> groupName;
	ArrayList<Integer> hangoutCount;
	ArrayList<Double> bacLevel;
	int totalhangoutCount=0;
	float startingDegree;
	float previousDegree=0;
	//constructor takes in social group name, its count, and bac level associated with that group.
	public SocialGraphics(Context context,ArrayList<String> g, ArrayList<Integer> h, ArrayList<Double> b) {
		super(context);
		// TODO Auto-generated constructor stub
		groupName=g;
		hangoutCount=h;
		bacLevel=b;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		startingDegree=0;
		for (int n=0;n<hangoutCount.size();n++){
			totalhangoutCount=totalhangoutCount+hangoutCount.get(n);
		}
		for (int n=0;n<hangoutCount.size();n++){
			float hangoutCountFloat=hangoutCount.get(n);
			float pieDegree=360*(hangoutCountFloat/totalhangoutCount);
			previousDegree=previousDegree+pieDegree;
			RectF rect=new RectF();
			rect.set(100, 50, 400, 350);
			Paint paint=new Paint();
				paint.setColor(Color.argb(255, 255-30*n, 255-30*n, 255));
			canvas.drawArc(rect, startingDegree, pieDegree, true, paint);
			float circleCenterX=250;
			float circleCenterY=200;
			float textDegree=previousDegree-(pieDegree)/2;
			float radius=150;
			double z=0;
			double y=0;
			double x=0;
			float endingX=0;
			float endingY=0;
			if (textDegree==0){
				x=radius;
				endingX=circleCenterX+(float)x;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree>0&&textDegree<90){
				z=180-textDegree-90;
				z=Math.PI*(z/180);
				x=radius*Math.cos(z);
				y=radius*Math.sin(z);
				endingX=circleCenterX+(float)x;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree==90){
				y=radius;
				endingX=circleCenterX+(float)x;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree>90&&textDegree<180){
				z=180-(textDegree-90)-90;
				z=Math.PI*(z/180);
				x=radius*Math.cos(z);
				y=radius*Math.sin(z);
				endingX=circleCenterX-(float)x;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree==180){
				x=radius;
				endingX=circleCenterX-(float)x;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree>180&&textDegree<270){
				z=180-(textDegree-180)-90;
				z=Math.PI*(z/180);
				y=radius*Math.cos(z);
				x=radius*Math.sin(z);
				endingX=circleCenterX-(float)x;
				endingY=circleCenterY-(float)y;
			}
			if (textDegree==270){
				y=radius;
				endingX=circleCenterX+(float)x;
				endingY=circleCenterY-(float)y;
			}
			if (textDegree>270&&textDegree<360){
				z=180-(textDegree-270)-90;
				z=Math.PI*(z/180);
				y=radius*Math.cos(z);
				x=radius*Math.sin(z);
				endingX=circleCenterX+(float)x;
				endingY=circleCenterY-(float)y;
			}
			
			startingDegree=pieDegree+startingDegree;
			
			canvas.drawRect(50, canvas.getHeight()/2+n*40+100, 100, canvas.getHeight()/2+n*40+20+100, paint);
			paint.setColor(Color.BLACK);
			paint.setTextSize(12);

			canvas.drawText(groupName.get(n), endingX, endingY, paint);
			String txt=groupName.get(n)+" "+hangoutCount.get(n)+" Occasions "+"Average BAC: "+bacLevel.get(n);
			canvas.drawText(txt, 50, canvas.getHeight()/2+n*40+20+100-6, paint);
		}
		//sets up the labels here:
		
		
		requestLayout();
		
		
	}

}
