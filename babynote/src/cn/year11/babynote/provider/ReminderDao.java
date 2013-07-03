package cn.year11.babynote.provider;

import cn.year11.babynote.BabyNoteApplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


public class ReminderDao extends BaseDao {
	private static ReminderDao _instance = null;
	
	public static ReminderDao getInstance()
	{
		if (_instance == null) {
			_instance = new ReminderDao();
		}
		
		return _instance;
	}
	
	public ReminderDao() {
		super(Reminder.TABLE_NAME);
	}

	static public final String TABLE_NAME = "reminder";
	static public final String ID = "_id";
	static public final String EVENT_TIME = "time";
	static public final String ALARM_TIME = "alarm_time";
	static public final String DATA_SOURCE = "data_source";
	static public final String TITLE = "title";
	static public final String MEMO = "memo";
	static public final String EVENT_CODE = "code";
	static public final String STATUS = "status";
	static public final String CATEGORY = "category";
	
	public void save(Reminder reminder)
	{
		saveOrUpdate(reminder);
	}
	
	public void saveOrUpdate(Reminder reminder)
	{
		ContentValues values = new ContentValues();

		values.put(Reminder.CATEGORY, reminder.getCategory());
		values.put(Reminder.EVENT_TIME, reminder.getEventTime());
		values.put(Reminder.ALARM_TIME, reminder.getAlarmTime());
		values.put(Reminder.TITLE, reminder.getTitle());
		values.put(Reminder.MEMO, reminder.getMemo());
		values.put(Reminder.DATA_SOURCE, reminder.getDataSource());
		values.put(Reminder.STATUS, reminder.getStatus());
		values.put(Reminder.EVENT_CODE, reminder.getEventCode());

		if (reminder.getId() != 0) {
			values.put(Reminder.UPDATE_TIME, System.currentTimeMillis());
			this.updateSingle(values, reminder.getId());
		}
		else {
			values.put(Reminder.CREATE_TIME, System.currentTimeMillis());
			insert(values);
		}
	}
	
	public Reminder getByEventCode(String code)
	{
		Cursor cursor = this.queryMany(null, Reminder.EVENT_CODE+"=?", new String[]{
				code}, null);
		
		if (cursor == null) {
			return null;
		}
		
		try {
			if (cursor.getCount() < 1) {
				return null;
			}
			cursor.moveToNext();
			return new Reminder(cursor);
		}
		finally {
			cursor.close();
		}
	}

	public Reminder getEarlistReminder() {
		Cursor cursor = this.queryMany(null, Reminder.EVENT_TIME+">? AND " + Reminder.STATUS + "=?"
				, new String[]{
				String.valueOf(System.currentTimeMillis())
				, String.valueOf(Reminder.ACTIVE)}, Reminder.EVENT_TIME);
		
		if (cursor == null) {
			return null;
		}
		
		try {
			if (cursor.getCount() < 1) {
				return null;
			}
			cursor.moveToNext();
			return new Reminder(cursor);
		}
		finally {
			cursor.close();
		}
	}

	public Cursor getActiveReminders() {
		Cursor cursor = this.queryMany(null, Reminder.STATUS + "=? AND " + Reminder.DATA_SOURCE + "=?"
				, new String[]{
				String.valueOf(Reminder.ACTIVE), Reminder.DATA_SOURCE_USER}, Reminder.EVENT_TIME);
		
		return cursor;
	}

	public Reminder getReminder(long reminderId) {
		Cursor cursor = querySingle(null, reminderId);
		if (cursor == null) {
			return null;
		}
		
		try {
			if (cursor.moveToNext()) {
				return new Reminder(cursor);
			}
		}
		finally {
			cursor.close();
		}
		return null;
	}

	
}
