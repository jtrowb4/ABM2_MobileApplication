package com.c196.abm2_mobileapplication.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.c196.abm2_mobileapplication.R;

public class NotificationReceiver extends BroadcastReceiver {

    String channelID = "Notification";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context,channelID);

        Notification notification = new NotificationCompat.Builder(context,channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("Reminder").build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID++, notification);

    }

    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        CharSequence channelName = context.getResources().getString(R.string.channel_name);
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}