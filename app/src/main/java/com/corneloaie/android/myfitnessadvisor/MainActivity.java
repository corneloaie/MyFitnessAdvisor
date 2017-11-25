package com.corneloaie.android.myfitnessadvisor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String URL = "https://www.fitbit.com/oauth2/" +
            "authorize?response_type=token&client_id=228MYG&redirect_uri=" +
            "myapp%3A%2F%2Fscreen&" +
            "scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800";
    TextView textView;
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
        setContentView(R.layout.activity_main);
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        Button buttonGetData = (Button) findViewById(R.id.button_getData);
        textView = (TextView) findViewById(R.id.textView);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(URL));
            }
        });

        buttonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser();
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
        if(uri!=null){
            Log.e("eroare", uri.toString());
            String str = uri.toString().replace("#", "&");
            Map<String, String> map = getQueryMap(uri.getFragment());
            token.setAccessToken(map.get("access_token"));
            token.setTokenType(map.get("token_type"));
            token.setUserID(map.get("user_id"));
            VolleyHelper.getInstance().setToken(token.getAccessToken());
            textView.setText(map.get("access_token") + map.get("token_type"));

        }


    }

    public void getUser() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    textView.setText(object.getJSONObject("user").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
            }
        };
        VolleyHelper.getInstance().get("1/user/" + token.getUserID() + "/profile.json", callback, getApplicationContext());
    }


}

