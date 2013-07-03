package cn.year11.babynote.app;

import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import android.app.Activity;

public abstract class BaseActivity extends Activity {

	protected Activity mThisActivity;
	protected DatabaseHelper mDatabaseHelper;
	
	final public EventDao getEventDao()
	{
		return EventDao.getInstance(); 
	}
	
	public BaseActivity()
	{
		mThisActivity = this;
		mDatabaseHelper = DatabaseHelper.getInstance(this);
	}
}
