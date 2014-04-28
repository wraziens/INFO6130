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
		//if notification for goals
		if (id != 666) {
			Intent homeIntent = new Intent(context, GoalsTracking.class);
			noteManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			CharSequence from = "Trickle";

			// if the goal is met:
			notificationEvaluation();
			if (reward) {
				message = "New goal achieved, collect reward!";
			} else {
				message = ":(";
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
		//else notification for morning after survey
		else{
			Intent homeIntent = new Intent(context, AfterDrinkSurvey.class);
			noteManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			CharSequence from = "Trickle";

			// if the goal is met:
			notificationEvaluation();
			message="How are you feeling?";
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					homeIntent, 0);
			@SuppressWarnings("deprecation")
			Notification notif = new Notification(R.drawable.trickleiconnote,
					message, System.currentTimeMillis());
			notif.setLatestEventInfo(context, from, message, contentIntent);

			notif.defaults = Notification.DEFAULT_VIBRATE;
			noteManager.notify(id, notif);
		}

	}

	// evaluates the goals
	private void notificationEvaluation() {
		// TODO Auto-generated method stub

		switch (id) {
		case 1:
			if (db.variableExistAll("drink_count")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForWeek(
						"drink_count", date);
				if (value_list != null) {
					// removes duplicate days using hash function
					int valueCalculated = 0;
					HashSet<String> hs = new HashSet<String>();
					for (int i = 0; i < value_list.size(); i++) {
						hs.add(value_list.get(i).day_week);
					}
					valueCalculated = hs.size();
					int valueTracked = Integer.parseInt(db.getAllVarValue(
							"goal_DaysPerWeek").get(0).value);
					if (valueCalculated <= valueTracked) {
						reward = true;
						db.addValue("star_DaysPerWeek", 1);
						db.addValue("reward_kiip", 1);
						db.addValue("reward_kiip_home", 1);
					} else {
						reward = false;
						db.deleteAllVariables("star_DaysPerWeek");
					}
				}
			} else {
				reward = true;
				db.addValue("star_DaysPerWeek", 1);

				db.addValue("reward_kiip", 1);
				db.addValue("reward_kiip_home", 1);
			}
			message = "1";
			break;

		case 2:
			if (db.variableExistAll("drink_count")) {
				Date date = new Date();
				// evaluates from the day before
				date.setTime(date.getTime() - 86400000);
				List<DatabaseStore> value_list = db.getVarValuesForDay(
						"drink_count", date);
				if (value_list != null) {
					int valueCalculated = value_list.size();
					int valueTracked = Integer.parseInt(db.getAllVarValue(
							"goal_DrinksPerOuting").get(0).value);
					if (valueCalculated <= valueTracked) {
						reward = true;
						db.addValue("star_DrinksPerOuting", 1);
						db.addValue("reward_kiip_home", 1);

						db.addValue("reward_kiip", 2);
					} else {
						reward = false;
						db.deleteAllVariables("star_DrinksPerOuting");
					}
				}
			} 
			/*else {
				reward = true;
				db.addValue("star_DrinksPerOuting", 1);
				db.addValue("reward_kiip_home", 1);

				db.addValue("reward_kiip", 2);
			}
			message = "2";
			*/
			break;
		case 3:
			if (db.variableExistAll("bac")) {
				Date date = new Date();
				// evaluates from the day before
				date.setTime(date.getTime() - 86400000);
				List<DatabaseStore> value_list = db.getVarValuesForDay("bac",
						date);
				if (value_list != null) {
					String dummy = db.getAllVarValue("goal_BAC_val").get(0).value
							.substring(1, 5);
					double valueTracked = Double.parseDouble(db.getAllVarValue(
							"goal_BAC_val").get(0).value.substring(1, 5));
					double valueCalculated = 0;
					int testedVal = 0;
					for (int i = 0; i < value_list.size(); i++) {
						valueCalculated = Double
								.parseDouble(value_list.get(i).value);
						if (valueCalculated > valueTracked)
							testedVal++;
					}
					if (testedVal == 0) {
						reward = true;
						db.addValue("star_BAC", 1);
						db.addValue("reward_kiip", 3);
						db.addValue("reward_kiip_home", 1);
					} else {
						reward = false;
						db.deleteAllVariables("star_BAC");
					}
				}
			} /*else {
				reward = true;
				db.addValue("star_BAC", 1);
				db.addValue("reward_kiip", 3);
				db.addValue("reward_kiip_home", 1);
			}*/
			message = "3";
			break;
		case 4:
			if (db.variableExistAll("drink_count")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForMonth(
						"drink_count", date);
				if (value_list != null) {
					// removes duplicate days using hash function
					int valueCalculated = 0;
					HashSet<String> hs = new HashSet<String>();
					for (int i = 0; i < value_list.size(); i++) {
						hs.add(value_list.get(i).day.toString());
					}
					valueCalculated = hs.size();
					int valueTracked = Integer.parseInt(db.getAllVarValue(
							"goal_DaysPerMonth").get(0).value);
					if (valueCalculated <= valueTracked) {
						reward = true;
						db.addValue("star_DaysPerMonth", 1);
						db.addValue("reward_kiip", 4);
						db.addValue("reward_kiip_home", 1);
					} else {
						reward = false;
						db.deleteAllVariables("star_DaysPerMonth");
					}
				}
			} else {
				reward = true;
				db.addValue("star_DaysPerMonth", 1);
				db.addValue("reward_kiip", 4);
				db.addValue("reward_kiip_home", 1);
			}

			message = "4";
			break;
		case 5:
			if (db.variableExistAll("drink_count")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForMonth(
						"drink_count", date);
				if (value_list != null) {
					int valueCalculated = 0;
					valueCalculated = value_list.size();
					int valueTracked = Integer.parseInt(db.getAllVarValue(
							"goal_DrinksPerMonth").get(0).value);
					if (valueCalculated <= valueTracked) {
						reward = true;
						db.addValue("star_DrinksPerMonth", 1);
						db.addValue("reward_kiip", 5);
						db.addValue("reward_kiip_home", 1);
					} else {
						reward = false;
						db.deleteAllVariables("star_DrinksPerMonth");
					}
				}
			} else {
				reward = true;
				db.addValue("star_DrinksPerMonth", 1);
				db.addValue("reward_kiip", 5);
				db.addValue("reward_kiip_home", 1);
			}
			message = "5";
			break;
		case 6:
			if (db.variableExistAll("money")) {
				Date date = new Date();
				List<DatabaseStore> value_list = db.getVarValuesForMonth(
						"money", date);
				if (value_list != null) {
					// sum up all the money
					double valueCalculated = 0;
					for (int i = 0; i < value_list.size(); i++) {
						valueCalculated = valueCalculated
								+ Double.parseDouble(value_list.get(i).value);
					}
					double valueTracked = Integer.parseInt(db.getAllVarValue(
							"goal_DollarsPerMonth").get(0).value);
					if (valueCalculated <= valueTracked) {
						reward = true;
						db.addValue("star_DollarsPerMonth", 1);
						db.addValue("reward_kiip", 6);
						db.addValue("reward_kiip_home", 1);
					} else {
						reward = false;
						db.deleteAllVariables("star_DollarsPerMonth");
					}
				}
			} else {
				reward = true;
				db.addValue("star_DollarsPerMonth", 1);
				db.addValue("reward_kiip", 6);
				db.addValue("reward_kiip_home", 1);
			}

			message = "6";
			break;
		}

	}

}
