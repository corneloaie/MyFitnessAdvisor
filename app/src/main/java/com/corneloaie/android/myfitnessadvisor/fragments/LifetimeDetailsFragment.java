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
import com.corneloaie.android.myfitnessadvisor.model.Lifetime;

public class LifetimeDetailsFragment extends DialogFragment {
    private String dataLifetimeItem;
    private Lifetime mLifetime;

    public static LifetimeDetailsFragment newInstance(String dataLifetimeItem, Lifetime lifetime) {

        Bundle args = new Bundle();
        args.putString("fragmentDetailsCase", dataLifetimeItem);
        args.putSerializable("lifetime", lifetime);
        LifetimeDetailsFragment fragment = new LifetimeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dataLifetimeItem = getArguments().getString("fragmentDetailsCase", null);
        mLifetime = (Lifetime) getArguments().getSerializable("summmary");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details2, container, false);
        ImageView imageView = view.findViewById(R.id.imageView4);
        TextView textView = view.findViewById(R.id.textView3);
        TextView textView2 = view.findViewById(R.id.textView4);
        switch (dataLifetimeItem) {
            case "stepsAllTime":
                imageView.setImageResource(R.drawable.ic_footsteps_icon);
                textView.setText("Steps");
                textView2.setText(getText(R.string.stepsAllTimeDetails));
                break;
            case "floorsAllTime":
                imageView.setImageResource(R.drawable.floors);
                textView.setText("Floors");
                textView2.setText(getText(R.string.floorsAllTimeDetails));
                break;
            case "distanceAllTime":
                imageView.setImageResource(R.drawable.distance_icon);
                textView.setText("Distance");
                textView2.setText(getText(R.string.distanceAllTimeDetails));
                break;
            case "floorsBest":
                imageView.setImageResource(R.drawable.floors);
                textView.setText("Floors");
                textView2.setText(getText(R.string.floorsBestDetails));
                break;
            case "distanceBest":
                imageView.setImageResource(R.drawable.distance_icon);
                textView.setText("Distance");
                textView2.setText(getText(R.string.distanceBestDetails));
                break;
            case "stepsBest":
                imageView.setImageResource(R.drawable.ic_footsteps_icon);
                textView.setText("Steps");
                textView2.setText(getText(R.string.stepsBestDetails));
                break;
        }


        return view;
    }
}
