package com.example.medical.pillar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medical.Calibrate;
import com.example.medical.R;
import com.example.medical.db.Pill;
import com.example.medical.db.PillDataAccessor;

public class ShowPill extends Activity implements Pullup.OnFragmentInteractionListener {
    private PillDataAccessor pda;
    private Pill pill;
    private TextView addName,addTube,addDose,addLoad;
    private long pillId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pill);
        pda = new PillDataAccessor(this);
        pda.open();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            pillId = extras.getLong(Prescriptions.ID);
            pill=pda.getPillById(pillId);
        }
        addDose = (TextView)findViewById(R.id.addDose);
        addTube = (TextView)findViewById(R.id.addTube);
        addName = (TextView)findViewById(R.id.addName);
        addLoad = (TextView)findViewById(R.id.addLoad);

        addDose.setText("Dose of: "+pill.getDose());
        addTube.setText("In Tube: "+pill.getTube());
        addName.setText(pill.getName());
        addLoad.setText("With "+pill.getLoad()+" Pills");

    }
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.edit:
                Intent intent = new Intent(ShowPill.this, EditPill.class);
                intent.putExtra(Prescriptions.ID, pillId);
                startActivity(intent);
                this.onStop();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_pill, menu);
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
