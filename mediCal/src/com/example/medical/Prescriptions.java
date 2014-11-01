package com.example.medical;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Prescriptions extends ListActivity {
    private PrescriptionDataAccessor PDA;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        PDA = new PrescriptionDataAccessor(this);
        PDA.open();

        List<Prescription> prescriptions = PDA.getAllPrescriptions();

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
