package org.anima.utils;
import org.anima.animacite.R;
/**
 * Created by momo on 03/04/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmReceiver;

import org.anima.activities.ImagePickActivity;

public class GcmBroadcastReceiver extends GcmReceiver {

    /*
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    } */

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Bundle extras = intent.getExtras();

        String message = "";
        String title = "";
        if (extras.containsKey("message")) {
            message = extras.getString("message");

        }
        if (extras.containsKey("title")) {
            title = extras.getString("title");

        }

        final Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true);

        Intent resultIntent = new Intent(context, ImagePickActivity.class);
        //resultIntent.setExtras(extras);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.getNotification());
    }


}