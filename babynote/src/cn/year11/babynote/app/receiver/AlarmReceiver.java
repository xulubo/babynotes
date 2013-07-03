package cn.year11.babynote.app.receiver;

import java.util.LinkedList;
import java.util.List;

import cn.year11.babynote.Consts;
import cn.year11.babynote.R;
import cn.year11.babynote.app.AlarmListActivity;
import cn.year11.babynote.logic.AlarmPrefs;
import cn.year11.babynote.logic.NotificationUtils;
import cn.year11.babynote.logic.ReminderManager;
import cn.year11.babynote.provider.TimerInfo;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.utils.AlarmUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {  
	
	String mTitle="收到新消息";
	String mText = "";
	
    @Override  
    public void onReceive(Context context, Intent intent) {
    	
    	Toast.makeText(context, "received alarm", 1).show();
    	findDueAlarm();
    	
        Intent i = new Intent(context, AlarmListActivity.class);
        NotificationUtils.showOnetime(context, mTitle,  mText, i, Consts.ALARM_NOTIFICATIO_ID, R.drawable.notify_icon_1);
        
        Intent n = new Intent(context, AlarmListActivity.class);
        // start activity from background
        n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(n);
        
        ReminderManager.getInstance().reschedule();
    }  
  
    
    
    public void findDueAlarm()
    {
    	List<Reminder> reminders = ReminderManager.getInstance().findDueReminder();
    	if (reminders.size() > 0) {
    		mTitle = String.format("您有%d个到期的提醒", reminders.size());
    		mText = reminders.get(0).getTitle();
    	}
    	
    	LinkedList<TimerInfo> alarms = new LinkedList<TimerInfo>();
    	for(int i=0; i<4; i++) {
    		TimerInfo alarm = AlarmPrefs.get(i);
    		if (alarm != null && alarm.stopTime<=System.currentTimeMillis()) {
    			alarms.add(alarm);
    		}
    	}
    	
    	if (alarms.size()>0) {
    		mTitle += String.format(" %d个报警", alarms.size());
    	}
    }
}  