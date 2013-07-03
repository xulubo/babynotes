package cn.year11.babynote.provider;

import android.content.ContentValues;
import android.database.Cursor;
import cn.year11.babynote.BabyNoteApplication;
import cn.year11.content.CursorGetter;

public class AttachmentDao extends BaseDao {
	private static AttachmentDao _instance;
	
	public static AttachmentDao getInstance()
	{
		if (_instance == null) {
			_instance = new AttachmentDao();
		}
		
		return _instance;
	}
	
	public AttachmentDao()
	{
		super(Attachment.TABLE_NAME);
	}
	
	public Attachment[] findByEventId(long eventId)
	{
		Cursor cursor = this.queryMany(null, Attachment.EVENT_ID+"=?", new String[]{
				String.valueOf(eventId)
		}, Attachment.CREATE_TIME);
		
		if (cursor == null) {
			return null;
		}
		
		try {
			Attachment[] atts = null;
			if (cursor.getCount()>0) {
				atts = new Attachment[cursor.getCount()];
				for(int i=0; cursor.moveToNext(); i++) {
					atts[i] = new Attachment(new CursorGetter(cursor));
				}
			}
			return atts;
		}
		finally {
			cursor.close();
		}
	}
	
	public long saveOrUpdate(Attachment att)
	{
		ContentValues values = att.getValues();
		if (att.getId() > 0) {
			boolean b = this.updateSingle(values, att.getId());
			return b ? att.getId() : -1;
		}
		else {
			return insert(values);
		}
	}

	public Cursor findAllByContentType(String contentTypeImage) {
		Cursor cursor = this.queryMany(null, Attachment.CONTENT_TYPE +"=?", new String[]{
				contentTypeImage
		}, Attachment.CREATE_TIME);
		
		return cursor;
	}

	public long deleteByEventId(long eventId) {
		return this.deleteMany(Attachment.EVENT_ID+"=?", new String[]{Long.toString(eventId)});
	}
}
