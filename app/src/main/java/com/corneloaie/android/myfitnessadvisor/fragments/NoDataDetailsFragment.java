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

public class NoDataDetailsFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details3, container, false);
        ImageView imageView = view.findViewById(R.id.imageView5);
        TextView textView = view.findViewById(R.id.textView5);
        TextView textView2 = view.findViewById(R.id.textView6);
        textView.setText(R.string.no_data);
        textView2.setText(R.string.no_data_message);
        return view;
    }
}
