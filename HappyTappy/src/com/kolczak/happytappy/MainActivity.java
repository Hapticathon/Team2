package com.kolczak.happytappy;

import nxr.tpad.lib.TPadImpl;
import nxr.tpad.lib.views.FrictionMapView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    private FrictionMapView fricView;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findLayoutElements();
        initTPad();
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
}
