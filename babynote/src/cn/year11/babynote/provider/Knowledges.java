package cn.year11.babynote.provider;

import android.content.Context;
import android.provider.BaseColumns;

public class Knowledges extends TableQuery implements BaseColumns {

	private static final String TABLE_NAME = "knowledge";
	private static final String _ID = "id";
	private static final String CONTENT = "content";
	private static final String DATE = "date";
	private static final String SOURCE = "source";
	private static Knowledges mInstance = null;
	
	public Knowledges(Context c)
	{
		super(TABLE_NAME, DatabaseHelper.getInstance(c));
	}
	
	public static String createSQL()
	{
		return "CREATE TABLE " + TABLE_NAME + "("
				+ _ID + " integer,"
				+ DATE + " integer,"
				+ SOURCE + " text,"
				+ CONTENT + " text);";
	}
	
    public Knowledges getInstance(Context c)
    {
    	if (mInstance == null) {
    		mInstance = new Knowledges(c);
    	}
    	
    	return mInstance;
    }
}
