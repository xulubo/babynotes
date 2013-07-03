package cn.year11.babynote.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BabyUtils {

	// return months since giving birth
	public static int getMonthAge(Date curDate, Date birthday)
	{
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(birthday);
		
		int birth_year = calendar.get(Calendar.YEAR);
		int birth_month = calendar.get(Calendar.MONTH);
		
		calendar.setTime(curDate);
		
		int cur_year = calendar.get(Calendar.YEAR);
		int cur_month = calendar.get(Calendar.MONTH);
		
		int months = (cur_year-birth_year) * 12 + (cur_month-birth_month);
		
		return months;
	}
	
	// get the Calendar object at when the baby is age of "month"
	public static Calendar getCalendarOfAge(int ageMonths, Date birthday)
	{
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(birthday);
		
		int birth_year = calendar.get(Calendar.YEAR);
		int birth_month = calendar.get(Calendar.MONTH)+1;
		
		int year = birth_year + (ageMonths+birth_month)/12;
		int month = (ageMonths+birth_month)%12 - 1;
		calendar.set(Calendar.YEAR,  year);
		calendar.set(Calendar.MONTH, month);
		
		return calendar;
	}
}
