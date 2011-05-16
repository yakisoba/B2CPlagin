package com.blogspot.yakisobayuki.birth2calP;

import com.blogspot.yakisobayuki.birth2calP.TodayCalendar.BirthNotification;

import android.app.Activity;
import android.os.Bundle;

public class BirthdayView extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birthdaylist);

		TodayCalendar change = new TodayCalendar();
		BirthNotification notification = change.new BirthNotification();
		notification.removeNotice(this);

	}
}