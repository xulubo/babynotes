package cn.year11.babynote.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import cn.year11.babynote.provider.event.Event;


public class EventDao extends BaseDao {
	private static EventDao _instance = null;
	
	public static EventDao getInstance()
	{
		if (_instance == null) {
			_instance = new EventDao();
		}
		
		return _instance;
	}
	
	
	public EventDao()
	{
		super(Event.TABLE_NAME);
	}
	

	public Cursor findAllDate(String type)
	{
		if (type == null) {
			return this.query(true, new String[]{Event.DATE}, null, null, Event.DATE);
		}
		else {
			return this.query(true, new String[]{Event.DATE},  Event.EVENT_TYPE + "=?", new String[]{type}, Event.DATE);
		}
	}
	
	public Cursor findDayEvents(String date, String type)
	{
		if (type == null) {
			return queryMany(null, Event.DATE + "=?", new String[]{date}, Event.TIME);
		}
		else {
			return queryMany(null, Event.DATE + "=? AND " + Event.EVENT_TYPE + "=?"
					, new String[]{date, type}, Event.TIME);
		}
	}
	
	
	//eventId will be set on success
	public long save(Event event)
	{
		event.beforeSave();

		ContentValues values = event.getValues();
		
		long eventId = insert(values);
		
		for(Attachment a : event.getAttachments()) {
			a.setEventId(eventId);
			AttachmentDao.getInstance().saveOrUpdate(a);
		}
		
		event.setId(eventId);
		return eventId;
	}
	
	public long saveOrUpdate(Event event)
	{
		event.beforeSave();
		if (event.getId() > 0) {
			boolean b = this.updateSingle(event.getValues(), event.getId());
			return b ? event.getId() : -1;		
		}
		else {
			long eventId = insert(event.getValues());
			event.setId(eventId);
			return eventId;
		}
	}
	
	public Event createEvent(Cursor cursor)
	{
		Event event = new Event(cursor);

		return event;
	}
	
	public Event findEventByUniqueCode(String eventType, String code)
	{
		Event event = null;
		
		Cursor cursor = this.queryMany(null, 
				Event.EVENT_TYPE + "=? AND " 
				+ Event.CODE + "=?", 
				new String[]{eventType, code}, null);
		if (cursor == null) {
			return null;
		}
		
		if (cursor.moveToNext()) {
			event = createEvent(cursor);
		}
		cursor.close();
		return event;
	}
	
	public Event createEmptyEvent(String eventType)
	{
		Event e = new Event();
		e.setEventType(eventType);
		e.setTime(System.currentTimeMillis());
		e.setCreateTime(System.currentTimeMillis());
		long eventId = save(e);
		if (eventId < 0) {
			return null;
		}
		e.setId(eventId);
		return e;
	}

	public Event[] findAllUnsynced() {
		
		Cursor cursor = this.queryMany(null, 
				Event.SYNC_STATUS + "=0" , 
				null, null);
		if (cursor == null) {
			return null;
		}
		
		try {
			Event[] events = new Event[cursor.getCount()];
			int i = 0;
			while (cursor.moveToNext()) {
				events[i++] = createEvent(cursor);
			}
			return events;
		}
		finally{
			cursor.close();
		}
	}
	
	public boolean updateSyncStatus(long eventId)
	{
		return this.updateSingleColumn(Event.SYNC_STATUS, 1, eventId)>0;
	}

	public boolean checkExistByGuid(String guid) {
		long count = this.count(Event.GUID+"=?", new String[]{guid});
		return count > 0;
	}
}
