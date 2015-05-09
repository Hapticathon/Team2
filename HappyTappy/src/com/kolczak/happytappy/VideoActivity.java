package com.kolczak.happytappy;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	private MediaController mediaControls;
	private VideoView videoView;
	private int position = 0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        findLayoutElements();
        startVideo();
    }

	private void findLayoutElements() {
		this.videoView = (VideoView)findViewById(R.id.activity_video_video_view);
	}
	
	private void startVideo() {
		if (this.mediaControls == null) {
			this.mediaControls = new MediaController(this);
		}
		
		this.videoView.setMediaController(this.mediaControls);
		this.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.animated_dog));
		
		this.videoView.requestFocus();
		
		//we also set an setOnPreparedListener in order to know when the video file is ready for playback
		this.videoView.setOnPreparedListener(new OnPreparedListener() {
		 
		    public void onPrepared(MediaPlayer mediaPlayer) {
		        // close the progress bar and play the video
		        //if we have a position on savedInstanceState, the video playback should start from here
		        videoView.seekTo(position);
		        if (position == 0) {
		        	videoView.start();
		        } else {
		            //if we come from a resumed activity, video playback will be paused
		        	videoView.pause();
		        }
		    }
		});

	}
	
	@Override
	 public void onSaveInstanceState(Bundle savedInstanceState) {
	     super.onSaveInstanceState(savedInstanceState);
	     //we use onSaveInstanceState in order to store the video playback position for orientation change
	     savedInstanceState.putInt("Position", this.videoView.getCurrentPosition());
	     this.videoView.pause();
	 }
	 @Override
	 public void onRestoreInstanceState(Bundle savedInstanceState) {
	     super.onRestoreInstanceState(savedInstanceState);
	     //we use onRestoreInstanceState in order to play the video playback from the stored position 
	     this.position = savedInstanceState.getInt("Position");
	     this.videoView.seekTo(this.position);
	 }
}
