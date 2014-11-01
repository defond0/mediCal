package com.example.medical;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Statistics extends Activity {

    private boolean scanning;
    private Handler handler;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothDevice mediCal;
    private BluetoothAdapter btAdapter;
    private static final long SCAN_PERIOD = 10000;
    private EditText number;
    private TextView banner;

    private void scan(final boolean enable) {
        if (enable) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    btAdapter.stopLeScan(leScanCallback);
                }

            },SCAN_PERIOD);
            scanning = true;
            btAdapter.startLeScan(leScanCallback);
        }else{
            scanning = false;
            btAdapter.stopLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback(){
        @Override
        public void onLeScan (final BluetoothDevice device, int rssi, byte[] scanRecord ){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("-----");
                    if ((device.getName()!=null)){
                        banner.setText(device.getName());
                        mediCal=device;
                        System.out.print("device "+device);
                        System.out.println(" device name " + device.getName());
                    }
                }
            });
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        final BluetoothManager btManager = (BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
        btAdapter = btManager.getAdapter();
        handler = new Handler();
        number= (EditText) findViewById(R.id.rotateText);
        banner= (TextView) findViewById(R.id.banner);
        mediCal=null;
        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 3);
        }
        else{
            scan(true);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
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
