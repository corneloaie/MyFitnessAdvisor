package com.corneloaie.android.myfitnessadvisor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String URL = "https://www.fitbit.com/oauth2/" +
            "authorize?response_type=token&client_id=228MYG&redirect_uri=" +
            "myapp%3A%2F%2Fscreen&" +
            "scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800";
    OAuthTokenAndId token;

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
        setContentView(R.layout.activity_login);
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(URL));
            }
        });
    }

    private void getData(String token) {

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
            VolleyHelper.getInstance().setToken(token.getAccessToken());
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.putExtra("token", token);
            startActivity(intent1);

        }


    }


}

