package com.agendapersonal.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.webkit.JavascriptInterface;

public class AgendaBridge {
    private final MainActivity activity;

    AgendaBridge(MainActivity activity) { this.activity = activity; }

    @JavascriptInterface
    public void requestNotificationPermission() { activity.runOnUiThread(activity::requestNotificationPermission); }

    @JavascriptInterface
    public boolean hasNotificationPermission() { return activity.hasNotificationPermission(); }

    @JavascriptInterface
    public void showNotification(String title, String body, String tag, String data) {
        NotificationReceiver.show(activity, title, body, tag, data);
    }

    @JavascriptInterface
    public void scheduleNotification(String title, String body, String tag, String data, long whenMillis) {
        Intent i = new Intent(activity, NotificationReceiver.class);
        i.putExtra("title", title);
        i.putExtra("body", body);
        i.putExtra("tag", tag);
        i.putExtra("data", data);
        int requestCode = (tag == null ? 0 : tag.hashCode()) & 0x7fffffff;
        PendingIntent pi = PendingIntent.getBroadcast(activity, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            if (android.os.Build.VERSION.SDK_INT >= 23) am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, whenMillis, pi);
            else am.set(AlarmManager.RTC_WAKEUP, whenMillis, pi);
        }
    }
}
