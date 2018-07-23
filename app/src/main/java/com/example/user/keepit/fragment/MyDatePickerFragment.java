package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MyDatePickerFragment extends DialogFragment {

    OnDatePickerSelected listener;
    interface OnDatePickerSelected{
        public void onDateSelected(Date date, String dateString);
    }

    public void setListener(OnDatePickerSelected listener){
        this.listener = listener;
    }

    public DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            @SuppressLint("DefaultLocale")
            Date date = getDateFromDatePicker(view);
            @SuppressLint("DefaultLocale") String pickedDate = (String.format("%02d/%02d/%04d",
                    view.getDayOfMonth(), view.getMonth()+1, view.getYear()));
            listener.onDateSelected(date, pickedDate);


        }
    };


    @NonNull
    @SuppressLint("NewApi")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), dateSetListener, year, month, day);
    }

    /**
     *
     * @param datePicker
     * @return a java.util.Date
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() +1;
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


}
