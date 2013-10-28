package com.example.drinkingapp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	public DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		//create Multi Question Table
		String CREATE_MULTI = "CREATE TABLE multichoice (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"question_id INTEGER, " + 
				"value TEXT ); ";
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
				"value TEXT );";

		//create the question table
		db.execSQL(CREATE_QUES);
	}
	
	//Adds an integer value to the database
	public void addValue(String variable, Integer int_value){
		Date date = new Date();
		DatabaseStore ds = DatabaseStore.DatabaseIntegerStore(variable, int_value.toString(), date);
		addTextQuestion(ds);
	}
	
	public void addTextQuestion(DatabaseStore store){
		//get reference to the database
		SQLiteDatabase db = this.getWritableDatabase();
		
		//create the ContentValues to hold column values
		ContentValues values = new ContentValues();
		values.put(QUES_KEY_DAY, store.day);
		values.put(QUES_KEY_MONTH, store.month);
		values.put(QUES_KEY_YEAR, store.year);
		values.put(QUES_KEY_TIME, store.time);
		values.put(QUES_KEY_DAY_WEEK, store.day_week);
		values.put(QUES_KEY_VALUE, store.value);
		values.put(QUES_KEY_TYPE, store.type);
		values.put(QUES_KEY_VAR, store.variable);
		//insert the values into the table
		db.insert(TABLE_QUES, null, values);
		
		//close the database
		db.close();
	}
	
	private List<DatabaseStore> handleCursor(Cursor cursor){
		//check to see if our query returned values
		try{
			DatabaseStore store = null;
			ArrayList<DatabaseStore> store_list = new ArrayList<DatabaseStore>();
			if(cursor.moveToFirst()){
				do{
					String variable = cursor.getString(1);
					Integer month = cursor.getInt(2);
					Integer day = cursor.getInt(3);
					Integer year = cursor.getInt(4);
					String time = cursor.getString(6);
					String type = cursor.getString(7);
					String value = cursor.getString(8);
					Date date = DatabaseStore.GetDate(month, day, year, time);
					store = DatabaseStore.FromDatabase(variable, value, date, type); 
					store_list.add(store);
				} while (cursor.moveToNext());
			}else{
				return null;
			}
			return store_list;
		} catch (ParseException pe) {
			System.out.println("Cannot parse string.");
			return null;
		}
	}
	
	public List<DatabaseStore> getAllVarValue(String variable){
		//Get reference to the database
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "Select * FROM " + TABLE_QUES + " WHERE " + 
				QUES_KEY_VAR + " = " + variable + "; ";
		Cursor cursor = db.rawQuery(query, null);
		return handleCursor(cursor);
	}
	
	public List<DatabaseStore> getVarValuesForDay(String variable, Integer month, 
			Integer day, Integer year){
		//get reference to the database
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_QUES + " WHERE " + 
				QUES_KEY_VAR + "='" + variable + "' AND " +
				QUES_KEY_DAY + "=" + day + " AND " + 
				QUES_KEY_MONTH + "=" + month + " AND " + 
				QUES_KEY_YEAR + "=" + year;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() == 0){
			return null;
		}else{
			return handleCursor(cursor);
		}
	}
	
	public List<DatabaseStore> getVarValuesForMonth(String variable, Integer month, Integer year){
		//get reference to the database
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "Select * FROM " + TABLE_QUES + " WHERE " + 
				QUES_KEY_VAR + "='" + variable + "' AND " +
				QUES_KEY_MONTH + "=" + month + " AND " + 
				QUES_KEY_YEAR + "=" + year + ";";
		Cursor cursor = db.rawQuery(query, null);
		return handleCursor(cursor);
	}	
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
