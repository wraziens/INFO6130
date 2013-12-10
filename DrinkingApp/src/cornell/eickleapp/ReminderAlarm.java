package cornell.eickleapp;

import java.util.Calendar;
import java.util.Date;
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

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Boolean checknotification = getPrefs.getBoolean("notification", true);
		if (checknotification) {

			db = new DatabaseHandler(context);
			// if everything
			if (db.variableExist("numberofcategories")) {
				Calendar calendar = Calendar.getInstance();
				int month = 12;
				int day = 5;
				int year = 2013;
				Date date = new Date();
				List<DatabaseStore> object = db.getVarValuesForDay(
						"numberofcategories", date);
				int number = Integer.parseInt(object.get(0).value);

				int num_Checked = 0;
				String surveyList[] = { "DailySurvey4", "DailySurvey2",
						"DailySurveyExercise", "DailySurveyProductivity" };
				for (int i = 0; i < surveyList.length; i++) {
					Boolean checked = db.variableExist(surveyList[i]
							+ "CheckList");
					if (checked)
						num_Checked++;
				}

				// if number checked is less than to the number then ring alarm
				if (num_Checked < number)
					ringAlarm = true;
				// else dont ring alarm
				else
					ringAlarm = false;
			}
			if (ringAlarm) {
				Intent homeIntent = new Intent(context, MainMenu.class);
				noteManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				CharSequence from = "Trickle";
				CharSequence message = "Don't forget to fill out today's assessment!";
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 0, homeIntent, 0);
				Notification notif = new Notification(
						R.drawable.trickleiconnote,
						"Have you filled today's Assessments?",
						System.currentTimeMillis());
				notif.setLatestEventInfo(context, from, message, contentIntent);

				notif.defaults = Notification.DEFAULT_VIBRATE;
				noteManager.notify(MainMenu.uniqueID, notif);
			}
		}
	}
}
