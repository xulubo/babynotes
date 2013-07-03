package cn.year11.babynote.logic;

import cn.year11.babynote.BabyNoteApplication;
import cn.year11.babynote.app.service.BackupService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class BackupManager {
	static private int DEFAULT_TIMEOUT_SECONDS = 5;

	public static boolean isSyncRequired()
	{
		// if isAutoBackupEnabled() && currentTime - lastSyncTime > backupInterval then backup
		// if lastBackupResult == false then backup
		if (SettingManager.getLong(BackupService.LAST_BACKUP_STATE) != BackupService.SUCCESS)
			return true;
		return false;
	}
	
    public static void check() {
    	if (isSyncRequired()) {
    		scheduleBackup(BabyNoteApplication.getContext(), 1);
    	}
    }
    
    public static void scheduleRegularBackup() {
        scheduleBackup(BabyNoteApplication.getContext(), DEFAULT_TIMEOUT_SECONDS);
    }
    
    public static void cancel(Context ctx) {
        AlarmManager aMgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        aMgr.cancel(createPendingIntent(ctx));
    }
    
    private static void scheduleBackup(Context ctx, int seconds) {
        long atTime = System.currentTimeMillis() + seconds * 1000l;
        PendingIntent pi = createPendingIntent(ctx);
        AlarmManager aMgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        aMgr.set(AlarmManager.RTC_WAKEUP, atTime, pi);
    }
    
    private static PendingIntent createPendingIntent(Context ctx) {
        Intent serviceIntent = new Intent(ctx, BackupService.class);
        return PendingIntent.getService(ctx, 0, serviceIntent, 0);
    }
    
}
