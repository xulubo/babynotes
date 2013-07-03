package cn.year11.babynote.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

public class MediaUtils {
    public static final String IMAGE_UNSPECIFIED = "image/*";  
    
	public static void startSelectImageActivity(Activity activity, int requestCode)
	{
        Intent intent = new Intent(Intent.ACTION_PICK, null);  

        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);  

        activity.startActivityForResult(intent, requestCode);  
	}
	
	public static void startCameraActivity(Activity activity, int requestCode, String path) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);

		File out = new File(path);
		Uri uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void startCameraActivity(Activity activity, int requestCode, String dir, String filename) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);

		File out = new File(dir, filename);
		Uri uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static Bitmap getThumbnailImageFromIntent(Intent intent)
	{
  	  	Bitmap thumbnail = (Bitmap)intent.getParcelableExtra("data");
  	  	return thumbnail;
	}

	/**
	 * 拍摄视频
	 */
	public static Intent videoIntent() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		return intent;
	}

	/**
	 * 录音功能
	 */
	public static void startSoundRecorderActivity(Activity parent) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/amr");
		parent.startActivity(intent);
	}
	
    static public void cropImage(Activity activity, Uri imageUri, int requestCode) {  

        Intent intent = new Intent("com.android.camera.action.CROP");  

        intent.setDataAndType(imageUri, IMAGE_UNSPECIFIED);  

        intent.putExtra("crop", "true");  

        // aspectX aspectY 是宽高的比例   

        intent.putExtra("aspectX",1);  

        intent.putExtra("aspectY",1);  

        // outputX outputY 是裁剪图片宽高   

        intent.putExtra("outputX",100);  

        intent.putExtra("outputY",100);  

        intent.putExtra("return-data", true);  

        activity.startActivityForResult(intent, requestCode);  

    }  
    
    static public void showVideo(Activity parent, String videoDir)
    {
	    Intent intent=new Intent(Intent.ACTION_VIEW);
	    intent.setDataAndType(Uri.parse("file://"+videoDir), "video/*");
	    parent.startActivity(intent);
    }
    
    static public void showPicture(Context parent, String picDir)
    {
    	Intent intent=new Intent(Intent.ACTION_VIEW);
     	 intent.setDataAndType(Uri.parse("file://"+picDir), "image/*");
     	parent.startActivity(intent);
    }
    
}
