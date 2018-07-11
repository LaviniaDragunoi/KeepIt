package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.user.keepit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingFragment extends Fragment {

    @BindView(R.id.picker_meeting_date)
    TextView meetingDate;
    //Empty constructor;
    public MeetingFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.meeting_edit_fragment, container, false);
        ButterKnife.bind(this,rootView);
        meetingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(meetingDate);

            }
        });

        return rootView;
    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new MeetingDatePickerFragment();
        newFragment.show(getFragmentManager(), "date picker");
    }


}
