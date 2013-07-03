package cn.year11.babynote.utils;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class ImageUtils {
	public static Bitmap drawableToBitmap(Drawable drawable)// drawable
															// ת����bitmap
	{
		int width = drawable.getIntrinsicWidth(); // ȡdrawable�ĳ���
		int height = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565; // ȡdrawable����ɫ��ʽ
		Bitmap bitmap = Bitmap.createBitmap(width, height, config); // ������Ӧbitmap
		Canvas canvas = new Canvas(bitmap); // ������Ӧbitmap�Ļ���
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas); // ��drawable���ݻ���������
		return bitmap;
	}

	public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap oldbmp = drawableToBitmap(drawable);// drawableת����bitmap
		Matrix matrix = new Matrix(); // ��������ͼƬ�õ�Matrix����
		float scaleWidth = ((float) w / width); // �������ű���
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight); // �������ű���
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
				matrix, true); // �����µ�bitmap���������Ƕ�ԭbitmap�����ź��ͼ
		return new BitmapDrawable(newbmp); // ��bitmapת����drawable������
	}

	public static Drawable createScaledDrawable(Drawable drawable, int w, int h)
	{
		Bitmap bitmap = drawableToBitmap(drawable);
		bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
		return new BitmapDrawable(bitmap);
	}
	
	public static Bitmap decodeBitmapFile(String filename, int inSampleSize)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;//ͼƬ��߶�Ϊԭ����inSampleSize��֮һ
		Bitmap b = BitmapFactory.decodeFile(filename, options);
		return b;
	}
	
    public static Bitmap drawable2Bitmap(Drawable drawable){  
        if(drawable instanceof BitmapDrawable){  
            return ((BitmapDrawable)drawable).getBitmap() ;  
        }else if(drawable instanceof NinePatchDrawable){  
            Bitmap bitmap = Bitmap  
                    .createBitmap(  
                            drawable.getIntrinsicWidth(),  
                            drawable.getIntrinsicHeight(),  
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                    : Bitmap.Config.RGB_565);  
            Canvas canvas = new Canvas(bitmap);  
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                    drawable.getIntrinsicHeight());  
            drawable.draw(canvas);  
            return bitmap;  
        }else{  
            return null ;  
        }  
    }  
}
