package com.agendapersonal.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        show(context,
            intent.getStringExtra("title"),
            intent.getStringExtra("body"),
            intent.getStringExtra("tag"),
            intent.getStringExtra("data"));
    }

    public static void show(Context context, String title, String body, String tag, String data) {
        Intent open = new Intent(context, MainActivity.class);
        open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, open, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(com.agendapersonal.app.R.drawable.ic_agenda)
            .setContentTitle(title == null || title.isEmpty() ? "Agenda Personal" : title)
            .setContentText(body == null ? "" : body)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(body == null ? "" : body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(new long[]{0,450,180,450,180,700})
            .setContentIntent(contentIntent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.notify((tag == null ? (int)System.currentTimeMillis() : tag.hashCode()), b.build());
    }
}
