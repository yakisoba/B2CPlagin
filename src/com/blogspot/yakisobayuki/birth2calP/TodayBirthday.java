package com.blogspot.yakisobayuki.birth2calP;
import android.app.Activity;
import android.os.Bundle;

import com.blogspot.yakisobayuki.birth2calP.CalendarReceiver.BirthNotification;

public class TodayBirthday extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birthdaylist);

		CalendarReceiver change = new CalendarReceiver();
		BirthNotification notification = change.new BirthNotification();
		notification.removeNotice(this);

	}
}