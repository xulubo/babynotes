package cn.year11.babynote.dialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.year11.babynote.R;
import cn.year11.babynote.provider.event.DiaperEvent;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.GrowthEvent;
import cn.year11.babynote.utils.FileUtils;
import cn.year11.babynote.utils.MediaUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

public class GrowthDialog extends EventDialog{
	
	protected EditText mHeight;
	protected EditText mWeight;
	protected Activity mActivity;
	
	public GrowthDialog(final Activity activity)
	{
		super(activity, R.layout.growth_dialog_view);
mActivity = activity;
	}
	public GrowthDialog(Context c, Event e)
	{
		super(c, R.layout.growth_dialog_view, e);

	}
	
	@Override
	public void setupView(View v)
	{
		mHeight = (EditText) v.findViewById(R.id.height);
		mWeight = (EditText) v.findViewById(R.id.weight);

	}


	@Override
	protected void onSave() {
		GrowthEvent event = new GrowthEvent();
		if (mHeight.getText() != null && !TextUtils.isEmpty(mHeight.getText().toString())) {
			event.setHeight(Long.valueOf(mHeight.getText().toString()));
		}
		
		if (mWeight.getText() != null && !TextUtils.isEmpty(mWeight.getText().toString())) {
			event.setWeight(Long.valueOf(mWeight.getText().toString()));
		} 
		
		if (mEventMemo.getText()!=null) {
			event.setMemo(mEventMemo.getText().toString());
		} 
		event.setTime(getTime());
		getDao().save(event);
	}
	


}
