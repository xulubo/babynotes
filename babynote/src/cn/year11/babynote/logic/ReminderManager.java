package cn.year11.babynote.logic;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.provider.TimerInfo;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.provider.ReminderDao;
import cn.year11.babynote.provider.TimerTemplate;
import cn.year11.babynote.utils.AlarmUtils;

public class ReminderManager {
	static private ReminderManager _instance = null;
	private Context mContext;
	static final long DAY_MILLS = 86400000L;
	
	static public final ReminderManager getInstance()
	{
		if (_instance == null) {
			_instance = new ReminderManager();
		}
		
		return _instance;
	}
	
	static public final List<TimerTemplate> getTemplates()
	{
		LinkedList<TimerTemplate> templates = new LinkedList<TimerTemplate>();
		
		TimerTemplate t = new TimerTemplate();
		t.setTitle("÷ÛƒÃ∆ø");
		t.setIcon_resource_id(0);
		t.setDuration(300);
		templates.add(t);
		
		t.setTitle("»»≈£ƒÃ");
		t.setIcon_resource_id(0);
		t.setDuration(50);
		templates.add(t);

		t.setTitle("÷Ûº¶µ∞");
		t.setIcon_resource_id(0);
		t.setDuration(600);
		templates.add(t);

		return templates;
	}
	public ReminderManager(){
		mContext = BabyNoteApplication.getContext();
	}
	
	public void reschedule()
	{
		ReminderDao dao = ReminderDao.getInstance();
		Reminder reminder = dao.getEarlistReminder();
		long alarmTime = 0;

		if (reminder != null) {
			alarmTime = reminder.getEventTime();
		}
		
    	for(int i=0; i<4; i++) {
    		TimerInfo alarm = AlarmPrefs.get(i);
    		if (alarm != null && alarm.stopTime>=System.currentTimeMillis() ) {
    			alarmTime = alarm.stopTime<alarmTime ? alarm.stopTime : alarmTime;
    		}
    	}
    	
		if (reminder != null) {
			AlarmUtils.setAlarm(mContext, alarmTime);
		}
	}
	
    public List<Reminder> findDueReminder()
    {
    	ReminderDao dao = ReminderDao.getInstance();
    	Cursor cursor = dao.queryMany(null, 
    			Reminder.STATUS + "=? AND " + Reminder.EVENT_TIME + "<=?", 
    			new String[] {
    			String.valueOf(Reminder.ACTIVE),
    			String.valueOf(System.currentTimeMillis())
    	}, null);
    	
    	if (cursor == null) {
    		return null;
    	}
    	
    	try {
    		LinkedList<Reminder> list = new LinkedList<Reminder>();
    		while(cursor.moveToNext()) {
    			list.add(new Reminder(cursor));
    		}
    		
    		return list;
    	}
    	finally {
    		cursor.close();
    	}
    	
    }
}
