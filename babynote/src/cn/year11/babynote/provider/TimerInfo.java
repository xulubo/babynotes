package cn.year11.babynote.provider;

import android.graphics.drawable.Drawable;

public class TimerInfo {
	public Drawable icon;
	public String title;		// 计时标题
	public int  mode;			// 0 count_up, 1 count down
	public long stopTime;		// 计时的到期时间
	public long current;		// 当前已完成时间
	public long startTime;		// 计时的开始时间
	public long duration;		// total duration
	public long status;		// running, pause, stop
	public long remaining;		// remaining seconds
	public int templateId;		// TimerTemplate ID
	
	enum AlarmType  {
		COUNT_DOWN,		// 
		COUNT_UP,
		ON_TIME			// alarm at specific time
	}

	
	final static public int STOP = 0;
	final static public int RUNNING = 1;
	final static public int SUSPEND = 2;
	
	public TimerInfo()
	{
		
	}
	
	public void decrease() {
		if (current == 0) {
			current = duration;
		}
		current--;
	}

	public void increate()
	{
		current++;
	}
	
	public void reset() {
		current = duration;
	}
	
	public void initialize(long seconds)
	{
		this.startTime = System.currentTimeMillis();
		this.stopTime = this.startTime + seconds * 1000L;
		this.duration = seconds;
		this.remaining = seconds;
		this.current = 0;
	}
	
	public void start(long seconds)
	{
		initialize(seconds);
		this.status = RUNNING;
	}
	
	public void restart()
	{
		this.startTime = System.currentTimeMillis();
		this.stopTime = this.startTime + this.duration * 1000L;
		this.remaining = this.duration;
		this.status = RUNNING;
		this.current = 0;
	}
	
	public void resume()
	{
		this.stopTime = System.currentTimeMillis() + this.remaining;
		this.status = RUNNING;
	}
	
	public void suspend()
	{
		this.status = SUSPEND;
	}
	
	public void stop()
	{
		this.status = STOP;
	}
}
