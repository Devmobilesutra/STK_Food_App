package com.stk.orderingapp.FireBase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.stk.orderingapp.Activity.ActitivtyNotificationAndOffers;
import com.stk.orderingapp.Activity.ActivityDashboard;
import com.stk.orderingapp.Config.MyApplication;
import com.stk.orderingapp.DataBase.TABLE_NOTIFICATION;
import com.stk.orderingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {
    public String LOG_TAG = "FirebaseService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MyApplication.log(LOG_TAG, "onMessageReceived(),  From :---> " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            MyApplication.log(LOG_TAG, "onMessageReceived(), NOTIFICATION BODY--->: " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            MyApplication.log(LOG_TAG, "onMessageReceived(), Message data payload: " + remoteMessage.getData());
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(jsonObject);
            } catch (JSONException e) {
                MyApplication.log(LOG_TAG, "onMessageReceived(), JSONException -->" + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        MyApplication.log(LOG_TAG, "handleDataMessage push json: " + json.toString());
        String notification_flag = "", title = "", message = "", Sent_on = "", id = "", Valid_till = "";
        try {
            notification_flag = json.getString("notification_flag");
            title = json.getString("title");
            message = json.getString("message");

            MyApplication.log(LOG_TAG, "handleDataMessage(),  message---->" + message);
            MyApplication.log(LOG_TAG, "handleDataMessage(), notification_flag----> " + notification_flag);
            MyApplication.log(LOG_TAG, "handleDataMessage(), title:----> " + title);

            //TABLE_NOTIFICATION.deleteRecordsBeforeTodays();
            //TABLE_NOTIFICATION.insertNotifications(json);

            if (notification_flag.equalsIgnoreCase("Offer"))
                MyApplication.log(LOG_TAG, "handleDataMessage() CHECKING FLAGS: Offer --->  TRUE");
            generateNotification(message, title);

        } catch (JSONException e) {
            MyApplication.log(LOG_TAG, "handleDataMessage() JSONException ERROR  " + e.getMessage());

        } catch (Exception e) {
            MyApplication.log(LOG_TAG, "handleDataMessage() Exception ERROR  " + e.getMessage());
        }
    }


    private void generateNotification(String message, String title) {
        MyApplication.log(LOG_TAG + " in generateNotification(),  TITLE: " + title + ", MESSAGE: " + message);

        Intent intent = new Intent(getApplicationContext(), ActitivtyNotificationAndOffers.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.stkfood_logo)  //a resource for your custom small icon
                .setContentTitle(title) //the "title" value you sent in your notification
                .setContentText(message) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri);

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("3",
                    getResources().getString(R.string.app_name), importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            notificationBuilder.setChannelId("3");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(3, notificationBuilder.build());
    }
}
