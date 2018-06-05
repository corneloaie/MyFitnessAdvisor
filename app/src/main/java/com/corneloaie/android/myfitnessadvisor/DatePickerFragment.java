package com.corneloaie.android.myfitnessadvisor;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {


    public static final String EXTRA_DATE =
            "com.corneloaie.android.myfitnessadvisor.date";
    private DatePassingListener mDatePassingListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDatePassingListener = (DatePassingListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        final DatePicker mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mDatePicker.setMaxDate(new Date().getTime());
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    int year = mDatePicker.getYear();
                    int month = mDatePicker.getMonth();
                    int day = mDatePicker.getDayOfMonth();
                    Date date = new GregorianCalendar(year, month, day).getTime();
                    mDatePassingListener.onDatePass(date);
                })
                .create();
    }

    public interface DatePassingListener {
        void onDatePass(Date date);
    }
}

