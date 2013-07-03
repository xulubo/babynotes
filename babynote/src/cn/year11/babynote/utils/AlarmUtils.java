package cn.year11.babynote.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.year11.babynote.app.receiver.AlarmReceiver;

public class AlarmUtils {

	static final long DAY_MILLS = 86400000L;
	
	static public void removeLastAlarm(AlarmManager alarmManager)
	{
	}
	
	static private AlarmManager getAlarmManager(Context context)
	{
		return (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
	
	static private PendingIntent getdAlarmPendingIntent(Context context, long time)
	{
		Intent intent = new Intent(context, AlarmReceiver.class);
		//intent.setAction("com.zdworks.android.zdclock.ACTION_ALARM_ALERT");
		//intent.setAction("cn.year11.babynote.ACTION_REMINDER_ALARM");
		//intent.putExtra("cn.year11.babynote.NextAlarmTime", time);
		
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	static private void setDateChangedAlarm(Context context)
	{

		Intent intent = new Intent();
		intent.setAction("cn.year11.babynote.ACTION_DATE_CHANGED");
		intent.addCategory("cn.year11.babynote.CATEGORY");
		PendingIntent pendingintent = PendingIntent.getBroadcast(context, 0, intent, 0x8000000);
		getAlarmManager(context).set(0, System.currentTimeMillis()+DAY_MILLS, pendingintent);
	}

	
	static public void setAlarm(Context context, long time)
	{
		AlarmManager alarmManager = getAlarmManager(context);
		removeLastAlarm(alarmManager);
		alarmManager.set(0, time, getdAlarmPendingIntent(context, time));
		//setDateChangedAlarm();
		Toast.makeText(context, "setAlarm: " + DateUtils.formatDateTime(time), 2).show();
	}
	
	static public void removeAllAlarm(Context context)
	{
		PendingIntent pendingIntent = getdAlarmPendingIntent(context, 0);
		getAlarmManager(context).cancel(pendingIntent);
	}
	
}
