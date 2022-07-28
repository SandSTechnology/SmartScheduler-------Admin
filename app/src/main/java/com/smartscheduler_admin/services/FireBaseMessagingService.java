package com.smartscheduler_admin.services;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smartscheduler_admin.util.BaseUtil;

public class FireBaseMessagingService extends FirebaseMessagingService {
    String title, message;
    public FireBaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");

        FirebaseNotification.notify(getApplicationContext(), title, message, this);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
        //Toast.makeText(getApplicationContext(), "Notification sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        new BaseUtil(this).SetDeviceToken(s);
    }

    @Override
    public boolean handleIntentOnMainThread(@NonNull Intent intent) {
        return super.handleIntentOnMainThread(intent);
    }

    @Override
    public void handleIntent(@NonNull Intent intent) {
        super.handleIntent(intent);
    }
}
