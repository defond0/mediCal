package com.example.medical.db;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MedicalSyncService extends Service {
    private static medicalSync sync = null;
    private final static Object lock = new Object();

    public MedicalSyncService() {
    }

    @Override
    public void onCreate(){

        synchronized (lock){
            if(sync==null){
                sync = new medicalSync(getApplicationContext(),true);
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return sync.getSyncAdapterBinder();
    }
}
