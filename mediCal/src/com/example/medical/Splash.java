package com.example.medical;

import android.support.v7.app.ActionBarActivity;
import android.content.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.medical.pillar.Prescriptions;


public class Splash extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
    
    public void toPrescriptions(View v){
    	Intent i = new Intent(this, Prescriptions.class);
    	startActivity(i);
    	this.onStop();
    }
    
    public void toStatistics(View v){
    	Intent i = new Intent(this, Statistics.class);
    	startActivity(i);
    	this.onStop();
    }
    
    public void toCalibrate(View v){
    	Intent i = new Intent(this, Calibrate.class);
    	startActivity(i);
    	this.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
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
}
