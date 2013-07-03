package cn.year11.babynote.provider;

import java.io.File;
import java.io.IOException;

import cn.year11.babynote.BabyNoteApplication;
import cn.year11.utils.Log;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;


public abstract class BaseDao {
	private static final Log _logger = Log.getLogger(BaseDao.class);

	private String mTableName = null;
	
	private DatabaseHelper mDatabaseHelper = null;
	private Context mContext;
	
	public BaseDao(String tableName)
	{
		mContext = BabyNoteApplication.getContext();
		mDatabaseHelper = DatabaseHelper.getInstance(BabyNoteApplication.getContext());
		mTableName = tableName;
	}
	

	
	public long count(String tableName, String whereClause, String[]whereArgs)
	{
		Cursor cursor = queryMany(tableName, new String[]{"count(*)"}, whereClause, whereArgs, null);
		try {
			cursor.moveToNext();
			return cursor.getLong(0);
		}
		finally {
			cursor.close();
		}
	}
	
	public long count(String whereClause, String[]whereArgs)
	{
		Cursor cursor = queryMany(mTableName, new String[]{"count(*)"}, whereClause, whereArgs, null);
		try {
			cursor.moveToNext();
			return cursor.getLong(0);
		}
		finally {
			cursor.close();
		}
	}
	
	public long count()
	{
		return count(mTableName);
	}
	
	public long count(String tableName) {
		return count(tableName, null, null);
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
		long affectedRows= mDatabaseHelper.delete(table, whereClause, whereArgs);
		mContext.getContentResolver().notifyChange(getTableUri(table), null);
		return affectedRows;
	}

	public long deleteMany(String selection, String[] args) {
		return deleteMany(mTableName, selection, args);
	}
	
	public void execSQL(String sql)
	{
		execSQL(sql, null);
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
	
	
	public long insert(ContentValues values) {
		return insert(mTableName, values);
	}
	
	public long insert(String table, ContentValues values)
	{
		long rowId = mDatabaseHelper.insert(table, "", values);
		if (mContext != null) {
			mContext = BabyNoteApplication.getContext();
			ContentResolver contentResolver = mContext.getContentResolver();
			contentResolver.notifyChange(getTableUri(table), null);
		}
		return rowId;
	}
	

	
	public Cursor queryAll()
	{
		return queryMany(mTableName, null, null, null, null);
	}
	
	//得到指定rowId的记录
	public Cursor querySingle(String table, String[]columns, long rowId)
	{
		return groupQuery(false, table, columns, "ROWID="+rowId, null, null, null, null);
	}

	// 没有GroupBy的query
	public Cursor queryMany(String table, String[] columns, String selection, String[] selectionArgs
			,String orderBy) 
	{
		return groupQuery(false, table, columns, selection, selectionArgs, null, null, orderBy);
	}
	
	// 没有GroupBy的query
	public Cursor query(boolean distinct, String[] columns, String selection, String[] selectionArgs
			,String orderBy) 
	{
		return groupQuery(distinct, mTableName, columns, selection, selectionArgs, null, null, orderBy);
	}
	
	// 支持GroupBy操作的query
	public Cursor groupQuery(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs
			, String groupBy, String having, String orderBy) 
	{
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		
		try {
			Cursor cursor = db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
		
			cursor.setNotificationUri(mContext.getContentResolver(), getTableUri(table));
			return cursor;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public Uri getTableUri(String tableName)
	{
		return Uri.parse("content://babynote/"+tableName + "/");
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
		long affectedRows = mDatabaseHelper.update(table, values, whereClause, whereArgs);

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