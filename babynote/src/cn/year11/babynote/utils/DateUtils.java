package cn.year11.babynote.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class DateUtils {
	public final static long MINUTE_MILLS = 60L * 1000l;
	public final static long HOUR_MILLS = 3600L * 1000L;
	public final static long DAY_MILLS = 24L * HOUR_MILLS;
	
	private final static String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	private final static String TIME_FORMAT_PATTERN = "HH:mm";
	private final static String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";

	static public Date parseDate(String date)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseDate(String mDate, String mTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
		
		try {
			return dateFormat.parse(mDate + " " + mTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}	
	}

	public static boolean isCurrentDay(String day) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		if (dateFormat.format(new Date()).equals(day)) {
			return true;
		}
		return false;
	}
	
	public static String formatDate(long time)
	{
		return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(new Date(time));
	}
	
	public static String formatTime(long time)
	{
		return new SimpleDateFormat(TIME_FORMAT_PATTERN).format(new Date(time));
	}
	
	public static String formatDateTime(long time)
	{
		return new SimpleDateFormat(DATETIME_FORMAT_PATTERN).format(new Date(time));
	}
	
	public static void attachDateDialog(final Context context, final TextView textView, final Date defaultDate)
	{
		textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar d = Calendar.getInstance(Locale.CHINA);
				Date myDate = defaultDate == null ? new Date() : defaultDate;
				d.setTime(myDate);

				int year=d.get(Calendar.YEAR);
				int month=d.get(Calendar.MONTH);
				int day=d.get(Calendar.DAY_OF_MONTH); 
				
				DatePickerDialog dlg = new DatePickerDialog(context, 
						new OnDateSetListener(){

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								textView.setText(""+year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
							}}, year, month, day);
				dlg.show();
			}
			
		});
	}
	
	public static void attachTimeDialog(final Context context, final TextView textView, final Date defaultTime)
	{
		textView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance(Locale.CHINA);
				Date myDate = defaultTime == null ? new Date() : defaultTime;
				calendar.setTime(myDate);
				int hour=calendar.get(Calendar.HOUR_OF_DAY);
				int min=calendar.get(Calendar.MINUTE);
				
				new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						textView.setText(hourOfDay + ":" + minute);
						
					}
				   }, hour, min, true).show();				
			}
			
		});
	}
}
