package cn.year11.babynote.dialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.year11.babynote.R;
import cn.year11.babynote.provider.event.DiaperEvent;
import cn.year11.babynote.provider.event.DosingEvent;
import cn.year11.babynote.provider.event.Event;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

public class DiaperDialog extends EventDialog {
	
	final String[] mExcretas = new String[] {"小便", "大便", "大便和小便" };
	MultiAutoCompleteTextView mExcreta;

	
	public DiaperDialog(Activity activity)
	{
		super(activity, R.layout.diaper_dialog_view);
		
	}
	public DiaperDialog(Context c, Event e)
	{
		super(c, R.layout.diaper_dialog_view, e);

	}
	@Override
	public void setupView(View view)
	{
		mExcreta = (MultiAutoCompleteTextView)view.findViewById(R.id.name);
		mExcreta.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				showSelectStuffDialog(getContext());
				return true;
			}
			 
		});

		setTitle("换尿布");
	}
	
	private void showSelectStuffDialog(Context context)
	{
		new AlertDialog.Builder(context).setTitle("药品名称").setSingleChoiceItems(
				mExcretas, 0,
			     new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  mExcreta.setText(mExcretas[which]);
			    	  dialog.dismiss();
			      }
			     }).setNegativeButton("取消", null).show();
	}


	@Override
	protected void onSave() {
		DiaperEvent event = new DiaperEvent();
		event.setTime(getTime());
		event.setExcretaName(mExcreta.getText().toString());
		event.setMemo(mEventMemo.getText().toString());
		getDao().save(event);
	}
	

}
