package com.example.user.keepit.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.keepit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BirthdayFragment extends Fragment {
    @BindView(R.id.picker_birth_date)
    TextView birthDate;
    //Empty constructor;
    public BirthdayFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.birthday_edit_fragment, container, false);
        ButterKnife.bind(this,rootView);
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(birthDate);

            }
        });

        return rootView;
    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new MeetingDatePickerFragment();
        newFragment.show(getFragmentManager(), "date picker");
    }

}
