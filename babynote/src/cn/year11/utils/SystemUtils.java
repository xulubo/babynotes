package cn.year11.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import cn.year11.babynote.BabyNoteApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;

public class SystemUtils {
	private static final Log _logger = Log.getLogger(SystemUtils.class);

	/**
	 * Get the SD card's root file.
	 * 
	 * @return
	 */
	public static File getSDRootFile() {
		if (isSdCardReady()) {
			return Environment.getExternalStorageDirectory();
		} else {
			return null;
		}
	}

	public static final boolean isSdCardReady() {
		String filename = new Date().getTime() + ".tmp";
		try {
			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录

			File saveFile = new File(sdCardDir, filename);
			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write("you can delete the file".getBytes());
			outStream.close();
			return true;

		} catch (Exception e) {
			_logger.e("can't find SD card", e);
			return false;
		} finally {
			try {
				File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
				File saveFile = new File(sdCardDir, filename);
				if (saveFile.exists()) {
					saveFile.delete();
				}
			} catch (Exception e) {
				_logger.e("unknown", e);
			}
		}

	}

	static public boolean isWifiConnected(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// wifi
		NetworkInfo wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (wifi.getState() == State.CONNECTED)
			return true;

		return false;
		
		
		// startActivity(new
		// Intent(Settings.ACTION_WIRELESS_SETTINGS));//进入无线网络配置界面
		// startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		// //进入手机中的wifi网络设置界面

	}
	
	static public boolean is3GConnected(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// mobile 3G Data Network
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		
		// 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
		if (mobile == State.CONNECTED)
			return true;
		
		return false;
	}
	
	static public int getWifiSignalStrength(Context context)
	{
        WifiManager mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo=mWifiManager.getConnectionInfo();
        return mWifiInfo.getRssi();
	}
}
