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
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONObject;

public class LifetimeFragment extends Fragment {

    private static final String ARG_TOKEN = "token";
    private OAuthTokenAndId token;
    private TextView mTextView2;

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
        mTextView2 = view.findViewById(R.id.textView2);
        getLifeTimeStats();
        return view;
    }


    public void getLifeTimeStats() {
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                mTextView2.setText(object.toString());
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
            }
        };

        VolleyHelper.getInstance().get("1/user/" + token.getUserID() +
                        "/activities.json",
                volleyCallback, getActivity());

    }


}
