package com.example.medical.pillar;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.medical.Calibrate;
import com.example.medical.R;
import com.example.medical.db.PillPrescriptionJoin;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;
import com.example.medical.db.PrescriptionPillJoinDataAccessor;

import java.util.ArrayList;
import java.util.Arrays;

public class ShowPrescription extends ListActivity implements Pullup.OnFragmentInteractionListener  {
    private final static byte[] tag1 = new byte[]{0x1A, (byte)0xE2, 0x41, (byte)0xD9, 0x00, 0x00, 0x00, 0x3B};
    private final static byte[] tag2 = new byte[]{(byte)0x4A, (byte)0x8B, (byte)0x41, (byte)0xD9, 0x00, 0x00, 0x00, 0x3B};
    private TextView Rfid;
    private TextView Patient;
    private TextView banner;
    private long prescriptionId;
    String patient;
    byte[] rfid;
    private ArrayList joins;
    private PrescriptionPillJoinDataAccessor joindDA;
    public final static String ID="id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        joindDA = new PrescriptionPillJoinDataAccessor(this);
        joindDA.open();
        setContentView(R.layout.activity_show_prescription);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patient = extras.getString(Prescriptions.PATIENT);
            rfid = extras.getByteArray(Prescriptions.RFID);
            prescriptionId = extras.getLong(Prescriptions.ID);
        }
        Patient = (TextView)findViewById(R.id.patient);
        Patient.setText("Patient Name: "+patient);
        Rfid = (TextView)findViewById(R.id.rfid);
        if(Arrays.equals(rfid, tag1)){
            Rfid.setText("RFID: 1");
        }
        else{
            Rfid.setText("RFID: 2");
        }
        joins = (ArrayList)joindDA.getJoinsByPrescriptionId(prescriptionId);
        System.out.println("Loading "+joins);
        ArrayAdapter<Prescription> adapter = new ArrayAdapter<Prescription>(this,
                android.R.layout.simple_list_item_1, joins);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        System.out.println("CLICKED "+joins.get(position));
        PillPrescriptionJoin join =(PillPrescriptionJoin)joins.get(position);
        Intent intent = new Intent(ShowPrescription.this, ShowJoin.class);
        intent.putExtra(ID, join.getId());
        startActivity(intent);
        this.onStop();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.edit:
                Intent intent = new Intent(ShowPrescription.this, EditPrescriptions.class);
                intent.putExtra(Prescriptions.PATIENT, patient);
                intent.putExtra(Prescriptions.RFID, rfid);
                intent.putExtra(Prescriptions.ID, prescriptionId);
                startActivity(intent);
                this.onStop();
                break;
            case R.id.addJoin:
                Intent intentj = new Intent(ShowPrescription.this, NewJoin.class);
                intentj.putExtra(Prescriptions.PATIENT, patient);
                intentj.putExtra(Prescriptions.RFID, rfid);
                intentj.putExtra(Prescriptions.ID, prescriptionId);
                startActivity(intentj);
                this.onStop();
                break;
        }

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
        getMenuInflater().inflate(R.menu.show_prescription, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println("Fragment uri "+ uri);
    }
}
