package cn.year11.babynote.provider.event;

import android.text.TextUtils;

public class DiaperEvent extends Event {
	public final static String TYPE = "diaper";
	
	public void setExcretaName(String name)
	{
		setValue(name);
	}
	
	public String getExcretaName()
	{
		return getValue();
	}
	
	public void setDetail()
	{
		String s = "»»Äò²¼£º " + getExcretaName();
		if (!TextUtils.isEmpty(getMemo())) {
			s += "\r\n" + getMemo();
		}
		setEventDetail(s);
	}
	
	@Override
	public void beforeSave()
	{
		setEventType("diaper");
		setDetail();
	}
}
