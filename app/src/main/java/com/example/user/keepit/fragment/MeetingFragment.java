package com.example.user.keepit.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventsEntity;
import com.example.user.keepit.database.MeetingsEntity;
import com.example.user.keepit.viewModels.EditMeetingViewModel;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected, MyTimePickerFragment.OnTimePickerSelected{

    @BindView(R.id.picker_meeting_date)
    TextView meetingDateTV;
    @BindView(R.id.picker_meeting_time)
    TextView meetingTimeTV;
    @BindView(R.id.meetings_name_title)
    EditText meetingTitleEditText;
    @BindView(R.id.meeting_person_name)
    EditText meetingPersonEditText;
    @BindView(R.id.meeting_location)
    EditText meetingLocationEditText;
    private Date meetingDateDate;
    private String meetingTimeString;
    private final static int DEFAULT_MEETING_ID = -1;
    private int mMeetingId = DEFAULT_MEETING_ID;
    private AppRoomDatabase roomDb;
    private EditMeetingViewModel mViewModel;

    //Empty constructor;
    public MeetingFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.meeting_edit_fragment, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        roomDb = AppRoomDatabase.getsInstance(getContext());
        meetingDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);

            }
        });

        meetingTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);

            }
        });

        mViewModel = ViewModelProviders.of(getActivity()).get(EditMeetingViewModel.class);
        return rootView;
    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "date picker");
    }

    /**
     *
     * @param v
     */
    public void showTimePickerDialog(View v) {
        MyTimePickerFragment newFragment = new MyTimePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "time picker");

    }

    @Override
    public void onDateSelected(Date date, String dateString) {
        meetingDateDate = date;
        meetingDateTV.setText(dateString);
    }

    @Override
    public void onTimeSelected(String time) {
        meetingTimeString = time;
        meetingTimeTV.setText(time);
    }

    //Inflating the menu bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Creating Intents for each menu item.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                //delete items from meetings list
                return true;
            case R.id.action_save:
                saveMeeting();
                return true;
            case R.id.action_share:
               //share meeting
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveMeeting() {
        mViewModel.meetingTitle = meetingTitleEditText.getText().toString();
        mViewModel.meetingPersonName = meetingPersonEditText.getText().toString();
        mViewModel.location = meetingLocationEditText.getText().toString();
        mViewModel.date = meetingDateDate;
        mViewModel.time = meetingTimeString;
    }
}
