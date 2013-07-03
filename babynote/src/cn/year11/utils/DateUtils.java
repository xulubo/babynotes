package cn.year11.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	static public String format(String fmt, Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		return format.format(date);
	}
}
