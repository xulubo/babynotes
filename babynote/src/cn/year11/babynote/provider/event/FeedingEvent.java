package cn.year11.babynote.provider.event;

import cn.year11.babynote.utils.LongUtils;
import android.text.TextUtils;

public class FeedingEvent extends Event {
	public final static String TYPE = "feeding";

	public void setFeedingTime(long time)
	{
		setTime(time);
	}
	
	public void setDrinkAmount(long v)
	{
		setValue1(v);
	}
	
	public long getDrinkAmount()
	{
		return LongUtils.valueOf(getValue1());
	}
	
	public void setEatAmount(long v)
	{
		setValue2(v);
		
	}
	
	public long getEatAmount()
	{
		return LongUtils.valueOf(getValue2());
	}
	
	public void setFoodName(String name)
	{
		setValue(name);
	}
	
	public String getFoodName()
	{
		return getValue();
	}
	
	public void setDetail()
	{
		String s = "Î¹Ñø£º " + getFoodName() + " ";
		s += getDrinkAmount() + " ºÁÉý";
		
		s += getEatAmount() + " ¿Ë";
		setEventDetail(s);
	}
	
	@Override
	public void beforeSave()
	{
		setEventType("feeding");
		setDetail();
	}
}
