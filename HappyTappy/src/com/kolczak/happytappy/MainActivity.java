package com.kolczak.happytappy;

import java.util.ArrayList;
import java.util.Locale;

import com.kolczak.happytappy.DrawingFrictionMapView.IDrawingStageListener;

import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.views.FrictionMapView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnInitListener, IDrawingStageListener {

    private static final int MY_DATA_CHECK_CODE = 0;
	private DrawingFrictionMapView fricView;
	private TextToSpeech myTTS;
	private TextView poemLine1;
	private TextView poemLine2;
	private TextView poemLine3;
	private TextView poemLine4;
	private ArrayList<TextView> lines = new ArrayList<TextView>();
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findLayoutElements();
        initTPad();
        
        checkTTS();
    }
	
    private void checkTTS() {
    	Intent checkTTSIntent = new Intent();
    	checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
    	startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	}
    
	private void initTPad() {
    	TPadImpl mTpad = new TPadImpl(this);
    	fricView.setTpad(mTpad);
		
		// Load an image from resources
		Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.haptic_background_1);
		
		// Set the friction data bitmap to the test image
		fricView.setDataBitmap(defaultBitmap);
		fricView.setDisplayShowing(false);
		fricView.setDataDisplayed(false);
//		fricView.setBackgroundResource(android.R.color.white);
	}

	private void findLayoutElements() {
		this.fricView = (DrawingFrictionMapView) findViewById(R.id.activity_main_fraction_map_view);
		this.fricView.setDrawingStageListener(this);
		this.poemLine1 = (TextView) findViewById(R.id.activity_main_line1);
		this.lines.add(this.poemLine1);
		this.poemLine2 = (TextView) findViewById(R.id.activity_main_line2);
		this.lines.add(this.poemLine2);
		this.poemLine3 = (TextView) findViewById(R.id.activity_main_line3);
		this.lines.add(this.poemLine3);
		this.poemLine4 = (TextView) findViewById(R.id.activity_main_line4);
		this.lines.add(this.poemLine4);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void startDrawing(View view) {
        startActivity(new Intent(this, DrwaingActivity.class));
    }
    
    public void startSpeaking() {
    	speakLine(0);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {      
                this.myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

	@Override
	public void onInit(int initStatus) {
		if (initStatus == TextToSpeech.SUCCESS) {
	        if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE) {
	        	this.myTTS.setLanguage(Locale.US);
	        }
	        Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					startSpeaking();
				}
			}, 1000);
	        
	    }
		else if (initStatus == TextToSpeech.ERROR) {
		    Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
		}
	}
	
	protected void onStop() {
		super.onStop();
		this.myTTS.shutdown();
	}

	@Override
	public void onDrawingStageDone(int stageId) {
		speakLine(stageId);
	}

	private void speakLine(int stageId) {
		
		if (stageId > this.lines.size())
			return;
		
		this.lines.get(stageId).setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
    	this.myTTS.speak(this.lines.get(stageId).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
    	
    	if (stageId + 1 == this.lines.size()) {
    		Handler handler = new Handler();
    		handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					startActivity(new Intent(MainActivity.this, VideoActivity.class));
				}
			}, 1000);
    	}
	}
}
