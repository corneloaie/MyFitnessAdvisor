package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.app.CustomGraphView;
import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.database.AppDatabase;
import com.corneloaie.android.myfitnessadvisor.model.HeartRateIntraday;
import com.corneloaie.android.myfitnessadvisor.model.HeartRateZone;
import com.corneloaie.android.myfitnessadvisor.model.Heartrate;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HeartRateFragment extends Fragment {
    private static final String ARG_TOKEN = "token";
    private static final String ARG_DATE = "date";
    private Date mDate;
    private OAuthTokenAndId token;
    CustomGraphView graphView;
    private TextView mTextView;


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
        mTextView = view.findViewById(R.id.textView5);
        graphView = view.findViewById(R.id.graph);
        AppDatabase appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        Heartrate heartrate = appDatabase.mHeartrateDao().getHeartrateStats(mDate);
        List<HeartRateIntraday> heartRateIntradays = appDatabase.mHeartRateIntradayDao().getHeartRateZonesIntraday(mDate);
        List<HeartRateZone> heartRateZones = appDatabase.mHeartRateZoneDao().getHeartRateZones(mDate);
        if (heartrate != null) {
            createGraph(heartRateIntradays);
        } else {
            getHeart1DaySummary();
        }
        return view;
    }

    synchronized private void createGraph(List<HeartRateIntraday> heartRateIntradays) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getHeartRatePoints(heartRateIntradays));
        graphView.setTitle("Heart Rate BPM");
        graphView.setTitleTextSize(16);
        graphView.removeAllSeries();
        graphView.addSeries(series);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graphView.getGridLabelRenderer().setHumanRounding(false);
        // graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
        // activate horizontal zooming and scrolling
        graphView.getViewport().setScalable(true);

// activate horizontal scrolling
        graphView.getViewport().setScrollable(true);

// activate horizontal and vertical zooming and scrolling
        graphView.getViewport().setScalableY(true);

// activate vertical scrolling
        graphView.getViewport().setScrollableY(true);

        graphView.getViewport().setXAxisBoundsManual(true);
        String time1 = "00:00:00";
        String time2 = "23:59:00";
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
        try {
            Date dateTime = sdf2.parse(date + " " + time1);
            Date dateTime2 = sdf2.parse(date + " " + time2);
            graphView.getViewport().setMinX(dateTime.getTime());
            graphView.getViewport().setMaxX(dateTime2.getTime());


        } catch (ParseException e) {
            e.printStackTrace();
        }

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
                    graphView.init();
                    createGraph(heartRateIntradays);
//                    DataPoint[] dataPoints = new DataPoint[heartRateIntradays.size()];
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    sdf.setTimeZone(TimeZone.getTimeZone("EEST"));
//                    String date = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
//                    for (int i = 0; i < heartRateIntradays.size(); i++) {
//                        String time = heartRateIntradays.get(i).getTime();
//
//                        Date dateTime = sdf.parse(date + " " + time);
//                        dataPoints[i] = new DataPoint(dateTime, heartRateIntradays.get(i).getValue());
//                    }
//
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
//                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//                    graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
//                        @Override
//                        public String formatLabel(double value, boolean isValueX) {
//                            if (isValueX) {
//                                return sdf2.format(new Date((long) value));
//                            } else {
//                                return super.formatLabel(value, isValueX);
//                            }
//                        }
//                    });
//                    graphView.getViewport().setXAxisBoundsManual(true);
//                    graphView.getGridLabelRenderer().setNumHorizontalLabels(3);
//                    graphView.getGridLabelRenderer().setHumanRounding(false);
//                    String time1 = "00:00:00";
//                    String time2 = "23:59:00";
//                    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String date123 = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
//                        Date dateTime = sdf3.parse(date123 + " " + time1);
//                        Date dateTime2 = sdf3.parse(date123 + " " + time2);
//                        graphView.getViewport().setMinX(dateTime.getTime());
//                        graphView.getViewport().setMaxX(dateTime2.getTime());
//                    graphView.removeAllSeries();
//                    graphView.addSeries(series);
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

    private DataPoint[] getHeartRatePoints(List<HeartRateIntraday> heartRateIntradays) {
        DataPoint[] dataPoints = new DataPoint[heartRateIntradays.size()];
        for (int i = 0; i < heartRateIntradays.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("EEST"));
            String time = heartRateIntradays.get(i).getTime();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
            try {
                Date dateTime = sdf.parse(date + " " + time);
                dataPoints[i] = new DataPoint(dateTime, heartRateIntradays.get(i).getValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return dataPoints;
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

    private List<HeartRateZone> parseHeartRateZone(JSONObject activitiesHeartJSON, List<HeartRateZone> heartRateZones) {
        try {
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
        } catch (JSONException e) {
            Log.e("PARSE", "EROAREEEEEEEEEEEEEEEEEEEEEEEEE");
        }
        return heartRateZones;
    }

    private Heartrate parseHeartRate(JSONObject activitiesHeartJSON, Heartrate heartrate) {
        heartrate.setHeartRateDate(mDate);
        try {
            heartrate.setRestingHeartRate(activitiesHeartJSON.getJSONObject("value").getInt("restingHeartRate"));
        } catch (JSONException e) {
            heartrate.setRestingHeartRate(77);
        }
        return heartrate;
    }


}
