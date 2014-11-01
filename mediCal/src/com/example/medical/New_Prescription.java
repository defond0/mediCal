package com.example.medical;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;


public class New_Prescription extends ActionBarActivity {
    private PrescriptionDataAccessor PresDA;
    private PillDataAccessor PillDA;
    private Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PresDA = new PrescriptionDataAccessor(this);
        PresDA.open();
        PillDA = new PillDataAccessor(this);
        PillDA.open();
        setContentView(R.layout.activity_new_prescriptions);
        List<Pill> pills = PillDA.getAllPills();

        s = (Spinner)findViewById(R.id.pillSpinner);
        ArrayAdapter<Pill> adapter = new ArrayAdapter<Pill>(this,
        android.R.layout.simple_spinner_item, pills);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println(adapter + "   "+s);
        s.setAdapter(adapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new__prescription, menu);
        return true;
    }

    public void onClick(View v){
        EditText patient = (EditText)findViewById(R.id.nameText);
        switch (v.getId()){
            case R.id.addTime:

                break;
            case R.id.addPill:
                break;
            case R.id.save:

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
}
