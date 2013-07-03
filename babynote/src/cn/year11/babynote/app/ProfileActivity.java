package cn.year11.babynote.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.year11.babynote.R;
import cn.year11.babynote.logic.VaccineReminder;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.Profile;
import cn.year11.babynote.provider.ProfileDao;
import cn.year11.babynote.utils.DateUtils;
import cn.year11.babynote.utils.MediaUtils;

// UI design
public class ProfileActivity extends BaseActivity {
	private final int TAKE_PICTURE = 1;
	private final int CROP_PICTURE = 2;
	private final int SELECT_PICTURE = 3;

	private String[] item = { "男", "女 " };
	TextView mDate, mTime;
	EditText mName, mHospital, mBirthday, mHeight, mWeight;
	Spinner mGender;
	ImageView mPortrait;
	ProfileDao mDao;
	Profile mProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_profile_layout);
		mDao = new ProfileDao();

		setupView();
		bindData();

	}

	public void setupView() {
		mGender = (Spinner) findViewById(R.id.gender);
		mName = (EditText) findViewById(R.id.name);
		mHospital = (EditText) findViewById(R.id.hospital);
		mHeight = (EditText) findViewById(R.id.height);
		mWeight = (EditText) findViewById(R.id.weight);
		mPortrait = (ImageView) findViewById(R.id.portrait);
		mDate = (TextView) findViewById(R.id.birth_date);
		mTime = (TextView) findViewById(R.id.birth_time);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, item);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mGender.setAdapter(adapter);

		// process selecting picture or taking photo event
		mPortrait.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(mThisActivity).setItems(
						new CharSequence[] { "拍照", "选取" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									MediaUtils.startCameraActivity(
											mThisActivity, TAKE_PICTURE,
											"/sdcard/", "12345.jpg");
									break;
								case 1:
									MediaUtils.startSelectImageActivity(
											mThisActivity, SELECT_PICTURE);
									break;
								}
							}

						}).show();
			}

		});

		// process saving profile event
		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveProfile();
				finish();
			}

		});

		findViewById(R.id.cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	public void bindData() {
		mProfile = mDao.getProfile();

		if (mProfile != null) {
			mName.setText(mProfile.getName());
			mPortrait.setImageDrawable(mProfile.getPortraitDrawable());
			mHeight.setText(String.valueOf(mProfile.getHeight()));
			mWeight.setText(String.valueOf(mProfile.getWeight()));
			mHospital.setText(mProfile.getHospital());
			mGender.setSelection(mProfile.getGender().equals("男") ? 0 : 1);
		}

		final long birthday = mProfile == null ? System.currentTimeMillis()
				: mProfile.getBirthday();

		mDate.setText(DateUtils.formatDate(birthday));
		mTime.setText(DateUtils.formatTime(birthday));
		mDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDateDialog(birthday);
			}

		});

		mTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimeDialog(birthday);
			}

		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, "" + resultCode, 1).show();
		
		if (data == null) {
			return;
		}
		
		switch (requestCode) {
		case TAKE_PICTURE:
			//Bitmap bitmap = MediaUtils.getThumbnailImageFromIntent(data);
			//bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
			//mPortrait.setImageBitmap(bitmap);
			MediaUtils.cropImage(this,
					Uri.fromFile(new File("/sdcard", "12345.jpg")),
					CROP_PICTURE);
			break;

		case SELECT_PICTURE:
			MediaUtils.cropImage(this, data.getData(), CROP_PICTURE);
			break;

		case CROP_PICTURE:
			Bundle extras = data.getExtras();

			if (extras != null) {

				Bitmap photo = extras.getParcelable("data");
				if (photo != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();

					photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
					mPortrait.setImageBitmap(photo);
				}
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	private void saveProfile() {
		Profile profile = mProfile == null ? new Profile() : mProfile;
		
		// remove sync flag
		profile.setSyncTime(0);
		profile.setUpdateTime(System.currentTimeMillis());
		profile.setName(mName.getText().toString());
		profile.setGender(mGender.getSelectedItem().toString());
		profile.setHospital(mHospital.getText().toString());
		profile.setBirthday(DateUtils.parseDate(mDate.getText().toString(),
				mTime.getText().toString()).getTime());
		profile.setHeight(Integer.valueOf(mHeight.getText().toString()));
		profile.setWeight(Integer.valueOf(mWeight.getText().toString()));
		profile.setPortrait(mPortrait.getDrawable());

		if (mProfile == null) {
			mProfile = profile;
			long babyId = mDao.save(profile);
			VaccineReminder.getInstance(mThisActivity).addReminderForKid(babyId);
		} else {
			mDao.update(mProfile.getId(), profile);
		}
	}

	private void showDateDialog(long time) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		if (time <= 0) {
			time = System.currentTimeMillis();
		}

		calendar.setTime(new Date(time));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		OnDateSetListener onDateSetListener = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mDate.setText("" + year + "-" + (monthOfYear + 1) + "-"
						+ dayOfMonth);
			}
		};
		new DatePickerDialog(ProfileActivity.this, onDateSetListener, year,
				month, day).show();
	}

	private void showTimeDialog(long time) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		if (time <= 0) {
			time = System.currentTimeMillis();
		}

		calendar.setTime(new Date(time));
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mTime.setText(hourOfDay + ":" + minute);

			}
		};
		new TimePickerDialog(ProfileActivity.this, onTimeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true).show();
	}

}
