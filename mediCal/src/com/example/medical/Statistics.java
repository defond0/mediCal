package com.example.medical;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Statistics extends Activity {
    private boolean scanning;
    private boolean discovered;
    private BluetoothGattService s;
    private BluetoothGattCharacteristic c;
    private boolean connected;
    private Handler handler;
    private BluetoothDevice mediCal;
    private BluetoothAdapter btAdapter;
    private static final long SCAN_PERIOD = 1000;
    private EditText number;
    private TextView banner;
    private BluetoothGatt gatt;
    private final BluetoothGattCallback btCb = new BluetoothGattCallback() {
    };
    private UUID serviceUUID =    UUID.fromString("b0bb5820-5a0d-11e4-93ee-0002a5d5c51b");

    private UUID rotateCharUUID = UUID.fromString("fb71bcc0-5a0c-11e4-91ae-0002a5d5c51b");

    //"7a77be20-5a0d-11e4-a95e-0002a5d5c51b"


    private void scan(final boolean enable, final Statistics s) {
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
                    if((device.getName()!=null)&&(device.getName().toLowerCase().equals("medical ble"))
                            && connected==false){
                        mediCal = device;
                        banner.setText(mediCal.getName());
                    }
                }
            });

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
        banner= (TextView) findViewById(R.id.banner);
        mediCal=null;
        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 3);
        }
        else{
            scan(true,this);
        }
	}

    public void bleSetup(View v){
        System.out.println("Beginning to Connect");
        gatt = mediCal.connectGatt(this,true,btCb);
        gatt.discoverServices();
        connected = true;
    }

    public void submit(View v) {
        System.out.println("Beginning submission");
        number = (EditText) findViewById(R.id.rotateText);
        String n = number.getText().toString();
        System.out.println("We want to rotate by " +n);
        if( gatt.discoverServices()){
            System.out.println("Services Discovered");
            s = gatt.getService(serviceUUID);
            System.out.println("s "+s);
            if (s==null){
                System.out.println("null s");
                this.submit(v);
                return;
            }
            c = s.getCharacteristic(rotateCharUUID);
            if (c==null){
                System.out.println("null c");
                this.submit(v);
                return;
            }
            int i = Integer.getInteger(n);
            c.setValue(Integer.toHexString(i));
            System.out.println("Writing "+ Integer.toHexString(i));
            gatt.writeCharacteristic(c);
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

    @Override
    public void onDestroy(){
        if (gatt!=null) {
            gatt.close();
        }
        super.onDestroy();
    }
}
