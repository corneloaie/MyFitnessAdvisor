package com.corneloaie.android.myfitnessadvisor.app;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.corneloaie.android.myfitnessadvisor.LoginActivity;
import com.corneloaie.android.myfitnessadvisor.R;

import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {
    public static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    long timeRemaining;

    public PollService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent intent = PollService.newIntent(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }

//        long timeRemaining = token2.getExpireTimeInSeconds();
        SharedPreferences sp = getSharedPreferences("Token", MODE_PRIVATE);
        long expireTimeInSeconds = sp.getLong("ExpireTimeInSeconds", 0);
        long savedTimeMillis = sp.getLong("CurrentTimeMilis", 0);
        Resources resources = getResources();
        Intent i = LoginActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Log.i(TAG, "Received an intent: " + (System.currentTimeMillis() - savedTimeMillis) + " " + expireTimeInSeconds);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if ((System.currentTimeMillis() - savedTimeMillis) > expireTimeInSeconds * 1000) {
            Notification notification = new NotificationCompat.Builder(this, "id")
                    .setSmallIcon(R.drawable.ic_stat_logolauncer)
                    .setLargeIcon(bitmap)
                    .setContentTitle(resources.getString(R.string.alert))
                    .setContentText(resources.getString(R.string.tokenExpired))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            notificationManager.notify(0, notification);
        }
        //TODO add notif in 2 hours if not enough steps or smth
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }


}
