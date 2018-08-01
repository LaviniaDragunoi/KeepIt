package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Solution found here: http://www.zoftino.com/android-timepicker-example
 */
@SuppressLint("ValidFragment")
public class MyTimePickerFragment extends DialogFragment {
    OnTimePickerSelected listener;
    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint({"NewApi", "SetTextI18n"})
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    @SuppressLint("DefaultLocale")
                    String timePicked = (String.format("%02d:%02d", view.getHour(), view.getMinute()));
                    listener.onTimeSelected(timePicked);
                }
            };

    public void setListener(OnTimePickerSelected listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), timeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    interface OnTimePickerSelected {
        public void onTimeSelected(String time);
    }
}
