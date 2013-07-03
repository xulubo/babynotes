package cn.year11.babynote.app;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.year11.babynote.R;
import cn.year11.babynote.dialog.BaseDialog.OnConfirmListener;
import cn.year11.babynote.dialog.ReminderDialog;
import cn.year11.babynote.dialog.VaccineDialog;
import cn.year11.babynote.logic.ProfileManager;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.Reminder;
import cn.year11.babynote.provider.ReminderDao;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.provider.event.VaccineEvent;

public class VaccinePlanActivity extends BaseActivity {

	static HashMap<Integer, Vaccine> mPlans = new HashMap<Integer, Vaccine>();

	class Vaccine {
		public String code;
		public String title;
		public Vaccine(String code, String title)
		{
			this.code = code;
			this.title = title;
		}
	}
	
	void init() {
		mPlans.put(0, new Vaccine("", "����"));
		mPlans.put(1, new Vaccine("", "������"));
		mPlans.put(2, new Vaccine("", "�Ҹ�����"));
		mPlans.put(3, new Vaccine("", "��������׻�����"));
		mPlans.put(4, new Vaccine("", "�װ���"));
		mPlans.put(5, new Vaccine("", "���������"));
		mPlans.put(6, new Vaccine("", "��������"));
		mPlans.put(7, new Vaccine("", "����"));
		mPlans.put(14,new Vaccine("",  "1����"));
		mPlans.put(15, new Vaccine("KJM_01", "����"));
		mPlans.put(16, new Vaccine("YGYM_01", "��һ��"));
		mPlans.put(21, new Vaccine("", "2����"));
		mPlans.put(24, new Vaccine("JSHZYHYM_01", "����"));
		mPlans.put(28, new Vaccine("", "3����"));
		mPlans.put(29, new Vaccine("KJM_02", "�ڶ���"));
		mPlans.put(30, new Vaccine("YGYM_02", "�ڶ���"));
		mPlans.put(31, new Vaccine("JSHZYHYM_02", "����"));
		mPlans.put(35, new Vaccine("", "4����"));
		mPlans.put(38, new Vaccine("JSHZYHYM_03", "����"));
		mPlans.put(39, new Vaccine("BBP_01", "��һ��"));
		mPlans.put(42, new Vaccine("", "5����"));
		mPlans.put(45, new Vaccine("JSHZYHYM_04", "�ڶ���"));
		mPlans.put(46, new Vaccine("BBP_02", "����"));
		mPlans.put(49, new Vaccine("", "6����"));
		mPlans.put(52, new Vaccine("JSHZYHYM_05", "����"));
		mPlans.put(53, new Vaccine("BBP_03", "�ڶ���"));
		mPlans.put(54, new Vaccine("MZHY_01", "����"));
		
		mPlans.put(56, new Vaccine("", "8����"));
		mPlans.put(58, new Vaccine("YGYM_03", "������"));
		mPlans.put(59, new Vaccine("JSHZYHYM_06", "������"));
		mPlans.put(60, new Vaccine("BBP_04", "����"));
		mPlans.put(61, new Vaccine("MZHY_02", "��ǿ"));
		mPlans.put(62, new Vaccine("YNYM_01", "��������(���7-10��)"));
		
		mPlans.put(63, new Vaccine("", "1����"));
		mPlans.put(66, new Vaccine("JSHZYHYM_07", "��ǿ"));
		mPlans.put(67, new Vaccine("BBP_05", "������"));
		mPlans.put(69, new Vaccine("YNYM_02", "��ǿ"));
		
		mPlans.put(70, new Vaccine("", "2����"));
		mPlans.put(74, new Vaccine("BBP_06", "��ǿ(����)"));
		mPlans.put(75, new Vaccine("MZHY_03", "��ǿ"));
		
		mPlans.put(77, new Vaccine("", "3����"));
		mPlans.put(84, new Vaccine("", "4����"));
		mPlans.put(87, new Vaccine("JSHZYHYM_08", "��ǿ"));
		mPlans.put(89, new Vaccine("MZHY_04", "��ǿ"));
		mPlans.put(90, new Vaccine("YNYM_03", "��ǿ"));
		
		mPlans.put(91, new Vaccine("", "7����"));
		mPlans.put(92, new Vaccine("KJM_03", "����"));
		mPlans.put(95, new Vaccine("BBP_07", "��ǿ(����)"));
		mPlans.put(96, new Vaccine("MZHY_04", "��ǿ"));
		mPlans.put(97, new Vaccine("YMYM_04", "��ǿ"));
		
		mPlans.put(98, new Vaccine("", "12-13��"));
		mPlans.put(99, new Vaccine("KJM_03", "����(ũ��)"));
		mPlans.put(102, new Vaccine("BBP_08", "��ǿ(����)"));
		mPlans.put(103, new Vaccine("MZHY_05", "��ǿ"));
		
		mPlans.put(105, new Vaccine("", "18-19��"));
		mPlans.put(109, new Vaccine("BBP_09", "��ǿ(����)"));
		
	}
	
	GridAdapter mAdapter = new GridAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_vaccine_plan);
		GridView g = (GridView)findViewById(R.id.grid);
		g.setAdapter(mAdapter);
		
		ImageView imageView = (ImageView)findViewById(R.id.portrait);
		Profile baby = ProfileManager.getInstance().getFirstProfile();
		if (baby != null) {
			Drawable portrait = baby.getPortraitDrawable();
			if (portrait != null)
				imageView.setImageDrawable(portrait);
		}
		else {
			Toast.makeText(mThisActivity, "������ӱ�����Ϣ", 1).show();
		}
		
		findViewById(R.id.screen_orientation).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mThisActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}
				else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
			}
		});
	}

	private class GridAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			return 112;
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
			LayoutInflater inflater = getLayoutInflater();
			View v1 = inflater.inflate(R.layout.vaccine_plan_item, null);
			TextView vt = (TextView)v1.findViewById(R.id.item_text);
			
			final Vaccine vaccine = mPlans.get(position);
			
			if (mPlans.get(position) != null) {
					vt.setText(mPlans.get(position).title);
					vt.setVisibility(View.VISIBLE);
					
					// not the first row and first column
					if (position > 7 && position%7 != 0) {
						final Event attachedEvent = getEventDao().findEventByUniqueCode(VaccineEvent.TYPE, vaccine.code);
						final Reminder attachedReminder = ReminderDao.getInstance().getByEventCode(vaccine.code);
						if (attachedEvent!=null) {
							Drawable icon = getApplicationContext().getResources().getDrawable(R.drawable.ok);
							vt.setCompoundDrawablesWithIntrinsicBounds((Drawable)icon, null, null, null); 
							v1.setTag(attachedEvent);
							
							v1.setOnLongClickListener(new View.OnLongClickListener() {
								
								@Override
								public boolean onLongClick(View v) {
									final View vv = v;
									new AlertDialog.Builder(VaccinePlanActivity.this)
									.setItems(new CharSequence[]{"ɾ��"}, new DialogInterface.OnClickListener(){
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											Event e = (Event)vv.getTag();
											if (e!=null) {
												EventDao.getInstance().deleteRow(e.getId());
												mAdapter.notifyDataSetChanged();
											}
										}
										
									}).show();
									
									return true;
								}
							});
						}
						else if (attachedReminder != null) {
							Drawable icon = getApplicationContext().getResources().getDrawable(R.drawable.ring_type_r);
							vt.setCompoundDrawablesWithIntrinsicBounds((Drawable)icon, null, null, null); 
							v1.setTag(attachedEvent);
						}
						

						
						v1.setOnClickListener(new View.OnClickListener(){

							@Override
							public void onClick(View v) {
								if (attachedEvent!=null) {
									VaccineDialog dialog = new VaccineDialog(VaccinePlanActivity.this);
									dialog.setEvent(attachedEvent);
									Reminder reminder = ReminderDao.getInstance().getByEventCode(vaccine.code);
									VaccineEvent newEvent = new VaccineEvent();
									newEvent.setCode(vaccine.code);
									if (reminder != null) {
										newEvent.setName(reminder.getTitle());
									}
									else {
										newEvent.setName(vaccine.title);
									}
									dialog.setEvent(newEvent);
									dialog.setOnConfirmListener(new OnConfirmListener(){

										@Override
										public void onConfirm() {
											mAdapter.notifyDataSetChanged();
										}
										
									});
									dialog.show();
								}
								else {
									ReminderDialog dialog = new ReminderDialog(mThisActivity, attachedReminder);
									dialog.setOnConfirmListener(new OnConfirmListener(){

										@Override
										public void onConfirm() {
											mAdapter.notifyDataSetChanged();
										}
										
									});
									dialog.show();

								}

							}
							
						});
					}
			}
			else {
//				v.setText(String.valueOf(position)+"0");
				vt.setVisibility(View.VISIBLE);
			}

			
//			v.setBackgroundResource(android.R.color.white);
			return v1;
		}
		
	}
}
