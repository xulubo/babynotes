package cn.year11.content;

import android.database.Cursor;

public class CursorGetter implements ValueGetter{
	private Cursor mCursor;
	
	public CursorGetter(Cursor c)
	{
		mCursor = c;
	}
	
	public Long getLong(String name)
	{
		return mCursor.getLong(mCursor.getColumnIndex(name));
	}
	
	public Integer getInt(String name)
	{
		return mCursor.getInt(mCursor.getColumnIndex(name));
	}
	
	public String getString(String name)
	{
		return mCursor.getString(mCursor.getColumnIndex(name));
	}

	@Override
	public byte[] getBlob(String name) {
		return mCursor.getBlob(mCursor.getColumnIndex(name));
	}
}
