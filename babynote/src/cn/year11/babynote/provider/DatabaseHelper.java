package cn.year11.babynote.provider;

import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.year11.babynote.app.NotePad.Notes;
import cn.year11.babynote.logic.BackupManager;
import cn.year11.babynote.provider.event.Event;

/**
 * This class helps open, create, and upgrade the database file.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "NotePadProvider";

    public static final String DATABASE_NAME = "babynote.db";
    public static final int DATABASE_VERSION = 4;
    public static final String NOTES_TABLE_NAME = "notes";
    private static DatabaseHelper mInstance = null;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " ("
                + Notes._ID + " INTEGER PRIMARY KEY,"
                + Notes.TITLE + " TEXT,"
                + Notes.NOTE + " TEXT,"
                + Notes.CREATED_DATE + " INTEGER,"
                + Notes.MODIFIED_DATE + " INTEGER"
                + ");");
        
        db.execSQL(Knowledges.createSQL());
        db.execSQL(makeCreateTableSQL(Event.TABLE_NAME, Event.columns()));
        db.execSQL(makeCreateTableSQL(Profile.TABLE_NAME, Profile.columns()));
        db.execSQL(makeCreateTableSQL(Reminder.TABLE_NAME, Reminder.columns()));
		db.execSQL(makeCreateTableSQL(Attachment.TABLE_NAME, Attachment.columns()));
        onUpgrade(db, 2, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	switch (oldVersion) {
    	}

    }

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		return super.getReadableDatabase();
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		return super.getWritableDatabase();
	}
    
    public static DatabaseHelper getInstance(Context c)
    {
    	if (mInstance == null) {
    		mInstance = new DatabaseHelper(c);
    	}
    	
    	return mInstance;
    }
    
    static public String makeCreateTableSQL(String name, Map<String, String> columns)
    {
      StringBuffer sql = new StringBuffer();
      sql.append("CREATE TABLE " + name + "(");
      int i=0;
      for (Object key : columns.keySet().toArray())
      {
    	  if (i++ > 0) 
    		  sql.append(",");

    	  sql.append(key + " " + (String)columns.get(key));
      }
      sql.append(")");
      
      return sql.toString();
    }
    
    	
	public long insert (String table, String nullColumnHack, ContentValues values) 

	{
		long ret = getWritableDatabase().insert(table, nullColumnHack, values);
		if (ret > 0) {
			BackupManager.scheduleRegularBackup();
		}
		return ret;
	}
	
	public long update (String table, ContentValues values, String whereClause, String[] whereArgs) 
	{
		long ret = getWritableDatabase().update(table, values, whereClause, whereArgs);
		
		return ret;
	}
	
	public long delete (String table, String whereClause, String[] whereArgs) 
	{
		long ret = getWritableDatabase().delete(table, whereClause, whereArgs);
		return ret;
	}
	
	void execSQL(String sql, Object[] bindArgs)
	{
		getWritableDatabase().execSQL(sql, bindArgs);
	}
}