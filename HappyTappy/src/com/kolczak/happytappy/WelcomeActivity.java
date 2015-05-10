package com.kolczak.happytappy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class WelcomeActivity extends Activity{
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
 
        View logo = findViewById(R.id.activity_welcome_logo);
        
        logo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
			}
		});
    }
}
