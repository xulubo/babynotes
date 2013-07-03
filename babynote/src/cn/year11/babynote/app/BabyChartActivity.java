package cn.year11.babynote.app;

import java.util.Date;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import cn.year11.babynote.R;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.ProfileDao;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.GrowthEvent;
import cn.year11.babynote.utils.BabyUtils;
import cn.year11.utils.Log;
import cn.year11.widget.BabyHeightChart;
import cn.year11.widget.BabyWeightChart;

public class BabyChartActivity extends BaseActivity {

	static private final Log _logger = Log.getLogger(BabyChartActivity.class);
	static public final int HEIGHT_CHART = 1;
	static public final int WEIGHT_CHART = 2;
	
	BabyHeightChart mHeightChart = new BabyHeightChart();
	BabyWeightChart mWeightChart = new BabyWeightChart();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_growth_chart);

		final LinearLayout layout = (LinearLayout)findViewById(R.id.chart);
		
		bindData();

		Intent intent = getIntent();
		switch(intent.getIntExtra("type", 0)) {
		case HEIGHT_CHART:
			layout.addView(mHeightChart.getView(mThisActivity));
			break;
		case WEIGHT_CHART:
			layout.addView(mWeightChart.getView(mThisActivity));		
			break;
		}

	}
	
	public void bindData()
	{
		Profile profile = new ProfileDao().getProfile();
		if (profile == null || profile.getBirthday()<=0) {
			_logger.e("baby profile is not set");
			return;
		}
		
		if (profile.getHeight()>0) {
			mHeightChart.add(0, profile.getHeight());
		}
		
		if (profile.getWeight()>0) {
			mWeightChart.add(0, profile.getWeight());
		}

		EventDao dao = getEventDao();
		Cursor cursor = dao.queryMany(null, Event.EVENT_TYPE+"=?", new String[]{"growth"}, Event.TIME);
		startManagingCursor(cursor);
		
		try {
			if (cursor == null) {
				return;
			}
			long height, weight, age;
			while(cursor.moveToNext()) {
				GrowthEvent e = new GrowthEvent(cursor);
				age = BabyUtils.getMonthAge(new Date(e.getBeginTime()), new Date(profile.getBirthday()));
				height = e.getHeight();
				if (height>0) {
					mHeightChart.add(age, height);
				}
				weight = e.getWeight();
				if (weight>0) {
					mWeightChart.add(age, weight);
				}
			}
		}
		finally{
			cursor.close();
		}
	}

}
