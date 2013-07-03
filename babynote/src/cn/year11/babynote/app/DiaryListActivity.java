package cn.year11.babynote.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import cn.year11.babynote.R;
import cn.year11.babynote.dialog.DiaperDialog;
import cn.year11.babynote.dialog.DoseDialog;
import cn.year11.babynote.dialog.FeedingDialog;
import cn.year11.babynote.dialog.GrowthDialog;
import cn.year11.babynote.dialog.HealthDialog;
import cn.year11.babynote.dialog.VaccineDialog;
import cn.year11.babynote.provider.Attachment;
import cn.year11.babynote.provider.AttachmentDao;
import cn.year11.babynote.provider.DatabaseHelper;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.email.ImapMessageStore;
import cn.year11.babynote.provider.email.ImapMessageStore.AuthenticationErrorException;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.utils.FileUtils;
import cn.year11.babynote.utils.MediaUtils;
import cn.year11.babynote.widget.VoicePlayer;
import cn.year11.babynote.widget.VoiceRecorder;

import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class DiaryListActivity extends BaseActivity implements OnClickListener {

	private static final int REQUEST_CODE_TAKE_PICTURE = 1;
	private static final int MINUTE5 = 5*60*1000;
	ImageButton mMedicineButton;
	ImageButton mDiaperButton;
	ImageButton mFeedingButton;
	ImageButton mHealthButton;
	ImageButton mVaccineButton;
	ImageButton mGrowthButton;
	Button mSaveNote;
	Button mTakePicture;
	Button mRecordVoice;
	Button mSync, mRestore;
	TextView mVoiceInfo;
	
	com.markupartist.android.widget.PullToRefreshListView mEventList;
	EventDao mEventDao;
	DiaryListAdapter mListAdapter;
	
	MultiAutoCompleteTextView mSimpleNote;
	private VoiceRecorder mVoiceRecorder = VoiceRecorder.getInstance();
	private VoicePlayer mVoicePlayer = VoicePlayer.getInstance();
	
	private String mPicPath;
	private Event mLastEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_diary_list);

		setupView();

		mEventDao = new EventDao();

		Cursor cursor = mEventDao.findAllDate(null);
		startManagingCursor(cursor);

		mListAdapter = new DiaryListAdapter(this, cursor, null) {
			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
				scrollToBottom();
			}
		};
		mEventList.setAdapter(mListAdapter);
		mEventList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
		scrollToBottom();
	}

	
	@Override
	protected void onStop() {
		if (mVoiceRecorder.isRecording())
			mVoiceRecorder.stop();
		super.onStop();
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		scrollToBottom();

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		scrollToBottom();

	}


	public void setupView() {
		mMedicineButton = (ImageButton) findViewById(R.id.take_medicine);
		mDiaperButton = (ImageButton) findViewById(R.id.change_diaper);
		mFeedingButton = (ImageButton) findViewById(R.id.feeding);
		mHealthButton = (ImageButton) findViewById(R.id.health_condition);
		mVaccineButton = (ImageButton) findViewById(R.id.take_vaccine);
		mGrowthButton = (ImageButton) findViewById(R.id.growth_data);
		mSaveNote = (Button) findViewById(R.id.save_note);
		mTakePicture = (Button)findViewById(R.id.take_picture);
		mRecordVoice = (Button)findViewById(R.id.record_voice);
		mSimpleNote = (MultiAutoCompleteTextView) findViewById(R.id.note);
		mSync = (Button)findViewById(R.id.sync);
		mRestore = (Button)findViewById(R.id.restore);

		mMedicineButton.setOnClickListener(this);
		mDiaperButton.setOnClickListener(this);
		mFeedingButton.setOnClickListener(this);
		mHealthButton.setOnClickListener(this);
		mVaccineButton.setOnClickListener(this);
		mGrowthButton.setOnClickListener(this);
		mSaveNote.setOnClickListener(this);
		mEventList = (com.markupartist.android.widget.PullToRefreshListView) findViewById(R.id.eventlist);
		mVoiceInfo = (TextView)findViewById(R.id.voice_info);
		View.OnClickListener onTakePictureListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPicPath = FileUtils.getUniquePhotoPath();
				MediaUtils.startCameraActivity(mThisActivity, REQUEST_CODE_TAKE_PICTURE, mPicPath);
			}
		};
		
		final VoiceRecorder.OnTimeListener onTimeListener = new VoiceRecorder.OnTimeListener() {
			
			@Override
			public void onTime(VoiceRecorder recorder) {
				long t = recorder.getElapsedTime();
				int a = recorder.getMaxAmplitude();
				mVoiceInfo.setText("振幅: " + a + " 时间:" + t);
			}
			
			@Override
			public void onStop() {
				mRecordVoice.setText("开始录音");
			}
			
			@Override
			public void onStart() {
				mRecordVoice.setText("录音中");
			}
		};
		View.OnClickListener onRecordVoiceListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mVoiceRecorder.isRecording()) {
					mVoiceRecorder.stop();
					Attachment att = new Attachment(Attachment.CONTENT_TYPE_VOICE, mVoiceRecorder.getVoicePath());
					saveAttachment(att);
				}
				else {
					mVoiceRecorder.start();
				}
			}
		};
		
		mTakePicture.setOnClickListener(onTakePictureListener);
		mRecordVoice.setOnClickListener(onRecordVoiceListener);
		mVoiceRecorder.setOnTimeListener(onTimeListener);
		
		mSync.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					ImapMessageStore store = new ImapMessageStore();
					store.backup();
				} catch (AuthenticationErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		
		switch (requestCode) {
		case REQUEST_CODE_TAKE_PICTURE:
			Bitmap thumbnail = MediaUtils.getThumbnailImageFromIntent(data);
			if (thumbnail != null) {
				Attachment att = new Attachment(thumbnail, mPicPath);
				saveAttachment(att);
			}

			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	public void scrollToBottom() {
		mEventList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mEventList.invalidateViews();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_medicine:
			// Intent inten=new Intent();
			// inten.setClass(this, TakeMedicineActivity.class);
			// this.startActivity(inten);
			new DoseDialog(this).show();
			break;

		case R.id.change_diaper:
			new DiaperDialog(this).show();
			break;

		case R.id.feeding:
			new FeedingDialog(this).show();
			break;

		case R.id.take_vaccine:
			new VaccineDialog(this).show();
			break;

		case R.id.health_condition:
			new HealthDialog(this).show();
			break;

		case R.id.growth_data:
			new GrowthDialog(this).show();
			// Intent intent = new Intent(this, EventEditActivity.class);
			// intent.putExtra("type", "growth");
			// startActivity(intent);
			break;

		case R.id.save_note:
			onSaveNote();
			break;
		}
	}

	public void onSaveNote() {
		if (mSimpleNote.getText().length()==0) {
			return;
		}
		
		Event e;
		if (mLastEvent != null 
				&& TextUtils.isEmpty(mLastEvent.getEventDetail()) 
				&& System.currentTimeMillis()-mLastEvent.getBeginTime()<MINUTE5)
		{
			e = mLastEvent;
		}
		else {
			e = new Event();
			e.setTime(System.currentTimeMillis());
			e.setEventType("note");
		}
		e.setEventDetail(mSimpleNote.getText().toString());

		EventDao.getInstance().saveOrUpdate(e);
		mLastEvent = e;
		mSimpleNote.setText("");
	}

	public void saveAttachment(Attachment att) {
		
		//thumbnail = Bitmap.createScaledBitmap(thumbnail, 150, 150, true);
		
		if (needNewEvent()) {
			mLastEvent = EventDao.getInstance().createEmptyEvent("note");
		}
		
		if (mLastEvent == null) {
			Toast.makeText(this, "保存失败", 1).show();
			return;
		}
		
		att.setEventId(mLastEvent.getId());
		AttachmentDao.getInstance().saveOrUpdate(att);
		mListAdapter.notifyDataSetChanged();
	}
	
	public boolean needNewEvent()
	{
		if (mLastEvent == null) {
			return true;
		}
		
		if (System.currentTimeMillis() - mLastEvent.getBeginTime() > MINUTE5)
		{
			return true;
		}
		
		return false;
	}
	
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            ImapMessageStore store;
			try {
				store = new ImapMessageStore();
				store.restore();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {

            // Call onRefreshComplete when the list has been refreshed.
            mEventList.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
}
