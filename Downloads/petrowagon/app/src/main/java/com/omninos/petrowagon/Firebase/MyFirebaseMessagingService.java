package com.omninos.petrowagon.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.omninos.petrowagon.Activities.HomePageActivity;
import com.omninos.petrowagon.Activities.TrackOrderActivity;
import com.omninos.petrowagon.R;
import com.omninos.petrowagon.RejectedOrderActivity;
import com.omninos.petrowagon.SharePreference.App;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    final static String TAG = "myTag";
    NotificationChannel mChannel;
    Notification notification;
    Uri defaultSound;
    private static final int REQUEST_CODE = 1;
    private Intent[] intent;
    private static final int NOTIFICATION_ID = 6578;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().get("type").equalsIgnoreCase("accept")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));

            } else {
                showNotification(remoteMessage.getData().get("type"), remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            }
        } else if (remoteMessage.getData().get("type").equalsIgnoreCase("reject")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            } else {
                showNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            }
        } else if (remoteMessage.getData().get("type").equalsIgnoreCase("progress")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            } else {
                showNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            }
        } else if (remoteMessage.getData().get("type").equalsIgnoreCase("onTheWay")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            } else {
                showNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            }
        } else if (remoteMessage.getData().get("type").equalsIgnoreCase("delivered")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBookingOreoNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            } else {
                showNotification(remoteMessage.getData().get("type"),remoteMessage.getData().get("orderId"),remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("subtitle"));
            }
        } else {
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                Intent intent = new Intent(this, HomePageActivity.class);
//                intent.putExtra(AppConstants.OPEN_SCREEN, AppConstants.OPEN_APPOINTMENT_SCREEN);
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                // Build notification
                // Actions are just fake
                Notification noti = new Notification.Builder(this)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setSmallIcon(R.drawable.logo)
                        .setContentIntent(pIntent)
                        .build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, noti);


//                if (/ Check if data needs to be processed by long running job / true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
//                } else {
                // Handle message within 10 seconds
                //handleNow();
//                }

            }
        }

        // Check if message contains a data payload.


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            // Prepare intent which is triggered if the
            // notification is selected

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void setBookingOreoNotification(String type, String id ,String title, String message, String s) {
        App.getSinltonPojo().setNotificetionBookingId(id);
        if (type.equalsIgnoreCase("reject")){
            intent = new Intent[]{new Intent(this, HomePageActivity.class).putExtra("subtitle", s).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)};
        }else {
            intent = new Intent[]{new Intent(this, TrackOrderActivity.class).putExtra("subtitle", s).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)};
//        startActivities(intent);
        }
        PendingIntent pendingIntent = PendingIntent.getActivities(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_ONE_SHOT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

// Sets an ID for the notification, so it can be updated.
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Tabeeb";// The user-visible name of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
// Create a notification and set the notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo)
                    .setVibrate(new long[]{200,200,200,200})
                    .setSound(defaultSound)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }

// Issue the notification.
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void showNotification(String type, String id, String title, String message, String s) {
        App.getSinltonPojo().setNotificetionBookingId(id);
//        intent = new Intent[]{new Intent(this, TrackOrderActivity.class).putExtra("subtitle", s).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)};
//        startActivities(intent);
        if (type.equalsIgnoreCase("reject")){
            intent = new Intent[]{new Intent(this, RejectedOrderActivity.class).putExtra("subtitle", s).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)};
        }else {
            intent = new Intent[]{new Intent(this, TrackOrderActivity.class).putExtra("subtitle", s).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)};
//        startActivities(intent);
        }
        PendingIntent pendingIntent = PendingIntent.getActivities(this, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
        defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification = new NotificationCompat.Builder(this)
                .setContentText(message)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setVibrate(new long[]{200, 200,200,200})
                .setSound(defaultSound)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
//        App.getSingleton().setRegId(token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }


}