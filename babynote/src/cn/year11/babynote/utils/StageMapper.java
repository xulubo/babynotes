package cn.year11.babynote.utils;

import com.babycenter.pregnancytracker.common.Day;
import com.babycenter.pregnancytracker.common.StageContent;
import com.babycenter.pregnancytracker.common.Week;
import com.babycenter.pregnancytracker.common.WeekAndDay;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StageMapper {
	StageContent stageContent;

	public int getDaysToGo(Calendar paramCalendar1, Calendar paramCalendar2) {
		return (int) ((paramCalendar1.getTimeInMillis() - paramCalendar2
				.getTimeInMillis()) / 86400000L);
	}

	public int getDaysToGo(Date paramDate1, Date paramDate2) {
		return (int) ((paramDate1.getTime() - paramDate2.getTime()) / 86400000L);
	}

	public StageContent getStageContent() {
		return this.stageContent;
	}

	WeekAndDay getWeekAndDay(int daysToGo, boolean paramBoolean) {
		int i = daysToGo;
		Week localWeek=null;
		Day localDay=null;
		if (paramBoolean) {
			if (266 < daysToGo)
				i = 266;
		}

		Iterator<Week> weeks = this.stageContent.weeks.iterator();
		do {
			Iterator<Day> days = null;
			
			while (days != null && !days.hasNext()) {
				if (!weeks.hasNext())
					break;
				localWeek = (Week) weeks.next();
				days = localWeek.days.iterator();
			}

			localDay = (Day) days.next();
		} while (i != localDay.daysToGo);

		WeekAndDay localWeekAndDay = new WeekAndDay(localWeek, localDay);
		return localWeekAndDay;
	}

	public WeekAndDay getWeekAndDay(Calendar paramCalendar1,
			Calendar paramCalendar2, boolean paramBoolean) {
		return getWeekAndDay(getDaysToGo(paramCalendar1, paramCalendar2),
				paramBoolean);
	}

	public WeekAndDay getWeekAndDay(Date paramDate1, Date paramDate2,
			boolean paramBoolean) {
		return getWeekAndDay(getDaysToGo(paramDate1, paramDate2), paramBoolean);
	}

	public void setStageContent(StageContent paramStageContent) {
		this.stageContent = paramStageContent;
	}
}

/*
 * Location: C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name: com.babycenter.pregnancytracker.util.StageMapper JD-Core
 * Version: 0.6.0
 */