package com.example.user.keepit.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
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
import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private String meetingDateString;
    private String meetingTimeString;
    private final static int DEFAULT_MEETING_ID = -1;
    private int mMeetingId = DEFAULT_MEETING_ID;
    private AppRoomDatabase roomDb;
    private EditEventViewModel mViewModel;
    private Repository mRepository;
    private static final String EVENT_TYPE = "meeting";
    private AppExecutors executors;

    //Empty constructor;
    public MeetingFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.meeting_edit_fragment, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        AppRoomDatabase roomDb = AppRoomDatabase.getsInstance(getContext());
        executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors, roomDb, roomDb.eventDao());
        EditEventModelFactory mViewModelFactory = new EditEventModelFactory(mRepository);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                mViewModelFactory).get(EditEventViewModel.class);
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
        meetingDateString = dateString;
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

            mViewModel.eventType = EVENT_TYPE;
            mViewModel.title = meetingTitleEditText.getText().toString();
            mViewModel.date = meetingDateDate;
            mViewModel.dateString = meetingDateString;
            mViewModel.time = meetingTimeString;
            mViewModel.personName = meetingPersonEditText.getText().toString();
            mViewModel.location = meetingLocationEditText.getText().toString();
            mViewModel.note = "";
            EventEntity meeting = new EventEntity(mViewModel.eventType, mViewModel.title, mViewModel.date,
                    mViewModel.dateString, mViewModel.time, mViewModel.personName, mViewModel.location, mViewModel.note);

            mViewModel.addMeeting(meeting);

    }
    private void deleteMeeting() {

    }

}
