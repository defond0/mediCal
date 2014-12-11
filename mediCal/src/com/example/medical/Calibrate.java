package com.example.medical;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;
import com.example.medical.db.PillPrescriptionJoin;
import com.example.medical.pillar.Prescriptions;
import com.example.medical.pillar.ShowJoin;
import com.example.medical.pillar.ShowPill;

import java.util.ArrayList;
import java.util.List;

public class Calibrate extends ListActivity {
    private PillDataAccessor PDA;
    public EditText addName,addTube,addDose,addLoad;
    private ArrayList<Pill> pills;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        PDA = new PillDataAccessor(this);
        PDA.open();



        pills = (ArrayList)PDA.getAllPills();

        ArrayAdapter<Pill> adapter = new ArrayAdapter<Pill>(this,
                android.R.layout.simple_list_item_1, pills);
        setListAdapter(adapter);

		setContentView(R.layout.activity_calibrate);
	}

    public void onClick(View v){
        addName = (EditText)findViewById(R.id.addName);
        addDose = (EditText)findViewById(R.id.addDose);
        addTube = (EditText)findViewById(R.id.addTube);
        addLoad = (EditText)findViewById(R.id.addLoad);
        Pill pill = null;
        ArrayAdapter<Pill> adapter = (ArrayAdapter<Pill>) getListAdapter();
        switch(v.getId()){
            case R.id.add:
                pill=PDA.createPill(addName.getText().toString(),
                        addTube.getText().toString(),
                        addDose.getText().toString(),addLoad.getText().toString());
                adapter.add(pill);
                adapter.notifyDataSetChanged();
                break;


        }

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calibrate, menu);
		return true;
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        System.out.println("CLICKED "+pills.get(position));
        Pill p =pills.get(position);
        Intent intent = new Intent(Calibrate.this, ShowPill.class);
        intent.putExtra(Prescriptions.ID, p.getId());
        startActivity(intent);
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
