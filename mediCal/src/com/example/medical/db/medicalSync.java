package com.example.medical.db;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by meanheffry on 11/15/14.
 */
public class medicalSync extends AbstractThreadedSyncAdapter {
    //ContentResolver resolver;
    public medicalSync(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
       // re
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        System.out.println("SYNCING");
        Log.d("TAG", "work damn it");
    }


}
