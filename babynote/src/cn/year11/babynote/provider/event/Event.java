package cn.year11.babynote.provider.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.year11.babynote.provider.Attachment;
import cn.year11.babynote.provider.BaseEntity;
import cn.year11.content.CursorGetter;
import cn.year11.content.ValueGetter;
import cn.year11.utils.DateUtils;

import android.content.ContentValues;
import android.database.Cursor;


public class Event implements BaseEntity {
	
	final static public String TABLE_NAME = "event";
	final static public String ID = "_id";
	final static public String GUID = "guid";
	final static public String DATE = "date";
	public static final String TIME = "time";
	
	final static public String EVENT_TYPE = "event_type";
	final static public String EVENT_DETAIL = "event_detail";
	public static final String CREATE_TIME = "create_time";
	public static final String UPDATE_TIME = "update_time";
	public static final String DUE_TIME = "due_time";
	public static final String COMPLETE_TIME = "complete_time";
	public static final String STATUS = "status";
	public static final String MEMO = "memo";
	public static final String IS_ACTIVE = "is_active";
	public static final String DATA = "data";
	public static final String CODE = "code";
	public static final String SYNC_STATUS = "sync_status";

	public static final String VALUE = "value";
	public static final String VALUE1 = "value1";
	public static final String VALUE2 = "value2";

	private long mId;
	private String mGuid;
	private String mEventType;
	private String mEventDetail;
	private String mDate;
	private long mTime;
	private long mCreateTime;
	private long mUpdateTime;
	private long mIsActive;
	private long mDueTime;
	private long mCompleteTime;
	private String mStatus;
	private String mMemo;
	private String mValue;
	private String mValue1;
	private String mValue2;
	private byte[] mData;
	private String mCode;
	private int mSyncStatus = 0;
	private List<Attachment> mAttachments = new ArrayList<Attachment>();
	
	public Event()
	{
		setGuid(makeGuid());
	}
	
	public Event(Cursor c)
	{
		bindData(new CursorGetter(c));
	}
	
	public Event(ValueGetter vg)
	{
		bindData(vg);
	}
	
	private void bindData(ValueGetter vg)
	{
		mId = vg.getLong(Event.ID);
		mGuid = vg.getString(Event.GUID);
		mEventType = vg.getString(Event.EVENT_TYPE);
		mEventDetail = vg.getString(Event.EVENT_DETAIL);
		mCreateTime = vg.getLong(Event.CREATE_TIME);
		mUpdateTime = vg.getLong(Event.UPDATE_TIME);
		mTime = vg.getLong(Event.TIME);
		mIsActive = vg.getLong(Event.IS_ACTIVE);
		mDueTime = vg.getLong(Event.DUE_TIME);
		mCompleteTime = vg.getLong(Event.COMPLETE_TIME);
		mStatus = vg.getString(Event.STATUS);
		mMemo = vg.getString(Event.MEMO);
		mValue = vg.getString(Event.VALUE);
		mValue1 = vg.getString(Event.VALUE1);
		mValue2 = vg.getString(Event.VALUE2);
		mData = vg.getBlob(Event.DATA);
		mCode = vg.getString(Event.CODE);
		mSyncStatus = vg.getInt(Event.SYNC_STATUS);
	}
	
	static public HashMap<String, String> columns()
	{
		HashMap<String, String> columns = new HashMap<String, String>();
		
		columns.put(ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		columns.put(GUID, "VARCHAR(36)");
		columns.put(EVENT_TYPE, "VARCHAR(255)");
		columns.put(EVENT_DETAIL, "TEXT");
		columns.put(CREATE_TIME, "INTEGER");
		columns.put(UPDATE_TIME, "INTEGER");
		columns.put(DATE, "VARCHAR(10)");		
		columns.put(TIME, "INTEGER");
		columns.put(IS_ACTIVE, "INTEGER");
		columns.put(DUE_TIME, "INTEGER");
		columns.put(COMPLETE_TIME, "INTEGER");
		columns.put(STATUS, "VARCHAR(20)");
		columns.put(MEMO, "TEXT");
		columns.put(VALUE, "VARCHAR(50)");
		columns.put(VALUE1, "VARCHAR(50)");
		columns.put(VALUE2, "VARCHAR(50)");
		columns.put(DATA, "BLOB");
		columns.put(CODE, "VARCHAR(50)");
		columns.put(SYNC_STATUS, "INTEGER");

		return columns;
	}

	public void setId(long id)
	{
		mId = id;
	}
	
	
	public void setGuid(String guid) {
		mGuid = guid;
	}
	
	public void setEventType(String type)
	{
		mEventType = type;
	}
	
	public void setEventDetail(String detail)
	{
		mEventDetail = detail;
	}
	
	public void setTime(long time)
	{
		mTime = time;
	}
	
	public void setMemo(String memo)
	{
		mMemo = memo;
	}
	
	public void setValue(long v)
	{
		mValue = String.valueOf(v);
	}
	
	public void setValue1(long v)
	{
		mValue1 = String.valueOf(v);
	}
	
	public void setValue2(long v)
	{
		mValue2 = String.valueOf(v);
	}
	
	public void setValue(String v)
	{
		mValue =v;
	}
	
	public void setValue1(String v)
	{
		mValue1 = v;
	}
	
	public void setValue2(String v)
	{
		mValue2 = v;
	}
	
	// To be invoked before EventDao to save this entity
	public void beforeSave() {
		
	}
	public long getId() {
		return mId;
	}
	public String getGuid() {
		return mGuid;
	}
	public String getEventType() {
		return mEventType;
	}
	public String getEventDetail() {
		return mEventDetail;
	}
	public long getCreateTime() {
		return mCreateTime;
	}
	public long getUpdateTime() {
		return mUpdateTime;
	}
	public long getBeginTime() {
		return mTime;
	}
	public long getIsActive() {
		return mIsActive;
	}
	public long getDueTime() {
		return mDueTime;
	}
	public long getCompleteTime() {
		return mCompleteTime;
	}
	public String getStatus() {
		return mStatus;
	}
	public String getMemo() {
		return mMemo;
	}
	public String getValue() {
		return mValue;
	}
	public String getValue1() {
		return mValue1;
	}
	public String getValue2() {
		return mValue2;
	}
	public byte[] getData() {
		return mData;
	}
	
	public String getCode() {
		return mCode;
	}
	
	public void setCode(String code) {
		mCode = code;
	}
	public void setCreateTime(long t) {
		mCreateTime = t;
	}
	
	public int getSyncStatus()
	{
		return mSyncStatus;
	}
	
	public void setSyncStatus(int n)
	{
		mSyncStatus = n;
	}
	
	protected String makeGuid()
	{
		return "E" + System.currentTimeMillis();
	}
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(Event.GUID, getGuid());
		values.put(Event.EVENT_TYPE, this.getEventType());
		values.put(Event.EVENT_DETAIL, this.getEventDetail());
		values.put(Event.CREATE_TIME, this.getCreateTime());
		values.put(Event.DATE, DateUtils.format("yyyy-MM-dd", new Date(this.getBeginTime())));
		values.put(Event.TIME, this.getBeginTime());
		values.put(Event.DUE_TIME, this.getDueTime());
		values.put(Event.MEMO, this.getMemo());
		values.put(Event.VALUE, this.getValue());
		values.put(Event.VALUE1, this.getValue1());
		values.put(Event.VALUE2, this.getValue2());
		values.put(Event.CODE, this.getCode());
		values.put(Event.SYNC_STATUS, this.getSyncStatus());
		
		return values;
	}
	
	public ContentValues getValues()
	{
		return toContentValues();
	}
	
	public List<Attachment> getAttachments()
	{
		return mAttachments;
	}
	
	public void addAttachment(Attachment att)
	{
		mAttachments.add(att);
	}
}
