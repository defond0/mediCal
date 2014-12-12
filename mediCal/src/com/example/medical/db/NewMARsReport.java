package com.example.medical.db;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.medical.Calibrate;
import com.example.medical.R;
import com.example.medical.pillar.Prescriptions;
import com.example.medical.pillar.Pullup;

public class NewMARsReport extends Activity implements Pullup.OnFragmentInteractionListener {
    private MarsDataAccessor marsDataAccessor;
    public EditText addFacilityName,addPhysicianName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mars_report);
        marsDataAccessor = new MarsDataAccessor(this);
        marsDataAccessor.open();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_mars_report, menu);
        return true;
    }

    public void onClick(View v) {
        addFacilityName = (EditText)findViewById(R.id.addFacilityName);
        addPhysicianName = (EditText)findViewById(R.id.addPhysician);
        switch (v.getId()) {
            case R.id.add:
                MarReport report = marsDataAccessor.createMARsReport(addFacilityName.getText().toString(),addPhysicianName.getText().toString());
                Intent i = new Intent(this, MARManager.class);
                startActivity(i);
                this.onStop();
                break;
        }
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

    public void toPrescriptions(View v){
        Intent i = new Intent(this, Prescriptions.class);
        startActivity(i);
        this.onStop();
    }

    public void toCalibrate(View v){
        Intent i = new Intent(this, Calibrate.class);
        startActivity(i);
        this.onStop();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
