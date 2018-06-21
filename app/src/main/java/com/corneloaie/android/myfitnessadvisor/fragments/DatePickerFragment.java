package com.corneloaie.android.myfitnessadvisor.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.corneloaie.android.myfitnessadvisor.R;

import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {


    public static final String ARG_CASE = "userCase";
    private DatePassingListener mDatePassingListener;
    private String userCase;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDatePassingListener = (DatePassingListener) context;
    }

    public static DatePickerFragment newInstance(String userCase) {

        Bundle args = new Bundle();
        args.putString(ARG_CASE, userCase);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userCase = getArguments().getString(ARG_CASE);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        final DatePicker mDatePicker = view.findViewById(R.id.dialog_date_picker);
        mDatePicker.setMaxDate(new Date().getTime());
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    int year = mDatePicker.getYear();
                    int month = mDatePicker.getMonth();
                    int day = mDatePicker.getDayOfMonth();
                    Date date = new GregorianCalendar(year, month, day).getTime();
                    mDatePassingListener.onDatePass(date, userCase);
                })
                .create();
    }

    public interface DatePassingListener {
        void onDatePass(Date date, String userCase);
    }
}

