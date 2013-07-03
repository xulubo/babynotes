package cn.year11.babynote.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.year11.babynote.utils.BabyUtils;
import cn.year11.content.ValueGetter;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Profile implements BaseEntity {
	public final static String TABLE_NAME = "profile";
	final static String ID = "_id";
	final static String NAME = "name";
	final static String GENDER = "gender";
	final static String BIRTHDAY = "birthday";
	final static String HOSPITAL = "hospital";
	final static String HEIGHT = "height";
	final static String WEIGHT = "weight";
	final static String PORTRAIT = "portrait";
	final static String CREATE_TIME = "create_time";	// 创建记录的时间
	final static String SYNC_TIME = "sync_time";		// 与邮件服务器同步的时间
	final static String UPDATE_TIME = "update_time";   	// 更新数据的时间
	final static String REMOTE_UID = "remote_uid";		// 保存在邮件服务器上的ID
	final static String GUID = "guid";		// 保存在邮件服务器上的ID

	long _id;
	long birthday;
	long createTime;
	long updateTime;
	long syncTime;
	int height;
	int weight; //单位 克
	byte[] portrait;
	String hospital;
	String name;
	String gender;
	String remoteUid;
	String guid;
	
	public Profile(ValueGetter value)
	{
		this._id = value.getLong(ID);
		this.name = value.getString(NAME);
		this.gender = value.getString(GENDER);
		this.birthday = value.getLong(BIRTHDAY);
		this.hospital = value.getString(HOSPITAL);
		this.height = value.getInt(HEIGHT);
		this.weight = value.getInt(WEIGHT);
		this.portrait = value.getBlob(PORTRAIT);
		this.createTime = value.getLong(CREATE_TIME);
		this.updateTime = value.getLong(UPDATE_TIME);
		this.remoteUid = value.getString(REMOTE_UID);
		this.syncTime = value.getLong(SYNC_TIME);
		this.guid = value.getString(GUID);
	}
	
	public Profile() {
		this.guid = makeGuid();
	}

	public Drawable getPortraitDrawable()
	{
		if (portrait == null) {
			return null;
		}
		
		return Drawable.createFromStream(new ByteArrayInputStream(portrait), null);		
	}
	
	public void setPortrait(Bitmap bitmap)
	{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, os);
		this.portrait = os.toByteArray();
	}
	
	public void setPortrait(byte[] bytes)
	{
		this.portrait = bytes;
	}
	
	
	static public HashMap<String, String> columns()
	{
		HashMap<String, String> columns = new HashMap<String, String>();
		
		columns.put(ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		columns.put(NAME, "VARCHAR(36)");
		columns.put(GENDER, "VARCHAR(10)");
		columns.put(BIRTHDAY, "INTEGER");
		columns.put(HOSPITAL, "VARCHAR(255)");
		columns.put(HEIGHT, "INTEGER");
		columns.put(WEIGHT, "INTEGER");		
		columns.put(PORTRAIT, "BLOB");
		columns.put(CREATE_TIME, "INTEGER");
		columns.put(UPDATE_TIME, "INTEGER");
		columns.put(SYNC_TIME, "INTEGER");
		columns.put(REMOTE_UID, "VARCHAR(255)");
		columns.put(GUID, "VARCHAR(255)");

		return columns;
	}

	public long getId()
	{
		return _id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public byte[] getPortrait() {
		return portrait;
	}

	public void setPortrait(Drawable portrait) {

		if (portrait == null) {
			return;
		}
		
		BitmapDrawable  bd = (BitmapDrawable)portrait;

		Bitmap  b = bd.getBitmap();	
		setPortrait(b);
	}
	
	public Calendar getCalendarOfAge(int month)
	{
		return BabyUtils.getCalendarOfAge(month, new Date(this.birthday));
	}
	
	public long getCreateTime()
	{
		return createTime;
	}
	
	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}

	public String getRemoteUid() {
		return remoteUid;
	}

	public void setRemoteUid(String remoteUid) {
		this.remoteUid = remoteUid;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	protected String makeGuid()
	{
		return "P" + System.currentTimeMillis();
	}
	
	public ContentValues getValues()
	{
		Profile profile = this;
		ContentValues values = new ContentValues();
		values.put(Profile.NAME, profile.getName());
		values.put(Profile.GENDER, profile.getGender());
		values.put(Profile.BIRTHDAY, profile.getBirthday());
		values.put(Profile.HEIGHT, profile.getHeight());
		values.put(Profile.WEIGHT, profile.getWeight());
		values.put(Profile.HOSPITAL, profile.getHospital());
		values.put(Profile.PORTRAIT, profile.getPortrait());
		values.put(Profile.CREATE_TIME, profile.getCreateTime());
		values.put(Profile.UPDATE_TIME, profile.getUpdateTime());
		values.put(Profile.SYNC_TIME, profile.getSyncTime());
		values.put(Profile.REMOTE_UID, profile.getRemoteUid());
		values.put(Profile.GUID, profile.getGuid());
		return values;
	}
	
	public String toString()
	{
		return getName();
	}
	
	public boolean isSyncRequired()
	{
		return updateTime > syncTime;
	}
}
