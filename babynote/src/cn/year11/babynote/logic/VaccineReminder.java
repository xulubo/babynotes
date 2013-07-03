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
		reminder.setTitle("卡介苗 初种");
		mReminderDao.save(reminder);

		reminder.setEventCode("YGYM_01");
		reminder.setTitle("乙肝疫苗  第一针");
		mReminderDao.save(reminder);

		// 2 month
		calendar = baby.getCalendarOfAge(2);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_01");
		reminder.setTitle("脊髓灰质炎活疫苗 初免");
		mReminderDao.save(reminder);

		// 3 month
		calendar = baby.getCalendarOfAge(3);
		reminder.setEventTime(calendar);
		reminder.setEventCode("KJM_02");
		reminder.setTitle("卡介苗 第二针");
		mReminderDao.save(reminder);

		reminder.setEventCode("YGYM_02");
		reminder.setTitle("乙肝疫苗 第二针");
		mReminderDao.save(reminder);

		reminder.setEventCode("JSHZYHYM_02");
		reminder.setTitle("脊髓灰质炎活疫苗 初免");
		mReminderDao.save(reminder);


		// 4 month
		calendar = baby.getCalendarOfAge(4);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_03");
		reminder.setTitle("脊髓灰质炎活疫苗 初免");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_01");
		reminder.setTitle("白百破 第一次");
		mReminderDao.save(reminder);


		// 5 month
		calendar = baby.getCalendarOfAge(5);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_04");
		reminder.setTitle("脊髓灰质炎活疫苗 第二次");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_02");
		reminder.setTitle("白百破 初免");
		mReminderDao.save(reminder);


		// 6 month
		calendar = baby.getCalendarOfAge(6);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_05");
		reminder.setTitle("脊髓灰质炎活疫苗 初免");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_03");
		reminder.setTitle("白百破 第二次");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_01");
		reminder.setTitle("麻疹活疫苗 初免");
		mReminderDao.save(reminder);


		// 8 month
		calendar = baby.getCalendarOfAge(8);
		reminder.setEventTime(calendar);
		reminder.setEventCode("YGYM_03");
		reminder.setTitle("乙肝疫苗 第三针");
		mReminderDao.save(reminder);

		reminder.setEventCode("JSHZYHYM_06");
		reminder.setTitle("脊髓灰质炎活疫苗 第三次");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_04");
		reminder.setTitle("白百破 初免");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_02");
		reminder.setTitle("麻疹活疫苗 加强");
		mReminderDao.save(reminder);

		reminder.setEventCode("YNYM_01");
		reminder.setTitle("乙脑疫苗 初免两针(间隔7-10天)");
		mReminderDao.save(reminder);


		// 12 month
		calendar = baby.getCalendarOfAge(12);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_07");
		reminder.setTitle("脊髓灰质炎活疫苗 加强");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_05");
		reminder.setTitle("白百破 第三次");
		mReminderDao.save(reminder);

		reminder.setEventCode("YNYM_02");
		reminder.setTitle("乙脑疫苗 加强");
		mReminderDao.save(reminder);


		// 24 month
		calendar = baby.getCalendarOfAge(24);
		reminder.setEventTime(calendar);
		reminder.setEventCode("BBP_06");
		reminder.setTitle("白百破 加强(百破)");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_03");
		reminder.setTitle("麻疹活疫苗 加强");
		mReminderDao.save(reminder);


		// 48 month
		calendar = baby.getCalendarOfAge(24);
		reminder.setEventTime(calendar);
		reminder.setEventCode("JSHZYHYM_08");
		reminder.setTitle("脊髓灰质炎活疫苗 加强");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_04");
		reminder.setTitle("麻疹活疫苗 加强");
		mReminderDao.save(reminder);

		reminder.setEventCode("YNYM_03");
		reminder.setTitle("乙脑疫苗 加强");
		mReminderDao.save(reminder);


		// 84 month
		calendar = baby.getCalendarOfAge(84);
		reminder.setEventTime(calendar);
		reminder.setEventCode("KJM_03");
		reminder.setTitle("复种");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_07");
		reminder.setTitle("白百破 加强(白类)");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_04");
		reminder.setTitle("麻疹活疫苗 加强");
		mReminderDao.save(reminder);

		reminder.setEventCode("YMYM_04");
		reminder.setTitle("加强");
		mReminderDao.save(reminder);


		// 144 month
		calendar = baby.getCalendarOfAge(144);
		reminder.setEventTime(calendar);
		reminder.setEventCode("KJM_03");
		reminder.setTitle("复种(农村)");
		mReminderDao.save(reminder);

		reminder.setEventCode("BBP_08");
		reminder.setTitle("白百破 加强(白类)");
		mReminderDao.save(reminder);

		reminder.setEventCode("MZHY_05");
		reminder.setTitle("麻疹活疫苗 加强");
		mReminderDao.save(reminder);


		// 216 month
		calendar = baby.getCalendarOfAge(216);
		reminder.setEventTime(calendar);
		reminder.setEventCode("BBP_09");
		reminder.setTitle("白百破 加强(白类)");
		mReminderDao.save(reminder);

	}
}
