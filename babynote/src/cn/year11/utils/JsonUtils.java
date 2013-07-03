package cn.year11.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.decoder.Base64InputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.year11.babynote.provider.Attachment;
import cn.year11.babynote.provider.BaseEntity;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.event.Event;
import cn.year11.content.JSONObjectGetter;
import cn.year11.json.BinaryJSONObject;


import android.content.ContentValues;


public class JsonUtils {
	private static Log _logger = Log.getLogger(JsonUtils.class);
	public static String toString(BaseEntity e) throws JSONException
	{
		ContentValues values = e.getValues();
		BinaryJSONObject json = new BinaryJSONObject();
        Set<Map.Entry<String, Object>> entrySet = values.valueSet();
        Iterator<Map.Entry<String, Object>> entriesIter = entrySet.iterator();
 
        while (entriesIter.hasNext()) {
            Map.Entry<String, Object> entry = entriesIter.next();
            json.put(entry.getKey(), entry.getValue());
        }

		_logger.d(json.toString());
		return json.toString();
	}
	
	public static Event parseEvent(String s)
	{
		Event event = null;
		
		try {
			BinaryJSONObject json = new BinaryJSONObject(s);
			event = new Event(new JSONObjectGetter(json));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return event;
	}

	public static Profile parseProfile(String s)
	{
		Profile profile = null;
		
		try {
			BinaryJSONObject json = new BinaryJSONObject(s);
			profile = new Profile(new JSONObjectGetter(json));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return profile;
	}
	
	public static Attachment parseAttachment(String string) {
		Attachment att = null;
		try {
			BinaryJSONObject json = new BinaryJSONObject(string);
			att = new Attachment(new JSONObjectGetter(json));
		} catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return att;
	}
	

}
