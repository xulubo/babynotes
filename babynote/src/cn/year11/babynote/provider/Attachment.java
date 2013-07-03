package cn.year11.babynote.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.R;
import cn.year11.babynote.utils.ImageUtils;
import cn.year11.content.ValueGetter;
import cn.year11.utils.SystemUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class Attachment implements BaseEntity {
	static public final String TABLE_NAME = "attachment";
	static public final String ID = "_id";
	static public final String CREATE_TIME = "create_time";
	static public final String EVENT_ID = "event_id";
	static public final String CONTENT_TYPE = "content_type";
	static public final String CONTENT_PATH = "path";
	static public final String THUMBNAIL = "thumbnail";
	static public final String SIZE = "size";
	
	static public final String CONTENT_TYPE_IMAGE = "image";
	static public final String CONTENT_TYPE_VOICE = "voice";

	private long _id;
	private long createTime;
	private long eventId = -1;
	private long size;
	private String contentPath;
	private String contentType;
	private byte[] thumbNail;
	
	public Attachment(ValueGetter val)
	{
		this._id = val.getLong(ID);
		this.contentPath = val.getString(CONTENT_PATH); 
		this.contentType = val.getString(CONTENT_TYPE);
		this.eventId = val.getLong(EVENT_ID);
		this.createTime = val.getLong(CREATE_TIME);
		this.thumbNail = val.getBlob(THUMBNAIL);
		this.size = val.getLong(SIZE);
	}
	
	public Attachment(Bitmap thumbnail, String imagePath)
	{
		if (thumbnail != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			thumbnail.compress(CompressFormat.PNG, 100, os);
			this.thumbNail = os.toByteArray();
		}
		
		setContentPath(imagePath);
		setContentType(CONTENT_TYPE_IMAGE);
	}
	
	public Attachment(String contentType, String contentPath)
	{
		this.contentType = contentType;
		this.contentPath = contentPath;
	}
	
	static public HashMap<String, String> columns()
	{
		HashMap<String, String> columns = new HashMap<String, String>();
		
		columns.put(ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		columns.put(CONTENT_PATH, "VARCHAR(36)");
		columns.put(CREATE_TIME, "INTEGER");
		columns.put(CONTENT_TYPE, "VARCHAR(255)");
		columns.put(EVENT_ID, "INTEGER");
		columns.put(THUMBNAIL, "BLOB");
		columns.put(SIZE, "INTEGER");

		return columns;
	}

	public long getId() {
		return _id;
	}

	public void setId(long _id) {
		this._id = _id;
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
		if (this.size == 0 && this.contentPath != null) {
			File f = new File(this.contentPath);
			this.size = f.length();
		}
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public byte[] getThumbNail() {
		return thumbNail;
	}

	public long getSize() {
		return size;
	}
	
	public void setThumbNail(byte[] thumbNail) {
		this.thumbNail = thumbNail;
	}
	
	public Drawable getThumbnailDrawable()
	{
		if (thumbNail != null) {
			return Drawable.createFromStream(new ByteArrayInputStream(thumbNail), null);		
		}
		
		if (this.contentType.equals(CONTENT_TYPE_IMAGE) && !TextUtils.isEmpty(this.contentPath))
		{
			File f = new File(this.contentPath);
			try {
				Bitmap bitmap = ImageUtils.decodeBitmapFile(this.contentPath, 4);
				return new android.graphics.drawable.BitmapDrawable(BabyNoteApplication.getContext().getResources(), bitmap);
				//FileInputStream s = new FileInputStream(f);
				//Drawable d = Drawable.createFromStream(s, null);	
				//return ImageUtils.createScaledDrawable(d, 100, 100);
			} catch(Exception e) {
				return null;
			}
			
		}
		else if (this.contentType.equals(CONTENT_TYPE_VOICE)) {
			return BabyNoteApplication.getContext().getResources().getDrawable(R.drawable.sound);
		}
		
		return null;
	}
	
	public boolean isImage()
	{
		return getContentType().equals(CONTENT_TYPE_IMAGE);
	}
	
	public boolean isVoice()
	{
		return getContentType().equals(CONTENT_TYPE_VOICE);
	}
	
	public ContentValues getValues()
	{
		ContentValues values = new ContentValues();
		values.put(Attachment.CONTENT_PATH, this.getContentPath());
		values.put(Attachment.THUMBNAIL, this.getThumbNail());
		if (this.getCreateTime() == 0) {
			values.put(Attachment.CREATE_TIME, System.currentTimeMillis());
		}
		else {
			values.put(Attachment.CREATE_TIME, this.getCreateTime());
		}
		values.put(Attachment.CONTENT_TYPE, this.getContentType());
		values.put(Attachment.EVENT_ID, this.getEventId());
		return values;
	}
}
