package cn.year11.babynote.logic;

import java.util.Calendar;

import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.ProfileDao;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.provider.ReminderDao;
import cn.year11.utils.Log;
import android.content.Context;

public class VaccineReminder {

	static private Log _logger = Log.getLogger(VaccineReminder.class);
	static private VaccineReminder _instance;
	private Context mContext;
	ProfileDao mProfileDao;
	ReminderDao mReminderDao;

	static public VaccineReminder getInstance(Context context) {
		if (_instance == null) {
			_instance = new VaccineReminder(context);
		}

		return _instance;
	}

	public VaccineReminder(Context context) {
		mContext = context;
		mProfileDao = ProfileDao.getInstance();
		mReminderDao = ReminderDao.getInstance();
	}

	public void removeReminderForKid(long babyId)
	{
		mReminderDao.deleteMany(Reminder.BABY_ID+"=?", new String[]{
				String.valueOf(babyId)
		});
	}
	
	public void addReminderForKid(long babyId) {
		Calendar calendar;
		Reminder reminder = new Reminder();
		reminder.setCategory("vaccine");
		reminder.setDataSource(Reminder.DATA_SOURCE_SYSTEM);
		reminder.setBabyId(babyId);

		Profile baby = mProfileDao.getProfile();
		if (baby == null) {
			_logger.e("can't find baby profile");
			return;
		}
		
		// 1 month
		calendar = baby.getCalendarOfAge(1);
		reminder.setEventTime(calendar);
		reminder.setEventCode("KJM_01");
		reminder.setTitle("������ ����");
		mReminderDao.save(reminder);

		reminder.setEventCode("YGYM_01");
		reminder.setTitle("�Ҹ�����  ��һ��");
		mReminderDao.save(reminder);

		// 2 month
		calendar = baby.getCalendarOfAge(2);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_01");
		reminder.setTitle("��������׻����� ����");
		mReminderDao.save(reminder);

		// 3 month
		calendar = baby.getCalendarOfAge(3);
		reminder.setEventTime(calendar);
		reminder.setEventCode("KJM_02");
		reminder.setTitle("������ �ڶ���");
		mReminderDao.save(reminder);

		reminder.setEventCode("YGYM_02");
		reminder.setTitle("�Ҹ����� �ڶ���");
		mReminderDao.save(reminder);

		reminder.setEventCode("JSHZYHYM_02");
		reminder.setTitle("��������׻����� ����");
		mReminderDao.save(reminder);


		// 4 month
		calendar = baby.getCalendarOfAge(4);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_03");
		reminder.setTitle("��������׻����� ����");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_01");
		reminder.setTitle("�װ��� ��һ��");
		mReminderDao.save(reminder);


		// 5 month
		calendar = baby.getCalendarOfAge(5);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_04");
		reminder.setTitle("��������׻����� �ڶ���");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_02");
		reminder.setTitle("�װ��� ����");
		mReminderDao.save(reminder);


		// 6 month
		calendar = baby.getCalendarOfAge(6);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_05");
		reminder.setTitle("��������׻����� ����");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_03");
		reminder.setTitle("�װ��� �ڶ���");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_01");
		reminder.setTitle("��������� ����");
		mReminderDao.save(reminder);


		// 8 month
		calendar = baby.getCalendarOfAge(8);
		reminder.setEventTime(calendar);
		reminder.setEventCode("YGYM_03");
		reminder.setTitle("�Ҹ����� ������");
		mReminderDao.save(reminder);

		reminder.setEventCode("JSHZYHYM_06");
		reminder.setTitle("��������׻����� ������");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_04");
		reminder.setTitle("�װ��� ����");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_02");
		reminder.setTitle("��������� ��ǿ");
		mReminderDao.save(reminder);

		reminder.setEventCode("YNYM_01");
		reminder.setTitle("�������� ��������(���7-10��)");
		mReminderDao.save(reminder);


		// 12 month
		calendar = baby.getCalendarOfAge(12);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_07");
		reminder.setTitle("��������׻����� ��ǿ");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_05");
		reminder.setTitle("�װ��� ������");
		mReminderDao.save(reminder);

		reminder.setEventCode("YNYM_02");
		reminder.setTitle("�������� ��ǿ");
		mReminderDao.save(reminder);


		// 24 month
		calendar = baby.getCalendarOfAge(24);
		reminder.setEventTime(calendar);
		reminder.setEventCode("BBP_06");
		reminder.setTitle("�װ��� ��ǿ(����)");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_03");
		reminder.setTitle("��������� ��ǿ");
		mReminderDao.save(reminder);


		// 48 month
		calendar = baby.getCalendarOfAge(24);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_08");
		reminder.setTitle("��������׻����� ��ǿ");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_04");
		reminder.setTitle("��������� ��ǿ");
		mReminderDao.save(reminder);

		reminder.setEventCode("YNYM_03");
		reminder.setTitle("�������� ��ǿ");
		mReminderDao.save(reminder);


		// 84 month
		calendar = baby.getCalendarOfAge(84);
		reminder.setEventTime(calendar);
		reminder.setEventCode("KJM_03");
		reminder.setTitle("����");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_07");
		reminder.setTitle("�װ��� ��ǿ(����)");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_04");
		reminder.setTitle("��������� ��ǿ");
		mReminderDao.save(reminder);

		reminder.setEventCode("YMYM_04");
		reminder.setTitle("��ǿ");
		mReminderDao.save(reminder);


		// 144 month
		calendar = baby.getCalendarOfAge(144);
		reminder.setEventTime(calendar);
		reminder.setEventCode("KJM_03");
		reminder.setTitle("����(ũ��)");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_08");
		reminder.setTitle("�װ��� ��ǿ(����)");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_05");
		reminder.setTitle("��������� ��ǿ");
		mReminderDao.save(reminder);


		// 216 month
		calendar = baby.getCalendarOfAge(216);
		reminder.setEventTime(calendar);
		reminder.setEventCode("BBP_09");
		reminder.setTitle("�װ��� ��ǿ(����)");
		mReminderDao.save(reminder);

	}
}
