package cn.year11.babynote.dialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.year11.babynote.R;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.HealthEvent;
import cn.year11.babynote.provider.event.DosingEvent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DoseDialog extends EventDialog {
	
	final String[] mMedicines = new String[] {"美林", "泰诺" };
	MultiAutoCompleteTextView mMedicine;
	EditText mDosage;
	
	public DoseDialog(Context c)
	{
		super(c, R.layout.medicine_dialog_view);
	}
	
	public DoseDialog(Context c, Event e)
	{
		super(c, R.layout.medicine_dialog_view, e);

	}
	
	@Override
	public void setupView(View v)
	{		
		mMedicine = (MultiAutoCompleteTextView)v.findViewById(R.id.medicine_name);
		mMedicine.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				showSelectMedicineDialog(getContext());
				return true;
			}
			 
		});

		mDosage = (EditText) v.findViewById(R.id.medicine_amount);
		mDosage.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				int x10 = 0;
				int x1 = 0;
				
				if (!TextUtils.isEmpty(mDosage.getText())) {
					x10 = Integer.valueOf(mDosage.getText().toString())/10;
					x1 = Integer.valueOf(mDosage.getText().toString())%10;
				}
				
				new DigitSelectDialog(getContext(), new int[] { x1, x10 },

				new DigitSelectDialog.OnConfirmListener() {

					@Override
					public void onConfirm(int number) {
						mDosage.setText(String.valueOf(number));
					}
				}
					
				).show();
				return true;
			}
			
		});
	}
	
	private void showSelectMedicineDialog(Context context)
	{
		new AlertDialog.Builder(context).setTitle("药品名称").setSingleChoiceItems(
				mMedicines, 0,
			     new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  mMedicine.setText(mMedicines[which]);
			    	  dialog.dismiss();
			      }
			     }).setNegativeButton("取消", null).show();
	}


	@Override
	protected void onSave()
	{
		DosingEvent event = new DosingEvent();
		event.setMedicineName(mMedicine.getText().toString());
		event.setMedicineAmount(mDosage.getText().toString());
		event.setMedicineTime(System.currentTimeMillis());
		getDao().save(event);
	}
	

}
