package com.example.medical.pillar;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.medical.Calibrate;
import com.example.medical.New_Prescription;
import com.example.medical.R;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;

import java.util.List;

public class Prescriptions extends ListActivity {
    private PrescriptionDataAccessor PDA;
    private mediCalBle pillar;
    private boolean bound;
    private  List<Prescription> prescriptions;

    public final static String PATIENT="Patient";
    public final static String RFID="Rfid";
    public final static String ID="id";

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mediCalBle.MedicalBinder binder = (mediCalBle.MedicalBinder) service;
            System.out.print("Bing");
            pillar = binder.getService();
            bound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        PDA = new PrescriptionDataAccessor(this);
        PDA.open();

        prescriptions = PDA.getAllPrescriptions();
        ArrayAdapter<Prescription> adapter = new ArrayAdapter<Prescription>(this,
                android.R.layout.simple_list_item_1, prescriptions);
        setListAdapter(adapter);

		setContentView(R.layout.activity_prescriptions);


	}



    public void onClick(View v){
        switch (v.getId()){
            case R.id.add:
                Intent i = new Intent(this, New_Prescription.class);
                startActivity(i);
                this.onStop();
                break;
            case R.id.bleNotify:
                pillar.enableDispensing();
                break;
            case R.id.bleConnect:
                if (pillar.getBtAdapter() == null || !pillar.getBtAdapter().isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 3);
                }
                else {
                    pillar.bleSetup();
                }
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Bind to mediCalBle
        Intent intent = new Intent(this, mediCalBle.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (bound) {
            unbindService(connection);
            bound = false;
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
            System.out.println("CLICKED "+prescriptions.get(position));
            Prescription prescription = prescriptions.get(position);
            Intent intent = new Intent(Prescriptions.this, ShowPrescription.class);
            intent.putExtra(PATIENT, prescription.getPatient());
            intent.putExtra(RFID, prescription.getRfid());
            intent.putExtra(ID, prescription.getId());
            startActivity(intent);

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reminders, menu);
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
