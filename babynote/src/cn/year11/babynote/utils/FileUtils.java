package cn.year11.babynote.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

public class FileUtils {
	public static String getVoiceDir()
	{
		String voiceDir = Environment.getExternalStorageDirectory()
				.toString() + "/babynote/voice/";// 存放照片的文件夹

		File out = new File(voiceDir);
		if (!out.exists()) {
			out.mkdirs();
		}
		
		return voiceDir;
	}
	
	public static String getImageDir()
	{
		String imagePath = Environment.getExternalStorageDirectory()
				.toString() + "/babynote/photos/";// 存放照片的文件夹

		File out = new File(imagePath);
		if (!out.exists()) {
			out.mkdirs();
		}
		
		return imagePath;
	}
	
	public static String getUniquePhotoPath()
	{
		String path = getImageDir();
		
		String filename = new SimpleDateFormat("yyyyMMddHHmmss")
		.format(new Date()) + ".jpg";// 照片命名
		
		return path + filename;
	}
	
	public static String getUniqueVoicePath()
	{
		String path = getVoiceDir();
		
		String filename = new SimpleDateFormat("yyyyMMddHHmmss")
		.format(new Date()) + ".amr";// 照片命名
		
		return path + filename;
	}
	
	public static File getTmpDir()
	{
		String imagePath = Environment.getExternalStorageDirectory()
				.toString() + "/babynote/tmp/";// 存放照片的文件夹

		File out = new File(imagePath);
		if (!out.exists()) {
			out.mkdirs();
		}
		
		return out;
	}
}
