package cn.year11.babynote.app;

import java.io.InputStream;

import cn.year11.babynote.R;
import cn.year11.babynote.utils.FileUtils;
import cn.year11.babynote.utils.MediaUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EventEditActivity extends Activity {

	LinearLayout imageHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);


	}

}
