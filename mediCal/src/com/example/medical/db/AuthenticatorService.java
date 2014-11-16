package com.example.medical.db;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {
    private AuthenticatorStub auth;
    public AuthenticatorService() {
    }

    @Override
    public void onCreate() {
        // Create a new authenticator object
        auth = new AuthenticatorStub(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return auth.getIBinder();
    }
}
