package cn.year11.babynote.app;

import java.util.Date;

import cn.year11.babynote.R;
import cn.year11.babynote.logic.ReminderManager;
import cn.year11.babynote.provider.ProfileDao;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.provider.ReminderDao;
import cn.year11.babynote.utils.DateUtils;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReminderEditActivity extends BaseActivity {

	private EditText mTitle;
	private EditText mMemo;
	private TextView mDate;
	private TextView mTime;
	private Reminder mReminder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder_edit);
		
		Intent intent = getIntent();
		long reminderId = intent.getLongExtra(Reminder.ID, -1);
		if (reminderId >= 0) {
			mReminder = ReminderDao.getInstance().getReminder(reminderId);
		}
		
		setupView();
	}

	protected void setupView()
	{
		mTitle = (EditText)findViewById(R.id.title);
		mMemo = (EditText)findViewById(R.id.memo);
		mDate = (TextView)findViewById(R.id.event_date);
		mTime = (TextView)findViewById(R.id.event_time);

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (saveReminder())
					finish();
			}
		});
		
		bindData();
	}
	protected void bindData()
	{
		if (mReminder != null) {
			mTitle.setText(mReminder.getTitle());
			mMemo.setText(mReminder.getMemo());
			mDate.setText(DateUtils.formatDate(mReminder.getEventTime()));
			mTime.setText(DateUtils.formatTime(mReminder.getEventTime()));
			DateUtils.attachDateDialog(mThisActivity, mDate, new Date(mReminder.getEventTime()));
			DateUtils.attachTimeDialog(mThisActivity, mTime, new Date(mReminder.getEventTime()));
		}
		else {
			long time = System.currentTimeMillis();
			mDate.setText(DateUtils.formatDate(time));
			mTime.setText(DateUtils.formatTime(time));
			DateUtils.attachDateDialog(mThisActivity, mDate, null);
			DateUtils.attachTimeDialog(mThisActivity, mTime, null);		
		}
	}
	
	protected long getTime()
	{
		return DateUtils.parseDate(mDate.getText().toString(), mTime.getText().toString()).getTime();

	}
	
	protected boolean validate()
	{
		if (TextUtils.isEmpty(mTitle.getText().toString())) {
			Toast.makeText(mThisActivity, "请输入标题", 1).show();
			return false;
		}
		
		if (getTime() < System.currentTimeMillis()) {
			Toast.makeText(mThisActivity, "您不能输入一个历史时间", 1).show();
			return false;
		}
		
		return true;
	}
	
	protected boolean saveReminder()
	{
		if (!validate()) {
			return false;
		}
		
		if (mReminder == null) {
			mReminder = new Reminder();
		}
		mReminder.setTitle(mTitle.getText().toString());
		mReminder.setMemo(mMemo.getText().toString());
		mReminder.setEventTime(getTime());
		
		ReminderDao.getInstance().saveOrUpdate(mReminder);
		ReminderManager.getInstance().reschedule();
		return true;
	}
}
