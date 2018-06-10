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
import com.corneloaie.android.myfitnessadvisor.model.Badge;
import com.corneloaie.android.myfitnessadvisor.model.Profile;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private static final String ARG_TOKEN = "token";
    private TextView mTextView4;
    private OAuthTokenAndId token;
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

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
        List<Badge> badges = AppDatabase.getInstance(getActivity().getApplicationContext())
                .mBadgeDao().getAllBadges();
        Profile profile = AppDatabase.getInstance(getActivity().getApplicationContext())
                .mProfileDao().getProfile();
        if (profile != null) {
            mTextView4.setText(badges.toString() + profile.toString());
        } else {
            getProfileData();
        }


        return view;
    }


    public void getProfileData() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                Profile profile = new Profile();
                List<Badge> badges = new ArrayList<>();
                try {
                    JSONObject userJSON = object.getJSONObject("user");
                    profile = parseUserJSONProfile(profile, userJSON);
                    badges = parseUserJSONBadges(badges, userJSON);
                    AppDatabase.getInstance(getActivity().getApplicationContext())
                            .mProfileDao().insert(profile);
                    AppDatabase.getInstance(getActivity().getApplicationContext())
                            .mBadgeDao().insert(badges);
                    mTextView4.setText(badges.toString() + profile.toString());
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
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

    private List<Badge> parseUserJSONBadges(List<Badge> badges, JSONObject userJSON) throws JSONException, ParseException {
        JSONArray badgesJSONArray = userJSON.getJSONArray("topBadges");
        for (int i = 0; i < badgesJSONArray.length(); i++) {
            Badge badge = new Badge();
            badge.setIdProfile(1);
            badge.setDateTimeAchivied(df.parse(badgesJSONArray.getJSONObject(i)
                    .getString("dateTime")));
            badge.setImageBadge(badgesJSONArray.getJSONObject(i)
                    .getString("image125px"));
            badge.setMobileDescription(badgesJSONArray.getJSONObject(i)
                    .getString("mobileDescription"));
            badge.setName(badgesJSONArray.getJSONObject(i)
                    .getString("name"));
            badge.setTimesAchieved(badgesJSONArray.getJSONObject(i)
                    .getInt("timesAchieved"));
            badges.add(badge);
        }

        return badges;
    }

    private Profile parseUserJSONProfile(Profile profile, JSONObject userJSON) throws JSONException, ParseException {
        profile.setAge(userJSON.getInt("age"));
        profile.setAvatar(userJSON.getString("avatar"));
        profile.setDateOfBirth(df.parse(userJSON.getString("dateOfBirth")));
        profile.setDisplayName(userJSON.getString("displayName"));
        profile.setHeight(userJSON.getInt("height"));
        profile.setMemberSince(df.parse(userJSON.getString("memberSince")));
        return profile;
    }


}
