package com.example.drinkingapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "QuestionDB";
	
	private static final String TABLE_MULTI = "multichoice";
	private static final String TABLE_QUES = "questions";
	
	// Multi Choice column names
	private static final String MULTI_KEY_ID = "id";
	private static final String MULTI_KEY_QUES_ID = "question_id";
	private static final String MULTI_KEY_VALUE = "value";
	
	// Question column names
	private static final String QUES_KEY_ID = "id";
	private static final String QUES_KEY_VAR = "variable";
	private static final String QUES_KEY_DAY = "day";
	private static final String QUES_KEY_MONTH = "month";
	private static final String QUES_KEY_YEAR = "year";
	private static final String QUES_KEY_DAY_WEEK = "day_week";
	private static final String QUES_KEY_TIME = "time";
	private static final String QUES_KEY_TYPE= "type";
	private static final String QUES_KEY_VALUE = "value";
	
	//values for the question types the db can handle
	private static final String text_type = "text";
	private static final String multi_type = "multi";
	private static final String slider_type = "slider";
	private static final String radio_type = "radio";
	private static final String number_type = "number";
	
	public DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//create Multi Question Table
		String CREATE_MULTI = "CREATE TABLE multichoice (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"question_id INTEGER, " + 
				"value TEXT ) ";
		//create the mutli-answer table
		db.execSQL(CREATE_MULTI);
		
		//create Question Table
		String CREATE_QUES = "CREATE TABLE questions (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"variable TEXT, " +
				"month INTEGER, " +
				"day INTEGER, "+
				"year INTEGER, " + 
				"day_week TEXT, " + 
				"time TEXT, " + 
				"type TEXT, " +
				"value TEXT )";

		//create the question table
		db.execSQL(CREATE_QUES);
	}
	
	public void addTextQuestion(Date date, String value, String variable_name){
		String type = text_type;
		//extract the values from Date to store in db.
		SimpleDateFormat day_ft = new SimpleDateFormat("dd", Locale.US);
		SimpleDateFormat month_ft = new SimpleDateFormat("MM", Locale.US);
		SimpleDateFormat year_ft = new SimpleDateFormat("yyyy", Locale.US);
		SimpleDateFormat day_week_ft = new SimpleDateFormat("E", Locale.US);
		SimpleDateFormat time_ft = new SimpleDateFormat("HH:mm:ss", Locale.US);
		
		Integer day_val = Integer.parseInt(day_ft.format(date));
		Integer month_val = Integer.parseInt(month_ft.format(date));
		Integer year_val = Integer.parseInt(year_ft.format(date));
		String day_week = day_week_ft.format(date);
		String time = time_ft.format(date);
		
		//get reference to the database
		SQLiteDatabase db = this.getWritableDatabase();
		
		//create the ContentValues to hold column values
		ContentValues values = new ContentValues();
		values.put(QUES_KEY_DAY, day_val);
		values.put(QUES_KEY_MONTH, month_val);
		values.put(QUES_KEY_YEAR, year_val);
		values.put(QUES_KEY_TIME, time);
		values.put(QUES_KEY_DAY_WEEK, day_week);
		values.put(QUES_KEY_VALUE,value);
		values.put(QUES_KEY_TYPE, type);
		values.put(QUES_KEY_VAR, variable_name);
		
		//insert the values into the table
		db.insert(TABLE_QUES, null, values);
		
		//close the database
		db.close();
	}
	
	public String getAllVarValue(String variable){
		//Get reference to the db
		SQLiteDatabase db = this.getWritableDatabase();
		
		String type = text_type;
		
		String query = "Select value FROM " + TABLE_QUES + "WHERE " + 
				QUES_KEY_TYPE + " = " + type + ", " +
				QUES_KEY_VAR + " = " + variable + ", ";
				
		Cursor cursor = db.rawQuery(query, null);
		
		//check to see if our query returned values
		if(cursor.moveToFirst()){
			String value = cursor.getString(0);
			return value;
		}else{
			return null;
		}
	}
	
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
