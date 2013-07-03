package cn.year11.babynote.utils;

import java.util.Calendar;
import java.util.Date;

public class DueDateHelper
{
  public static final int INVALID_TOO_FAR_IN_FUTURE = 1;
  public static final int INVALID_TOO_FAR_IN_PAST = 2;
  static final int LMP_DAYS_FUTURE_SLACK = 45;
  static final int LMP_DAYS_UNTIL_BIRTH = 280;
  static final int MAX_DAYS_POST_PARTUM = 13;
  static final int MAX_DAYS_PREGNANT = 270;
 // public static final int VALID_DUE_DATE;
  private Date today;

  static
  {
    if (!DueDateHelper.class.desiredAssertionStatus());
  }

  public Date calculateDueDate(Date paramDate)
  {
    assert (paramDate != null);
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate);
    localCalendar.add(6, 280);
    return localCalendar.getTime();
  }

  public Calendar getCalendarNoTime(Calendar paramCalendar)
  {
    paramCalendar.set(11, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);
    return paramCalendar;
  }

  public Calendar getCurrentCalendarNoTime()
  {
    Calendar localCalendar = Calendar.getInstance();
    if (this.today != null)
      localCalendar.setTime(this.today);
    return getCalendarNoTime(localCalendar);
  }

  public Date getCurrentDateNoTime()
  {
    return getCurrentCalendarNoTime().getTime();
  }

  public Date getToday()
  {
    return this.today;
  }

  public void setToday(Date paramDate)
  {
    this.today = paramDate;
  }

  public int validateDueDate(Date paramDate, boolean paramBoolean)
  {
    Calendar localCalendar1 = getCurrentCalendarNoTime();
    localCalendar1.add(6, 270);
    int i;
    if (paramDate.after(localCalendar1.getTime()))
      i = 1;

      Calendar localCalendar2 = getCurrentCalendarNoTime();
      localCalendar2.add(5, -13);
      if (paramDate.before(localCalendar2.getTime()))
      {
        i = 2;
      }
      else {
      i = 0;
      }

    return i;

  }
}

/* Location:           C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     com.babycenter.pregnancytracker.util.DueDateHelper
 * JD-Core Version:    0.6.0
 */