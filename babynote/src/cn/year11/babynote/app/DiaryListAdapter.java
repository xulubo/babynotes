package cn.year11.babynote.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.year11.babynote.R;
import cn.year11.babynote.dialog.DialogFactory;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.utils.DateUtils;
import cn.year11.content.CursorGetter;

public class DiaryListAdapter extends BaseAdapter
{
	Cursor mCursor;
	boolean mDataValid = false;
	Context mContext;
	EventDao mEventDao;
	String mFilterEventType;
	
	public DiaryListAdapter(Context context, Cursor c, String filterEventType) {
		mContext = context;
		mCursor = c;
		mDataValid = c!=null;
		if (c!=null) {
			c.registerContentObserver(new ChangeObserver());
			c.registerDataSetObserver(new MyDataSetObserver());
		}
		
		mEventDao = EventDao.getInstance();
		mFilterEventType = filterEventType;
	}


	@Override
	public int getCount() {
		return mCursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mCursor.moveToPosition(position);
		CursorGetter cursorGetter = new CursorGetter(mCursor);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View dayView = inflater.inflate(R.layout.diarylist_day_item, null);
		TextView dayLabel = (TextView) dayView.findViewById(R.id.day_label);
		String day = cursorGetter.getString(Event.DATE);
		Date date = DateUtils.parseDate(day);
		
		if (DateUtils.isCurrentDay(day)) {
			dayView.setBackgroundResource(R.drawable.shape_bg_current_day);
		}
		else {
			dayView.setBackgroundResource(0);

			dayView.setBackgroundColor(android.R.color.white);
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("E\nyyÄêMÔÂd");

		dayLabel.setText(dateFormat.format(date));

		LinearLayout dayItemContainer = (LinearLayout)dayView.findViewById(R.id.day_item_right_column);
		Cursor dayCursor = mEventDao.findDayEvents(day, mFilterEventType);
		if (dayCursor == null) {
			return dayView;
		}
		
		int i=0;
		while(dayCursor.moveToNext()) {
			DiaryEventItem item = new DiaryEventItem(mContext, new Event(dayCursor), i++>0);
			dayItemContainer.addView(item.getView());
		}
		dayCursor.close();
		return dayView;
	}
	
	protected void onContentChanged() {
		mDataValid = mCursor.requery();
	}
	
	private class ChangeObserver extends ContentObserver {
		public ChangeObserver() {
			super(new Handler());
		}
		
		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}
		
		@Override
		public void onChange(boolean selfChange) {
			onContentChanged();
		}
	}
	
	private class MyDataSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			mDataValid = true;
			notifyDataSetChanged();
		}
		
		@Override
		public void onInvalidated() {
			mDataValid = false;
			notifyDataSetInvalidated();
		}
	}


	

}