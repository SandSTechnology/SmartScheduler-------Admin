package com.smartscheduler_admin.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.database.DatabaseReference;

public class LocationTrackerService extends Service {
    DatabaseReference ref;


    public LocationTrackerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String keyValue = intent.getStringExtra("key");

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LocationNotification.cancel(this);
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    private void sendBroadcast (boolean success){
        Intent intent = new Intent ("Connection"); //put the same message as in the filter you used in the activity when registering the receiver
        intent.putExtra("isConnected", String.valueOf(success).toLowerCase());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}