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

public class NoteFragment extends Fragment {
    @BindView(R.id.picker_note_deadline)
    TextView noteDeadline;
    //Empty constructor;
    public NoteFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.note_edit_fragment, container, false);
        ButterKnife.bind(this,rootView);
        noteDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(noteDeadline);

            }
        });

        return rootView;
    }

    public void showDatePicker(View v) {
        DialogFragment newFragment = new MeetingDatePickerFragment();
        newFragment.show(getFragmentManager(), "date picker");
    }
}
