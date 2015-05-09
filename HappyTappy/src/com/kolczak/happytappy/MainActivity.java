package com.kolczak.happytappy;

import java.util.Locale;

import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.views.FrictionMapView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 0;
	private FrictionMapView fricView;
	private TextToSpeech myTTS;

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
		Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spacialgrad);
		
		// Set the friction data bitmap to the test image
		fricView.setDataBitmap(defaultBitmap);
	}

	private void findLayoutElements() {
		this.fricView = (FrictionMapView) findViewById(R.id.activity_main_fraction_map_view);
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
    
    public void startSpeaking(View view) {
    	this.myTTS.speak("This app is amazing!", TextToSpeech.QUEUE_FLUSH, null);
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
	    }
		else if (initStatus == TextToSpeech.ERROR) {
		    Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
		}
	}
	
	protected void onStop() {
		this.myTTS.shutdown();
	}
}
