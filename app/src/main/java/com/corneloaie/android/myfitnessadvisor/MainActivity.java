package com.corneloaie.android.myfitnessadvisor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.fragments.DatePickerFragment;
import com.corneloaie.android.myfitnessadvisor.fragments.HeartRateFragment;
import com.corneloaie.android.myfitnessadvisor.fragments.LifeCoachFragment;
import com.corneloaie.android.myfitnessadvisor.fragments.LifetimeFragment;
import com.corneloaie.android.myfitnessadvisor.fragments.MenuListFragment;
import com.corneloaie.android.myfitnessadvisor.fragments.ProfileFragment;
import com.corneloaie.android.myfitnessadvisor.fragments.SleepFragment;
import com.corneloaie.android.myfitnessadvisor.fragments.SummaryFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements MenuListFragment.OnMenuSelectedListener,
        DatePickerFragment.DatePassingListener, SummaryFragment.InfoClickedListener {
    public static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DialogDate";
    TextView textView;
    OAuthTokenAndId token;

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
        Fragment fragment;
        DatePickerFragment dialog;
        switch (menu) {
            case "Summary":
                dialog = DatePickerFragment.newInstance("Summary");
                dialog.show(fragmentManager, DIALOG_DATE);
                break;
            case "Lifetime":
                fragment = LifetimeFragment.newInstance(token);
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_main_container, fragment)
                        .commit();
                break;
            case "Heartrate":
                dialog = DatePickerFragment.newInstance("Heartrate");
                dialog.show(fragmentManager, DIALOG_DATE);
                break;
            case "Sleep":
                dialog = DatePickerFragment.newInstance("Sleep");
                dialog.show(fragmentManager, DIALOG_DATE);
                break;
            case "Profile":
                fragment = ProfileFragment.newInstance(token);
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_main_container, fragment)
                        .commit();
                break;
            case "LifeCoach":
                fragment = new LifeCoachFragment();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_main_container, fragment)
                        .commit();
                break;
        }
    }

    @Override
    public void onDatePass(Date date, String userCase) {
        Fragment fragment;
        switch (userCase) {
            case "Summary":
                fragment = SummaryFragment.newInstance(date, token);
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_main_container, fragment)
                        .commit();
                break;
            case "Sleep":
                fragment = SleepFragment.newInstance(date, token);
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_main_container, fragment)
                        .commit();
                break;
            //TODO verify code with avd
            case "Heartrate":
                fragment = HeartRateFragment.newInstance(date, token);
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_main_container, fragment)
                        .commit();
        }
    }

    @Override
    public void onClickInfoCallback() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance("test");
        dialog.show(fragmentManager, DIALOG_DATE);
    }


}
