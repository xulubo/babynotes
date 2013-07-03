package cn.year11.babynote.provider.event;

import cn.year11.utils.Log;


public class EventWrapper extends Event {
	protected static Log _logger = Log.getLogger(EventWrapper.class);
	protected Event mEvent = null;

	public EventWrapper(Event e) {
		if (e == null) {
			_logger.e("event can't be null");
		}
		mEvent = e;
	}

	public void setEvent(Event e)
	{
		mEvent  = e;
	}
	
	public void setGuid(String guid) {
		mEvent.setGuid(guid);
	}

	public void setEventType(String type) {
		mEvent.setEventType(type);
	}

	public void setEventDetail(String detail) {
		mEvent.setEventDetail(detail);
	}

	public void setTime(long time) {
		mEvent.setTime(time);
	}

	public void setMemo(String memo) {
		mEvent.setMemo(memo);
	}

	public void setValue(long v) {
		mEvent.setValue(v);
	}

	public void setValue1(long v) {
		mEvent.setValue1(v);
	}

	public void setValue2(long v) {
		mEvent.setValue2(v);
	}

	public void setValue(String v) {
		mEvent.setValue(v);
	}

	public void setValue1(String v) {
		mEvent.setValue1(v);
	}

	public void setValue2(String v) {
		mEvent.setValue2(v);
	}

	public long getId() {
		return mEvent.getId();
	}

	public String getGuid() {
		return mEvent.getGuid();
	}

	public String getEventType() {
		return mEvent.getEventType();
	}

	public String getEventDetail() {
		return mEvent.getEventDetail();
	}

	public long getCreateTime() {
		return mEvent.getCreateTime();
	}

	public long getUpdateTime() {
		return mEvent.getUpdateTime();
	}

	public long getBeginTime() {
		return mEvent.getBeginTime();
	}

	public long getIsActive() {
		return mEvent.getIsActive();
	}

	public long getDueTime() {
		return mEvent.getDueTime();
	}

	public long getCompleteTime() {
		return mEvent.getCompleteTime();
	}

	public String getStatus() {
		return mEvent.getStatus();
	}

	public String getMemo() {
		return mEvent.getMemo();
	}

	public String getValue() {
		return mEvent.getValue();
	}

	public String getValue1() {
		return mEvent.getValue1();
	}

	public String getValue2() {
		return mEvent.getValue2();
	}

	public byte[] getData() {
		return mEvent.getData();
	}

	public String getCode() {
		return mEvent.getCode();
	}

	public void setCode(String code) {
		mEvent.setCode(code);
	}
}
