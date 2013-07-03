package cn.year11.babynote.provider.event;

import android.database.Cursor;
import android.text.Html;

public class DosingEvent extends Event {
	public static String TYPE = "dosing";
	
	public DosingEvent()
	{
		
	}
	public DosingEvent(Cursor c)
	{
		super(c);
	}
	
	public void setMedicineName(String name)
	{
		setValue(name);
	}
	
	public String getMedicineName()
	{
		return getValue();
	}
	
	public void setMedicineAmount(String amount)
	{
		setValue1(amount);
	}

	public void setMedicineUnit(String unit)
	{
		setValue2(unit);
	}
	
	public void setMedicineTime(long time)
	{
		setTime(time);
	}
	
	public void setDetail()
	{
		setEventDetail("<b>·þÓÃ:</b> <a href=\"http://drugs.dxy.cn/drug/85118.htm\">" 
				+ getMedicineName() + "</a> " + getValue1() + "ºÁÉý");
	}
	
	@Override
	public void beforeSave()
	{
		setEventType("dosing");
		setDetail();
	}
}
