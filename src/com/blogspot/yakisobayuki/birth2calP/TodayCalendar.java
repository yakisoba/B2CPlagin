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
import android.widget.RemoteViews;

public class TodayCalendar extends BroadcastReceiver {
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
		String action = intent.getAction();
		if (!"Birth2CalPlagin".equals(action)) {
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

		if (c1 != null) {
			try {
				while (c1.moveToNext()) {
					String name = c1.getString(c1
							.getColumnIndex(Event.DISPLAY_NAME));
					String date = c1.getString(c1.getColumnIndex(Event.DATA));
					String type = c1.getString(c1.getColumnIndex(Event.TYPE));

					int yyyy, mm, dd;
					yyyy = Integer.parseInt(date.substring(0, 4));
					mm = Integer.parseInt(date.substring(5, 7));
					dd = Integer.parseInt(date.substring(8, 10));

					if (mm == mMonth + 1 && dd == mDay) {
						if (Integer.parseInt(type) == 1) {
							Strings strings = new Strings();
							strings.putStrings(name, "ãLîOì˙", (mYear - yyyy)
									+ "é¸îN");
							mList.add(strings);
						} else if (Integer.parseInt(type) == 3) {
							Strings strings = new Strings();
							strings.putStrings(name, "íaê∂ì˙", (mYear - yyyy)
									+ "çŒ");
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

			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.icon, "",
					System.currentTimeMillis());

			Intent intent = new Intent(context, BirthdayView.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					intent, 0);
			notification.contentIntent = contentIntent;

			RemoteViews contentView = new RemoteViews("com.test",
					R.layout.statusview);
			contentView.setImageViewResource(R.id.image, R.drawable.icon);
			for (Strings obj : mList) {
				switch (mList.indexOf(obj)) {
				case 0:
					contentView.setTextViewText(R.id.name1, obj.getName());
					contentView.setTextViewText(R.id.type1, obj.getType());
					contentView.setTextViewText(R.id.age1, obj.getAge());
					break;
				case 1:
					contentView.setTextViewText(R.id.name2, obj.getName());
					contentView.setTextViewText(R.id.type2, obj.getType());
					contentView.setTextViewText(R.id.age2, obj.getAge());
					break;
				case 2:
					if (mList.size() <= 3) {
						contentView.setTextViewText(R.id.name3, obj.getName());
						contentView.setTextViewText(R.id.type3, obj.getType());
						contentView.setTextViewText(R.id.age3, obj.getAge());
					} else {
						contentView.setTextViewText(R.id.name3,
								"ëº" + (mList.size() - 2) + "åè");
					}
					break;

				default:
					break;
				}
			}

			notification.contentView = contentView;
			nm.notify(R.string.app_name, notification);
		}

		public void removeNotice(Context context) {
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(R.string.app_name);
		}
	}
}
