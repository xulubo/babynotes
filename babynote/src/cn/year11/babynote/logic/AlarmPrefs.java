package cn.year11.babynote.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.Preference;
import android.preference.PreferenceManager;
import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.provider.TimerInfo;

public class AlarmPrefs {

	public static SharedPreferences getPrefs()
	{
		Context ctx = BabyNoteApplication.getContext();
		return ctx.getSharedPreferences("alarm_prefs", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
	}
	
	static public boolean save(int panelId, TimerInfo info)
	{
		Editor editor = getPrefs().edit();
		editor.putString(String.format("%d.title", panelId), info.title);
		editor.putLong(String.format("%d.startTime", panelId), info.startTime);
		editor.putLong(String.format("%d.stopTime", panelId), info.stopTime);
		editor.putLong(String.format("%d.remaining", panelId), info.remaining);
		editor.putLong(String.format("%d.status", panelId), info.status);
		
		boolean b = editor.commit();
		return b;
	}
	
	static public TimerInfo get(int panelId)
	{
		TimerInfo info = new TimerInfo();
		SharedPreferences pref = getPrefs();
		
		String title = pref.getString(String.format("%d.title", panelId), null);
		if (title == null) {
			return null;
		}
		
		info.title = title;
		info.startTime = pref.getLong(String.format("%d.startTime", panelId), 0);
		info.stopTime =pref.getLong(String.format("%d.stopTime", panelId), 0);
		info.remaining = pref.getLong(String.format("%d.remaining", panelId), 0);
		info.status = pref.getLong(String.format("%d.status", panelId), 0);
		
		return info;
	}
}
