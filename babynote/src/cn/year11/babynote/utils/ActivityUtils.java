package cn.year11.babynote.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityUtils {
	static public void startActivity(Context context, Class<?> cl)
	{
		Intent i = new Intent(context, cl);
		context.startActivity(i);
	}
	
	static public void startActivityForResult(Activity parent, Class<?> cl, int requestCode)
	{
		Intent i = new Intent(parent, cl);
		parent.startActivityForResult(i, requestCode);
	}
	
}
