package com.example.medical.pillar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.medical.Calibrate;
import com.example.medical.R;
import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;
import com.example.medical.db.PillPrescriptionJoin;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;
import com.example.medical.db.PrescriptionPillJoinDataAccessor;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowJoin extends Activity implements Pullup.OnFragmentInteractionListener {
    private long id;
    private PrescriptionPillJoinDataAccessor joinDataAccessor;
    private PillDataAccessor pillDataAccessor;
    private PrescriptionDataAccessor prescriptionDataAccessor;
    private PillPrescriptionJoin join;
    private Pill pill;
    private Prescription prescription;
    private TextView pillname,pilltime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_join);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong(Prescriptions.ID);
        }
        joinDataAccessor = new PrescriptionPillJoinDataAccessor(this);
        pillDataAccessor = new PillDataAccessor(this);
        prescriptionDataAccessor = new PrescriptionDataAccessor(this);
        prescriptionDataAccessor.open();
        pillDataAccessor.open();
        joinDataAccessor.open();
        join = joinDataAccessor.getJoinbyId(id);
        pill = pillDataAccessor.getPillById(join.getPillId());
        prescription = prescriptionDataAccessor.getPrescriptionbyId(join.getPrescriptionId());
        TextView v = (TextView)findViewById(R.id.banner);
        v.setText("Patient: "+prescription.getPatient());
        pillname = (TextView)findViewById(R.id.pill);
        pilltime = (TextView)findViewById(R.id.time);
        pillname.setText("Pill :"+pill.getName());
        pilltime.setText("At: "+join.getTime());

    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.Edit:
                Intent intent = new Intent(ShowJoin.this, EditJoin.class);
                intent.putExtra(Prescriptions.ID, id);
                startActivity(intent);
                this.onStop();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_join, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
