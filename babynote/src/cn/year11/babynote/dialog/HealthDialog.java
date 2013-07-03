package cn.year11.babynote.dialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.year11.babynote.R;
import cn.year11.babynote.provider.event.DiaperEvent;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.HealthEvent;
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

public class HealthDialog extends EventDialog {
	
	final String[] mSymptoms = new String[] {"¸ÐÃ°", "·¢ÉÕ", "¸¹Ðº", "Å»ÍÂ", "ºìÑÛ", "±ÇÈû", "¿ÈËÔ" };
	MultiAutoCompleteTextView mSymptom;
	EditText mTemperature;
	
	public HealthDialog(Activity activity)
	{
		super(activity, R.layout.health_dialog_view);
	}
	public HealthDialog(Context c, Event e)
	{
		super(c, R.layout.health_dialog_view, e);

	}
	@Override
	public void setupView(View v)
	{
		mTemperature = (EditText) v.findViewById(R.id.temperature);
		
		mSymptom = (MultiAutoCompleteTextView)v.findViewById(R.id.name);
		mSymptom.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				showSelectStuffDialog(getContext());
				return true;
			}
			 
		});


	}
	
	private void showSelectStuffDialog(Context context)
	{
		new AlertDialog.Builder(context).setTitle("Ò©Æ·Ãû³Æ").setSingleChoiceItems(
				mSymptoms, 0,
			     new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  mSymptom.setText(mSymptoms[which]);
			    	  dialog.dismiss();
			      }
			     }).setNegativeButton("È¡Ïû", null).show();
	}


	@Override
	protected void onSave() {
		HealthEvent event = new HealthEvent();
		event.setName(mSymptom.getText().toString());
		event.setTemperature(Float.valueOf(mTemperature.getText().toString()));
		event.setMemo(mEventMemo.getText().toString());
		event.setTime(getTime());
		getDao().save(event);
	}
	

}
