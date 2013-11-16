package com.example.drinkingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class ProductivityVisualization extends Activity {

	GridView productivityGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productivityvisualization);

		// sample database stuff (data that has already been manipulated into
		// the proper format)

		ArrayList<Double> gradesAverageList = new ArrayList<Double>();
		ArrayList<Double> productivityAverageList = new ArrayList<Double>();
		ArrayList<Double> stressAverageList = new ArrayList<Double>();
		ArrayList<Integer> daysDrankList = new ArrayList<Integer>();
		ArrayList<Double> bacAverageList = new ArrayList<Double>();

		gradesAverageList.add(10.0);
		gradesAverageList.add(9.0);
		gradesAverageList.add(8.0);
		productivityAverageList.add(8.0);
		productivityAverageList.add(7.0);
		productivityAverageList.add(5.0);
		stressAverageList.add(10.0);
		stressAverageList.add(5.0);
		stressAverageList.add(7.0);
		daysDrankList.add(2);
		daysDrankList.add(1);
		daysDrankList.add(3);
		bacAverageList.add(0.15);
		bacAverageList.add(0.05);
		bacAverageList.add(0.08);
		
		
		productivityGridView = (GridView) findViewById(R.id.gvProductivity);
		ProductivityAdapter adapter = new ProductivityAdapter(this, gradesAverageList,
				productivityAverageList, stressAverageList, daysDrankList, bacAverageList);
		
		
		
		productivityGridView.setAdapter(adapter);
	}

}
