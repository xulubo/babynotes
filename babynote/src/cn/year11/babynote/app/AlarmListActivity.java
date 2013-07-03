package cn.year11.babynote.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.year11.babynote.R;
import cn.year11.babynote.logic.ActivityManager;
import cn.year11.babynote.logic.AlarmPrefs;
import cn.year11.babynote.logic.NotificationUtils;
import cn.year11.babynote.provider.TimerInfo;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.provider.ReminderDao;
import cn.year11.babynote.utils.ActivityUtils;
import cn.year11.babynote.utils.DateUtils;
import cn.year11.babynote.widget.SingleAlarmView;
  
public class AlarmListActivity extends BaseActivity {  
    /** Called when the activity is first created. */  
    private Button mButtonAddReminder;  
    private ListView mReminderList;
    
    SingleAlarmView[] mAlarms = new SingleAlarmView[4];
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        NotificationUtils.clear(mThisActivity, 12345);

        setContentView(R.layout.activity_alarm_list);  

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SingleAlarmView cell1 = (SingleAlarmView) findViewById(R.id.cell1);
        SingleAlarmView cell2 = (SingleAlarmView) findViewById(R.id.cell2);
        SingleAlarmView cell3 = (SingleAlarmView) findViewById(R.id.cell3);
        SingleAlarmView cell4 = (SingleAlarmView) findViewById(R.id.cell4);
        
        ViewGroup.LayoutParams p = cell1.getLayoutParams();
        p.height = p.WRAP_CONTENT;
        p.width = dm.widthPixels/2;
        cell1.setLayoutParams(p);
        cell2.setLayoutParams(p);
        cell3.setLayoutParams(p);
        cell4.setLayoutParams(p);

        
        cell1.setTitle("ÖóÄÌÆ¿");
        cell2.setTitle("ÎÂÅ£ÄÌ");
        cell3.setTitle("»»Äò²¼");
        cell4.setTitle("Öó¼¦µ°");

        mAlarms[0] = cell1;
        mAlarms[1] = cell2;
        mAlarms[2] = cell3;
        mAlarms[3] = cell4;
        
        mAlarms[0].setOnSelectAlarmListener(new SingleAlarmView.OnSelectAlarmListener(){

			@Override
			public void onSelect() {
		    	ActivityUtils.startActivityForResult(mThisActivity, AlarmSelectActivity.class, 0);				
			}
        	
        });
        
        mAlarms[1].setOnSelectAlarmListener(new SingleAlarmView.OnSelectAlarmListener(){

			@Override
			public void onSelect() {
		    	ActivityUtils.startActivityForResult(mThisActivity, AlarmSelectActivity.class, 1);				
			}
        	
        });
        
        mAlarms[2].setOnSelectAlarmListener(new SingleAlarmView.OnSelectAlarmListener(){

			@Override
			public void onSelect() {
		    	ActivityUtils.startActivityForResult(mThisActivity, AlarmSelectActivity.class, 2);				
			}
        	
        });
        
        mAlarms[3].setOnSelectAlarmListener(new SingleAlarmView.OnSelectAlarmListener(){

			@Override
			public void onSelect() {
		    	ActivityUtils.startActivityForResult(mThisActivity, AlarmSelectActivity.class, 3);				
			}
        	
        });        
        mButtonAddReminder = (Button) this.findViewById(R.id.add_reminder);  
        mButtonAddReminder.setOnClickListener(new Button.OnClickListener(){  
  
            public void onClick(View v) {  
            	showAddReminderDialog();
            }  
              
        });  

        mReminderList = (ListView)findViewById(R.id.reminder_list);
        Cursor cursor = ReminderDao.getInstance().getActiveReminders();
        this.startManagingCursor(cursor);
        
        ListAdapter adapter = new CursorAdapter(mThisActivity, cursor)
        {

			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				
			}

			@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent) {
				TextView t = new TextView(mThisActivity);
				Reminder r = new Reminder(cursor);
				t.setText(DateUtils.formatDateTime(r.getEventTime()) + "\r\n" + r.getTitle());
				return t;
			}
        	
        };
        
        mReminderList.setAdapter(adapter);
        
        mReminderList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, final long id) {
				
				new AlertDialog.Builder(mThisActivity)
				.setItems(new CharSequence[]{"É¾³ý"}, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						ReminderDao.getInstance().deleteRow(id);
					}
					
				}).show();				
				return false;
			}
		});
    }  
    
    
    @Override
	protected void onResume() {
		super.onResume();

	}


	private void showAddReminderDialog()
    {
        //ActivityManager.startReminderEditActivity(mThisActivity);    	
    	ActivityUtils.startActivityForResult(mThisActivity, AlarmSelectActivity.class, 1);
        
/*        
    	final Calendar calendar = Calendar.getInstance();  

        calendar.setTimeInMillis(System.currentTimeMillis());  
        int hour = calendar.get(Calendar.HOUR_OF_DAY);  
        int minute = calendar.get(Calendar.MINUTE);
        
        new TimePickerDialog(mThisActivity,new TimePickerDialog.OnTimeSetListener(){  

            public void onTimeSet(TimePicker view, int hourOfDay,  
                    int minute) {  
                calendar.setTimeInMillis(System.currentTimeMillis());  
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);  
                calendar.set(Calendar.MINUTE, minute);  
                calendar.set(Calendar.SECOND, 0);  
                calendar.set(Calendar.MILLISECOND, 0);  
                
                AlarmUtils.setAlarm(mThisActivity, calendar.getTimeInMillis());
            }  
              
        },hour,minute,true).show();  */
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		
		int seconds = data.getIntExtra("second", 0);
		switch(requestCode){
		case 0:
		case 1:
		case 2:
		case 3:
			mAlarms[requestCode].setCountDownTime(seconds);
			mAlarms[requestCode].onStart();
			break;
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
    
    
}  