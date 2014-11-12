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
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.example.medical.db.Pill;

import java.util.ArrayList;
import java.util.UUID;

public class mediCalBle extends Service {
    private IBinder binder = new MedicalBinder();



    private boolean scanning, connected;
    private BluetoothGattService s;
    private BluetoothGattCharacteristic c;
    private BluetoothManager btManager;
    private BluetoothDevice pillar;
    private BluetoothAdapter btAdapter;
    private Handler handler;
    private static final long SCAN_PERIOD = 1000;
    private BluetoothGatt gatt;

    private ArrayList<Pill> pills;

    public class MedicalBinder extends Binder {
        mediCalBle getService() {
            // Return this instance of mediCalBle so clients can call public methods
            return mediCalBle.this;
        }
    }

    private final BluetoothGattCallback btCb = new BluetoothGattCallback() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic btchar){
            System.out.println("CALLBACK");
            c = btchar;
            byte[] b = c.getValue();
            rotate(dc.dispense(b));

        }
    };

    private final UUID serviceUUID =    UUID.fromString("b0bb5820-5a0d-11e4-93ee-0002a5d5c51b");

    private final UUID rotateCharUUID = UUID.fromString("fb71bcc0-5a0c-11e4-91ae-0002a5d5c51b");

    private final UUID rfidUUID = UUID.fromString("7a77be20-5a0d-11e4-a95e-0002a5d5c51b");

    private final UUID notifyDescUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");


    private DispenserCoordinator dc;

    public mediCalBle() {
    }

    public void initialize(){

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

        if (btAdapter==null){
            assert(1==2);
        }
        scan(true,this);

    }

    public void bleSetup(){
        System.out.println("Beginning to Connect");
//        pillar.fetchUuidsWithSdp();
        gatt = pillar.connectGatt(this, true, btCb);
        gatt.discoverServices();
        connected = true;
    }

    public void enableDispensing(){
        System.out.println("Now Setting Notifications on");
        if (gatt.discoverServices()) {
            boolean write = this.enableNotification();
            while (write == false) {
                write = this.enableNotification();
            }
            System.out.println("Success " + write);
        }


    }

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

    private boolean enableNotification(){
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
                    if((device.getName()!=null)&&(device.getName().toLowerCase().equals("medical ble"))
                            && connected==false){
                        pillar = device;
                    }
                }
            }).start();

        }
    };
}
