package cn.year11.babynote.widget.timer;

import cn.year11.babynote.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TimerAlarmActivity extends Activity {
	private boolean wasVisible = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Log.DEBUG)
			Log.v("TimerAlarmActivity: onCreate()");

		// Hide the title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert);
		Button okbutton = (Button) findViewById(R.id.okbutton);
		okbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ManageNotification.clearAll(TimerAlarmActivity.this);
				finish();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Log.DEBUG)
			Log.v("TimerAlarmActivity: onResume()");

		wasVisible = false;

		Intent i = getIntent();
		int flags = i.getFlags();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (Log.DEBUG)
			Log.v("TimerAlarmActivity: onPause()");
		if (wasVisible) {
			ClearAllReceiver.removeCancel(this);
			ManageKeyguard.reenableKeyguard();
			ManageWakeLock.release();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (Log.DEBUG)
			Log.v("TimerAlarmActivity: onStop()");
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			wasVisible = true;
			if (Log.DEBUG)
				Log.v("TimerAlarmActivity: onWindowFocusChanged(true)");
		} else {
			if (Log.DEBUG)
				Log.v("TimerAlarmActivity: onWindowFocusChanged(false)");
		}
	}
}