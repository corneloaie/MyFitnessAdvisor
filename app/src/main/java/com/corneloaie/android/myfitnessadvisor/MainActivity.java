package com.corneloaie.android.myfitnessadvisor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    OAuthTokenAndId token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = (OAuthTokenAndId) getIntent().getSerializableExtra("token");

        Button buttonGetData = (Button) findViewById(R.id.button_getData);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(Long.toString(token.getExpireTimeInSeconds()));

        buttonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivitySummary();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_main_container);
    }

    public void getActivitySummary() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    textView.setText(object.toString());
                    if (false) {
                        throw new JSONException("tral");
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
        Date date = new Date(System.currentTimeMillis());
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        VolleyHelper.getInstance().get("1/user/" + token.getUserID() +
                        "/activities/date/" + stringDate + ".json",
                callback, getApplicationContext());
    }
}
