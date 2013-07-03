package cn.year11.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import android.os.Environment;


/**
 * Log utility
 */
public final class Log {
	public  static final boolean DBG = false;
	private static final String LOG_FILE_DIR = "/youliao/log";
	private static final String LOG_FILE_NAME = "/youliao.txt";
	private static final String APP_TAG = "YouLiao";
	private static final String LOG_ENTRY_FORMAT = "[%tF %tT][%s]%s";

	public static final String TAG = "year11";

	private static File mLogFile = null;
	private static PrintStream mLogStream;
	private String mClassName = "";

	static {
		init();
	}

	public static Log getLogger(Class<?> c) {
		Log log = new Log();
		log.mClassName = c.getName();
		return log;
	}

	public void d(String msg) {
		Log.d("DBG", mClassName + ":" + msg);
	}

	public void e(String msg) {
		Log.e("ERR", mClassName + ":" + msg);
	}

	public void e(String msg, Throwable tr) {
		Log.e("ERR", mClassName + ":" + msg, tr);
	}

	private static String formatMsg(String tag, String msg) {
		return tag + " - " + msg;
	}

	public static void e(String tag, String msg) {
		android.util.Log.e(APP_TAG, formatMsg(tag, msg));
		write(APP_TAG, formatMsg(tag, msg), null);
	}

	public static void e(String tag, String msg, Throwable tr) {
		android.util.Log.e(APP_TAG, formatMsg(tag, msg), tr);
		write(APP_TAG, formatMsg(tag, msg), tr);
	}

	public static void w(String tag, String msg) {
		android.util.Log.w(APP_TAG, formatMsg(tag, msg));
		write(APP_TAG, formatMsg(tag, msg), null);
	}

	public static void w(String tag, String msg, Throwable tr) {
		android.util.Log.w(APP_TAG, formatMsg(tag, msg), tr);
		write(APP_TAG, formatMsg(tag, msg), tr);
	}

	public static void i(String tag, String msg) {
		android.util.Log.i(APP_TAG, formatMsg(tag, msg));
		write(APP_TAG, formatMsg(tag, msg), null);
	}

	public static void i(String tag, String msg, Throwable tr) {
		android.util.Log.i(APP_TAG, formatMsg(tag, msg), tr);
		write(APP_TAG, formatMsg(tag, msg), tr);
	}

	public static void d(String tag, String msg) {
		android.util.Log.d(APP_TAG, formatMsg(tag, msg));
		write(APP_TAG, formatMsg(tag, msg), null);
	}

	public static void d(String tag, String msg, Throwable tr) {
		android.util.Log.d(APP_TAG, formatMsg(tag, msg), tr);
		write(APP_TAG, formatMsg(tag, msg), tr);
	}

	public static void v(String tag, String msg) {
		android.util.Log.v(APP_TAG, formatMsg(tag, msg));
		write(APP_TAG, formatMsg(tag, msg), null);
	}

	public static void v(String tag, String msg, Throwable tr) {
		android.util.Log.v(APP_TAG, formatMsg(tag, msg), tr);
		write(APP_TAG, formatMsg(tag, msg), tr);
	}

	private static void write(String tag, String msg, Throwable tr) {
		if(!Log.DBG) return;
		try{
			
			if(null == mLogStream || !mLogFile.exists()){
				synchronized(Log.class){
//					if(null == logStream){
						init();
//					}
				}
			}
			
			Date now = new Date();
			if(null != mLogStream){
				mLogStream.printf(LOG_ENTRY_FORMAT, now, now, tag, msg);
				mLogStream.print("\n");
//				logStream.println();
			}
			if (null != tr) {
				tr.printStackTrace(mLogStream);
				if(null != mLogStream){
					mLogStream.print("\n");
//					logStream.println("\n");
				}
			}
		}
		catch(Throwable t){
		}
	}


	
	public static void init() {
		if (!Log.DBG)
			return;
		
		if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			android.util.Log.w(APP_TAG, "SDCARD is not ready");
			return;
		}
		
		try {
			File sdRoot = SystemUtils.getSDRootFile();
			if (sdRoot != null) {
				File logDir = new File(sdRoot.getPath()+LOG_FILE_DIR);
				if(!logDir.exists()){
					logDir.mkdir();
				}
				mLogFile = new File(sdRoot.getPath()+LOG_FILE_DIR, LOG_FILE_NAME);
				android.util.Log.d(APP_TAG,
						formatMsg(TAG, " : Log to file : " + mLogFile));
				mLogStream = new PrintStream(
						new FileOutputStream(mLogFile, true), true);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			super.finalize();
			if (mLogStream != null)
				mLogStream.close();
		} catch (Throwable t) {
		}
	}

}