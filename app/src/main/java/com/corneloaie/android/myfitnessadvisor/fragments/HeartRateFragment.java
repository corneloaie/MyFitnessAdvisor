package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.database.AppDatabase;
import com.corneloaie.android.myfitnessadvisor.model.HeartRateIntraday;
import com.corneloaie.android.myfitnessadvisor.model.HeartRateZone;
import com.corneloaie.android.myfitnessadvisor.model.Heartrate;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HeartRateFragment extends Fragment {
    private static final String ARG_TOKEN = "token";
    private static final String ARG_DATE = "date";
    private Date mDate;
    private OAuthTokenAndId token;
    private TextView mTextView5;

    public static HeartRateFragment newInstance(Date date, OAuthTokenAndId token) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        args.putSerializable(ARG_TOKEN, token);
        HeartRateFragment fragment = new HeartRateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date) getArguments().getSerializable(ARG_DATE);
        token = (OAuthTokenAndId) getArguments().getSerializable(ARG_TOKEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heartrate, container, false);
        mTextView5 = view.findViewById(R.id.textView5);
        AppDatabase appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        Heartrate heartrate = appDatabase.mHeartrateDao().getHeartrateStats(mDate);
        List<HeartRateIntraday> heartRateIntradays = appDatabase.mHeartRateIntradayDao().getHeartRateZonesIntraday(mDate);
        List<HeartRateZone> heartRateZones = appDatabase.mHeartRateZoneDao().getHeartRateZones(mDate);
        if (heartrate != null) {
            mTextView5.setText(heartrate.toString() + heartRateIntradays + heartRateZones);
        } else {
            getHeart1DaySummary();
        }
        return view;
    }


    public void getHeart1DaySummary() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                Heartrate heartrate = new Heartrate();
                List<HeartRateZone> heartRateZones = new ArrayList<>();
                List<HeartRateIntraday> heartRateIntradays = new ArrayList<>();
                try {
                    JSONArray activitiesHeartJSONArray = object.getJSONArray("activities-heart");
                    JSONObject activitiesHeartJSON = activitiesHeartJSONArray.getJSONObject(0);
                    heartrate = parseHeartRate(activitiesHeartJSON, heartrate);
                    heartRateZones = parseHeartRateZone(activitiesHeartJSON, heartRateZones);
                    JSONObject activitesHeartIntradayJSON = object.getJSONObject("activities-heart-intraday");
                    heartRateIntradays = parseHeartRateIntraday(activitesHeartIntradayJSON, heartRateIntradays);
                    AppDatabase.getInstance(getActivity().getApplicationContext())
                            .mHeartrateDao().insert(heartrate);
                    AppDatabase.getInstance(getActivity().getApplicationContext())
                            .mHeartRateZoneDao().insert(heartRateZones);
                    AppDatabase.getInstance(getActivity().getApplicationContext())
                            .mHeartRateIntradayDao().insert(heartRateIntradays);
                    mTextView5.setText(heartrate.toString() + heartRateZones + heartRateIntradays);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
            }
        };
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
        VolleyHelper.getInstance().get("1/user/" + token.getUserID() +
                        "/activities/heart/date/" + stringDate + "/1d.json",
                callback, getActivity());
    }

    private List<HeartRateIntraday> parseHeartRateIntraday(JSONObject activitesHeartIntradayJSON, List<HeartRateIntraday> heartRateIntradays) throws JSONException {
        JSONArray jsonArray = activitesHeartIntradayJSON.getJSONArray("dataset");
        for (int i = 0; i < jsonArray.length(); i++) {
            HeartRateIntraday heartRateIntraday = new HeartRateIntraday();
            heartRateIntraday.setHeartRateFK(mDate);
            heartRateIntraday.setTime(jsonArray.getJSONObject(i).getString("time"));
            heartRateIntraday.setValue(jsonArray.getJSONObject(i).getInt("value"));
            heartRateIntradays.add(heartRateIntraday);
        }


        return heartRateIntradays;
    }

    private List<HeartRateZone> parseHeartRateZone(JSONObject activitiesHeartJSON, List<HeartRateZone> heartRateZones) throws JSONException {
        JSONArray heartRateZonesJSONArray = activitiesHeartJSON.getJSONObject("value").getJSONArray("heartRateZones");
        for (int i = 0; i < heartRateZonesJSONArray.length(); i++) {
            HeartRateZone heartRateZone = new HeartRateZone();
            JSONObject json = heartRateZonesJSONArray.getJSONObject(i);
            heartRateZone.setHeartRateFK(mDate);
            heartRateZone.setMin(json.getInt("min"));
            heartRateZone.setMax(json.getInt("max"));
            heartRateZone.setMinutes(json.getInt("minutes"));
            heartRateZone.setName(json.getString("name"));
            heartRateZone.setCaloriesOut(json.getDouble("caloriesOut"));
            heartRateZones.add(heartRateZone);
        }
        return heartRateZones;
    }

    private Heartrate parseHeartRate(JSONObject activitiesHeartJSON, Heartrate heartrate) throws JSONException {
        heartrate.setHeartRateDate(mDate);
        heartrate.setRestingHeartRate(activitiesHeartJSON.getJSONObject("value").getInt("restingHeartRate"));
        return heartrate;
    }


}
