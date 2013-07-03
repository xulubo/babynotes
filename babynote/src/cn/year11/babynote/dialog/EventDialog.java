package cn.year11.babynote.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.year11.babynote.R;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.utils.DateUtils;

public abstract class EventDialog extends BaseDialog {
	protected TextView mDate;
	protected TextView mTime;
	protected EditText mEventMemo;
	protected Event mEvent;

	public EventDialog(Context context, int layoutId)
	{
		super(context, layoutId);
	}
	
	public EventDialog(Context context, int layoutId, Event e) {
		super(context, layoutId);
		if (e != null) {
			mEvent = e;
		}
	}
	
	public void setupView() {

		mEventMemo = (EditText)findViewById(R.id.event_memo);
		
		mDate = (TextView)findViewById(R.id.event_date);
		mTime = (TextView)findViewById(R.id.event_time);
		
		setupView(mView);
	}

	public long getTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String s = mDate.getText().toString() + " " + mTime.getText().toString();
			Date date;
			try {
				date = dateFormat.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}
			long t = date.getTime();
			return t;

	}
	public abstract void setupView(View v);
	protected abstract void onSave();	
	
	public EventDao getDao()
	{
		EventDao dao = EventDao.getInstance();
		return dao;
	}
	
	public Event getEvent()
	{
		return mEvent;
	}
	
	public void setEvent(Event e)
	{
		mEvent = e;
	}

	public void bindData()
	{
		
	}
	
	@Override
	public void show() {
		long time;

		if (mEvent == null) {
			 time = System.currentTimeMillis();
		}
		else {
			time = mEvent.getBeginTime();
			mEventMemo.setText(mEvent.getMemo());
		}
		
		mDate.setText(DateUtils.formatDate(time));
		mTime.setText(DateUtils.formatTime(time));

		DateUtils.attachDateDialog(getContext(), mDate, new Date(time));
		DateUtils.attachTimeDialog(getContext(), mTime, new Date(time));
		bindData();
		super.show();
	}
	
}
