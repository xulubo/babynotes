package cn.year11.babynote.widget;

import java.io.IOException;

import cn.year11.utils.Log;


import android.media.MediaPlayer;

public final class VoicePlayer implements MediaPlayer.OnPreparedListener, 
		MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, 
		MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, 
		MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener {
	private static Log _logger = Log.getLogger(VoicePlayer.class);
	private static final VoicePlayer _Instance = new VoicePlayer(); 
	
	public static VoicePlayer getInstance() {
		return _Instance; 
	}
	
	public static interface OnStatusUpdateListener {
		public void onPrepared(); 
		public void onBufferingUpdate(int percent); 
		public void onCompletion(); 
		public void onPaused(); 
		public void onStopped(); 
		public void onStarted(); 
		public void onTimed(VoicePlayer player); 
	}
	
	private MediaPlayer mMediaPlayer = null; 
	private OnStatusUpdateListener mListener = null; 
	private OnStatusUpdateListener mSavedListener = null; 
	private String mPath = null; 
	
	private DelayedThread mThread = new DelayedThread(); 
	private boolean mPlaying = false; 
	
	private class DelayedThread extends Thread {
		private Object mLock = new Object(); 
		private boolean mRunning = false; 

		public void run() {
			mRunning = true; 
			while (!Thread.interrupted()) {
				if (mMediaPlayer == null && !mPlaying) {
					synchronized (mLock) {
						try { mLock.wait(); } catch (InterruptedException e) {} 
					}
				}
				try { Thread.sleep(200); } catch (InterruptedException e) {} 
				onThreadTimed(); 
			}
			mRunning = false; 
		}
		
		public void notifyPlaying() {
			if (!isAlive()) {
				start(); return; 
			}
			synchronized (mLock) {
				mLock.notifyAll(); 
			}
		}
	}
	
	private VoicePlayer() { }
	
	private MediaPlayer createPlayer() {
		MediaPlayer player = new MediaPlayer(); 
		
		player.setOnPreparedListener(this); 
		player.setOnBufferingUpdateListener(this); 
		player.setOnCompletionListener(this); 
		player.setOnSeekCompleteListener(this); 
		player.setOnVideoSizeChangedListener(this); 
		player.setOnErrorListener(this); 
		player.setOnInfoListener(this); 
		
		return player; 
	}
	
	public String getPath() {
		return mPath; 
	}
	
	public boolean prepare(String path, OnStatusUpdateListener l) {
		if (path == null || path.length() == 0) 
			return false; 
		
		synchronized (this) {
			try {
				stop(); 
				
				mPath = path; 
				mListener = l; 
				
				mMediaPlayer = createPlayer(); 
				mMediaPlayer.setDataSource(path);
				mMediaPlayer.prepare();
				
				mSavedListener = l; 
				
				return true; 
				
			} catch (IllegalStateException e) {
				_logger.e("prepare audio: "+path+" error", e);
			} catch (IOException e) {
				_logger.e("prepare audio: "+path+" error", e);
			}
		}
		
		mListener = null; 
		
		return false; 
	}
	
	public boolean attachListener(OnStatusUpdateListener l) {
		synchronized (this) {
			if (l != null) {
				mListener = l; 
				mSavedListener = l; 
				
				if (isPlaying()) 
					l.onStarted(); 
				else
					l.onStopped(); 
				
				return true; 
			}
		}
		return false; 
	}
	
	public OnStatusUpdateListener getListener() {
		return mListener; 
	}
	
	private OnStatusUpdateListener getSavedListener() {
		synchronized (this) {
			return mListener != mSavedListener && mSavedListener != null ? 
					mSavedListener : mListener; 
		}
	}
	
	public void start() {
		synchronized (this) {
			if (mMediaPlayer == null) 
				return; 
			
			if (!mMediaPlayer.isPlaying()) {
				mMediaPlayer.start(); 
				mPlaying = true; 
				
				OnStatusUpdateListener l = mListener; 
				if (l != null) 
					l.onStarted(); 
				
				mThread.notifyPlaying(); 
			}
		}
	}
	
	public void stop() {
		synchronized (this) {
			if (mMediaPlayer == null) 
				return; 
			
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop(); 
				mMediaPlayer.release(); 
				mMediaPlayer = null; 
				
				OnStatusUpdateListener l = mListener; 
				if (l != null) 
					l.onStopped(); 
			}
			
			mPlaying = false; 
		}
	}
	
	public void pause() {
		synchronized (this) {
			if (mMediaPlayer == null) 
				return; 
			
			mMediaPlayer.pause(); 
			
			OnStatusUpdateListener l = mListener; 
			if (l != null) 
				l.onPaused(); 
		}
	}
	
	public boolean isPlaying() {
		return mMediaPlayer != null && mMediaPlayer.isPlaying(); 
	}
	
	public int getDuration() {
		return mMediaPlayer != null ? mMediaPlayer.getDuration() : 0; 
	}
	
	public int getCurrentPosition() {
		return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : 0; 
	}
	
	private void onThreadTimed() {
		OnStatusUpdateListener l = getSavedListener(); 
		if (l != null) 
			l.onTimed(this); 
	}
	
	public void onPrepared(MediaPlayer mp) {
		OnStatusUpdateListener l = mListener; 
		if (l != null) 
			l.onPrepared(); 
	}
	
	public void onCompletion(MediaPlayer mp) {
		mPlaying = false; 
		
		OnStatusUpdateListener l = getSavedListener(); 
		if (l != null) 
			l.onCompletion(); 
	}
	
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		OnStatusUpdateListener l = getSavedListener(); 
		if (l != null) 
			l.onBufferingUpdate(percent); 
	}
	
	public void onSeekComplete(MediaPlayer mp) {
		
	}
	
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		
	}
	
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false; 
	}
	
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false; 
	}
	
}
