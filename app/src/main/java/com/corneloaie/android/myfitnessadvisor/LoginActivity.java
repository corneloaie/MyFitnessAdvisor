package com.corneloaie.android.myfitnessadvisor;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.app.PollService;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    public static final String URL = "https://www.fitbit.com/oauth2/" +
            "authorize?response_type=token&client_id=228MYG&redirect_uri=" +
            "myapp%3A%2F%2Fscreen&" +
            "scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=86400";
    OAuthTokenAndId token;


    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("Token", MODE_PRIVATE);
        String accessToken = sp.getString("AccessToken", null);
        String userID = sp.getString("UserID", null);
        long expireTimeInSeconds = sp.getLong("ExpireTimeInSeconds", 0);
        String tokenType = sp.getString("TokenType", null);
        long savedTimeMillis = sp.getLong("CurrentTimeMilis", 0);


        if ((System.currentTimeMillis() - savedTimeMillis) < expireTimeInSeconds * 1000 && accessToken != null) {
            token = new OAuthTokenAndId(accessToken, userID, tokenType, expireTimeInSeconds);
            VolleyHelper.getInstance().setToken(token.getAccessToken());
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.putExtra("token", token);
            startActivity(intent1);
        } else {

            setContentView(R.layout.activity_login);
            Button buttonLogin = findViewById(R.id.button_login);
            buttonLogin.setOnClickListener(view -> {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(URL));
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        token = new OAuthTokenAndId();
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            Log.e("eroare", uri.toString());
            //      String str = uri.toString().replace("#", "&");
            Map<String, String> map = getQueryMap(uri.getFragment());
            token.setAccessToken(map.get("access_token"));
            token.setTokenType(map.get("token_type"));
            token.setUserID(map.get("user_id"));
            token.setExpireTimeInSeconds(Long.parseLong(map.get("expires_in")));
            SharedPreferences sp = getSharedPreferences("Token", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("AccessToken", token.getAccessToken());
            editor.putString("UserID", token.getUserID());
            editor.putLong("ExpireTimeInSeconds", token.getExpireTimeInSeconds());
            editor.putLong("CurrentTimeMilis", System.currentTimeMillis());
            editor.putString("TokenType", token.getTokenType());
            editor.apply();
            launchNotificationDemo();
            PollService.setServiceAlarm(getApplicationContext(), true);
            VolleyHelper.getInstance().setToken(token.getAccessToken());
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.putExtra("token", token);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);

        }


    }

    public void launchNotificationDemo() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(R.drawable.ic_stat_logolauncer)
                .setLargeIcon(bitmap)
                .setContentTitle("Test")
                .setContentText("This is just a demo")
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
    }


}

