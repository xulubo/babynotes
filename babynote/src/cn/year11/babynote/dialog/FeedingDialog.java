package cn.year11.babynote.dialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.year11.babynote.R;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.event.DosingEvent;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.FeedingEvent;
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

public class FeedingDialog extends EventDialog {
	
	final String[] mStuffs = new String[] {"小便", "大便", "大便和小便" };
	MultiAutoCompleteTextView mStuff;
	Activity mActivity;

	EditText mDrinkAmount;
	EditText mEatAmount;
	MultiAutoCompleteTextView mFoodName;

	
	public FeedingDialog(Activity activity)
	{
		super(activity, R.layout.feeding_dialog_view);
		mActivity = activity;		

	}
	
	public FeedingDialog(Context c, Event e)
	{
		super(c, R.layout.feeding_dialog_view, e);

	}
	public void setupView(View view)
	{
		mDrinkAmount = (EditText) view.findViewById(R.id.drink_amount);
		mEatAmount = (EditText) view.findViewById(R.id.eat_amount);
		mFoodName = (MultiAutoCompleteTextView)view.findViewById(R.id.food_name);


		mStuff = (MultiAutoCompleteTextView)view.findViewById(R.id.food_name);
		mStuff.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				showSelectStuffDialog(mActivity);
				return true;
			}
			 
		});

		setTitle("喂养");
	}
	
	private void showSelectStuffDialog(Context context)
	{
		new AlertDialog.Builder(context).setTitle("药品名称").setSingleChoiceItems(
				mStuffs, 0,
			     new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  mStuff.setText(mStuffs[which]);
			    	  dialog.dismiss();
			      }
			     }).setNegativeButton("取消", null).show();
	}
	

	@Override
	protected void onSave()
	{
		FeedingEvent event = new FeedingEvent();

		event.setFoodName(mFoodName.getText().toString());
		event.setFeedingTime(getTime());
		event.setDrinkAmount(Long.valueOf(mDrinkAmount.getText().toString()));
		event.setEatAmount(Long.valueOf(mEatAmount.getText().toString()));
		event.setMemo(mEventMemo.getText().toString());
		event.setTime(getTime());
		
		EventDao dao = new EventDao();
		dao.save(event);
	}
}
