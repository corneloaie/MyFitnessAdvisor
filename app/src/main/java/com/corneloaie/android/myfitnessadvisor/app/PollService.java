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

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.LoginActivity;
import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {
    public static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    long timeRemaining;
    OAuthTokenAndId tokenAndId;

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
        getActivitySummary();
    }

    public void getActivitySummary() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONObject summaryObj = object.getJSONObject("summary");
                    int steps = summaryObj.getInt("steps");

                    Intent i = LoginActivity.newIntent(getApplicationContext());
                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    if (steps < 10000) {
                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "id")
                                .setSmallIcon(R.drawable.ic_stat_logolauncer)
                                .setLargeIcon(bitmap)
                                .setContentTitle("Steps")
                                .setContentText("Still under 10000, with only " + steps + ", try to move a bit.")
                                .setContentIntent(pi)
                                .setAutoCancel(true)
                                .build();

                        NotificationManagerCompat notificationManager =
                                NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(2, notification);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
            }
        };
        SharedPreferences sp = getSharedPreferences("Token", MODE_PRIVATE);
        String userId = sp.getString("UserID", null);
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        VolleyHelper.getInstance().get("1/user/" + userId +
                        "/activities/date/" + stringDate + ".json",
                callback, getApplicationContext());
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
