package cn.year11.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.decoder.Base64InputStream;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.email.codec.binary.Base64OutputStream;

public class BinaryJSONObject extends JSONObject {
	
	public BinaryJSONObject() {
		super();
	}
	
	public BinaryJSONObject(String s) throws JSONException {
		super(s);
	}

	public JSONObject put(String name, byte[] value) throws JSONException, IOException {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(value);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		Base64OutputStream base64Out = new Base64OutputStream(byteOut, true);
		IOUtils.copy(byteIn, base64Out);
		return super.put(name, byteOut.toString());
	}
	
	public byte[] getBytes(String name) throws IOException, JSONException {
		String str = getString(name);
		if (str == null || str.length() == 0) {
			return null;
		}
		
		ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes());
		Base64InputStream base64In = new Base64InputStream(byteIn);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		IOUtils.copy(base64In, byteOut);
		return byteOut.toByteArray();
	}
}
