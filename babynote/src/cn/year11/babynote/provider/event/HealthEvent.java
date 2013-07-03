package cn.year11.babynote.provider.event;

public class HealthEvent extends Event {

	public final static String TYPE = "health";

	
	public void setName(String name)
	{
		setValue(name);
	}
	
	public String getName()
	{
		return getValue();
	}
	
	public void setTemperature(float v)
	{
		setValue1(String.valueOf(v));
	}
	
	public float getTemperature()
	{
		return Float.valueOf(getValue1());
	}
	
	public void setDetail()
	{
		setEventDetail(getName() + " ÌוÎÂ" + getTemperature() + " ¶È");
	}
	
	@Override
	public void beforeSave()
	{
		setEventType("health");
		setDetail();
	}
}
