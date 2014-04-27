package cornell.trickleapp;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class ReminderAlarm extends BroadcastReceiver {
	private NotificationManager noteManager;
	DatabaseHandler db;
	boolean reward = false;
	Boolean ringAlarm = true;
	int id;
	CharSequence message;
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub

		db = new DatabaseHandler(context);

		id = arg1.getExtras().getInt("id");

		// if everything
		// db.variableExistAll("numberofcategories")
		// 1st check: if the goal is still checked:
		// if (true) {
		/*
		 * Calendar calendar = Calendar.getInstance(); int month = 12; int day =
		 * 5; int year = 2013; Date date = new Date(); List<DatabaseStore>
		 * object = db.getVarValuesForDay( "numberofcategories", date); int
		 * number = Integer.parseInt(object.get(0).value);
		 * 
		 * int num_Checked = 0; String surveyList[] = { "DailySurvey2",
		 * "DailySurveyExercise", "DailySurveyProductivity" }; for (int i = 0; i
		 * < surveyList.length; i++) { Boolean checked =
		 * db.variableExistAll(surveyList[i] + "CheckList"); if (checked)
		 * num_Checked++; }
		 * 
		 * // if number checked is less than to the number then ring alarm if
		 * (num_Checked < number) ringAlarm = true; // else dont ring alarm else
		 * ringAlarm = false;
		 */
		Intent homeIntent = new Intent(context, GoalsTracking.class);
		noteManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence from = "Trickle";

		// if the goal is met:
		notificationEvaluation();
		if (reward) {
			message = message+"New goal achieved, collect reward!";
		} else {
			message = message+ ":(";
		}
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				homeIntent, 0);
		@SuppressWarnings("deprecation")
		Notification notif = new Notification(R.drawable.trickleiconnote,
				message, System.currentTimeMillis());
		notif.setLatestEventInfo(context, from, message, contentIntent);

		notif.defaults = Notification.DEFAULT_VIBRATE;
		noteManager.notify(id, notif);

	}

	private void notificationEvaluation() {
		// TODO Auto-generated method stub

		switch (id) {
		case 1:
			if (db.variableExistAll("drink_count")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForWeek(
						"drink_count", date);
				if (value_list==null) {
					reward = true;
					message = "1";
					break;
				}
				// removes duplicate days using hash function
				int valueCalculated = 0;
				HashSet<String> hs = new HashSet<String>();
				for (int i = 0; i < value_list.size(); i++) {
					hs.add(value_list.get(i).day_week);
				}
				valueCalculated = hs.size();
				int valueTracked = Integer.parseInt(db.getAllVarValue(
						"goal_DaysPerWeek").get(0).value);
				if (valueCalculated <= valueTracked)
					reward = true;
				else
					reward = false;
			} else
				reward = true;
			message="1";
			break;
		case 4:
			if (db.variableExistAll("drink_count")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForMonth(
						"drink_count", date);
				if (value_list==null) {
					reward = true;
					message = "4";
					break;
				}
				// removes duplicate days using hash function
				int valueCalculated = 0;
				HashSet<String> hs = new HashSet<String>();
				for (int i = 0; i < value_list.size(); i++) {
					hs.add(value_list.get(i).day.toString());
				}
				valueCalculated = hs.size();
				int valueTracked = Integer.parseInt(db.getAllVarValue(
						"goal_DaysPerMonth").get(0).value);
				if (valueCalculated <= valueTracked)
					reward = true;
				else
					reward = false;
			} else
				reward = true;

			message="4";
			break;
		case 5:
			if (db.variableExistAll("drink_count")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForMonth(
						"drink_count", date);
				if (value_list==null) {
					reward = true;
					message = "5";
					break;
				}
				int valueCalculated = 0;
				valueCalculated = value_list.size();
				int valueTracked = Integer.parseInt(db.getAllVarValue(
						"goal_DrinksPerMonth").get(0).value);
				if (valueCalculated <= valueTracked)
					reward = true;
				else
					reward = false;
			} else
				reward = true;

			message="5";
			break;
		case 6:
			if (db.variableExistAll("money")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForMonth(
						"money", date);
				if (value_list==null) {
					reward = true;
					message = "6";
					break;
				}
				// sum up all the money
				double valueCalculated = 0;
				for (int i = 0; i < value_list.size(); i++) {
					valueCalculated = valueCalculated
							+ Double.parseDouble(value_list.get(i).value);
				}
				double valueTracked = Integer.parseInt(db.getAllVarValue(
						"goal_DollarsPerMonth").get(0).value);
				if (valueCalculated <= valueTracked)
					reward = true;
				else
					reward = false;
			} else
				reward = true;

			message="6";
			break;
		}

	}

}
