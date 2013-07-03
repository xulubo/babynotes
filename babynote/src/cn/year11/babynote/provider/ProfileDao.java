package cn.year11.babynote.provider;

import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.provider.event.Event;
import cn.year11.content.CursorGetter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileDao extends BaseDao {

	static private ProfileDao _instance;
	
	public static ProfileDao getInstance()
	{
		if (_instance == null) {
			_instance = new ProfileDao();
		}
		
		return _instance;
	}
	
	public ProfileDao() {
		super(Profile.TABLE_NAME);
	}

	public Profile getProfile()
	{
		Cursor cursor = queryAll();
		if (cursor == null) {
			return null;
		}
		
		try {
			if (cursor.moveToNext()){
				Profile profile = new Profile(new CursorGetter(cursor));
				return profile;
			}
		}
		finally {
			cursor.close();
		}
		return null;
	}
	
	public Profile getProfileByName(String name)
	{
		Cursor cursor = this.queryMany(null, Profile.NAME+"=?", new String[]{name}, null);
		if (cursor == null) {
			return null;
		}
		
		try {
			if (cursor.moveToNext()){
				Profile profile = new Profile(new CursorGetter(cursor));
				return profile;
			}
		}
		finally {
			cursor.close();
		}
		return null;
	}
	
	public long save(Profile profile)
	{
		profile.setUpdateTime(System.currentTimeMillis());
		ContentValues values = profile.getValues();
		return insert(values);
	}
	
	public void update(long id, Profile profile)
	{
		if (profile.getUpdateTime() == 0)
			profile.setUpdateTime(System.currentTimeMillis());
		ContentValues values = profile.getValues();
		this.updateSingle(values, id);
	}

	// return rows successfully updated
	public long updateByName(Profile profile)
	{
		if (profile.getUpdateTime() == 0)
			profile.setUpdateTime(System.currentTimeMillis());
		ContentValues values = profile.getValues();
		long ret = this.updateMany(values, Profile.NAME + "=?", new String[]{profile.getName()});
		return ret;
	}
	
	public void updateSyncTime(long profileId) {
		ContentValues values = new ContentValues();
		values.put(Profile.SYNC_TIME, System.currentTimeMillis());
		this.updateSingle(values, profileId);
	}
	
	public void updateRemoteUid(long profileId, String uid) {
		ContentValues values = new ContentValues();
		values.put(Profile.REMOTE_UID, uid);
		this.updateSingle(values, profileId);
	}
	
	public boolean checkExistByName(String name) {
		long count = this.count(Profile.NAME+"=?", new String[]{name});
		return count > 0;
	}
}
