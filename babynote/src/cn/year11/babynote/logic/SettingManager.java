package cn.year11.babynote.logic;

import cn.year11.babynote.BabyNoteApplication;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class SettingManager {
	static public final String LOGIN_PASSWORD = "login_password";
	static public final String LOGIN_USERNAME = "login_user";
	static public final String MAILBOX_PROVIDER = "mailbox_provider";
	static public final String ENABLE_AUTO_BACKUP = "enable_auto_backup";
	static public final String BACKUP_VIA_WIFI = "backup_via_wifi";
	
    static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(BabyNoteApplication.getContext());
    }
    
    public static boolean set(String key, String value)
    {
    	Editor editor = getSharedPreferences().edit();
    	editor.putString(key, value);
    	return editor.commit();
    }
    
    public static boolean set(String key, long value)
    {
    	Editor editor = getSharedPreferences().edit();
    	editor.putLong(key, value);
    	return editor.commit();
    }
    
    public static String getString(String key)
    {
    	return getSharedPreferences().getString(key, null);
    }
    
    public static long getLong(String key)
    {
    	return getSharedPreferences().getLong(key, 0L);
    }
    
	public static String getUserName()
	{
		return getSharedPreferences().getString(LOGIN_USERNAME, null);
	}
	
	public static String getPassword()
	{
		return getSharedPreferences().getString(LOGIN_PASSWORD, null);
	}
	
	public static String getMailboxProvider()
	{
		return getSharedPreferences().getString(MAILBOX_PROVIDER, null);
	}
	
	public static boolean isAutoBackupEnabled()
	{
		if (!isSettingGood()) {
			return false;
		}
		return getSharedPreferences().getBoolean(ENABLE_AUTO_BACKUP, false);
	}	
	
	public static boolean backupViaWifi()
	{
		return getSharedPreferences().getBoolean(BACKUP_VIA_WIFI, false);
	}	
	
	
	public static String getMailbox()
	{
		return "\"Robert Xu\" <xulubo@gmail.com>";
	}
	
	public static boolean isSettingGood()
	{
		if (TextUtils.isEmpty(getUserName())) {
			return false;
		}
		
		if (TextUtils.isEmpty(getPassword())) {
			return false;
		}
		
		if (TextUtils.isEmpty(getMailboxProvider())) {
			return false;
		}
		
		return true;
	}
}
