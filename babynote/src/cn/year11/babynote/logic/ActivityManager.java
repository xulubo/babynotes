package cn.year11.babynote.logic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.app.AlarmSelectActivity;
import cn.year11.babynote.app.ReminderEditActivity;

public class ActivityManager {

	static public void startReminderEditActivity(Activity parent)
	{		
		Intent intent = new Intent(parent, ReminderEditActivity.class);
		parent.startActivity(intent);
	}
	
	static public void startAlarmSelectActivity(Activity parent)
	{
		Intent intent = new Intent(parent, AlarmSelectActivity.class);
		parent.startActivity(intent);		
	}
}
