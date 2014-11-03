package com.example.medical;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
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
    private boolean connected;
    private Handler handler;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothDevice mediCal;
    private BluetoothAdapter btAdapter;
    private static final long SCAN_PERIOD = 10000;
    private EditText number;
    private TextView banner;
    private BluetoothGatt gatt;
    private BluetoothGattService gattService;


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothGattService.ACTION_GATT_CONNECTED.equals(action)) {
                connected = true;
                invalidateOptionsMenu();
            } else if (BluetoothGattService.ACTION_GATT_DISCONNECTED.equals(action)) {
                connected = false;
                invalidateOptionsMenu();
            }
        }
    };

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
            devices = null;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if ((device.getName()!=null)) {
                        banner.setText(device.getName());
                        devices.add(device);
                    }
                }
            });
            int s=devices.size();
            for (int i =0;i<s;i++){
                BluetoothDevice d = devices.get(i);
                System.out.println("-----");
                System.out.print("device "+d);
                System.out.println(" device name " + d.getName());
                System.out.println(" device size "+ devices.size());
                if (d.getName().toLowerCase()=="medical"){
                    banner.setText(device.getName());
                    mediCal= d;
                }
            }

        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        connected = false;
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
            if (mediCal!=null){
                gattService = new BluetoothGattService();
                gatt = mediCal.connectGatt(this,false,gattService.gattCallback);
            }
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
