package com.deniszbecskei.personaldata;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String CHANNEL_ID = "person_notification_channel";
    private NotificationManager mManager;
    private Context mContext;
    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationChannel nc = new NotificationChannel(CHANNEL_ID, "Person Notification", NotificationManager.IMPORTANCE_DEFAULT);
        nc.enableLights(true);
        nc.enableVibration(true);
        nc.setLightColor(Color.BLUE);
        nc.setDescription("Notification from The Personal Data App");
        this.mManager.createNotificationChannel(nc);
    }

    public void send(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID).
                setContentTitle("Personal Data").setContentText(message).setSmallIcon(R.drawable.ic_baseline_run_circle_24);
        this.mManager.notify(0, builder.build());
    }

}
