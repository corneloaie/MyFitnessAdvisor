package com.corneloaie.android.myfitnessadvisor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private static final String ARG_TOKEN = "token";
    private TextView mTextView4;
    private OAuthTokenAndId token;

    public static ProfileFragment newInstance(OAuthTokenAndId token) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TOKEN, token);
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mTextView4 = view.findViewById(R.id.textView4);
        getProfileData();

        return view;
    }


    public void getProfileData() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                mTextView4.setText(object.toString());
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
            }
        };
        VolleyHelper.getInstance().get("1/user/" + token.getUserID() +
                        "/profile.json",
                callback, getActivity());
    }


}
