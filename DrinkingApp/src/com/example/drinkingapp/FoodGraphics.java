package com.example.drinkingapp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class FoodGraphics extends View {

	Context mContext;
	ArrayList<Double> bacForSkippedHungover = new ArrayList<Double>();
	ArrayList<Double> bacForSkippedSick = new ArrayList<Double>();
	ArrayList<Double> bacForSkippedEither = new ArrayList<Double>();
	ArrayList<Double> bacForNoSkipHungover = new ArrayList<Double>();
	ArrayList<Double> bacForNoSkipSick = new ArrayList<Double>();
	ArrayList<Double> bacForNoSkipSickEither = new ArrayList<Double>();

	int daysWithSkippedMeals = 0;
	int daysDidntSkipMeals = 0;
	int daysSkipMealHungOver = 0;
	int daysSkipMealSick = 0;
	int daysNoSkipHungOver = 0;
	int daysNoSkipMealSick = 0;
	double avgBacSkipMeal = 0;
	double avgBacNoSkipMeal = 0;
	double daysSkipMealHungOverPercent = 0;
	double daysSkipMealSickPercent = 0;
	double daysNoSkipHungOverPercent = 0;
	double daysNoSkipMealSickPercent = 0;

	public FoodGraphics(Context context, int s, int n, double as, double ans,
			int daysSH, int daysSS, int daysNH, int daysNS) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		daysWithSkippedMeals = s;
		daysDidntSkipMeals = n;
		avgBacSkipMeal = as;
		avgBacNoSkipMeal = ans;
		daysSkipMealHungOverPercent = (double) daysSH / daysWithSkippedMeals
				* 100;
		daysSkipMealSickPercent = (double) daysSS / daysWithSkippedMeals * 100;
		daysNoSkipHungOverPercent = (double) daysNH / daysDidntSkipMeals * 100;
		daysNoSkipMealSickPercent = (double) daysNS / daysDidntSkipMeals * 100;
		// reorganize the data into viable ouputs
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint topHalfPaint = new Paint();
		RectF topHalfRect = new RectF();
		topHalfRect.set(20, 20, canvas.getWidth() - 20, canvas.getHeight() / 2);

		// top 1/2 rounded Rect
		// outside
		canvas.drawRoundRect(topHalfRect, 50f, 10f, topHalfPaint);
		// inside
		topHalfPaint.setColor(Color.WHITE);
		topHalfRect.set(30, 30, canvas.getWidth() - 30,
				canvas.getHeight() / 2 - 10);
		canvas.drawRoundRect(topHalfRect, 50f, 10f, topHalfPaint);

		Paint bottomHalfPaint = new Paint();
		RectF bottomHalfRect = new RectF();

		// bottom 1/2 rounded Rect
		// outside
		bottomHalfRect.set(20, 20 + canvas.getHeight() / 2,
				canvas.getWidth() - 20, canvas.getHeight() - 5);
		canvas.drawRoundRect(bottomHalfRect, 50f, 10f, bottomHalfPaint);
		// inside
		bottomHalfPaint.setColor(Color.WHITE);
		bottomHalfRect.set(30, canvas.getHeight() / 2 + 30,
				canvas.getWidth() - 30, canvas.getHeight() - 15);
		canvas.drawRoundRect(bottomHalfRect, 50f, 10f, bottomHalfPaint);
		// end rounded Rect

		Paint topHalfTextPaint = new Paint();
		topHalfTextPaint.setTextSize(20);
		Rect textBounds = new Rect();
		String skipMealTxt = "No. of Days that I Skipped Meals+Drank: ";
		topHalfTextPaint.getTextBounds(skipMealTxt, 0, skipMealTxt.length(),
				textBounds);
		canvas.drawText("No. Days that I Skipped Meals: ", 35,
				35 + textBounds.height(), topHalfTextPaint);
		topHalfTextPaint.setTextSize(30);
		canvas.drawText(Integer.toString(daysWithSkippedMeals),
				canvas.getWidth()-100, 35 + textBounds.height(),
				topHalfTextPaint);

		topHalfTextPaint.setTextSize(20);
		topHalfPaint.setColor(Color.BLACK);
		// left circle--------------

		float leftSideCircleOriginX = canvas.getWidth() / 4 + 25;
		// outside
		canvas.drawCircle(leftSideCircleOriginX, canvas.getHeight() / 4 + 25,
				canvas.getWidth() / 8, topHalfPaint);
		topHalfPaint.setColor(Color.WHITE);
		// inside
		canvas.drawCircle(leftSideCircleOriginX, canvas.getHeight() / 4 + 25,
				canvas.getWidth() / 8 - 5, topHalfPaint);
		topHalfTextPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("Hungover", leftSideCircleOriginX,
				canvas.getHeight() / 4 + 25 - 15, topHalfTextPaint);
		topHalfTextPaint.setTextSize(30);
		canvas.drawText(Double.toString(daysSkipMealHungOverPercent) + "%",
				leftSideCircleOriginX, canvas.getHeight() / 4 + 25 + 15,
				topHalfTextPaint);
		// right circle----------------

		// outside
		topHalfPaint.setColor(Color.BLACK);
		canvas.drawCircle(leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 4 + 25, canvas.getWidth() / 8,
				topHalfPaint);
		topHalfPaint.setColor(Color.WHITE);
		// inside
		canvas.drawCircle(leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 4 + 25, canvas.getWidth() / 8 - 5,
				topHalfPaint);
		topHalfTextPaint.setTextAlign(Paint.Align.CENTER);
		topHalfTextPaint.setTextSize(20);
		canvas.drawText("Upset", leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 4 + 25 - 30, topHalfTextPaint);
		canvas.drawText("Stomach", leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 4 + 25 - 15, topHalfTextPaint);
		topHalfTextPaint.setTextSize(30);
		canvas.drawText(Double.toString(daysSkipMealSickPercent) + "%",
				leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 4 + 25 + 15, topHalfTextPaint);
		// -----------Bottom 1/2----------------------------
		Paint bottomHalfTextPaint = new Paint();
		bottomHalfTextPaint.setTextSize(20);
		skipMealTxt = "No. of Days that I Did not Skip Meals+Drank: ";
		bottomHalfTextPaint.getTextBounds(skipMealTxt, 0, skipMealTxt.length(),
				textBounds);
		canvas.drawText("No. Days that I Skipped Meals: ", 35,
				35 + textBounds.height()+canvas.getHeight()/2, bottomHalfTextPaint);
		bottomHalfTextPaint.setTextSize(30);
		canvas.drawText(Integer.toString(daysDidntSkipMeals),
				canvas.getWidth()-100, 35 + textBounds.height()+canvas.getHeight()/2,
				bottomHalfTextPaint);
		bottomHalfTextPaint.setTextSize(20);
		
		bottomHalfPaint.setColor(Color.BLACK);

		leftSideCircleOriginX = canvas.getWidth() / 4 + 25;
		// outside
		canvas.drawCircle(leftSideCircleOriginX, canvas.getHeight() / 2
				+ canvas.getHeight() / 4 + 25, canvas.getWidth() / 8,
				bottomHalfPaint);
		bottomHalfPaint.setColor(Color.WHITE);
		// inside
		canvas.drawCircle(leftSideCircleOriginX, canvas.getHeight() / 2
				+ canvas.getHeight() / 4 + 25, canvas.getWidth() / 8 - 5,
				bottomHalfPaint);
		bottomHalfTextPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText("Hungover", leftSideCircleOriginX, canvas.getHeight()
				/ 2 + canvas.getHeight() / 4 + 25 - 15, bottomHalfTextPaint);
		bottomHalfTextPaint.setTextSize(30);
		canvas.drawText(Double.toString(daysNoSkipHungOverPercent) + "%",
				leftSideCircleOriginX,
				canvas.getHeight() / 2 + canvas.getHeight() / 4 + 25 + 15,
				bottomHalfTextPaint);
		// right circle----------------

		// outside
		bottomHalfPaint.setColor(Color.BLACK);
		canvas.drawCircle(leftSideCircleOriginX * 2 + 25, canvas.getHeight()
				/ 2 + canvas.getHeight() / 4 + 25, canvas.getWidth() / 8,
				bottomHalfPaint);
		bottomHalfPaint.setColor(Color.WHITE);
		// inside
		canvas.drawCircle(leftSideCircleOriginX * 2 + 25, canvas.getHeight()
				/ 2 + canvas.getHeight() / 4 + 25, canvas.getWidth() / 8 - 5,
				bottomHalfPaint);
		bottomHalfTextPaint.setTextAlign(Paint.Align.CENTER);
		bottomHalfTextPaint.setTextSize(20);
		canvas.drawText("Upset", leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 2 + canvas.getHeight() / 4 + 25 - 30,
				bottomHalfTextPaint);
		canvas.drawText("Stomach", leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 2 + canvas.getHeight() / 4 + 25 - 15,
				bottomHalfTextPaint);
		bottomHalfTextPaint.setTextSize(30);
		canvas.drawText(Double.toString(daysNoSkipMealSickPercent) + "%",
				leftSideCircleOriginX * 2 + 25,
				canvas.getHeight() / 2 + canvas.getHeight() / 4 + 25 + 15,
				bottomHalfTextPaint);

		requestLayout();
	}

}
