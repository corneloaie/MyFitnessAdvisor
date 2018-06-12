package com.corneloaie.android.myfitnessadvisor.fragments;

import android.content.Context;
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
import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.app.DataSummaryListObject;
import com.corneloaie.android.myfitnessadvisor.app.OAuthTokenAndId;
import com.corneloaie.android.myfitnessadvisor.database.AppDatabase;
import com.corneloaie.android.myfitnessadvisor.model.ActiveMinutes;
import com.corneloaie.android.myfitnessadvisor.model.Summary;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyCallback;
import com.corneloaie.android.myfitnessadvisor.voley.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SummaryFragment extends Fragment {
    private static final String ARG_DATE = "date";
    private static final String ARG_TOKEN = "token";
    private TextView mTextView;
    private OAuthTokenAndId token;
    private Date mDate;
    private RecyclerView mRecyclerView;
    private SummaryAdapter mSummaryAdapter;
    private InfoClickedListener mInfoClickedListener;

    public static SummaryFragment newInstance(Date date, OAuthTokenAndId token) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        args.putSerializable(ARG_TOKEN, token);
        SummaryFragment fragment = new SummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mInfoClickedListener = (InfoClickedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInfoClickedListener = null;
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
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        TextView summaryDayTextView = view.findViewById(R.id.summary_day_textView);
        summaryDayTextView.setText(new SimpleDateFormat("dd.MM.yyyy").format(mDate));
//        TextView summaryTitleTextView = view.findViewById(R.id.summaryTitle_textView);
//        summaryTitleTextView.setText("Summary");
        ImageView summaryImageView = view.findViewById(R.id.summary_logo_imageView);
        //TODO set image logo

        mRecyclerView = view.findViewById(R.id.summary_recycler_view);
        Summary summary = AppDatabase.getInstance(getActivity().getApplicationContext()).mSummaryDao().getSummaryFromDate(mDate);
        ActiveMinutes activeMinutes = AppDatabase.getInstance(getActivity().getApplicationContext())
                .mActiveMinutesDao().getActiveMinutesFromDate(mDate);
        if (summary != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            List<String> list = DataSummaryListObject.get(getActivity().getApplicationContext()).getList();
            mSummaryAdapter = new SummaryAdapter(list, summary, activeMinutes);
            mRecyclerView.setAdapter(mSummaryAdapter);
        } else {
            getActivitySummary();
        }


        return view;
    }

    public void getActivitySummary() {
        VolleyCallback callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONObject summaryObj = object.getJSONObject("summary");
                    Summary summary = new Summary(summaryObj.getInt("activityCalories"),
                            summaryObj.getInt("caloriesBMR"),
                            summaryObj.getInt("caloriesOut"),
                            summaryObj.getInt("floors"),
                            summaryObj.getInt("restingHeartRate"),
                            summaryObj.getInt("steps"),
                            mDate);
                    ActiveMinutes activeMinutes = new ActiveMinutes(mDate,
                            summaryObj.getInt("fairlyActiveMinutes"),
                            summaryObj.getInt("lightlyActiveMinutes"),
                            summaryObj.getInt("sedentaryMinutes"),
                            summaryObj.getInt("veryActiveMinutes"));
                    AppDatabase.getInstance(getActivity().getApplicationContext()).mSummaryDao().insert(summary);
                    AppDatabase.getInstance(getActivity().getApplicationContext()).mActiveMinutesDao().insert(activeMinutes);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    List<String> list = DataSummaryListObject.get(getActivity().getApplicationContext()).getList();
                    mSummaryAdapter = new SummaryAdapter(list, summary, activeMinutes);
                    mRecyclerView.setAdapter(mSummaryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                //TODO if no internet and data
            }
        };
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
        VolleyHelper.getInstance().get("1/user/" + token.getUserID() +
                        "/activities/date/" + stringDate + ".json",
                callback, getActivity());
    }


    public interface InfoClickedListener {
        void onClickInfoCallback();
    }

    private class SummaryViewHolder extends RecyclerView.ViewHolder {

        private TextView mItemTextView;
        private ImageView mInfoImageView;
        private ImageView mImageView;

        private SummaryViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.data_summary_item, parent, false));
            mInfoImageView = itemView.findViewById(R.id.summaryItemInfo_imageView);
            mItemTextView = itemView.findViewById(R.id.summaryItem_textView);
            mImageView = itemView.findViewById(R.id.summaryItem_imageView);
        }

        public void bind(String dataSummaryItem, Summary summary, ActiveMinutes activeMinutes) {
            switch (dataSummaryItem) {
                case "calories":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.calories, summary.getCaloriesOut()));
//                    mImageView.setImageResource();
                    mImageView.setClickable(true);
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mInfoClickedListener.onClickInfoCallback();
                        }
                    });
                    break;
                case "restingHR":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.restingHR, summary.getRestingHeartRate()));
//                    mImageView.setImageResource();
                    mImageView.setClickable(true);
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mInfoClickedListener.onClickInfoCallback();
                        }
                    });
                    break;
                case "steps":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.steps, summary.getSteps()));
//                    mImageView.setImageResource();
                    break;
                case "floors":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.floors, summary.getFloors()));
//                    mImageView.setImageResource();
                    break;
                case "sedentaryMinutes":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.sedentaryMinutes, activeMinutes.getSedentaryMinutes()));
//                    mImageView.setImageResource();
                    break;
                case "lightlyActiveMinutes":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.lightlyActiveMinutes, activeMinutes.getLightlyActiveMinutes()));
//                    mImageView.setImageResource();
                    break;
                case "fairlyActiveMinutes":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.fairlyActiveMinutes, activeMinutes.getFairlyActiveMinutes()));
//                    mImageView.setImageResource();
                    break;
                case "veryActiveMinutes":
                    mInfoImageView.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    mItemTextView.setText(getString(R.string.veryActiveMinutes, activeMinutes.getVeryActiveMinutes()));
//                    mImageView.setImageResource();
                    break;
            }
        }
    }

    private class SummaryAdapter extends RecyclerView.Adapter<SummaryViewHolder> {
        private List<String> summaryItemList;
        private Summary mSummary;
        private ActiveMinutes mActiveMinutes;

        public SummaryAdapter(List<String> summaryItemList, Summary summary, ActiveMinutes activeMinutes) {
            this.summaryItemList = summaryItemList;
            mSummary = summary;
            mActiveMinutes = activeMinutes;
        }

        @Override
        public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SummaryViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SummaryViewHolder holder, int position) {
            String dataSummaryItem = summaryItemList.get(position);
            holder.bind(dataSummaryItem, mSummary, mActiveMinutes);
        }

        @Override
        public int getItemCount() {
            return summaryItemList.size();
        }
    }


}
