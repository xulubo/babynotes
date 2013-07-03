package cn.year11.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.decoder.Base64InputStream;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.email.codec.binary.Base64;

import cn.year11.json.BinaryJSONObject;


public class JSONObjectGetter implements ValueGetter{

	private JSONObject mJson;
	
	public JSONObjectGetter(BinaryJSONObject json)
	{
		mJson = json;
	}
	
	@Override
	public Long getLong(String name) {
		try {
			long v =  mJson.getLong(name);
			return v;
		} catch (JSONException e) {
		}
		return 0L;
	}

	@Override
	public Integer getInt(String name) {
		try {
			return mJson.getInt(name);
		} catch (JSONException e) {
		}
		return 0;
	}

	@Override
	public String getString(String name) {
		try {
			return mJson.getString(name);
		} catch (JSONException e) {
		}
		return null;	
	}

	@Override
	public byte[] getBlob(String name) {
		try {
			String s = mJson.getString(name);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());
			Base64InputStream base64In = new Base64InputStream(in);
			IOUtils.copy(base64In, out);
			return out.toByteArray();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
	

}
