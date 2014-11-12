package com.example.medical.pillar;

import android.app.ListActivity;
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

import com.example.medical.New_Prescription;
import com.example.medical.R;
import com.example.medical.db.Prescription;
import com.example.medical.db.PrescriptionDataAccessor;
import com.example.medical.pillar.mediCalBle;

import java.util.List;

public class Prescriptions extends ListActivity {
    private PrescriptionDataAccessor PillDA;
    private mediCalBle pillar;
    private boolean bound;

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
        PillDA = new PrescriptionDataAccessor(this);
        PillDA.open();

        List<Prescription> prescriptions = PillDA.getAllPrescriptions();
        ArrayAdapter<Prescription> adapter = new ArrayAdapter<Prescription>(this,
                android.R.layout.simple_list_item_1, prescriptions);
        setListAdapter(adapter);
		setContentView(R.layout.activity_prescriptions);
	}



    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
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
                pillar.initialize();
                pillar.bleSetup();
                break;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
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
