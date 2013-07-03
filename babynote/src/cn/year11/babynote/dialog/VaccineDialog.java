package cn.year11.babynote.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.MultiAutoCompleteTextView;
import cn.year11.babynote.R;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.VaccineEvent;

public class VaccineDialog extends EventDialog{
	
	final String[] mVaccinums = new String[] {"¿¨½éÃç", "°ÙÈÕ¿È", "Á÷¸ÐÒßÃç", "¼×¸ÎÒßÃç", "ÒÒ¸ÎÒßÃç", "Âé·çÒßÃç", "Ë®¶»ÒßÃç" };
	MultiAutoCompleteTextView mVaccinumName;

	
	public VaccineDialog(Activity activity)
	{
		super(activity, R.layout.vaccine_dialog_view);
	}
	
	public VaccineDialog(Context c, Event e)
	{
		super(c, R.layout.vaccine_dialog_view, e);

	}
	
	public void setupView(View view)
	{
		mVaccinumName = (MultiAutoCompleteTextView)view.findViewById(R.id.name);
		mVaccinumName.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				showSelectStuffDialog(getContext());
				return true;
			}
			 
		});
	}
	
	@Override
	public void bindData()
	{
		if (getEvent() == null) {
			return;
		}
		
		VaccineEvent vaccineEvent = new VaccineEvent(getEvent());
		if (!TextUtils.isEmpty(vaccineEvent.getName())) {
			mVaccinumName.setText(vaccineEvent.getName());
		}
	}
	
	private void showSelectStuffDialog(Context context)
	{
		new AlertDialog.Builder(context).setTitle("Ò©Æ·Ãû³Æ").setSingleChoiceItems(
				mVaccinums, 0,
			     new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  mVaccinumName.setText(mVaccinums[which]);
			    	  dialog.dismiss();
			      }
			     }).setNegativeButton("È¡Ïû", null).show();
	}


	@Override
	protected void onSave() {
		VaccineEvent event = new VaccineEvent(getEvent());
		event.setName(mVaccinumName.getText().toString());
		event.setMemo(mEventMemo.getText().toString());
		event.setTime(getTime());
		getDao().save(event);
	}


}
