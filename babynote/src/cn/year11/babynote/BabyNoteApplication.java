package cn.year11.babynote;

import cn.year11.utils.Log;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;


public class BabyNoteApplication extends Application {
	private final static Log _logger = Log.getLogger(BabyNoteApplication.class);
	private final static long CWJ_HEAP_SIZE = 6 * 1024 * 1024;
	private final static float TARGET_HEAP_UTILIZATION = 0.75f;
	/**
	 * For get the application context
	 */
	private static Context mContext;
	
	public BabyNoteApplication(){
		//优化虚拟机内存堆大小解决BitMap过大问题
		//VMRuntime.getRuntime().setMinimumHeapSize(CWJ_HEAP_SIZE);
	    //VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);
		mContext = this;
	}
	
	public static boolean isDebuggable()
	{
		ApplicationInfo info = mContext.getApplicationInfo();
		if ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0) {
			return false;
		}
		return true;
	}
	

	public static final String getVersion() {
		PackageInfo pkgInfo;
		try {
			pkgInfo = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0);
			return pkgInfo.versionName;
		} catch (NameNotFoundException e) {
			_logger.e("", e);
		}
		return null;
	}
	
	public static Context getContext()
	{
		return mContext;
	}
}
