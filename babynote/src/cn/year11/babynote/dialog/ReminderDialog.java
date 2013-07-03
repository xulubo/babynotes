package cn.year11.babynote.dialog;

import java.util.Date;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TextView;
import cn.year11.babynote.R;
import cn.year11.babynote.app.ReminderEditActivity;
import cn.year11.babynote.logic.ReminderManager;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.provider.ReminderDao;
import cn.year11.babynote.provider.event.VaccineEvent;
import cn.year11.babynote.utils.ActivityUtils;
import cn.year11.babynote.utils.DateUtils;

public class ReminderDialog extends BaseDialog {
	
	final String[] mMedicines = new String[] {"美林", "泰诺" };
	EditText mTitle, mMemo;
	TextView mDate, mTime;
	Reminder mReminder;
	ReminderDao mReminderDao;

	public ReminderDialog(Context c)
	{
		super(c, R.layout.dialog_reminder_view);
		mReminderDao  = ReminderDao.getInstance();
	}
	
	public ReminderDialog(Context c, Reminder reminder)
	{
		super(c, R.layout.dialog_reminder_view);
		mReminder = reminder;
		mReminderDao  = ReminderDao.getInstance();
		
		setButton("接种完成", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				saveEvent();
				if (mConfirmListener != null)
				mConfirmListener.onConfirm();
			}

		});
		
		setButton(BUTTON_NEUTRAL , "编辑", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ActivityUtils.startActivity(getContext(), ReminderEditActivity.class);
//				saveReminder();
			}
		});
	}
	
	protected long getTime()
	{
		return DateUtils.parseDate(mDate.getText().toString(), mTime.getText().toString()).getTime();

	}
	
	@Override
	protected void setupView()
	{		
		mTitle = (EditText)findViewById(R.id.title);
		mDate = (TextView)findViewById(R.id.event_date);
		mTime = (TextView)findViewById(R.id.event_time);
		mMemo = (EditText)findViewById(R.id.event_memo);
	}
	
	@Override
	protected void bindData()
	{
		if (mReminder != null) {
			mTitle.setText(mReminder.getTitle());
			mMemo.setText(mReminder.getMemo());
			mDate.setText(DateUtils.formatDate(mReminder.getEventTime()));
			mTime.setText(DateUtils.formatTime(mReminder.getEventTime()));
			DateUtils.attachDateDialog(getContext(), mDate, new Date(mReminder.getEventTime()));
			DateUtils.attachTimeDialog(getContext(), mTime, new Date(mReminder.getEventTime()));
		}
		else {
			DateUtils.attachDateDialog(getContext(), mDate, null);
			DateUtils.attachTimeDialog(getContext(), mTime, null);		
		}
	}

	@Override
	protected void onSave()
	{
	}
	
	protected void saveEvent()
	{
		VaccineEvent event = new VaccineEvent();
		event.setTime(DateUtils.parseDate(mDate.getText().toString(), mTime.getText().toString()).getTime());
		event.setName(mTitle.getText().toString());
		event.setMemo(mMemo.getText().toString());
		event.setCode(mReminder.getEventCode());
		EventDao.getInstance().save(event);
	}
	
	protected void saveReminder()
	{
		if (mReminder == null) {
			mReminder = new Reminder();
		}
		mReminder.setEventTime(DateUtils.parseDate(mDate.getText().toString(), mTime.getText().toString()).getTime());
		mReminder.setTitle(mTitle.getText().toString());
		mReminder.setMemo(mMemo.getText().toString());
		mReminderDao.saveOrUpdate(mReminder);
		
		ReminderManager.getInstance().reschedule();
	}
}
