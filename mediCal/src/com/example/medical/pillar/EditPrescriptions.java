package com.example.medical.pillar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.medical.Calibrate;
import com.example.medical.R;
import com.example.medical.db.PrescriptionDataAccessor;

import java.util.Arrays;

public class EditPrescriptions extends Activity {
    private final static byte[] tag1 = new byte[]{0x1A, (byte)0xE2, 0x41, (byte)0xD9, 0x00, 0x00, 0x00, 0x3B};
    private final static byte[] tag2 = new byte[]{(byte)0xAA, 0x79, (byte)0x9B, 0x23, 0x00, 0x00, 0x00, 0x3B};
    private NumberPicker np;
    private EditText etPatient;
    private TextView banner;
    private long prescriptionId;
    private PrescriptionDataAccessor prescriptionDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prescriptionDA = new PrescriptionDataAccessor(this);
        prescriptionDA.open();
        setContentView(R.layout.activity_edit_prescriptions);
        Bundle extras = getIntent().getExtras();
        String patient = null;
        byte[] rfid = null;
        if (extras != null) {
            patient = extras.getString(Prescriptions.PATIENT);
            rfid = extras.getByteArray(Prescriptions.RFID);
            prescriptionId = extras.getLong(Prescriptions.ID);
        }
        etPatient = (EditText)findViewById(R.id.patient);
        banner = (TextView)findViewById(R.id.banner);
        banner.setText("Editing "+patient);
        etPatient.setText(patient);
        np = (NumberPicker)findViewById(R.id.RfidPicker);
        np.setMinValue(1);
        np.setMaxValue(2);
        if(Arrays.equals(rfid,tag1)){
            np.setValue(1);
        }
        else{
            np.setValue(2);
        }
        np.computeScroll();


    }

    public void onClick(View v){
        Intent intent = new Intent(EditPrescriptions.this, Prescriptions.class);
        switch (v.getId()){
            case R.id.delete:
                prescriptionDA.deletePrescriptionSafe(prescriptionId);
                startActivity(intent);
                this.onStop();
                break;
            case R.id.save:
                switch (np.getValue()){
                    case 1:
                        System.out.println(getName());
                        prescriptionDA.updatePrescription(prescriptionId, getName(), tag1);
                        break;
                    case 2:
                        prescriptionDA.updatePrescription(prescriptionId, getName(), tag2);
                        break;
                }
                startActivity(intent);
                this.onStop();
                break;
        }
    }

    public String getName(){
        etPatient = (EditText)findViewById(R.id.patient);
        return etPatient.getText().toString();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_prescriptions, menu);
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
