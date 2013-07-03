package cn.year11.babynote.provider.event;

import cn.year11.babynote.utils.LongUtils;
import android.database.Cursor;
import android.text.TextUtils;

public class GrowthEvent extends Event {
	public final static String TYPE = "growth";

	public GrowthEvent()
	{
		
	}
	public GrowthEvent(Cursor c)
	{
		super(c);
	}
	public void setHeight(long v)
	{
		setValue(v);
	}
	
	public long getHeight()
	{
		return LongUtils.valueOf(getValue());
	}
	public void setWeight(long v)
	{
		setValue1(v);
	}
	public long getWeight()
	{
		return LongUtils.valueOf(getValue1());
	}
	
	
	public void setDetail()
	{
		setEventDetail("身高: " + getHeight() + "厘米, 体重: " + getWeight() + "千克");
	}
	
	@Override
	public void beforeSave()
	{
		setEventType("growth");
		setDetail();
	}
}
