package cn.year11.babynote.utils;

import android.text.TextUtils;

public class LongUtils {
	public static long valueOf(String s)
	{
		if(!TextUtils.isEmpty(s)) {
			return Long.valueOf(s);
		}
		
		return 0;
	}
}
