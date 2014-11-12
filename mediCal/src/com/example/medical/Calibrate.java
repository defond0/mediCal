package com.example.medical;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;

import java.util.List;

public class Calibrate extends ListActivity {
    private PillDataAccessor PDA;
    public EditText addName;
    public EditText addTube;
    public EditText addDose;
    public EditText addLoad;
    public EditText deleteName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        PDA = new PillDataAccessor(this);
        PDA.open();



        List<Pill> pills = PDA.getAllPills();

        ArrayAdapter<Pill> adapter = new ArrayAdapter<Pill>(this,
                android.R.layout.simple_list_item_1, pills);
        setListAdapter(adapter);

		setContentView(R.layout.activity_calibrate);
	}

    public void onClick(View v){
        addName = (EditText)findViewById(R.id.addName);
        addDose = (EditText)findViewById(R.id.addDose);
        addTube = (EditText)findViewById(R.id.addTimes);
        addLoad = (EditText)findViewById(R.id.addLoad);
        deleteName = (EditText)findViewById(R.id.deleteName);
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
            case R.id.delete:
                pill = PDA.getPillByName(deleteName.getText().toString());
                adapter.remove(pill);
                PDA.deletePill(pill);
                List<Pill> pills = PDA.getAllPills();
                ArrayAdapter<Pill> adapter0 = new ArrayAdapter<Pill>(this,
                        android.R.layout.simple_list_item_1, pills);
                setListAdapter(adapter0);
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
