package com.example.medical.db;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.medical.Calibrate;
import com.example.medical.New_Prescription;
import com.example.medical.R;
import com.example.medical.pillar.Prescriptions;
import com.example.medical.pillar.Pullup;
import com.example.medical.pillar.ShowPrescription;

import java.util.ArrayList;

public class MARManager extends ListActivity implements Pullup.OnFragmentInteractionListener {
    private MarsDataAccessor marsDataAccessor;
    private ArrayList<MarReport> reports;
    public final static String ID = "id";
    public final static String facilityname= "facility";
    public final static String physicianname= "physician";
    public final static String starttime = "stime";
    public final static String endtime="etime";
    public final static String comments = "comments";




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marmanager);
        marsDataAccessor = new MarsDataAccessor(this);
        marsDataAccessor.open();
        reports = (ArrayList)marsDataAccessor.getAllMARsReports();
        ArrayAdapter<MarReport> adapter = new ArrayAdapter<MarReport>(this,
                android.R.layout.simple_list_item_1, reports);
        setListAdapter(adapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                Intent i = new Intent(this, NewMARsReport.class);
                startActivity(i);
                this.onStop();
                break;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        System.out.println("CLICKED "+reports.get(position));
        MarReport marReport = reports.get(position);
        Intent intent = new Intent(this, ShowMARReport.class);
        intent.putExtra(ID, marReport.getId());
        intent.putExtra(facilityname, marReport.getFacilityName());
        intent.putExtra(physicianname, marReport.getPhysician());
        intent.putExtra(starttime,marReport.getStartDate());
        intent.putExtra(endtime,marReport.getEndDate());
        intent.putExtra(comments,marReport.getComments());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.marmanager, menu);
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
