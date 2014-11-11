package com.example.medical;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class Statistics extends Activity {
    private boolean scanning;
    private boolean written;
    private boolean discovered;
    private BluetoothGattService s;
    private BluetoothGattCharacteristic c;
    private BluetoothManager btManager;
    private boolean connected;
    private Handler handler;
    private BluetoothDevice mediCal;
    private BluetoothAdapter btAdapter;
    private static final long SCAN_PERIOD = 1000;
    private EditText number;
    private TextView banner;
    private BluetoothGatt gatt;

    private final BluetoothGattCallback btCb = new BluetoothGattCallback() {
    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic btchar){
        System.out.println("CALLBACK");
        c = btchar;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //int rfid = c.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16,0);
                byte[] b = c.getValue();



                String rfid= c.getStringValue(0);
                int r = 100;
                if(b[0]==(byte)0x12){
                    rfid = "Pill 1";
                    r=r*(-1);
                }
                else{
                    rfid= "Pill 2";
                    //rotate(100);
                }
                banner.setText(rfid);
                rotate(r);


            }
        });
    }


    };

    private final UUID serviceUUID =    UUID.fromString("b0bb5820-5a0d-11e4-93ee-0002a5d5c51b");

    private final UUID rotateCharUUID = UUID.fromString("fb71bcc0-5a0c-11e4-91ae-0002a5d5c51b");

    private final UUID rfidUUID = UUID.fromString("7a77be20-5a0d-11e4-a95e-0002a5d5c51b");

    private final UUID notificationUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");



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

    public void rotate(int n){
        final int q = n;
        boolean write = writeCharacteristic(q);
        while(!write){
            write = writeCharacteristic(q);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean w =writeCharacteristic((-1) * q);
                while(!w) {
                    w = writeCharacteristic((-1) * q);
                }
            }
        },500);

    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = null;
        c = null;
        written=false;
        connected = false;
        scanning=true;
        btManager = (BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE);
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
            mediCal.fetchUuidsWithSdp();
            gatt = mediCal.connectGatt(this, true, btCb);
            discovered = gatt.discoverServices();
            connected = true;
    }

    public void submit(View v) {
        number = (EditText) findViewById(R.id.rotateText);
        String n = number.getText().toString();
        int i = Integer.valueOf(n) ;
        System.out.println(i);
        System.out.println("We want to rotate by " +i);
        if( gatt.discoverServices()){
            System.out.println("Services Discovered");
            boolean write=writeCharacteristic(i);
            while(write==false){
                write = this.writeCharacteristic(i);
            }
        }


    }



    public void listen(View v){
        System.out.println("Now Setting Notifications on");
        if (gatt.discoverServices()) {
            boolean write = this.enableNotification();
            while (write == false) {
                write = this.enableNotification();
            }
            System.out.println("write " + write);
        }


    }

    public BluetoothGattService grabService(UUID uuid, BluetoothGatt gatt){
        BluetoothGattService service = gatt.getService(uuid);
        if(gatt.discoverServices()) {
            System.out.println("Trying to find Service");
            while (service == null) {
                service = gatt.getService(uuid);
            }
            return service;
        }
        else{return null;}


    }

    public BluetoothGattDescriptor grabDescriptor(UUID uuid, BluetoothGattCharacteristic c){
        BluetoothGattDescriptor descriptor = c.getDescriptor(uuid);
        System.out.println("Trying to find Descriptor");
        if (gatt.discoverServices()) {
            while(descriptor==null){
                descriptor = c.getDescriptor(uuid);
            }
            return descriptor;
        }
        else{return null;}
    }

    public BluetoothGattCharacteristic grabCharacteristic(UUID uuid, BluetoothGattService s){
        BluetoothGattCharacteristic characteristic = s.getCharacteristic(uuid);
        System.out.println("Trying to find Characteristic");
        if (gatt.discoverServices()){
            System.out.println("Trying to find Service");
            while (characteristic == null) {
                characteristic = s.getCharacteristic(uuid);
            }
            return  characteristic;
        }
        else{ return null;}
    }

    public boolean enableNotification(){
        if (gatt == null||btAdapter==null) {
            System.out.println("no setup");
            return false;
        }
        s = gatt.getService(serviceUUID);
        if (s == null) {
            System.out.println("Null Service");
            return false;
        }
        c = s.getCharacteristic(rfidUUID);
        if (c == null) {
            System.out.println("Null Characteristic");
            return false;
        }
        BluetoothGattDescriptor d = c.getDescriptor(notificationUUID);
        if(d ==null){
            System.out.println("Null Descriptor");
            return false;
        }
        gatt.setCharacteristicNotification(c, true);
        d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        boolean write = gatt.writeDescriptor(d);
        return write;
    }

    public boolean writeCharacteristic(int n) {
        if (gatt == null||btAdapter==null) {
            System.out.println("no setup");
            return false;
        }
        s = gatt.getService(serviceUUID);
        if (s == null) {
            System.out.println("Null Service");
            return false;
        }
        c = s.getCharacteristic(rotateCharUUID);
        if (c == null) {
            System.out.println("Null Characteristic");
            return false;
        }
        c.setValue(n,BluetoothGattCharacteristic.FORMAT_SINT16,0);//Character.toString((char) Integer.parseInt(n)));
        boolean status = gatt.writeCharacteristic(c);
        System.out.println("Write Status "+ status);
        return status;
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
            gatt.disconnect();
            gatt.close();
            gatt = null;
        }
        super.onDestroy();
    }
}
