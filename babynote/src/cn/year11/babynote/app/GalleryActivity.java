package cn.year11.babynote.app;

import cn.year11.babynote.R;
import cn.year11.babynote.provider.Attachment;
import cn.year11.babynote.provider.AttachmentDao;
import cn.year11.content.CursorGetter;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class GalleryActivity extends Activity implements
		OnItemSelectedListener, ViewFactory {
	private Gallery gallery;
	private ImageSwitcher imageSwitcher;
	private ImageAdapter imageAdapter;
	private Cursor mCursor;

	private int getPosition(int position) {
		int curPos = position % mCursor.getCount();
		return curPos;
	}

	private Drawable getDrawable(int position) {
		mCursor.moveToPosition(getPosition(position));
		Attachment att = new Attachment(new CursorGetter(mCursor));
		return att.getThumbnailDrawable();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// 选中Gallery中某个图像时，在ImageSwitcher组件中放大显示该图像
		imageSwitcher.setImageDrawable(getDrawable(position));

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	// ImageSwitcher组件需要这个方法来创建一个View对象（一般为ImageView对象）
	// 来显示图像
	public View makeView() {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundColor(0xFF000000);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return imageView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		gallery = (Gallery) findViewById(R.id.gallery);
		mCursor = AttachmentDao.getInstance().findAllByContentType(Attachment.CONTENT_TYPE_IMAGE);
		if (mCursor.getCount() > 0) {
			imageAdapter = new ImageAdapter(this, mCursor);
			gallery.setAdapter(imageAdapter);
			gallery.setOnItemSelectedListener(this);
			imageSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);
			// 设置ImageSwitcher组件的工厂对象
			imageSwitcher.setFactory(this);
			// 设置ImageSwitcher组件显示图像的动画效果
			imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in));
			imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
					android.R.anim.fade_out));
		}
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		Cursor mCursor;

		public ImageAdapter(Context context, Cursor cursor) {
			mContext = context;
			mCursor = cursor;

			TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
			mGalleryItemBackground = typedArray.getResourceId(
					R.styleable.Gallery_android_galleryItemBackground, 0);
		}

		// 第1点改进，返回一个很大的值，例如，Integer.MAX_VALUE
		public int getCount() {
			return mCursor.getCount(); //Integer.MAX_VALUE;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			int curPos = position % mCursor.getCount();
			mCursor.moveToPosition(curPos);
			Attachment att = new Attachment(new CursorGetter(mCursor));

			ImageView imageView = new ImageView(mContext);
			// 第2点改进，通过取余来循环取得resIds数组中的图像资源ID
			imageView.setImageDrawable(att.getThumbnailDrawable());
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			Gallery.LayoutParams params = new Gallery.LayoutParams(183, 150);
			imageView.setLayoutParams(params);
			imageView.setPadding(0, 0, 33, 0);
			//imageView.setBackgroundResource(mGalleryItemBackground);
			return imageView;
		}
	}
}