package com.example.medical;

import android.support.v7.app.ActionBarActivity;
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

import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;
import com.example.medical.db.PillPrescriptionJoin;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;
import com.example.medical.db.PrescriptionPillJoinDataAccessor;

import java.util.List;


public class New_Prescription extends ActionBarActivity {
    private PrescriptionDataAccessor PresDA;
    private PillDataAccessor PillDA;
    private PrescriptionPillJoinDataAccessor JoinDA;
    private Spinner s;
    private NumberPicker np;
    private TimePicker tp;
    private TextView times, pills;
    private EditText nameText;
    private String currentPillName;
    private Pill currentPill;
    private PillPrescriptionJoin currentJoin;
    private String currentTimes;
    private Prescription currentPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PresDA = new PrescriptionDataAccessor(this);
        PresDA.open();
        PillDA = new PillDataAccessor(this);
        PillDA.open();
        JoinDA = new PrescriptionPillJoinDataAccessor(this);
        JoinDA.open();
        setContentView(R.layout.activity_new_prescriptions);
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

        np = (NumberPicker)findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(2);
        np.computeScroll();

        tp = (TimePicker)findViewById(R.id.timePicker);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new__prescription, menu);
        return true;
    }

    public void onClick(View v){
        String cur;
        switch (v.getId()){
            case R.id.addTime:
                times = (TextView)findViewById(R.id.pillTimes);
                cur = times.getText().toString();
                currentTimes = cur+" "+ getTime() + ",";
                times.setText(currentTimes);
                break;
            case R.id.addPill:
                times = (TextView)findViewById(R.id.pillTimes);
                pills = (TextView)findViewById(R.id.pills);
                cur = pills.getText().toString();
                pills.setText(cur + " "+currentPill.getName()+",");
                currentJoin = JoinDA.createPPJoin((int)currentPill.getId(),(int)currentPrescription.getId(),parseTime(currentTimes));
                System.out.println(JoinDA.getAllJoins());
                resetTime();
                break;
            case R.id.save:
                currentPrescription = PresDA.createPrescription(getName(),getRfid());
                break;

        }

    }

    public String parseTime(String s){
        System.out.println(s);
        System.out.println(s.replace("Times:", ""));
        return s.replace("Times:","");
    }

    public String getTime(){
        return tp.getCurrentHour()+":"+tp.getCurrentMinute();
    }

    public void resetTime(){
           currentTimes = "Times:";
    }

    public String getName(){
        nameText = (EditText)findViewById(R.id.nameText);
        return nameText.getText().toString();
    }

    public byte[] getRfid(){
        byte[] b = null;
        System.out.println(np.getValue());
        switch (np.getValue()){
            case 1:
                b = new byte[]{0x1A, (byte)0xE2, 0x41, (byte)0xD9};
            break;
            case 2:
                b= new byte[]{0x04, (byte)0x92, 0x6E, 0x7A, 0x7A, 0x31, (byte)0x80};
            break;
        }
        return b;
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
