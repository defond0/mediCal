package com.example.medical;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.v7.app.ActionBarActivity;
import android.content.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.medical.pillar.Prescriptions;


public class Splash extends ActionBarActivity {

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.medical.db";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com.account";
    // The account name
    public static final String ACCOUNT = "cs294";
    // Instance fields
    Account account;

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 1L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;

    ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        account= createAccount(this);
        resolver=getContentResolver();
        ContentResolver.setIsSyncable(account,AUTHORITY,1);
        ContentResolver.setSyncAutomatically(account,AUTHORITY,true);
        ContentResolver.addPeriodicSync(account, AUTHORITY, new Bundle(), 5);
    }

    private Account createAccount(Context context) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) this.getSystemService(
                        ACCOUNT_SERVICE);
        System.out.println("logging");
        if(accountManager.addAccountExplicitly(newAccount,null,null)){
            System.out.println("on");
            return newAccount;
        }
        else{
            System.out.println("off");
            return newAccount;
        }
    }

    public void toPrescriptions(View v){
    	Intent i = new Intent(this, Prescriptions.class);
    	startActivity(i);
    	this.onStop();
    }
    
    public void toStatistics(View v){
    	Intent i = new Intent(this, Statistics.class);
    	startActivity(i);
    	this.onStop();
    }
    
    public void toCalibrate(View v){
    	Intent i = new Intent(this, Calibrate.class);
    	startActivity(i);
    	this.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
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
