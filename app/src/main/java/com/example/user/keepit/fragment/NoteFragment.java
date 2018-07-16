package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected{

    @BindView(R.id.picker_note_deadline)
    TextView noteDeadline;
    //Empty constructor;
    public NoteFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.note_edit_fragment, container, false);
        ButterKnife.bind(this,rootView);
        noteDeadline = rootView.findViewById(R.id.picker_note_deadline);
        noteDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);

            }
        });

        return rootView;
    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "date picker");
    }

    @Override
    public void onDateSelected(Date date, String dateString) {
        noteDeadline.setText(date.toString());
    }
}
