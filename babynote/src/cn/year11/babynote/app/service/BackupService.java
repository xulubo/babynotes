package cn.year11.babynote.app.service;

import cn.year11.babynote.logic.SettingManager;
import cn.year11.babynote.provider.email.ImapMessageStore;
import cn.year11.babynote.provider.email.ImapMessageStore.AuthenticationErrorException;
import cn.year11.utils.Log;
import cn.year11.utils.SystemUtils;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

public class BackupService extends Service {
	static private Log _logger = Log.getLogger(BackupService.class);
	static private boolean _isRunning = false;
	static public String LAST_BACKUP_STATE = "last_backup_state";
	
	static public final long SUCCESS = 0;
	static public final long FAILED = 1;
	static public final long WAIT_FOR_WIFI = 2;


	
	/**
	 * A wakelock held while this service is working.
	 */
	private static WakeLock _wakeLock;

	/**
	 * A wifilock held while this service is working.
	 */
	private static WifiLock _wifiLock;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private static void acquireWakeLock(Context ctx) {
		if (_wakeLock == null) {
			PowerManager pMgr = (PowerManager) ctx
					.getSystemService(POWER_SERVICE);
			_wakeLock = pMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					"SyncService.sync() wakelock.");

			WifiManager wMgr = (WifiManager) ctx.getSystemService(WIFI_SERVICE);
			_wifiLock = wMgr.createWifiLock("BabyNote Backup");
		}
		_wakeLock.acquire();
		_wifiLock.acquire();
	}

	private static void releaseWakeLock(Context ctx) {
		_wakeLock.release();
		_wifiLock.release();
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (SettingManager.backupViaWifi()) {
			if (!SystemUtils.isWifiConnected(this)) {
				_logger.d("wifi is not connected, backup is cancelled, waiting for wifi");
				SettingManager.set(LAST_BACKUP_STATE, WAIT_FOR_WIFI);
				return;
			}
		}
		
		synchronized (this.getClass()) {
			if (_isRunning) {
				return;
			}
			acquireWakeLock(this);
			_isRunning = true;
			
			Thread thread = new Thread() {

				@Override
				public void run() {
					super.run();
					try {
                        // Lower thread priority a little. We're not the UI.
                        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    					ImapMessageStore store = new ImapMessageStore();
                        store.backup();
                        SettingManager.set(LAST_BACKUP_STATE, SUCCESS);
					} catch (AuthenticationErrorException e) {
						_logger.e("failed to backup", e);
                        SettingManager.set(LAST_BACKUP_STATE, FAILED);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
                        SettingManager.set(LAST_BACKUP_STATE, FAILED);
					}
					finally {
                        _isRunning = false;
                        releaseWakeLock(BackupService.this);
					}
				}

			};
			
			thread.start();
		}

	}
}
