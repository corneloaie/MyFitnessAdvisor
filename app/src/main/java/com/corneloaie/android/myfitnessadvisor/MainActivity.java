package com.corneloaie.android.myfitnessadvisor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MenuListFragment.OnMenuSelectedListener, DatePickerFragment.DatePassingListener {
    TextView textView;
    OAuthTokenAndId token;
    public static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DialogDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = (OAuthTokenAndId) getIntent().getSerializableExtra("token");

//        Button buttonGetData = (Button) findViewById(R.id.button_getData);
//        textView = (TextView) findViewById(R.id.textView);
//        textView.setText(Long.toString(token.getExpireTimeInSeconds()));

//        buttonGetData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivitySummary();
//            }
//        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_main_container);
        if (fragment == null) {
            fragment = new MenuListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main_container, fragment)
                    .commit();
        }
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

    @Override
    public void onMenuSelcted(String menu) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (menu) {
            case "Summary":
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(fragmentManager, DIALOG_DATE);
            case "Lifetime":
                break;
            case "Heartrate":
                break;
            case "Sleep":
                break;
            case "Profile":
                break;
            case "LifeCoach":
                Fragment fragment = new LifeCoachFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main_container, fragment)
                        .commit();
                break;
        }
    }

    @Override
    public void onDatePass(Date date) {
        Fragment fragment = SummaryFragment.newInstance(date);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_container, fragment)
                .commit();
    }
}
