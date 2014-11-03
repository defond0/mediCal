package com.example.medical;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.IBinder;

public class BluetoothGattService extends Service {
    private BluetoothManager btManager;
    private BluetoothAdapter btAdaper;
    private String btAddress;
    private int ConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED=0;
    private static final int STATE_CONNECTING=1;
    private static final int STATE_CONNECTED=2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.medical.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.medical.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.medical.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.medical.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.medical.EXTRA_DATA";

    public BluetoothGattService() {
    }

    public final BluetoothGattCallback gattCallback = new BluetoothGattCallback(){
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                ConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                ConnectionState = STATE_DISCONNECTED;
                broadcastUpdate(intentAction);
            }
        }

    };

    public void broadcastUpdate(final String action){
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
