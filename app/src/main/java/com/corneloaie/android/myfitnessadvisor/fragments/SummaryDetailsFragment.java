package com.corneloaie.android.myfitnessadvisor.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.corneloaie.android.myfitnessadvisor.R;
import com.corneloaie.android.myfitnessadvisor.model.ActiveMinutes;
import com.corneloaie.android.myfitnessadvisor.model.Summary;

public class SummaryDetailsFragment extends DialogFragment {
    private String dataSummaryItem;
    private Summary mSummary;
    private ActiveMinutes mActiveMinutes;

    public static SummaryDetailsFragment newInstance(String dataSummaryItem, Summary summary, ActiveMinutes activeMinutes) {
        Bundle args = new Bundle();
        args.putString("fragmentDetailsCase", dataSummaryItem);
        args.putSerializable("summmary", summary);
        args.putSerializable("activeMinutes", activeMinutes);
        SummaryDetailsFragment fragment = new SummaryDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dataSummaryItem = getArguments().getString("fragmentDetailsCase", null);
        mSummary = (Summary) getArguments().getSerializable("summmary");
        mActiveMinutes = (ActiveMinutes) getArguments().getSerializable("activeMinutes");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ImageView imageView = view.findViewById(R.id.imageView3);
        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        switch (dataSummaryItem) {
            case "calories":
                imageView.setImageResource(R.drawable.ic_octicons_flame);
                textView.setText("Calories");
                textView2.setText(getString(R.string.caloriesDetails, mSummary.getCaloriesOut(),
                        mSummary.getCaloriesBMR(), mSummary.getCaloriesOut() / mSummary.getCaloriesBMR()));
                break;
            case "restingHR":
                imageView.setImageResource(R.drawable.ic_heart_rate);
                textView.setText("Resting HR");
                textView2.setText(getString(R.string.restingHrDetails, mSummary.getRestingHeartRate()));
                break;
            case "steps":
                imageView.setImageResource(R.drawable.ic_footsteps_icon);
                textView.setText("Steps");
                if (mSummary.getSteps() > 10000)
                    textView2.setText(getString(R.string.stepsDetailsAbove,
                            mSummary.getSteps(), mSummary.getSteps() - 10000));
                else
                    textView2.setText(getString(R.string.stepsDetailsBelow,
                            mSummary.getSteps(), mSummary.getSteps() - 10000));
                break;
            case "floors":
                imageView.setImageResource(R.drawable.floors);
                textView.setText("Floors");
                textView2.setText(getString(R.string.floorsDetails, mSummary.getFloors()));
                break;
            case "sedentaryMinutes":
                imageView.setImageResource(R.drawable.sedentary_active);
                textView.setText("Sedentary time");
                textView2.setText(getString(R.string.sedentaryActiveDetails, mActiveMinutes.getSedentaryMinutes()));
                break;
            case "lightlyActiveMinutes":
                imageView.setImageResource(R.drawable.lightly_active);
                textView.setText("Lighty active time ");
                textView2.setText(getString(R.string.lightlyActiveDetails, mActiveMinutes.getLightlyActiveMinutes()));
                break;
            case "fairlyActiveMinutes":
                imageView.setImageResource(R.drawable.fairly_active);
                textView.setText("Fairly active time");
                textView2.setText(getString(R.string.fairlyActiveDetails, mActiveMinutes.getFairlyActiveMinutes()));
                break;
            case "veryActiveMinutes":
                imageView.setImageResource(R.drawable.very_active);
                textView.setText("Very active time");
                textView2.setText(getString(R.string.veryActiveActiveDetails, mActiveMinutes.getVeryActiveMinutes()));
                break;
        }
        return view;
    }
}
