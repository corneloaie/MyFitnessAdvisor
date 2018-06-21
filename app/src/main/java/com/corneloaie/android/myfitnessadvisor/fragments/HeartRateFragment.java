package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GridLabelRenderer;
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
    private CustomGraphView graphView;
    private Date mDate;
    private OAuthTokenAndId token;
    private TextView hrValue_textView;
    private ImageView hrLogo_imageView;
    private HorizontalBarChart chart;


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
        graphView = view.findViewById(R.id.heart_graph);
        chart = view.findViewById(R.id.heart_chart);
        hrValue_textView = view.findViewById(R.id.hrValue_textView);
        hrLogo_imageView = view.findViewById(R.id.hrLogo_imageView);
        hrLogo_imageView.setImageResource(R.drawable.ic_heart_rate);
        AppDatabase appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        Heartrate heartrate = appDatabase.mHeartrateDao().getHeartrateStats(mDate);
        List<HeartRateIntraday> heartRateIntradays = appDatabase.mHeartRateIntradayDao().getHeartRateZonesIntraday(mDate);
        List<HeartRateZone> heartRateZones = appDatabase.mHeartRateZoneDao().getHeartRateZones(mDate);
        if (heartrate != null) {
            if (heartrate.getRestingHeartRate() != 0)
                hrValue_textView.setText(getString(R.string.restingHR, heartrate.getRestingHeartRate()));
            createGraph(heartRateIntradays);
            createBarGraph(heartRateZones);
        } else {
            getHeart1DaySummary();
        }
        return view;
    }

    private void createGraph(List<HeartRateIntraday> heartRateIntradays) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getHeartRatePoints(heartRateIntradays));
        graphView.setTitle("Heart Rate BPM");
        graphView.setTitleTextSize(60);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graphView.removeAllSeries();
        graphView.addSeries(series);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel((int) value, isValueX);
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
                    if (heartrate.getRestingHeartRate() != 0)
                        hrValue_textView.setText(getString(R.string.restingHR, heartrate.getRestingHeartRate()));
                    graphView.init();
                    createGraph(heartRateIntradays);
                    createBarGraph(heartRateZones);
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

    private void createBarGraph(List<HeartRateZone> heartRateZones) {
        BarDataSet set1;
        set1 = new BarDataSet(getDataSet(heartRateZones), "heartRateZones");
        int[] colors = {getResources().getColor(R.color.yellow_500), getResources().getColor(R.color.orange_500),
                getResources().getColor(R.color.red_500)};
        set1.setColors(colors);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter(heartRateZones));
        // hide Y-axis
        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setDrawLabels(false);
        left.setDrawGridLines(false);
        left.setEnabled(true);
        YAxis right = chart.getAxisRight();
        right.setDrawLabels(false);
        right.setDrawGridLines(false);
        right.setEnabled(true);


        // custom X-axis labels
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setData(data);

        // custom description
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.getDescription().setEnabled(false);
        chart.getData().setValueTextSize(14);

        // put legend
        chart.getLegend().setEnabled(true);
        chart.getLegend().setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        chart.getLegend().setDrawInside(true);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        List<LegendEntry> entries = new ArrayList<>();
        for (int i = 1; i < heartRateZones.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i - 1];
            entry.label = heartRateZones.get(i).getName();
            entries.add(entry);

        }
        chart.getLegend().setCustom(entries);
        chart.animateY(1000);
        chart.invalidate();
        chart.setViewPortOffsets(-0f, 0f, 80f, 80f);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getActivity(), (int) e.getX() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    private ArrayList<BarEntry> getDataSet(List<HeartRateZone> heartRateZones) {

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (int i = 1; i < heartRateZones.size(); i++) {
            BarEntry barEntry = new BarEntry(i, heartRateZones.get(i).getMinutes());
            valueSet1.add(barEntry);
        }
        return valueSet1;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        chart = null;
        graphView = null;
    }

    private class MyValueFormatter implements IValueFormatter {
        private List<HeartRateZone> heartRateZones;

        public MyValueFormatter(List<HeartRateZone> heartRateZones) {
            this.heartRateZones = heartRateZones;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return String.valueOf(heartRateZones.get((int) entry.getX()).getMinutes() + " min");
        }
    }
}
