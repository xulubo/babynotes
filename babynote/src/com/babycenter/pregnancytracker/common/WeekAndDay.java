package com.babycenter.pregnancytracker.common;

public class WeekAndDay
{
  private final Day day;
  private final Week week;

  public WeekAndDay(Week paramWeek, Day paramDay)
  {
    this.week = paramWeek;
    this.day = paramDay;
  }

  public int compareTo(Day paramDay)
  {
    return getDay().daysInPregnancy - paramDay.daysInPregnancy;
  }

  public int compareTo(Week paramWeek)
  {
    return getWeek().weeksInPregnancy - paramWeek.weeksInPregnancy;
  }

  public Day getDay()
  {
    return this.day;
  }

  public Week getWeek()
  {
    return this.week;
  }

  public String toString()
  {
    return "WeekAndDay:" + this.week.sortOrder + "/" + this.day.daysInPregnancy;
  }
}

/* Location:           C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     com.babycenter.pregnancytracker.common.WeekAndDay
 * JD-Core Version:    0.6.0
 */