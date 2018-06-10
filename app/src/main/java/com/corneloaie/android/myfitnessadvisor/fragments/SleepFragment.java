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
import com.corneloaie.android.myfitnessadvisor.model.Sleep;
import com.corneloaie.android.myfitnessadvisor.model.SleepData;
import com.corneloaie.android.myfitnessadvisor.model.SleepStage;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SleepFragment extends Fragment {
    private static final String ARG_TOKEN = "token";
    private static final String ARG_DATE = "date";
    private OAuthTokenAndId token;
    private TextView mTextView3;
    private Date mDate;

    public static SleepFragment newInstance(Date date, OAuthTokenAndId token) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        args.putSerializable(ARG_TOKEN, token);
        SleepFragment fragment = new SleepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (OAuthTokenAndId) getArguments().getSerializable(ARG_TOKEN);
        mDate = (Date) getArguments().getSerializable(ARG_DATE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_sleep, container, false);
        mTextView3 = view.findViewById(R.id.textView3);
        getSleepData();

        return view;
    }


    public void getSleepData() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                Sleep sleep = new Sleep();
                List<SleepData> sleepDataList = new ArrayList<>();
                List<SleepStage> sleepStageList = new ArrayList<>();
                AppDatabase appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
                try {
                    JSONArray sleepJSONArray = object.getJSONArray("sleep");
                    JSONObject sleepSummaryJSONObject = object.getJSONObject("summary");
                    sleep = parseSleep(sleep, sleepSummaryJSONObject);
                    praseSleepStageAndDataList(sleepStageList, sleepDataList, sleepJSONArray);
                    appDatabase.mSleepDao().insert(sleep);
                    appDatabase.mSleepStageDao().insert(sleepStageList);
                    appDatabase.mSleepDataDao().insert(sleepDataList);
                    mTextView3.setText(sleep.toString() + sleepStageList + sleepDataList);


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
        VolleyHelper.getInstance().get("1.2/user/" + token.getUserID() +
                        "/sleep/date/" + stringDate + ".json",
                callback, getActivity());
    }

    private void praseSleepStageAndDataList(List<SleepStage> sleepStageList,
                                            List<SleepData> sleepDataList, JSONArray sleepJSONArray) throws JSONException {
        for (int i = 0; i < sleepJSONArray.length(); i++) {
            JSONObject jsonObject = sleepJSONArray.getJSONObject(i);
            SleepStage sleepStage = new SleepStage();
            sleepStage.setDuration(jsonObject.getLong("duration"));
            sleepStage.setSleepDateFK(mDate);
            sleepStage.setEfficiency(jsonObject.getInt("efficiency"));
            sleepStage.setStartTime(jsonObject.getString("startTime"));
            sleepStage.setEndTime(jsonObject.getString("endTime"));
            sleepStage.setTimeInBed(jsonObject.getInt("timeInBed"));
            sleepStageList.add(sleepStage);
            for (int j = 0; j <; j++) {

            }
        }

    }

    private Sleep parseSleep(Sleep sleep, JSONObject sleepSummaryJSONObject) throws JSONException {
        sleep.setDateOfSleep(mDate);
        sleep.setTotalMinutesAsleep(sleepSummaryJSONObject.getInt("totalMinutesAsleep"));
        sleep.setTotalTimeInBed(sleepSummaryJSONObject.getInt("totalTimeInBed"));
        sleep.setDeep(sleepSummaryJSONObject.getJSONObject("stages").getInt("deep"));
        sleep.setLight(sleepSummaryJSONObject.getJSONObject("stages").getInt("light"));
        sleep.setRem(sleepSummaryJSONObject.getJSONObject("stages").getInt("rem"));
        sleep.setWake(sleepSummaryJSONObject.getJSONObject("stages").getInt("wake"));
        return sleep;
    }

}
