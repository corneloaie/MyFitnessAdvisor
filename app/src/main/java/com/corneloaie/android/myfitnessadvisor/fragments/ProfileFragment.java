package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
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
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
    private OAuthTokenAndId token;
    private RecyclerView mRecyclerView;
    private BadgeAdapter mBadgeAdapter;
    private ImageView avatarImageView;
    private TextView displayNameTextView, memberSinceTextView, ageTextView, heightTextView;


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
        bindXmlToJava(view);
        List<Badge> badges = AppDatabase.getInstance(getActivity().getApplicationContext())
                .mBadgeDao().getAllBadges();
        Profile profile = AppDatabase.getInstance(getActivity().getApplicationContext())
                .mProfileDao().getProfile();
        if (profile != null) {
            bindDataToXml(profile);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBadgeAdapter = new BadgeAdapter(badges);
            mRecyclerView.setAdapter(mBadgeAdapter);
        } else {
            getProfileData();
        }


        return view;
    }

    private void bindXmlToJava(View view) {
        avatarImageView = view.findViewById(R.id.avatar_imageView);
        displayNameTextView = view.findViewById(R.id.displayName_textView);
        memberSinceTextView = view.findViewById(R.id.memberSince_textView);
        ageTextView = view.findViewById(R.id.age_textView);
        heightTextView = view.findViewById(R.id.height_textView);
        mRecyclerView = view.findViewById(R.id.badge_recycler_view);
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
                    bindDataToXml(profile);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mBadgeAdapter = new BadgeAdapter(badges);
                    mRecyclerView.setAdapter(mBadgeAdapter);
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

    private void bindDataToXml(Profile profile) {
        Glide.with(getActivity()).load(profile.getAvatar())
                .into(avatarImageView);
        displayNameTextView.setText(profile.getDisplayName());
        memberSinceTextView.setText(getString(R.string.member_since, df2.format(profile.getMemberSince())));
        ageTextView.setText(getString(R.string.age_profile, profile.getAge()));
        heightTextView.setText(getString(R.string.height_profile, profile.getHeight()));
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


    private class BadgeViewHolder extends RecyclerView.ViewHolder {

        private TextView mBadgeRecordsTextView;
        private TextView mBadgeNameTextView;
        private ImageView mBadgeLogoImageView;

        public BadgeViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.badge_item, parent, false));
            mBadgeRecordsTextView = itemView.findViewById(R.id.badgeRecords_textView);
            mBadgeLogoImageView = itemView.findViewById(R.id.badgeLogo_imageView);
            mBadgeNameTextView = itemView.findViewById(R.id.badgeName_textView);
        }

        public void bind(Badge badge) {
            Glide.with(getActivity()).load(badge.getImageBadge())
                    .into(mBadgeLogoImageView);
            mBadgeNameTextView.setText(badge.getName());
            mBadgeRecordsTextView.setText(getString(R.string.records,
                    badge.getTimesAchieved(), df2.format(badge.getDateTimeAchivied()),
                    badge.getMobileDescription()));
        }
    }

    private class BadgeAdapter extends RecyclerView.Adapter<BadgeViewHolder> {

        private List<Badge> mBadges;

        public BadgeAdapter(List<Badge> badges) {
            mBadges = badges;
        }

        @Override
        public BadgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new BadgeViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BadgeViewHolder holder, int position) {
            Badge badge = mBadges.get(position);
            holder.bind(badge);

        }

        @Override
        public int getItemCount() {
            return mBadges.size();
        }
    }


}
