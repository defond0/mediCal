package com.example.medical.pillar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.medical.Calibrate;
import com.example.medical.R;
import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;

public class EditPill extends Activity {
    public EditText addName,addTube,addDose,addLoad;
    private PillDataAccessor pda;
    private Pill pill;
    private long pillId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pill);
        pda = new PillDataAccessor(this);
        pda.open();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            pillId = extras.getLong(Prescriptions.ID);
            pill=pda.getPillById(pillId);
        }
        addDose = (EditText)findViewById(R.id.addDose);
        addTube = (EditText)findViewById(R.id.addTube);
        addName = (EditText)findViewById(R.id.addName);
        addLoad = (EditText)findViewById(R.id.addLoad);

        addDose.setText(pill.getDose());
        addTube.setText(pill.getTube());
        addName.setText(pill.getName());
        addLoad.setText(pill.getLoad());
    }

    public void onClick(View v){
        Intent intent = new Intent(EditPill.this, Calibrate.class);
        switch (v.getId()){
            case R.id.delete:
                startActivity(intent);
                pda.deletePillSafe(pillId);
                this.onStop();
                break;
            case R.id.save:
                startActivity(intent);
                pda.updatePill(pillId,addName.getText().toString(),addTube.getText().toString(),addDose.getText().toString(),addLoad.getText().toString());
                this.onStop();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_pill, menu);
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
}
