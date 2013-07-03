package cn.year11.babynote.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.year11.babynote.R;

import com.babycenter.pregnancytracker.common.Artifact;
import com.babycenter.pregnancytracker.common.Day;
import com.babycenter.pregnancytracker.common.StageContent;
import com.babycenter.pregnancytracker.common.Week;
import com.babycenter.pregnancytracker.common.WeekAndDay;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TodayActivity extends ListActivity {
	private static final int CURRPOS_LIST_PADDING_TOP = 45;
	static final String OMNITURE_PAGENAME = "My Pregnancy Today | Calendar Pages";
	ArrayList<TodayListItem> itemList = null;
	long startArtifactId = 0L;
	int startPos = 0;
	int startWeekId = 0;

	private StageContent getStageContent() {
		InputStream localInputStream = getResources().openRawResource(
				R.raw.pregnancy_tracker_calendar);
		assert (localInputStream != null);
		try {
			StageContent stageContent = null;
			stageContent = ((StageContent) new ObjectInputStream(
					localInputStream).readObject());

			// unmarshallArticleAdMap();
			return stageContent;
		} catch (Exception localException) {
			while (true)
				Log.e("PregnancyTracker", localException.getMessage(),
						localException);
		}
	}

	private void constructData() {
		StageContent localStageContent = getStageContent();
		if (localStageContent == null)
			return;

		// WeekAndDay localWeekAndDay = null;//new WeekAndDay(2,
		// 15);//this.application.getCurrentWeekAndDay(true);
		this.itemList = new ArrayList();
		int i = 0;
		Iterator weeks = localStageContent.weeks.iterator();
		while (weeks.hasNext()) {
			Week localWeek = (Week) weeks.next();
			WeekListItem localWeekListItem = new WeekListItem(localWeek);
			// if (localWeekAndDay.compareTo(localWeek) == 0)
			// localWeekListItem.setIsCurrentWeek(true);
			this.itemList.add(localWeekListItem);

			i++;
			Iterator days = localWeek.days.iterator();
			while (days.hasNext()) {
				Day localDay = (Day) days.next();
				DayListItem localDayListItem = new DayListItem(localDay);
				// if (localWeekAndDay.compareTo(localDay) == 0)
				// {
				// localDayListItem.setCurrentDay(true);
				// this.startPos = i;
				// }

				while (this.startArtifactId > 0L) {
					Iterator artifacts = localDay.artifacts.iterator();
					while (artifacts.hasNext()) {
						if (((Artifact) artifacts.next()).id != this.startArtifactId)
							continue;
						this.startPos = i;
					}
					// if (localWeekAndDay.compareTo(localWeek) != 0)
					// continue;
					localDayListItem.setInCurrentWeek(true);
				}
				this.itemList.add(localDayListItem);
				i++;
			}
			if (!localWeekListItem.isCurrentWeek())
				continue;
			// this.itemList.add(new BirthClubListItem(this));
			i++;
		}

	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.todaylist);

		constructData();
		getListView().setItemsCanFocus(true);
		setListAdapter(new TodayViewAdapter());
		setSelectionCurrentArtifact();

	}

	public void setSelectionCurrentArtifact() {
		if (this.startPos > 0)
			getListView().post(new Runnable() {
				public void run() {
					TodayActivity.this.getListView().setSelectionFromTop(
							TodayActivity.this.startPos, 45);
				}
			});
	}

	class TodayViewAdapter extends ArrayAdapter<TodayListItem> {
		private static final int TYPE_DAY_ITEM = 0;
		private static final int TYPE_MAX_COUNT = 5;
		private static final int TYPE_WEEK_ITEM = 1;

		public TodayViewAdapter() {
			super(TodayActivity.this, R.layout.todaylist_week,
					TodayActivity.this.itemList);
		}

		public boolean areAllItemsEnabled() {
			return false;
		}

		public int getItemViewType(int paramInt) {
			int i;
			if (paramInt >= TodayActivity.this.itemList.size())
				i = -1;

			TodayListItem localTodayListItem = (TodayListItem) TodayActivity.this.itemList
					.get(paramInt);
			if (localTodayListItem.blockViewRecycling()) {
				i = -1;
			} else if ((localTodayListItem instanceof DayListItem)) {
				i = 0;
			} else if ((localTodayListItem instanceof WeekListItem)) {
				i = 1;
			} else {
				i = -1;
			}

			return i;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			return ((TodayListItem) TodayActivity.this.itemList.get(paramInt))
					.getView(TodayActivity.this, paramView, paramViewGroup);
		}

		public int getViewTypeCount() {
			return 5;
		}

		public boolean isEnabled(int paramInt) {
			return ((TodayListItem) TodayActivity.this.itemList.get(paramInt))
					.isEnabled();
		}
	}
}

/*
 * Location: C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name: com.babycenter.pregnancytracker.app.TodayActivity JD-Core
 * Version: 0.6.0
 */