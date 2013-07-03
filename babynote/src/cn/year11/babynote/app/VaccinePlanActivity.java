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
		mPlans.put(0, new Vaccine("", "年龄"));
		mPlans.put(1, new Vaccine("", "卡介苗"));
		mPlans.put(2, new Vaccine("", "乙肝疫苗"));
		mPlans.put(3, new Vaccine("", "脊髓灰质炎活疫苗"));
		mPlans.put(4, new Vaccine("", "白百破"));
		mPlans.put(5, new Vaccine("", "麻疹活疫苗"));
		mPlans.put(6, new Vaccine("", "乙脑疫苗"));
		mPlans.put(7, new Vaccine("", "出生"));
		mPlans.put(14,new Vaccine("",  "1足月"));
		mPlans.put(15, new Vaccine("KJM_01", "初种"));
		mPlans.put(16, new Vaccine("YGYM_01", "第一针"));
		mPlans.put(21, new Vaccine("", "2足月"));
		mPlans.put(24, new Vaccine("JSHZYHYM_01", "初免"));
		mPlans.put(28, new Vaccine("", "3足月"));
		mPlans.put(29, new Vaccine("KJM_02", "第二针"));
		mPlans.put(30, new Vaccine("YGYM_02", "第二针"));
		mPlans.put(31, new Vaccine("JSHZYHYM_02", "初免"));
		mPlans.put(35, new Vaccine("", "4足月"));
		mPlans.put(38, new Vaccine("JSHZYHYM_03", "初免"));
		mPlans.put(39, new Vaccine("BBP_01", "第一次"));
		mPlans.put(42, new Vaccine("", "5足月"));
		mPlans.put(45, new Vaccine("JSHZYHYM_04", "第二次"));
		mPlans.put(46, new Vaccine("BBP_02", "初免"));
		mPlans.put(49, new Vaccine("", "6足月"));
		mPlans.put(52, new Vaccine("JSHZYHYM_05", "初免"));
		mPlans.put(53, new Vaccine("BBP_03", "第二次"));
		mPlans.put(54, new Vaccine("MZHY_01", "初免"));
		
		mPlans.put(56, new Vaccine("", "8足月"));
		mPlans.put(58, new Vaccine("YGYM_03", "第三针"));
		mPlans.put(59, new Vaccine("JSHZYHYM_06", "第三次"));
		mPlans.put(60, new Vaccine("BBP_04", "初免"));
		mPlans.put(61, new Vaccine("MZHY_02", "加强"));
		mPlans.put(62, new Vaccine("YNYM_01", "初免两针(间隔7-10天)"));
		
		mPlans.put(63, new Vaccine("", "1周岁"));
		mPlans.put(66, new Vaccine("JSHZYHYM_07", "加强"));
		mPlans.put(67, new Vaccine("BBP_05", "第三次"));
		mPlans.put(69, new Vaccine("YNYM_02", "加强"));
		
		mPlans.put(70, new Vaccine("", "2周岁"));
		mPlans.put(74, new Vaccine("BBP_06", "加强(百破)"));
		mPlans.put(75, new Vaccine("MZHY_03", "加强"));
		
		mPlans.put(77, new Vaccine("", "3周岁"));
		mPlans.put(84, new Vaccine("", "4周岁"));
		mPlans.put(87, new Vaccine("JSHZYHYM_08", "加强"));
		mPlans.put(89, new Vaccine("MZHY_04", "加强"));
		mPlans.put(90, new Vaccine("YNYM_03", "加强"));
		
		mPlans.put(91, new Vaccine("", "7周岁"));
		mPlans.put(92, new Vaccine("KJM_03", "复种"));
		mPlans.put(95, new Vaccine("BBP_07", "加强(白类)"));
		mPlans.put(96, new Vaccine("MZHY_04", "加强"));
		mPlans.put(97, new Vaccine("YMYM_04", "加强"));
		
		mPlans.put(98, new Vaccine("", "12-13岁"));
		mPlans.put(99, new Vaccine("KJM_03", "复种(农村)"));
		mPlans.put(102, new Vaccine("BBP_08", "加强(白类)"));
		mPlans.put(103, new Vaccine("MZHY_05", "加强"));
		
		mPlans.put(105, new Vaccine("", "18-19岁"));
		mPlans.put(109, new Vaccine("BBP_09", "加强(白类)"));
		
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
			Toast.makeText(mThisActivity, "请先添加宝贝信息", 1).show();
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
									.setItems(new CharSequence[]{"删除"}, new DialogInterface.OnClickListener(){
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
