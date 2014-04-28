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
	Boolean ringAlarm = true;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub

		db = new DatabaseHandler(context);
		// if everything
		// db.variableExistAll("numberofcategories")
		// 1st check: if the goal is still checked:
		if (true) {
			/*
			 * Calendar calendar = Calendar.getInstance(); int month = 12; int
			 * day = 5; int year = 2013; Date date = new Date();
			 * List<DatabaseStore> object = db.getVarValuesForDay(
			 * "numberofcategories", date); int number =
			 * Integer.parseInt(object.get(0).value);
			 * 
			 * int num_Checked = 0; String surveyList[] = { "DailySurvey2",
			 * "DailySurveyExercise", "DailySurveyProductivity" }; for (int i =
			 * 0; i < surveyList.length; i++) { Boolean checked =
			 * db.variableExistAll(surveyList[i] + "CheckList"); if (checked)
			 * num_Checked++; }
			 * 
			 * // if number checked is less than to the number then ring alarm
			 * if (num_Checked < number) ringAlarm = true; // else dont ring
			 * alarm else ringAlarm = false;
			 */
			if (true) {
				Intent homeIntent = new Intent(context, GoalsTracking.class);
				noteManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				CharSequence from = "Trickle";
				CharSequence message;
				// if the goal is met:
				boolean reward=false;
				if (db.variableExistAll("drink_count")) {
					Date date = new Date();
					List<DatabaseStore> drinkCount_list = db
							.getVarValuesForWeek("drink_count", date);
					// removes duplicate days using hash function
					int daysDrank = 0;
					HashSet<String> hs = new HashSet<String>();
					for (int i = 0; i < drinkCount_list.size(); i++) {
						hs.add(drinkCount_list.get(i).day_week);
					}
					daysDrank = hs.size();
					int valueTracked = Integer.getInteger(db.getAllVarValue(
							"goal_DaysPerWeek").get(0).value);
					if(daysDrank <= valueTracked)
						reward=true;
					else reward=false;
				}
				else
					reward=true;
				if (reward) {
					message = "New goal achieved, collect reward!";
				} else {
					message = ":(";
				}
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 0, homeIntent, 0);
				@SuppressWarnings("deprecation")
				Notification notif = new Notification(
						R.drawable.trickleiconnote, message,
						System.currentTimeMillis());
				notif.setLatestEventInfo(context, from, message, contentIntent);

				notif.defaults = Notification.DEFAULT_VIBRATE;
				noteManager.notify(MainMenu.uniqueID, notif);

			}
		}
	}
}