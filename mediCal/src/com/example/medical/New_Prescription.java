package com.example.medical;

import android.app.Activity;
import android.content.Intent;
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
import com.example.medical.pillar.NewJoin;
import com.example.medical.pillar.Prescriptions;

import java.util.List;


public class New_Prescription extends Activity  {
    private PrescriptionDataAccessor PresDA;
    private PillDataAccessor PillDA;
    private PrescriptionPillJoinDataAccessor JoinDA;
    private NumberPicker np;
    private EditText nameText;
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

        np = (NumberPicker)findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(2);
        np.computeScroll();

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
            case R.id.save:
                currentPrescription = PresDA.createPrescription(getName(),getRfid());
                Intent intentP = new Intent(New_Prescription.this, Prescriptions.class);
                startActivity(intentP);
                this.onStop();
                break;
            case R.id.saveload:
                currentPrescription = PresDA.createPrescription(getName(),getRfid());
                Intent intentJ = new Intent(New_Prescription.this, NewJoin.class);
                intentJ.putExtra(Prescriptions.ID, currentPrescription.getId());
                startActivity(intentJ);
                this.onStop();
                break;

        }

    }

    public String parseTime(String s){
        System.out.println(s);
        System.out.println(s.replace("Times:", ""));
        return s.replace("Times:","");
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
                b = new byte[]{0x1A, (byte)0xE2, 0x41, (byte)0xD9, 0x00, 0x00, 0x00, 0x3B};
            break;
            case 2:
                b = new byte[]{(byte)0xAA, 0x79, (byte)0x9B, 0x23, 0x00, 0x00, 0x00, 0x3B};
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
