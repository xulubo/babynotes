package cn.year11.babynote.provider;

public class EventManager {
	public static void delete(long eventId)
	{
		EventDao.getInstance().deleteRow(eventId);
		AttachmentDao.getInstance().deleteByEventId(eventId);
	}
}
