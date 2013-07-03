package cn.year11.babynote.provider.event;

public class VaccineEvent extends EventWrapper {
	public final static String TYPE = "vaccine";

	public VaccineEvent()
	{
		super(new Event());
	}
	
	public VaccineEvent(Event event)
	{
		super(event);
	}
	
	public void setName(String v)
	{
		setValue(v);
	}
	
	public String getName()
	{
		return getValue();
	}
	
	public void setDetail()
	{
		setEventDetail("∑¿“ﬂ£∫ " + getName());
	}
	
	@Override
	public void beforeSave()
	{
		setEventType("vaccine");
		setDetail();
	}
}
