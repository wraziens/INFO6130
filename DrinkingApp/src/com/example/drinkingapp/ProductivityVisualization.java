package com.example.drinkingapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class ProductivityVisualization extends Activity{

	GridView productivityGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productivityvisualization);
		productivityGridView=(GridView)findViewById(R.id.gvProductivity);
		ProductivityAdapter adapter=new ProductivityAdapter(this);
		productivityGridView.setAdapter(adapter);
	}

}
