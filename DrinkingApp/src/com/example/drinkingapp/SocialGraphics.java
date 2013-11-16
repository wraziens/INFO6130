package com.example.drinkingapp;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class SocialGraphics extends View{

	
	ArrayList<String> groupName;
	ArrayList<Integer> hangoutCount;
	ArrayList<Double> bacLevel;
	int totalhangoutCount=0;
	float startingDegree;
	float previousDegree=0;
	Bitmap bitmap;
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
		float legendBarMaxWidth=400;
		for (int n=0;n<hangoutCount.size();n++){
			totalhangoutCount=totalhangoutCount+hangoutCount.get(n);
		}
		for (int n=0;n<hangoutCount.size();n++){
			String currentGroupName=groupName.get(n);
			float hangoutCountFloat=hangoutCount.get(n);
			float pieDegree=360*(hangoutCountFloat/totalhangoutCount);
			previousDegree=previousDegree+pieDegree;
			RectF rect=new RectF();
			rect.set(100, 50, 400, 350);
			
			Paint paint=new Paint();
			//transition the color depending on bac
			
			double currentBac=bacLevel.get(n);
			int red=100;
			int green=220;
			int blue=50;
			Double colorShift=0.0;
			if (currentBac<=0.09){
				colorShift=((0.09-currentBac)/0.09)*155;
				paint.setColor(Color.rgb(red+colorShift.intValue(),green,blue));
			}
			else if (currentBac>0.09&&currentBac<=0.15){
				red=255;
				colorShift=((0.15-currentBac)/0.06)*150;
				paint.setColor(Color.rgb(red,green-colorShift.intValue(),blue));
			}
			else if (currentBac>0.15&&currentBac<0.2){
				red=255;
				green=50;
				colorShift=((0.2-currentBac)/0.05);
				Double doubleRed=red*colorShift;
				Double doubleGreen=green*colorShift;
				Double doubleBlue=blue*colorShift;
				
				paint.setColor(Color.rgb(red-doubleRed.intValue(),green-doubleGreen.intValue(),blue-doubleBlue.intValue()));
				//paint.setColor(Color.rgb(red,green,blue);
			}
			else
				paint.setColor(Color.BLACK);
			
			
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
			//gets text's width
			Rect textBounds=new Rect();
			Paint text=new Paint();
			text.getTextBounds(currentGroupName, 0, currentGroupName.length(), textBounds);
			
			if (textDegree==0){
				x=radius;
				endingX=circleCenterX+(float)x+5;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree>0&&textDegree<90){
				z=180-textDegree-90;
				z=Math.PI*(z/180);
				x=radius*Math.cos(z);
				y=radius*Math.sin(z);
				endingX=circleCenterX+(float)x+5;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree==90){
				y=radius;
				endingX=circleCenterX+(float)x-(textBounds.width()/2);
				endingY=circleCenterY+(float)y;
			}
			if (textDegree>90&&textDegree<180){
				z=180-(textDegree-90)-90;
				z=Math.PI*(z/180);
				x=radius*Math.cos(z);
				y=radius*Math.sin(z);
				endingX=circleCenterX-(float)x-textBounds.width()-10;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree==180){
				x=radius;
				endingX=circleCenterX-(float)x-textBounds.width()-10;
				endingY=circleCenterY+(float)y;
			}
			if (textDegree>180&&textDegree<270){
				z=180-(textDegree-180)-90;
				z=Math.PI*(z/180);
				y=radius*Math.cos(z);
				x=radius*Math.sin(z);
				endingX=circleCenterX-(float)x-textBounds.width()-10;
				endingY=circleCenterY-(float)y;
			}
			if (textDegree==270){
				y=radius;
				endingX=circleCenterX+(float)x-(textBounds.width()/2)-10;
				endingY=circleCenterY-(float)y;
			}
			if (textDegree>270&&textDegree<360){
				z=180-(textDegree-270)-90;
				z=Math.PI*(z/180);
				y=radius*Math.cos(z);
				x=radius*Math.sin(z);
				endingX=circleCenterX+(float)x+5;
				endingY=circleCenterY-(float)y;
			}
			
			startingDegree=pieDegree+startingDegree;
			
			//draws danger lines
			bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.dangericon);
			canvas.drawBitmap(bitmap, 0.75f*legendBarMaxWidth, canvas.getHeight()/2+100-bitmap.getHeight(), paint);
			/*
			Paint dangerLine=new Paint();
			dangerLine.setStrokeWidth(5);
			dangerLine.setColor(Color.rgb(150, 150, 150));
			canvas.drawLine(0.75f*legendBarMaxWidth+bitmap.getWidth()/2, canvas.getHeight()/2+100-bitmap.getHeight(), 0.75f*legendBarMaxWidth+bitmap.getWidth()/2, canvas.getHeight(), dangerLine);
			*/
			
			String txt="Average BAC: "+bacLevel.get(n);
			text.getTextBounds(txt, 0, txt.length(), textBounds);
			//draws legend's rect
			Double barPercentage=bacLevel.get(n)/0.2;
			if (barPercentage>1)
				barPercentage=1.0;
			
			float barWidth=50+(legendBarMaxWidth*barPercentage.floatValue());
			canvas.drawRect(50, canvas.getHeight()/2+n*40+100, barWidth, canvas.getHeight()/2+n*40+20+100, paint);
			paint.setColor(Color.BLACK);
			canvas.drawText(currentGroupName, barWidth, canvas.getHeight()/2+n*40+20+100-6, paint);
			canvas.drawText(currentGroupName, endingX, endingY, paint);
			Double barLightnessLevel=barPercentage*0.8*255;
			paint.setColor(Color.argb(255, barLightnessLevel.intValue(), barLightnessLevel.intValue(), barLightnessLevel.intValue()));
			canvas.drawText(txt, 50, canvas.getHeight()/2+n*40+20+100-6, paint);
			
			requestLayout();
		}
		//sets up the labels here:
		
		

		
		
	}

}
