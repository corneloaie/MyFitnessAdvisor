package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.database.AppDatabase;
import com.corneloaie.android.myfitnessadvisor.model.Sleep;
import com.corneloaie.android.myfitnessadvisor.model.SleepData;
import com.corneloaie.android.myfitnessadvisor.model.SleepType;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SleepFragment extends Fragment {
    private static final String ARG_TOKEN = "token";
    private static final String ARG_DATE = "date";
    private OAuthTokenAndId token;
    private Date mDate;
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat timestampformat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private HorizontalBarChart chart;
    private TextView sleepTimeValue_textView, sleepTimeValue_textView2;
    private ImageView clockSleepLogo_imageView;
    private TextView bedTimeValue_textView, wakeUpValue_textView;

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
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);
        bedTimeValue_textView = view.findViewById(R.id.bedTimeValue_textView);
        wakeUpValue_textView = view.findViewById(R.id.wakeUpValue_textView);
        clockSleepLogo_imageView = view.findViewById(R.id.clockSleepLogo_imageView);
        clockSleepLogo_imageView.setImageResource(R.drawable.moon_icon);
        sleepTimeValue_textView = view.findViewById(R.id.sleepTimeValue_textView);
        sleepTimeValue_textView2 = view.findViewById(R.id.sleepTimeValue_textView2);
        chart = view.findViewById(R.id.sleep_chart);
        List<SleepData> sleepDataList = new ArrayList<>();
        List<SleepType> sleepTypeList = new ArrayList<>();
        AppDatabase appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        Sleep sleep = appDatabase.mSleepDao().getSleepBasicStats(mDate);
        if (sleep != null) {
            sleepTimeValue_textView2.setText(getString(R.string.sleepTimeHours, sleep.getTotalMinutesAsleep() / 60));
            sleepTimeValue_textView.setText(getString(R.string.sleepTimeMinutes, sleep.getTotalMinutesAsleep() % 60));
            sleepTypeList = appDatabase.mSleepTypeDao().getSleepStages(mDate);
            if (sleepTypeList.size() == 1) {
                try {
                    bedTimeValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(0).getStartTime())));
                    wakeUpValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(0).getEndTime())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bedTimeValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(1).getStartTime())));
                    wakeUpValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(1).getEndTime())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            sleepDataList = appDatabase.mSleepDataDao().getSleepData(1);
            createBarGraph(sleep);
        } else {
            getSleepData();
        }

        return view;
    }


    public void getSleepData() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                Sleep sleep = new Sleep();
                List<SleepData> sleepDataList = new ArrayList<>();
                List<SleepType> sleepTypeList = new ArrayList<>();
                AppDatabase appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
                try {
                    JSONArray sleepJSONArray = object.getJSONArray("sleep");
                    JSONObject sleepSummaryJSONObject = object.getJSONObject("summary");
                    sleep = parseSleep(sleep, sleepSummaryJSONObject, sleepJSONArray);
                    parseSleepStageAndDataList(sleepTypeList, sleepDataList, sleepJSONArray);
                    appDatabase.mSleepDao().insert(sleep);
                    appDatabase.mSleepTypeDao().insert(sleepTypeList);
                    appDatabase.mSleepDataDao().insert(sleepDataList);
                    sleepTimeValue_textView2.setText(getString(R.string.sleepTimeHours, sleep.getTotalMinutesAsleep() / 60));
                    sleepTimeValue_textView.setText(getString(R.string.sleepTimeMinutes, sleep.getTotalMinutesAsleep() % 60));
                    if (sleepTypeList.size() == 1) {
                        try {
                            bedTimeValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(0).getStartTime())));
                            wakeUpValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(0).getEndTime())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bedTimeValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(1).getStartTime())));
                            wakeUpValue_textView.setText(sdf.format(timestampformat.parse(sleepTypeList.get(1).getEndTime())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    createBarGraph(sleep);

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

    private void parseSleepStageAndDataList(List<SleepType> sleepTypeList,
                                            List<SleepData> sleepDataList, JSONArray sleepJSONArray)
            throws JSONException {
        for (int i = 0; i < sleepJSONArray.length(); i++) {
            JSONObject jsonObject = sleepJSONArray.getJSONObject(i);
            SleepType sleepType = new SleepType();
            sleepType.setDuration(jsonObject.getLong("duration"));
            sleepType.setSleepDateFK(mDate);
            sleepType.setEfficiency(jsonObject.getInt("efficiency"));
            sleepType.setStartTime(jsonObject.getString("startTime"));
            sleepType.setEndTime(jsonObject.getString("endTime"));
            sleepType.setTimeInBed(jsonObject.getInt("timeInBed"));
            sleepTypeList.add(sleepType);
            JSONArray jsonArray = jsonObject.getJSONObject("levels").getJSONArray("data");
            for (int j = 0; j < jsonArray.length(); j++) {
                SleepData sleepData = new SleepData();
                sleepData.setIdSleepTypeFK(i + 1);
                sleepData.setDateTime(jsonArray.getJSONObject(j).getString("dateTime"));
                sleepData.setLevel(jsonArray.getJSONObject(j).getString("level"));
                sleepData.setSeconds(jsonArray.getJSONObject(j).getInt("seconds"));
                sleepDataList.add(sleepData);
            }
        }

    }

    private Sleep parseSleep(Sleep sleep, JSONObject sleepSummaryJSONObject, JSONArray sleepJSONArray) throws JSONException {
        sleep.setDateOfSleep(mDate);
        JSONObject jsonObject;
        if (sleepJSONArray.length() == 2) {
            jsonObject = sleepJSONArray.getJSONObject(1);
        } else {
            jsonObject = sleepJSONArray.getJSONObject(0);
        }
        sleep.setTotalMinutesAsleep(sleepSummaryJSONObject.getInt("totalMinutesAsleep"));
        sleep.setTotalTimeInBed(sleepSummaryJSONObject.getInt("totalTimeInBed"));
        sleep.setDeep(jsonObject.getJSONObject("levels").getJSONObject("summary").getJSONObject("deep").getInt("minutes"));
        sleep.setLight(jsonObject.getJSONObject("levels").getJSONObject("summary").getJSONObject("light").getInt("minutes"));
        sleep.setRem(jsonObject.getJSONObject("levels").getJSONObject("summary").getJSONObject("rem").getInt("minutes"));
        sleep.setWake(jsonObject.getJSONObject("levels").getJSONObject("summary").getJSONObject("wake").getInt("minutes"));
        return sleep;
    }

    private ArrayList<BarEntry> getDataSet(Sleep sleep) {

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        int totalTime = sleep.getDeep() + sleep.getLight() + sleep.getRem() + sleep.getWake();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    valueSet1.add(new BarEntry(i, sleep.getDeep()));
                    break;
                case 1:
                    valueSet1.add(new BarEntry(i, sleep.getLight()));
                    break;
                case 2:
                    valueSet1.add(new BarEntry(i, sleep.getRem()));
                    break;
                case 3:
                    valueSet1.add(new BarEntry(i, sleep.getWake()));
                    break;
            }
        }
        return valueSet1;
    }

    private void createBarGraph(Sleep sleep) {
        BarDataSet set2;
        set2 = new BarDataSet(getDataSet(sleep), "sleepStages");
        int[] colors = {getResources().getColor(R.color.blue_900), getResources().getColor(R.color.blue_600),
                getResources().getColor(R.color.blue_300), getResources().getColor(R.color.pink_600)};
        set2.setColors(colors);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set2);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter2(sleep));
        // hide Y-axis
        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setDrawLabels(false);
        left.setDrawGridLines(false);
        left.setEnabled(true);
        YAxis right = chart.getAxisRight();
        //  right.setAxisMinimum(0);
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
        for (int i = 0; i < 4; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            switch (i) {
                case 0:
                    entry.label = "Deep";
                    break;
                case 1:
                    entry.label = "Light";
                    break;
                case 2:
                    entry.label = "REM";
                    break;
                case 3:
                    entry.label = "Awake";
                    break;
            }
            entries.add(entry);

        }
        chart.getLegend().setCustom(entries);

        chart.animateY(1000);
        chart.invalidate();
        chart.setViewPortOffsets(0f, 0f, 80f, 80f);
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

    @Override
    public void onDetach() {
        super.onDetach();
        chart = null;
    }

    private class MyValueFormatter2 implements IValueFormatter {
        private Sleep mSleep;

        public MyValueFormatter2(Sleep mSleep) {
            this.mSleep = mSleep;
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            switch ((int) entry.getX()) {
                case 0:
                    return String.valueOf(mSleep.getDeep() + " min");

                case 1:
                    return String.valueOf(mSleep.getLight() + " min");

                case 2:
                    return String.valueOf(mSleep.getRem() + " min");

                case 3:
                    return String.valueOf(mSleep.getWake() + " min");
                default:
                    return "";
            }
        }
    }
}
