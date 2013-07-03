package cn.year11.babynote.app;

import cn.year11.babynote.R;
import cn.year11.babynote.dialog.GrowthDialog;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.event.GrowthEvent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class GrowthDataActivity extends BaseActivity {

	ListView mEventList;
	EventDao mEventDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_growth_data);
		
		mEventDao = new EventDao();
		//Cursor cursor = mEventDao.queryMany(null, null, null, Event.TIME);
		Cursor cursor = mEventDao.findAllDate(GrowthEvent.TYPE);
		startManagingCursor(cursor);
		
		mEventList = (ListView)findViewById(R.id.eventlist);
		DiaryListAdapter listAdapter = new DiaryListAdapter(this, cursor, GrowthEvent.TYPE){
			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
				scrollToBottom();
			}
		};
		mEventList.setAdapter(listAdapter);
		scrollToBottom();
		
		findViewById(R.id.add).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				new GrowthDialog(mThisActivity).show();
			}
			
		});
	}
	
	public void scrollToBottom()
	{
        mEventList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL); 
        mEventList.invalidateViews(); 
	}
}
