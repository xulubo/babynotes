package cn.year11.babynote.app;


import cn.year11.babynote.R;
import cn.year11.babynote.utils.ActivityUtils;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class HomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		GridView grid = (GridView)findViewById(R.id.grid);
		grid.setAdapter(new GridViewAdapter());
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		onGridItemClicked(position); 
        	}

		}); 
	}

	private class GridViewAdapter extends BaseAdapter
	{
		final int mTotalItem = 6;
		@Override
		public int getCount() {
			return mTotalItem;
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
			View view;
			LayoutInflater inflater = getLayoutInflater();
			view = inflater.inflate(R.layout.home_screen_icon, null);
			TextView tv = (TextView) view.findViewById(R.id.homescreen_griditem);
			Drawable icon;
			switch(position) {
			case 0:
				icon = getApplicationContext().getResources().getDrawable(R.drawable.home_screen_messages_icon);
				tv.setCompoundDrawablesWithIntrinsicBounds(null, (Drawable)icon, null, null); 
				tv.setText("养育日记");
				break;
			case 1:
				icon = getApplicationContext().getResources().getDrawable(R.drawable.home_screen_people_icon);
				tv.setCompoundDrawablesWithIntrinsicBounds(null, (Drawable)icon, null, null); 
				tv.setText("宝贝照片");
				break;
			case 2:
				icon = getApplicationContext().getResources().getDrawable(R.drawable.home_screen_storage_icon);
				tv.setCompoundDrawablesWithIntrinsicBounds(null, (Drawable)icon, null, null); 
				tv.setText("成长数据");
				break;
			case 3:
				icon = getApplicationContext().getResources().getDrawable(R.drawable.home_screen_notes_icon);
				tv.setCompoundDrawablesWithIntrinsicBounds(null, (Drawable)icon, null, null); 
				tv.setText("免疫计划");
				break;
			case 4:
				icon = getApplicationContext().getResources().getDrawable(R.drawable.home_screen_profile_icon);
				tv.setCompoundDrawablesWithIntrinsicBounds(null, (Drawable)icon, null, null); 
				tv.setText("宝贝资料");
				break;
			case 5:
				icon = getApplicationContext().getResources().getDrawable(R.drawable.home_screen_conversations_icon);
				tv.setCompoundDrawablesWithIntrinsicBounds(null, (Drawable)icon, null, null); 
				tv.setText("设置");
				break;

			}
			return view;
		}
		
	}
	private void onGridItemClicked(int position) {
    	switch (position) { 
    	case 0:
    		startActivity(DiaryListActivity.class);
    		break;
    		
    	case 1:
    		ActivityUtils.startActivity(mThisActivity, GalleryActivity.class);
    		break;
    		
    	case 2: 
    		startActivity(GrowthActivity.class);
    		break; 
    		
    	case 3: 
    		startActivity(VaccinePlanActivity.class);
    		break; 
    		
    	case 4:
    		startActivity(ProfileActivity.class);
    		break;

    	case 5:
    		//startActivity(AlarmListActivity.class);
    		startActivity(SettingActivity.class);
    		break;
    	}		
	}
	private void startActivity(Class<?> cls)
	{
		Intent intent = new Intent(HomeActivity.this, cls);
		startActivity(intent);
	}
}
