package cn.year11.babynote.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import cn.year11.babynote.R;
import cn.year11.babynote.app.ReminderEditActivity;
import cn.year11.babynote.logic.AlarmPrefs;
import cn.year11.babynote.logic.ReminderManager;
import cn.year11.babynote.provider.TimerInfo;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.utils.ActivityUtils;

public class SingleAlarmView extends FrameLayout implements Checkable {

	private boolean mIsEnabled = false;
	private static final int[] CHECKED_STATE_SET = {
		android.R.attr.state_checked
	};
	
	protected TextView mTitle;
	protected TextView mLiveTime;
	protected TextView mTimeLabel;
	protected ImageView mIcon;
	protected ImageView mStop;
	protected ImageView mClose;
	protected ImageView mResume;
	protected FrameLayout mOperationPannel;
	protected CheckableLinearLayout mTimerPanel;
	protected ImageView mAddAlarmImage;
	protected TimerInfo mTimerInfo = new TimerInfo();
	protected int mPanelId = 0;
	
	protected Timer mTimer;
	int i=0;
	
	public enum AlarmType {
		COUNT_DOWN,
		ON_TIME
	}
	
	private Handler mHandler = new Handler(){  
		  
        public void handleMessage(Message msg) {  
            switch (msg.what) {      
            case 1: 
                 if (mTimerInfo.current <= 0) {
                	 onSuspend();
                	 setChecked(false);
                 }
                 else {
                	 mTimerInfo.decrease();
                	 mLiveTime.setText(formatTime((int)(mTimerInfo.current)));
                 }
                break;      
             }      
            super.handleMessage(msg);  
         }  
          
     };  
     
	public SingleAlarmView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean isChecked() {
		return mIsEnabled;
	}

	@Override
	public void setChecked(boolean checked) {
		mIsEnabled = checked;
		mTimerPanel.setChecked(checked);
	}

	@Override
	public void toggle() {
		setChecked(!mIsEnabled);
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace+1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		
		return drawableState;
	}

	@Override
	public boolean performClick() {
		toggle();
		if (mIsEnabled) {
			onStart();
		}
		else {
			onSuspend();
		}
		
		return super.performClick();
	}

	@Override
	public boolean performLongClick() {
		ActivityUtils.startActivity(getContext(), ReminderEditActivity.class);
		return super.performLongClick();
	}

	// 
	public void onStart()
	{		
		mOperationPannel.setVisibility(View.GONE);

		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
		}
		mTimer = new Timer();
		
		TimerTask task = new TimerTask()
		{

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			}
			
		};
		mTimer.schedule(task, 0, 1000);
		setChecked(true);
		this.refreshDrawableState();
		
		mTimerInfo.resume();
		mTimerInfo.title = mTitle.getText().toString();
		mTimerPanel.setVisibility(View.VISIBLE);

		AlarmPrefs.save(mPanelId, mTimerInfo);
		ReminderManager.getInstance().reschedule();
	}
	
	public void stopTimer()
	{
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
	}
	
	public void onStop()
	{
		stopTimer();
		mTimerInfo.reset();
		updateTime();
		mOperationPannel.setVisibility(View.GONE);
		setChecked(false);
		//mTimerPanel.refreshDrawableState();
		//mTimerPanel.invalidate();
	}
	
	public void onSuspend()
	{
		stopTimer();
		mOperationPannel.setVisibility(View.VISIBLE);
	}
	
	public void onResume()
	{
		onStart();
	}
	
	public void onClose()
	{
		stopTimer();
		mOperationPannel.setVisibility(View.GONE);
		mTimerPanel.setVisibility(View.GONE);
		mAddAlarmImage.setVisibility(View.VISIBLE);
	}
	

	@Override
	protected void onFinishInflate() {
		mTitle = (TextView) findViewById(R.id.title);
		mTimeLabel = (TextView) findViewById(R.id.time_label);
		mLiveTime = (TextView) findViewById(R.id.live_time_label);
		mStop = (ImageView) findViewById(R.id.stop);
		mResume = (ImageView)findViewById(R.id.pause);
		mClose = (ImageView)findViewById(R.id.close);
		mOperationPannel = (FrameLayout)findViewById(R.id.cover);
		mTimerPanel = (CheckableLinearLayout) findViewById(R.id.timer_panel);
		mIcon = (ImageView)findViewById(R.id.icon);
		mAddAlarmImage = (ImageView)findViewById(R.id.set_alarm);
		
		mStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onStop();

			}
		});
		
		mResume.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onResume();
			}
		});	
		
		mClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClose();
			}
		});	
		
		mAddAlarmImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOnSelectAlarmListener != null) {
					mOnSelectAlarmListener.onSelect();
				}
			}
		});
		
		recover();

		super.onFinishInflate();
	}
	
	public void recover()
	{
    	TimerInfo info = AlarmPrefs.get(mPanelId);
    	if (info != null) {
    		mTimerInfo = info;

    		mTitle.setText(info.title);
    		
    		if (info.status == TimerInfo.RUNNING) {
    			onStart();
    		}
    		if (info.status == TimerInfo.SUSPEND) {
    			onSuspend();
    		}
    	}
	}
	
	protected String formatTime(int t)
	{
		int h = t/3600;
		int m = (t%3600) / 60;
		int s = (t%60);
		if (h>0) {
			return String.format("%02d:%02d:%02d", h, m, s);
		}
		else if (m>0) {
			return String.format("%02d:%02d", m, s);
		}
		else {
			return String.format("%02d", s);
		}
	}
	
	public void setTitle(String title)
	{
		mTitle.setText(title);
	}
	public String getTitile()
	{
		return mTitle.getText().toString();
	}
	
	public void setCountDownTime(int seconds)
	{
		mTimerInfo.start(seconds);
		mTimerInfo.reset();

		updateTime();
		mAddAlarmImage.setVisibility(View.GONE);
		mTimerPanel.setVisibility(View.VISIBLE);
	}
	
	public long getCountDownTime()
	{
		return mTimerInfo.remaining;
	}
	
	public void setIcon(int resId)
	{
		mIcon.setImageResource(resId);
	}
	
	public void setAlarmType(AlarmType t)
	{
	}
	
	public void updateTime()
	{
        mLiveTime.setText(formatTime((int)(mTimerInfo.current)));
	}
	
	public interface OnSelectAlarmListener {
		public void onSelect();
	}
	
	public void setOnSelectAlarmListener(OnSelectAlarmListener l)
	{
		mOnSelectAlarmListener = l;
	}
	
	private OnSelectAlarmListener mOnSelectAlarmListener = null;
	

}
