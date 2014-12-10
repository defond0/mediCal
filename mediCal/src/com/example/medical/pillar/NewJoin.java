package com.example.medical.pillar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.medical.Calibrate;
import com.example.medical.R;
import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;
import com.example.medical.db.PillPrescriptionJoin;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;
import com.example.medical.db.PrescriptionPillJoinDataAccessor;

import java.util.List;

public class NewJoin extends Activity {
    private PrescriptionDataAccessor PresDA;
    private PillDataAccessor PillDA;
    private PrescriptionPillJoinDataAccessor JoinDA;
    private Spinner s;
    private TimePicker tp;
    private TextView times, pills;
    private EditText nameText;
    private String currentPillName;
    private Pill currentPill;
    private PillPrescriptionJoin currentJoin;
    private String currentTimes;
    private Prescription currentPrescription;
    private long prescriptionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_join);


        PresDA = new PrescriptionDataAccessor(this);
        PresDA.open();
        PillDA = new PillDataAccessor(this);
        PillDA.open();
        JoinDA = new PrescriptionPillJoinDataAccessor(this);
        JoinDA.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            prescriptionId = extras.getLong(Prescriptions.ID);
            currentPrescription=PresDA.getPrescriptionbyId(prescriptionId);
        }

        List<Pill> pills = PillDA.getAllPills();

        s = (Spinner)findViewById(R.id.pillSpinner);
        ArrayAdapter<Pill> adapter = new ArrayAdapter<Pill>(this,
                android.R.layout.simple_spinner_item, pills);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        System.out.println(adapter + "   "+s);
        s.setAdapter(adapter);


                s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object currentObject = parent.getItemAtPosition(position);
                currentPill = (Pill)currentObject;
                currentPillName = parent.getItemAtPosition(position).toString();
                System.out.println("Current pill "+currentPillName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tp = (TimePicker)findViewById(R.id.timePicker);

    }

    public void onClick(View v){
        String cur;
        switch (v.getId()){
            case R.id.addPill:
                times = (TextView)findViewById(R.id.pillTimes);
                pills = (TextView)findViewById(R.id.pills);
                cur = pills.getText().toString();
                pills.setText(cur + " "+currentPill.getName()+",");
                currentTimes=getTime();
                currentJoin = JoinDA.createPPJoin((int)currentPill.getId(),(int)currentPrescription.getId(),parseTime(currentTimes));
                System.out.println(JoinDA.getAllJoins());
                resetTime();
                Intent intent = new Intent(NewJoin.this, ShowPrescription.class);
                intent.putExtra(Prescriptions.PATIENT, currentPrescription.getPatient());
                intent.putExtra(Prescriptions.RFID, currentPrescription.getRfid());
                intent.putExtra(Prescriptions.ID, currentPrescription.getId());
                startActivity(intent);
                this.onStop();
                break;

        }

    }

    public String getName(){
        nameText = (EditText)findViewById(R.id.nameText);
        return nameText.getText().toString();
    }


    public String getTime(){
        return tp.getCurrentHour()+":"+tp.getCurrentMinute();
    }

    public String parseTime(String s){
        System.out.println(s);
        System.out.println(s.replace("Times:", ""));
        return s.replace("Times:","");
    }

    public void resetTime(){
        currentTimes = "Times:";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_join, menu);
        return true;
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
