package cornell.trickleapp;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

/**
 * Helper class used when dealing with BAC intervals
 * 
 * @author wraziens
 */
public class BacTime extends Object{
	private double gender_constant;
	private double weight_kilograms;
	private double metabolism_constant;
	private DatabaseHandler db;
	
	public BacTime(Context context) {
		//Open the Database
		db = new DatabaseHandler(context);

		// get the users gender
		ArrayList<DatabaseStore> stored_gender = (ArrayList<DatabaseStore>) db
				.getAllVarValue("gender");
		// If user did not set gender use "Female" as default
		String gender = "Female";
		if (stored_gender != null) {
			gender = stored_gender.get(0).value;
		}

		// fetch the users weight
		ArrayList<DatabaseStore> stored_weight = (ArrayList<DatabaseStore>) db
				.getAllVarValue("weight");
		Integer weight_lbs = 120;
		if (stored_weight != null) {
			weight_lbs = Integer.parseInt(stored_weight.get(0).value);
		}

		metabolism_constant = 0;
		gender_constant = 0;
		weight_kilograms = weight_lbs * 0.453592;

		if (gender.equals("Male")) {
			metabolism_constant = 0.015;
			gender_constant = 0.58;
		} else {
			metabolism_constant = 0.017;
			gender_constant = 0.49;
		}
	}
	
	
	private double calcElapsed(int numDrinks, double bac){
		double value = ( ((0.806 * numDrinks * 1.2)/(gender_constant * weight_kilograms)) - bac) / metabolism_constant;
		return value;
	}
	
	// Returns the number of drinks for the bac to be at value
	public int getDrinksForBac(double bac){
		double value = ((bac + metabolism_constant) * (gender_constant * weight_kilograms)) / (0.806 * 1.2);
		return (int) Math.floor(value);
	}
	
	/**
	 * Calculates the time when the BAC value will be 0
	 * 
	 * @param drinkValue
	 * 		The number of drinks the person had as a DatabaseStore
	 * 
	 * @return
	 * 		Time in seconds when the BAC value will be within 0  BAC or -1 if the 
	 * 		the value is already 0.
	 */
	public int getClearDate(int drinkCount, int time_seconds){
		int drinks = getDrinksForBac(0.059);
		if(drinkCount > drinks){
			drinkCount = drinks;
		}
		//Green is less than 0.06 bac value
		double elapsed = calcElapsed(drinkCount, 0);
		if (elapsed > 0){
			return time_seconds + (int)Math.ceil(elapsed * 60 * 60);
		}else{
			return -1;
		}
	}
	
	public int adjustDrinkCount(TrendsSliceItem item){
		double timeProcess = calcElapsed(item.getDrinkCount(), 0);
		//Calculate the number of drinks proccessed by the individual
		int remove = 0;
		if(item.getTimeNextDrink() > -1){
			remove = (int)Math.floor(item.getDuration() / (timeProcess * 60 * 60 ));
			
		}
		return remove;
	}
	
	/**
	 * Calculates the time when the BAC value will be within green limits
	 * 
	 * @param drinkValue
	 * 		The number of drinks the person had as a DatabaseStore
	 * 
	 * @return
	 * 		Time in seconds when the BAC value will be within Green limits or -1 if the 
	 * 		the value is already below it.
	 */
	public int getGreenDate(int drinkCount, int time_seconds){
		int drinks = getDrinksForBac(0.14);
		
		if(drinkCount > drinks){
			drinkCount = drinks;
		}
		//Green is less than 0.06 bac value
		double elapsed = calcElapsed(drinkCount, 0.059);

		if (elapsed > 0){
			return time_seconds + (int)Math.ceil(elapsed * 60 *60);
		}else{
			return -1;
		}
	}
	
	
	/**
	 * Calculates the time when the BAC value will be within yellow limits
	 * 
	 * @param drinkValue
	 * 		The number of drinks the person had as a DatabaseStore
	 * 
	 * @return
	 * 		time in seconds when the BAC value will be within Yellow limits or -1 if the 
	 * 		the value is already below it.
	 */
	public int getYellowDate(int drinkCount, int time_seconds){
		int drinks = getDrinksForBac(0.23);
		if(drinkCount > drinks){
			drinkCount = drinks;
		}
		//Yellow is less than 0.15 bac value
		double elapsed = calcElapsed(drinkCount, 0.14);
		if (elapsed > 0){
			return time_seconds +  (int)Math.ceil(elapsed * 60 * 60);
		}else{
			return -1;
		}
	}
	
	/**
	 * Calculates the time when the BAC value will be within red limits
	 * 
	 * @param drinkValue
	 * 		The number of drinks the person had as a DatabaseStore
	 * 
	 * @return
	 * 		time in seconds when the BAC value will be within Red limits or -1 if the 
	 * 		the value is already below it.
	 */
	public int getRedDate (int drinkCount, int time_seconds){
		//Red is less than 0.24 bac value
		double elapsed = calcElapsed(drinkCount, 0.23);
		if (elapsed > 0){
			return time_seconds + (int)Math.ceil(elapsed * 60 * 60);
		}else{
			return -1;
		}
	}
	
	/***
	 * Returns a list of double BAC values given a list of DatabaseStore
	 * that contain 'number of drinks' for a given day.
	 * 
	 * @param dsStore
	 * 		An ArrayList of DatabaseStores containing number of drinks for a day.
	 * 
	 */
	public ArrayList<Double> getBacValues(ArrayList<DatabaseStore> dsStore) {
		
		ArrayList<DatabaseStore> values = DatabaseStore.sortByTime(dsStore);
		
		//retrieve the starting value
		DatabaseStore start = values.get(0);
		
		ArrayList<Double> bacValues = new ArrayList<Double>();
		DatabaseStore current = null;
		
		for (int i=0; i< values.size(); i++){
			current = values.get(i);
			long elapsedTime = (current.date.getTime() - start.date.getTime()) / (1000 * 60 * 60);
			int number_drinks = Integer.parseInt(current.value);
			
			double bac = ((0.806 * number_drinks * 1.2) / (gender_constant * weight_kilograms))
					- (metabolism_constant * elapsedTime);
			bacValues.add(bac);
		}
		return bacValues;
	}

}
