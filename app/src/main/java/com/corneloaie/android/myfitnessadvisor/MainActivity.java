package com.corneloaie.android.myfitnessadvisor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;

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


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_main_container);
        if (fragment == null) {
            fragment = new MenuListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main_container, fragment)
                    .commit();
        }
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
        Fragment fragment = SummaryFragment.newInstance(date, token);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_container, fragment)
                .commit();
    }
}
