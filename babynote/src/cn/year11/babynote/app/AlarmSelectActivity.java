package cn.year11.babynote.app;

import java.util.ArrayList;
import java.util.List;

import cn.year11.babynote.R;
import cn.year11.babynote.logic.ReminderManager;
import cn.year11.babynote.provider.TimerInfo;
import cn.year11.babynote.provider.TimerTemplate;
import cn.year11.babynote.widget.IconToolbar;
import cn.year11.widget.picker.NumberPicker;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AlarmSelectActivity extends BaseActivity{

	private Button mConfirm;
	private Button mCancel;
	private NumberPicker mSecondPicker;
	private NumberPicker mMinutePicker;
	private NumberPicker mHourPicker;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_select);
		setupView();
	}
	
	private void setupView()
	{
		mConfirm = (Button)findViewById(R.id.confirm);
		mCancel = (Button)findViewById(R.id.cancel);
		mSecondPicker = (NumberPicker)findViewById(R.id.second_picker);
		mMinutePicker = (NumberPicker)findViewById(R.id.minute_picker);
		mHourPicker = (NumberPicker)findViewById(R.id.hour_picker);
		
		mSecondPicker.setRange(0,  59);
		mMinutePicker.setRange(0,  59);
		mHourPicker.setRange(0,  23);

		GridView toolbar = (GridView)findViewById(R.id.icon_list);
		toolbar.setAdapter(mToolbarAdapter);
		
		mConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int h = mHourPicker.getCurrent();
				int m = mMinutePicker.getCurrent();
				int s = h*3600 + m* 60 + mSecondPicker.getCurrent();
				if (s > 0) {
					Intent result = new Intent();
					result.putExtra("hour", h);
					result.putExtra("minute", m);
					result.putExtra("second", s);
					setResult(RESULT_OK, result);
					finish();
				}
				else {
					Toast.makeText(mThisActivity, "请设定时间", 1).show();
				}
			}
		});
		
		mCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setNumber(long n)
	{
		long h = n/3600;
		long m = (n%3600) / 60;
		long s = n%60;
		
		mHourPicker.setCurrent((int)h);
		mMinutePicker.setCurrent((int)m);
		mSecondPicker.setCurrent((int)s);
	}
	
	private BaseAdapter mToolbarAdapter = new BaseAdapter()
	{
		List<TimerTemplate> templates = ReminderManager.getTemplates();
		
		@Override
		public int getCount() {
			return templates.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Button v = new Button(mThisActivity);
			//v.setImageResource(R.drawable.alarm_icon);
			v.setText(templates.get(position).getTitle());
			v.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setNumber(templates.get(position).getDuration());
				}
			});
			return v;
		}
		
	};
}
