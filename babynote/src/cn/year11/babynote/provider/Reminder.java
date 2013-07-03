package cn.year11.babynote.provider;

import java.util.Calendar;
import java.util.HashMap;

import android.database.Cursor;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.utils.DateUtils;

public class Reminder {

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
	static public final String BABY_ID = "babyId";
	static public final String CREATE_TIME = "create_time";
	static public final String UPDATE_TIME = "update_time";
	
	static public final int ACTIVE = 0;
	static public final int SUSPEND = 1;
	static public final int EXPIRED = 2;
	static public final int FINISHED = 3;
	
	static public final String DATA_SOURCE_SYSTEM = "system";
	static public final String DATA_SOURCE_USER = "user";
	
	private long _id;
	private long eventTime;
	private long alarmTime = -3 * DateUtils.DAY_MILLS; // 发出通知的时间, 缺省提前3天
	private long createTime;
	private long updateTime;
	private String eventNote;  // 事件说明
	private String dataSource = DATA_SOURCE_USER; // 程类, 系统预制, 还是用户自定义
	private String category; // 分类, 疫苗, 拍照...
	private String eventCode; // 事件代码, 用于标识一个预定义事件
	private long status = ACTIVE; // 事件状态, active, suspend, expired, finished
	private String title;
	private String memo;
	private long babyId;
	
	
	
	static public HashMap<String, String> columns()
	{
		HashMap<String, String> columns = new HashMap<String, String>();
		
		columns.put(ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		columns.put(EVENT_TIME, "INTEGER");
		columns.put(ALARM_TIME, "INTEGER");
		columns.put(DATA_SOURCE, "VARCHAR(50)");
		columns.put(TITLE, "VARCHAR(255)");
		columns.put(MEMO, "TEXT");
		columns.put(EVENT_CODE, "VARCHAR(10)");		
		columns.put(STATUS, "INTEGER");
		columns.put(CATEGORY, "VARCHAR(50)");
		columns.put(BABY_ID, "INTEGER");
		columns.put(CREATE_TIME, "INTEGER");
		columns.put(UPDATE_TIME, "INTEGER");

		return columns;
	}

	public Reminder()
	{
		createTime = System.currentTimeMillis();
	}
	
	public Reminder(Cursor c)
	{
		_id = c.getLong(c.getColumnIndex(Event.ID));
		eventTime = c.getLong(c.getColumnIndex(EVENT_TIME));
		alarmTime = c.getLong(c.getColumnIndexOrThrow(ALARM_TIME));
		createTime = c.getLong(c.getColumnIndexOrThrow(CREATE_TIME));
		updateTime = c.getLong(c.getColumnIndexOrThrow(UPDATE_TIME));
		dataSource = c.getString(c.getColumnIndexOrThrow(DATA_SOURCE));
		title = c.getString(c.getColumnIndexOrThrow(TITLE));
		memo = c.getString(c.getColumnIndexOrThrow(MEMO));
		eventCode = c.getString(c.getColumnIndexOrThrow(EVENT_CODE));
		status = c.getLong(c.getColumnIndexOrThrow(STATUS));
		category = c.getString(c.getColumnIndexOrThrow(CATEGORY));
		babyId = c.getLong(c.getColumnIndexOrThrow(BABY_ID));

	}
	
	public long getId()
	{
		return _id;
	}
	
	public void setId(long id) {
		_id = id;
	}
	
	public long getEventTime() {
		return eventTime;
	}


	public void setEventTime(Calendar c)
	{
		this.eventTime = c.getTimeInMillis();
	}
	
	public void setEventTime(long event_time) {
		this.eventTime = event_time;
	}



	public long getAlarmTime() {
		if (alarmTime < 0) {
			return getEventTime() + alarmTime;
		}
		
		return alarmTime;
	}



	public void setAlarmTime(long alarm_time) {
		this.alarmTime = alarm_time;
	}



	public String getEventNote() {
		return eventNote;
	}



	public void setEventNote(String event_note) {
		this.eventNote = event_note;
	}



	public String getSource() {
		return dataSource;
	}



	public void setSource(String source) {
		this.dataSource = source;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}



	public String getEventCode() {
		return eventCode;
	}



	public void setEventCode(String event_code) {
		this.eventCode = event_code;
	}



	public long getStatus() {
		return status;
	}



	public void setStatus(long status) {
		this.status = status;
	}


	public String getDataSource() {
		return dataSource;
	}


	public void setDataSource(String data_source) {
		this.dataSource = data_source;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getMemo() {
		return memo;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}

	public long getBabyId() {
		return babyId;
	}

	public void setBabyId(long babyId) {
		this.babyId = babyId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
