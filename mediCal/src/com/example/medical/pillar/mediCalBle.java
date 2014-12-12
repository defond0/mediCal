package com.example.medical.pillar;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.medical.db.Pill;

import java.util.ArrayList;
import java.util.UUID;

public class mediCalBle extends Service {
    private IBinder binder = new MedicalBinder();




    private boolean scanning;

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    private boolean connected;
    private BluetoothGattService s;
    private BluetoothGattCharacteristic c;
    private BluetoothManager btManager;
    private BluetoothDevice pillar;

    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    private BluetoothAdapter btAdapter;
    private Handler handler;
    private static final long SCAN_PERIOD = 1000;
    private BluetoothGatt gatt;
    private long lastCallbackTime;

    private ArrayList<Pill> pills;

    public class MedicalBinder extends Binder {
        mediCalBle getService() {
            // Return this instance of mediCalBle so clients can call public methods
            return mediCalBle.this;
        }
    }

    private final BluetoothGattCallback btCb = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
               connected = true;
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
              connected =false;
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic btchar){

            long dif = Math.abs(System.currentTimeMillis()-lastCallbackTime);
            System.out.println("Callback dif "+dif);
            if(dif>=2000) {
                c = btchar;
                byte[] b = c.getValue();
                ArrayList<Integer> pillsToDispense = dc.dispense(b);
                for (int i = 0; i < pillsToDispense.size(); i++) {
                    int dispensing = pillsToDispense.get(i);
                    System.out.println("Dispensing " + dispensing);
                    dispenseTube(dispensing);
                    lastCallbackTime = System.currentTimeMillis();
                }
            }
            else{
                System.out.println("BOUNCE");
                System.out.println("Callback bounce dif "+dif);
                lastCallbackTime = System.currentTimeMillis();
            }



        }
    };

    private final UUID serviceUUID =    UUID.fromString("b0bb5820-5a0d-11e4-93ee-0002a5d5c51b");

    private final UUID rotateCharUUID = UUID.fromString("fb71bcc0-5a0c-11e4-91ae-0002a5d5c51b");

    private final UUID rfidUUID = UUID.fromString("7a77be20-5a0d-11e4-a95e-0002a5d5c51b");

    private final UUID notifyDescUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    private DispenserCoordinator dc;

    public mediCalBle() {
    }

    public void onCreate(){
        lastCallbackTime = System.currentTimeMillis();
        dc=new DispenserCoordinator(this);

        s = null;
        c = null;
        connected = false;
        scanning=true;
        btManager = (BluetoothManager)
                getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        handler = new Handler();
        pillar=null;
        if (btAdapter == null || !btAdapter.isEnabled()) {
            System.out.println("Bluetooth is of not disabled");
        }
        else{
            scan(true,this);
        }

    }

    public void bleSetup(){
        System.out.println("Beginning to Connect");
        Log.v("Connect","Now Beginning to Connect");
        gatt = pillar.connectGatt(this, true, btCb);
        connected = true;
    }

    public void enableDispensing(){
        Log.v("Dispensing","Now Setting Dispensing on");
        if (gatt.discoverServices()) {
            boolean write = this.enableNotification();
            while (write == false) {
                write = this.enableNotification();
            }
            System.out.println("Success " + write);
            Log.v("dispense","Success " + write);

        }


    }

    public void dispenseTube(int n){
        final int q = n;
        boolean write = writeCharacteristic(q);
        while(!write){
            write = writeCharacteristic(q);
        }
    }

    private boolean enableNotification(){

        if (gatt == null||btAdapter==null) {
            System.out.println("no setup");
            return false;
        }
        s = gatt.getService(serviceUUID);
        if (s == null) {
            System.out.println("Null Service");
            Log.v("nullservice","Null Service");
            return false;
        }
        c = s.getCharacteristic(rfidUUID);
        if (c == null) {
            System.out.println("Null Characteristic");
            return false;
        }
        BluetoothGattDescriptor d = c.getDescriptor(notifyDescUUID);
        if(d ==null){
            System.out.println("Null Descriptor");
            return false;
        }
        gatt.setCharacteristicNotification(c, true);
        d.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        boolean write = gatt.writeDescriptor(d);
        return write;
    }

    private boolean writeCharacteristic(int n) {
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
    public IBinder onBind(Intent intent) {
        return binder;
    }



    private void scan(final boolean enable, final mediCalBle s) {
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
            new Thread(new Runnable() {
                @Override
                public void run() {

                    if(device.getAddress().equals("DA:58:0F:53:19:23")&& connected==false){
                        pillar = device;
                        System.out.println("DEVICE "+ device.getAddress());
                    }
                }
            }).start();

        }
    };
}
