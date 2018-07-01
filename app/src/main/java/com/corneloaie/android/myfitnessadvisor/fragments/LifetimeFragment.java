package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.database.AppDatabase;
import com.corneloaie.android.myfitnessadvisor.model.Lifetime;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LifetimeFragment extends Fragment {

    private static final String ARG_TOKEN = "token";
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private OAuthTokenAndId token;
    private static DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
    TextView stepsAllTime_textView, distanceAllTime_textView, floorsDataAllTime_textView,
            stepsBest_textView, bestFloors_textView, distanceBest_textView;

    public static LifetimeFragment newInstance(OAuthTokenAndId token) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TOKEN, token);
        LifetimeFragment fragment = new LifetimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (OAuthTokenAndId) getArguments().getSerializable(ARG_TOKEN);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lifetime, container, false);
        setImageViews(view);
        bindTextViewsXml(view);
        getLifeTimeStats();
        return view;
    }

    private void bindTextViewsXml(View view) {
        stepsAllTime_textView = view.findViewById(R.id.stepsAllTime_textView);
        distanceAllTime_textView = view.findViewById(R.id.distanceAllTime_textView);
        floorsDataAllTime_textView = view.findViewById(R.id.floorsDataAllTime_textView);
        stepsBest_textView = view.findViewById(R.id.stepsBest_textView);
        bestFloors_textView = view.findViewById(R.id.bestFloors_textView);
        distanceBest_textView = view.findViewById(R.id.distanceBest_textView);


    }

    private void setImageViews(View view) {
        ImageView stepsAllTime_imageView = view.findViewById(R.id.stepsAllTime_imageView);
        stepsAllTime_imageView.setImageResource(R.drawable.ic_footsteps_icon);
        ImageView distanceAllTime_imageView = view.findViewById(R.id.distanceAllTime_imageView);
        distanceAllTime_imageView.setImageResource(R.drawable.distance_icon);
        ImageView floorsAllTime_imageView = view.findViewById(R.id.floorsAllTime_imageView);
        floorsAllTime_imageView.setImageResource(R.drawable.floors);
        ImageView bestFloors_imageView = view.findViewById(R.id.bestFloors_imageView);
        bestFloors_imageView.setImageResource(R.drawable.floors);
        ImageView distanceBest_imageView = view.findViewById(R.id.distanceBest_imageView);
        distanceBest_imageView.setImageResource(R.drawable.distance_icon);
        ImageView stepsBest_imageView = view.findViewById(R.id.stepsBest_imageView);
        stepsBest_imageView.setImageResource(R.drawable.ic_footsteps_icon);
    }


    public void getLifeTimeStats() {
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                Lifetime lifetime = new Lifetime();
                try {
                    lifetime = parseLifetimeStats(object, lifetime);
                    AppDatabase.getInstance(getActivity().getApplicationContext()).mLifetimeDao()
                            .insert(lifetime);
                    bindDataToXml(lifetime);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {
                Lifetime lifetime = AppDatabase.getInstance(getActivity().getApplicationContext())
                        .mLifetimeDao().getLifetimeStats();
                if (lifetime != null) {
                    bindDataToXml(lifetime);
                } else {
                    showNoDataDialogFragment();
                }
            }
        };

        VolleyHelper.getInstance().get("1/user/" + token.getUserID() +
                        "/activities.json",
                volleyCallback, getActivity());

    }

    private void bindDataToXml(Lifetime lifetime) {
        stepsAllTime_textView.setText(getString(R.string.stepsTotal, lifetime.getLifetimeSteps()));
        stepsAllTime_textView.setOnClickListener(view -> inflateDialogFragment("stepsAllTime", lifetime));
        floorsDataAllTime_textView.setText(getString(R.string.floorsTotal, lifetime.getLifetimeFloors()));
        floorsDataAllTime_textView.setOnClickListener(view -> inflateDialogFragment("floorsAllTime", lifetime));
        distanceAllTime_textView.setText(getString(R.string.distanceTotal, lifetime.getLifetimeDistance()));
        distanceAllTime_textView.setOnClickListener(view -> inflateDialogFragment("distanceAllTime", lifetime));
        bestFloors_textView.setText(getString(R.string.floorsBest, lifetime.getBestFloors(),
                df2.format(lifetime.getBestFloorsDate())));
        bestFloors_textView.setOnClickListener(view -> inflateDialogFragment("floorsBest", lifetime));
        distanceBest_textView.setText(getString(R.string.distanceBest, lifetime.getBestDistance(),
                df2.format(lifetime.getBestDistanceDate())));
        distanceBest_textView.setOnClickListener(view -> inflateDialogFragment("distanceBest", lifetime));
        stepsBest_textView.setText(getString(R.string.stepsBest, lifetime.getBestSteps(),
                df2.format(lifetime.getBestStepsDate())));
        stepsBest_textView.setOnClickListener(view -> inflateDialogFragment("stepsBest", lifetime));
    }

    private Lifetime parseLifetimeStats(JSONObject object, Lifetime lifetime) throws JSONException, ParseException {
        JSONObject bestTotal = object.getJSONObject("best").getJSONObject("total");
        JSONObject distanceJSON = bestTotal.getJSONObject("distance");
        JSONObject floorsJSON = bestTotal.getJSONObject("floors");
        JSONObject stepsJSON = bestTotal.getJSONObject("steps");
        lifetime.setBestDistance(distanceJSON.getInt("value"));
        lifetime.setBestDistanceDate(df.parse(distanceJSON.getString("date")));
        lifetime.setBestFloors(floorsJSON.getInt("value"));
        lifetime.setBestFloorsDate(df.parse(floorsJSON.getString("date")));
        lifetime.setBestSteps(stepsJSON.getInt("value"));
        lifetime.setBestStepsDate(df.parse(stepsJSON.getString("date")));
        JSONObject lifetimeTotalJSON = object.getJSONObject("lifetime").getJSONObject("total");
        lifetime.setLifetimeDistance(lifetimeTotalJSON.getInt("distance"));
        lifetime.setLifetimeFloors(lifetimeTotalJSON.getInt("floors"));
        lifetime.setLifetimeSteps(lifetimeTotalJSON.getInt("steps"));
        return lifetime;
    }

    private void inflateDialogFragment(String dataSummaryItem, Lifetime lifetime) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
//                        ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment newFragment = LifetimeDetailsFragment.newInstance(dataSummaryItem, lifetime);
        newFragment.show(ft, "dialog");
    }

    private void showNoDataDialogFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
//                        ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment newFragment = new NoDataDetailsFragment();
        newFragment.show(ft, "dialog");
    }
}
