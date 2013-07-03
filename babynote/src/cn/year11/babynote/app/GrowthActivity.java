package cn.year11.babynote.app;

import java.lang.reflect.Field;

import cn.year11.babynote.R;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class GrowthActivity extends TabActivity {

	private TabHost mTabHost = null;
	private TabWidget mTabWidget;
	Field mBottomLeftStrip;
	Field mBottomRightStrip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_growth);
		makeTab();
	}

	public void addDataTab() {
		Intent intent = new Intent();
		intent.putExtra("type", "growth");
		intent.setClass(GrowthActivity.this, GrowthDataActivity.class);

		TabSpec spec = mTabHost.newTabSpec("One");
		spec.setIndicator("数据", getResources().getDrawable(R.drawable.other));
		spec.setContent(intent);

		mTabHost.addTab(spec);
	}

	public void addHeightChartTab() {
		Intent intent = new Intent();
		intent.putExtra("type", 1);
		intent.setClass(GrowthActivity.this, BabyChartActivity.class);

		TabSpec spec = mTabHost.newTabSpec("Two");
		spec.setIndicator("身高图", getResources().getDrawable(R.drawable.height));
		spec.setContent(intent);
		mTabHost.addTab(spec);
	}

	public void addWeightChartTab() {
		Intent intent = new Intent();
		intent.putExtra("type", 2);
		intent.setClass(GrowthActivity.this, BabyChartActivity.class);

		TabSpec spec = mTabHost.newTabSpec("Three");
		spec.setIndicator("体重图", getResources().getDrawable(R.drawable.weight));
		spec.setContent(intent);
		mTabHost.addTab(spec);
	}

	public TabHost makeTab() {
		if (this.mTabHost == null) {
			mTabHost = getTabHost();

			mTabWidget = (TabWidget) findViewById(android.R.id.tabs);

			// add tabs,这里用于添加具体的Tabs,并用Tab触发相应的Activity
			addDataTab();
			addHeightChartTab();
			addWeightChartTab();

			// hide the bottom strip
			if (Integer.valueOf(Build.VERSION.SDK) <= 7) {
				try {
					mBottomLeftStrip = mTabWidget.getClass().getDeclaredField(
							"mBottomLeftStrip");
					mBottomRightStrip = mTabWidget.getClass().getDeclaredField(
							"mBottomRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(mTabWidget,
							getResources().getDrawable(R.drawable.divider));
					mBottomRightStrip.set(mTabWidget, getResources()
							.getDrawable(R.drawable.divider));

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					mBottomLeftStrip = mTabWidget.getClass().getDeclaredField(
							"mLeftStrip");
					mBottomRightStrip = mTabWidget.getClass().getDeclaredField(
							"mRightStrip");
					if (!mBottomLeftStrip.isAccessible()) {
						mBottomLeftStrip.setAccessible(true);
					}
					if (!mBottomRightStrip.isAccessible()) {
						mBottomRightStrip.setAccessible(true);
					}
					mBottomLeftStrip.set(mTabWidget,
							getResources().getDrawable(R.drawable.divider));
					mBottomRightStrip.set(mTabWidget, getResources()
							.getDrawable(R.drawable.divider));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (int i = 0; i < mTabWidget.getChildCount(); i++) {

				final TextView tv = (TextView) mTabWidget.getChildAt(i)
						.findViewById(android.R.id.title);
				tv.setTextColor(Color.WHITE);
				View vvv = mTabWidget.getChildAt(i);
				if (mTabHost.getCurrentTab() == i) {
					vvv.setBackgroundDrawable(null);
				} else {
					vvv.setBackgroundDrawable(null);
				}
			}
			mTabHost
					.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

						@Override
						public void onTabChanged(String tabId) {
							for (int i = 0; i < mTabWidget.getChildCount(); i++) {
								View vvv = mTabWidget.getChildAt(i);
								if (mTabHost.getCurrentTab() == i) {
									vvv.setBackgroundDrawable(getResources()
											.getDrawable(
													R.drawable.icon_shadow_on));
								} else {
									vvv.setBackgroundDrawable(null);
								}
							}
						}
					});

		} else {
			return null;
		}
		return null;
	}
}
