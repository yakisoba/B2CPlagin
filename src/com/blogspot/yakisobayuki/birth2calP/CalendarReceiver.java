package com.blogspot.yakisobayuki.birth2calP;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Data;
import android.util.Log;

public class CalendarReceiver extends BroadcastReceiver {
	// 今日の日付取得
	final Calendar mCalendar = Calendar.getInstance();
	final int mYear = mCalendar.get(Calendar.YEAR);
	final int mMonth = mCalendar.get(Calendar.MONTH);
	final int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

	private List<Strings> mList = null;

	public class Strings {
		String name = null;
		String type = null;
		String age = null;

		public void putStrings(String name, String type, String age) {
			this.name = name;
			this.type = type;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public String getAge() {
			return age;
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("test", "receive");

		String action = intent.getAction();
		Log.d("test", action);

		if (!"Birth2cal_notification".equals(action)) {
			return;
		}

		this.mList = new ArrayList<Strings>();

		Uri uri = Data.CONTENT_URI;
		String[] projection = new String[] { Event.CONTACT_ID,
				Event.DISPLAY_NAME, Event.DATA, Event.TYPE };
		String selection = Data.MIMETYPE + "=? AND (" + Event.TYPE + "=? OR "
				+ Event.TYPE + "=?) ";
		String[] selectionArgs = new String[] { Event.CONTENT_ITEM_TYPE,
				String.valueOf(Event.TYPE_ANNIVERSARY),
				String.valueOf(Event.TYPE_BIRTHDAY) };

		Cursor c1 = context.getContentResolver().query(uri, projection,
				selection, selectionArgs, null);

		Log.d("test", Integer.toString(c1.getCount()));

		if (c1 != null) {
			try {
				while (c1.moveToNext()) {
					String name = c1.getString(c1
							.getColumnIndex(Event.DISPLAY_NAME));
					String date = c1.getString(c1.getColumnIndex(Event.DATA));
					String type = c1.getString(c1.getColumnIndex(Event.TYPE));

					int yyyy, mm, dd;
					// 年、月、日に分けint型へキャスト
					yyyy = Integer.parseInt(date.substring(0, 4));
					mm = Integer.parseInt(date.substring(5, 7));
					dd = Integer.parseInt(date.substring(8, 10));

					if (mm == mMonth + 1 && dd == mDay) {
						if (Integer.parseInt(type) == 1) {
							Strings strings = new Strings();
							Log.d("test", name);
							strings.putStrings(name, "記念日", (mYear - yyyy)
									+ "周年");
							mList.add(strings);
						} else if (Integer.parseInt(type) == 3) {
							Strings strings = new Strings();
							Log.d("test", name);
							strings.putStrings(name, "誕生日", (mYear - yyyy)
									+ "歳");
							mList.add(strings);
						}
					}
				}
			} catch (Exception e) {
				c1.close();
			}
		}

		BirthNotification notification = new BirthNotification();
		notification.putNotice(context);
	}

	public class BirthNotification {

		public void putNotice(Context context) {
			String value = null;

			for (Strings obj : mList) {
				if (mList.indexOf(obj) == 0) {
					value = obj.getName() + "¥t" + obj.getType() + "¥t"
							+ obj.getAge();
				} else {
					value = value + "¥t¥t他" + (mList.size() - 1) + "件";
					break;
				}
			}

			if (value != null) {

				NotificationManager nm = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.icon,
						"", System.currentTimeMillis());

				Intent intent = new Intent(context, TodayBirthday.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						context, 0, intent, 0);
				notification.contentIntent = contentIntent;

				notification.setLatestEventInfo(context,
						context.getString(R.string.app_name), value,
						contentIntent);

				nm.notify(R.string.app_name, notification);
			}
		}

		public void removeNotice(Context context) {
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(R.string.app_name);
		}
	}
}
