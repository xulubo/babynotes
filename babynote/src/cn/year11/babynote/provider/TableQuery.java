package cn.year11.babynote.provider;

import java.io.File;
import java.io.IOException;

import cn.year11.utils.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;


public abstract class TableQuery {
	private static final Log _logger = Log.getLogger(TableQuery.class);

	private String mTableName = null;
	
	private SQLiteOpenHelper mDatabaseHelper = null;
	
	public TableQuery(String tableName, SQLiteOpenHelper dbHelper)
	{
		mTableName = tableName;
		mDatabaseHelper = dbHelper;
	}
	
	public long count()
	{
		return count(mTableName);
	}
	
	public long count(String tableName) {
		Cursor cursor = queryMany(tableName, new String[]{"count(*)"}, null, null, null);
		try {
			cursor.moveToNext();
			return cursor.getLong(0);
		}
		finally {
			cursor.close();
		}
	}
	
	public boolean deleteRow(long id)
	{
		return deleteRow(mTableName, id);
	}

	public boolean deleteRow(String table, long rowId)
	{
		return deleteMany(table, "ROWID=" + rowId, null) > 0;
	}
	
	public long deleteMany(String table, String whereClause, String[] whereArgs)
	{
		int affectedRows= mDatabaseHelper.getWritableDatabase().delete(table, whereClause, whereArgs);
		return affectedRows;
	}

	public long deleteMany(String selection, String[] args) {
		return deleteMany(mTableName, selection, args);
	}
	
	public void execSQL(String sql)
	{
		mDatabaseHelper.getWritableDatabase().execSQL(sql);
	}
	
	public void execSQL(String sql, Object[] bindArgs)
	{
		mDatabaseHelper.getWritableDatabase().execSQL(sql, bindArgs);
	}
	
	public Uri getRowUri(long rowId)
	{
		return getRowUri(mTableName, rowId);
	}
	
	public Uri getRowUri(String tableName, long rowId)
	{
//		return Uri.parse("content://"+PeConstants.AUTH+"/"+tableName+"/"+rowId);
		return null;
	}
	

	
	public int getSingleIntValue(String table, String column, long id)
	{
		Cursor cursor = querySingle(table, new String[]{column}, id);
		if (cursor == null) {
			return 0;
		}
		try {
			if (cursor.moveToNext()) {
				return cursor.getInt(cursor.getColumnIndexOrThrow(column));
			}
		}
		finally {
			cursor.close();
		}
		
		return 0;
	}
	
	public String getSingleStringValue(String columnName, long id)
	{
		return getSingleStringValue(mTableName, columnName, id);
	}
	
	public String getSingleStringValue(String table, String column, long id)
	{
		Cursor cursor = querySingle(table, new String[]{column}, id);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToNext()) {
				return cursor.getString(cursor.getColumnIndexOrThrow(column));
			}
		}
		finally {
			cursor.close();
		}
		
		return null;
	}
	
	public String getTableName()
	{
		return mTableName;
	}
	
	
	public long insertNew(ContentValues values) {
		return insertNew(mTableName, values);
	}
	
	public long insertNew(String table, ContentValues values)
	{
		long rowId = mDatabaseHelper.getWritableDatabase().insert(table, "", values);
		
		return rowId;
	}
	

	
	public Cursor queryAll()
	{
		return queryMany(mTableName, null, null, null, null);
	}
	
	//得到指定rowId的记录
	public Cursor querySingle(String table, String[]columns, long rowId)
	{
		return groupQuery(table, columns, "ROWID="+rowId, null, null, null, null);
	}

	// 没有GroupBy的query
	public Cursor queryMany(String table, String[] columns, String selection, String[] selectionArgs
			,String orderBy) 
	{
		return groupQuery(table, columns, selection, selectionArgs, null, null, orderBy);
	}
	
	// 支持GroupBy操作的query
	public Cursor groupQuery(String table, String[] columns, String selection, String[] selectionArgs
			, String groupBy, String having, String orderBy) 
	{
		Cursor cursor = mDatabaseHelper.getReadableDatabase()
		.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		
		return cursor;
	}
	
	public Cursor querySingle(String[] columns, long id)
	{
		return querySingle(mTableName, columns, id);
	}

	//----standard database operations
	public Cursor queryMany(String[] columns, String where, String[]whereArgs, String orderBy)
	{
		return queryMany(mTableName, columns, where, whereArgs, orderBy);
	}
	
	public Cursor rawQuery(String sql, String[] selectionArgs) 
	{
		return mDatabaseHelper.getReadableDatabase().rawQuery(sql, selectionArgs);
	}
	
	
	public boolean updateSingle(ContentValues values, long id)
	{
		return updateSingle(mTableName, values, id)>0;
	}

	public long updateMany(ContentValues values, String selection, String[] args) {
		return updateMany(mTableName, values, selection, args);
	}
	
	public long updateSingle(String table, ContentValues values, long rowId)
	{
		return updateMany(table, values, "ROWID=" + rowId, null);
	}
	

	public long updateMany(String table, ContentValues values, String whereClause, String[] whereArgs) 
	{
		int affectedRows = mDatabaseHelper.getWritableDatabase().update(table, values, whereClause, whereArgs);

		return affectedRows;
	}
	
	public long updateSingleColumn(String name, long value, long rowId)
	{
		ContentValues values = new ContentValues();
		values.put(name, value);
		return updateSingle(mTableName, values, rowId);
	}
	
	public long updateSingleColumn(String name, String value, long rowId)
	{
		ContentValues values = new ContentValues();
		values.put(name, value);
		return updateSingle(mTableName, values, rowId);
	}
	
	public long updateSingleValue(String name, long value, long rowId)
	{
		ContentValues values = new ContentValues();
		values.put(name, value);
		return updateSingle(mTableName, values, rowId);
	}

	public long updateSingleValue(String name, String value, long rowId)
	{
		ContentValues values = new ContentValues();
		values.put(name, value);
		return updateSingle(mTableName, values, rowId);
	}
	
	/**
	 * Helper function to create a new empty file in the specific location.
	 * 
	 * @param path
	 *            The file path and name.
	 * @throws IllegalStateException
	 */
	public static final void createNewFile(String path)
			throws IllegalStateException {

		File partFile = new File(path);
		if (!partFile.exists()) {
			try {
				if (!partFile.createNewFile()) {
					throw new IllegalStateException(
							"Unable to create new partFile: " + path);
				}
			} catch (IOException e) {
				if (Log.DBG)
					_logger.e("createNewFile", e);
				throw new IllegalStateException(
						"Unable to create new partFile: " + path);
			}
		}
	}
	
	/**
	 * Delete specified file
	 */
	public static boolean deleteFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}

		File partFile = new File(path);
		if (partFile.exists()) {
			return partFile.delete();
		} else {
			return false;
		}
	}
}