package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private ImageView imageView;

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
        imageView = view.findViewById(R.id.stepsAllTime_imageView);
        getLifeTimeStats();
        return view;
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

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Lifetime lifetime = AppDatabase.getInstance(getActivity().getApplicationContext())
                        .mLifetimeDao().getLifetimeStats();
                if (lifetime != null) {

                } else {
                    //TODO if no internet and no data
                }
            }
        };

        VolleyHelper.getInstance().get("1/user/" + token.getUserID() +
                        "/activities.json",
                volleyCallback, getActivity());

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


}
