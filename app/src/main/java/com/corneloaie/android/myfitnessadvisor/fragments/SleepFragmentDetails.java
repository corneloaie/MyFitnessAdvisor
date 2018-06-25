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

public class SleepFragmentDetails extends DialogFragment {
    private String dataItemType;
    private int value;

    public static SleepFragmentDetails newInstance(String dataItemType) {

        Bundle args = new Bundle();
        args.putString("dataItemType", dataItemType);
        SleepFragmentDetails fragment = new SleepFragmentDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataItemType = getArguments().getString("dataItemType");
        value = getArguments().getInt("value", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details3, container, false);
        ImageView imageView = view.findViewById(R.id.imageView5);
        TextView textView = view.findViewById(R.id.textView5);
        TextView textView2 = view.findViewById(R.id.textView6);

        switch (dataItemType) {
            case "sleepTime":
                imageView.setImageResource(R.drawable.moon_icon);
                textView.setText("Sleep Time");
                textView2.setText(getString(R.string.sleepTimeDetails));
                break;
            case "Deep":
                textView.setText("Deep stage");
                textView2.setText(getString(R.string.deepDetails));
                break;
            case "Light":
                textView.setText("Light stage");
                textView2.setText(getString(R.string.lightDetails));
                break;
            case "REM":
                textView.setText("REM stage");
                textView2.setText(getString(R.string.remDetails));
                break;
            case "Awake":
                textView.setText("Awake stage");
                textView2.setText(getString(R.string.awakeDetails));
                break;
        }

        return view;
    }
}
