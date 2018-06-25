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


public class HrFragmentDetails extends DialogFragment {
    private String dataHRItem;
    private int restingHrValue;

    public static HrFragmentDetails newInstance(String dataHRItem, int restingHrValue) {

        Bundle args = new Bundle();
        args.putString("restingHR", dataHRItem);
        args.putInt("restingHrValue", restingHrValue);
        HrFragmentDetails fragment = new HrFragmentDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHRItem = getArguments().getString("restingHR");
        restingHrValue = getArguments().getInt("restingHrValue", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details3, container, false);
        ImageView imageView = view.findViewById(R.id.imageView5);
        TextView textView = view.findViewById(R.id.textView5);
        TextView textView2 = view.findViewById(R.id.textView6);
        switch (dataHRItem) {
            case "restingHR":
                imageView.setImageResource(R.drawable.ic_heart_rate);
                textView.setText("Heart Rate");
                textView2.setText(getString(R.string.restingHrDetails, restingHrValue));
                break;
            case "fatBurn":
                textView.setText("Fat burn");
                textView2.setText(getString(R.string.fatBurnDetails));
                break;
            case "cardio":
                textView.setText("Fat burn");
                textView2.setText(getString(R.string.cardioDetails));
                break;
            case "peak":
                textView.setText("Fat burn");
                textView2.setText(getString(R.string.peakDetails));
                break;
        }

        return view;
    }
}
