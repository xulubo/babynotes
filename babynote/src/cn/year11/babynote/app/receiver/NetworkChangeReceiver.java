package cn.year11.babynote.app.receiver;

import cn.year11.babynote.logic.BackupManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		BackupManager.check();
	}

}
