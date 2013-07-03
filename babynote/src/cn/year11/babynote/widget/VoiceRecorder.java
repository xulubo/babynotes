package cn.year11.babynote.widget;

import cn.year11.babynote.utils.FileUtils;
import cn.year11.utils.Log;
import android.media.MediaRecorder;
import android.os.Handler;

public class VoiceRecorder {
	private static Log _logger = Log.getLogger(VoiceRecorder.class);
	private static VoiceRecorder _instance;
	
	private MediaRecorder mMediaRecorder;
	
	public static interface OnTimeListener {
		public void onStart(); 
		public void onStop(); 
		public void onTime(VoiceRecorder recorder); 
	}
	
	private OnTimeListener onTimeListener = null; 
	private Handler mHandler = new Handler(); 
	private long mStartTime = 0; 
	private long mStopTime = 0; 
	private boolean mRecording = false; 
	private String mVoicePath;
	
	static public VoiceRecorder getInstance()
	{
		if (_instance == null) {
			_instance = new VoiceRecorder();
		}
		
		return _instance;
	}
	
	public boolean isRecording() {
		return mRecording; 
	}
	
	public void setOnTimeListener(OnTimeListener l) {
		onTimeListener = l; 
	}
	/**
	 * @description Start record the voice
	 */
	public String start() {
		String strPath = null;
		if (null == mMediaRecorder) {
			try {
				mVoicePath = FileUtils.getUniqueVoicePath();
				mMediaRecorder = new MediaRecorder();
				mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
				mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
				mMediaRecorder.setOutputFile(mVoicePath);
				mMediaRecorder.prepare();
				mMediaRecorder.start();
				mStartTime = System.currentTimeMillis(); 
				mStopTime = 0; 
				
				mRecording = true; 
				
				if (onTimeListener != null) 
					onTimeListener.onStart(); 
				
				postDelayed(); 
			} catch (Exception e) {
				_logger.e("VoiceRecorder:start()", e);
				e.printStackTrace();
			}
		}
		return strPath;
	}

	/**
	 * @description Stop record the voice
	 */
	public void stop() {
		if (null != mMediaRecorder) {
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
			
			mStopTime = System.currentTimeMillis(); 
			
			if (onTimeListener != null) 
				onTimeListener.onStop(); 
		}
		mRecording = false;
	}

	private void postDelayed() {
		if (!mRecording) return; 
		
		if (onTimeListener == null) 
			return; 
		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				handleDelayed(); 
			}
		}, 200); 
	}
	
	private void handleDelayed() {
		if (!mRecording) return; 
		
		if (onTimeListener != null) 
			onTimeListener.onTime(this); 
		
		postDelayed(); 
	}
	
	public long getElapsedTime() {
		long current = mStopTime; 
		if (current <= 0) 
			current = System.currentTimeMillis(); 
		
		return current - mStartTime; 
	}
	

	//
	public int getMaxAmplitude() {
		if (mMediaRecorder != null) 
			return mMediaRecorder.getMaxAmplitude(); 
		else
			return 0; 
	}
	
	public String getVoicePath()
	{
		return mVoicePath;
	}
}
