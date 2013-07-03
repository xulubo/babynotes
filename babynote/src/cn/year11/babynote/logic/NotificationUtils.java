package cn.year11.babynote.logic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NotificationUtils {
		
	public static void clear(Context context, int i)
	{
		((NotificationManager)context.getSystemService("notification")).cancel(i);
	}

	private static NotificationManager getNotificationManager(Context context)
	{
		return ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE));
	}
	
	public static void show(Context context, String title, RemoteViews remoteviews, Intent intent, int notificationId, int iconResourceId)
	{
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | Intent.FLAG_ACTIVITY_CLEAR_TOP /*0x14000000*/);
		Notification notification = new Notification();
		notification.icon = iconResourceId;
		notification.tickerText = title;
		notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
		notification.setLatestEventInfo(context, notification.tickerText, null, pendingIntent);
		notification.contentView = remoteviews;
		if (notificationId == 1002)
		{
			notification.defaults = 1;
			notification.flags = Notification.FLAG_AUTO_CANCEL;
		}
		getNotificationManager(context).notify(notificationId, notification);
	}

	public static void show(Context context, String title, String text, Intent intent, int notificationId, int iconResourceId)
	{
		PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Notification notification = new Notification();
		notification.icon = iconResourceId;
		notification.tickerText = title;
		notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
		notification.setLatestEventInfo(context, notification.tickerText, text, pendingintent);
		getNotificationManager(context).notify(notificationId, notification);
	}

	public static void showOnetime(Context context, String tickerText, String text, Intent intent, int notificationId, int iconResourceId)
	{
		PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		Notification notification = new Notification();
		notification.icon = iconResourceId;
		notification.tickerText = tickerText;
		//notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, notification.tickerText, text, pendingintent);
		notification.defaults = Notification.DEFAULT_ALL;
		getNotificationManager(context).notify(notificationId, notification);
	}
}
