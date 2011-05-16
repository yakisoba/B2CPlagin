package com.blogspot.yakisobayuki.birth2calP;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

public class Birth2CalPlagin extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context context = getApplicationContext();

		Intent intent = new Intent(context, TodayCalendar.class);
		intent.setAction("Birth2CalPlagin");

		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager am = (AlarmManager) (context
				.getSystemService(ALARM_SERVICE));

		long interval = 1 * 1000 * 60;
		long firstTime = SystemClock.elapsedRealtime();
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
				interval, sender);
	}
}