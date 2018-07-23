package com.example.user.keepit.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.EditActivity.EVENT_ENTITY_ID;
import static com.example.user.keepit.adapters.ListAdapter.EXTRA_EVENT;

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
    public static final String MEETING_TYPE = "Meeting";
    private AppExecutors executors;
    private int eventId;
    private Repository mRepository;

    private String eventType;
    private String title;
    private Date date;
    private String dateString;
    private String time;
    private String personName;
    private String location;
    private String note;
    private EditEventModelFactory factory;
    private EventEntity currentEvent;

    //Empty constructor;
    public MeetingFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.meeting_edit_fragment, container, false);
        ButterKnife.bind(this,rootView);
        setHasOptionsMenu(true);
        roomDb = AppRoomDatabase.getsInstance(getContext());
        executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors,roomDb,roomDb.eventDao());

        Bundle bundle = getArguments();
        if(bundle != null) {
            if (bundle.containsKey(EXTRA_EVENT)) {
                currentEvent = bundle.getParcelable(EXTRA_EVENT);
                eventId = currentEvent.getId();
                populateUI(currentEvent);

          }else if(bundle.containsKey(EVENT_ENTITY_ID)){
                eventId = bundle.getInt(EVENT_ENTITY_ID);

            }
        }
        factory = new EditEventModelFactory(mRepository, eventId);
        mViewModel = ViewModelProviders.of(this, factory).get(EditEventViewModel.class);
        mViewModel.getEvent().observe(this, new Observer<EventEntity>() {
            @Override
            public void onChanged(@Nullable EventEntity eventEntity) {
                mViewModel.getEvent().removeObserver(this);

            }
        });

       showPickerSelected();

        return rootView;
    }

    private void populateUI(EventEntity eventEntity) {
        meetingTitleEditText.setText(eventEntity.getTitle());
        meetingDateTV.setText(eventEntity.getDateString());
        date = eventEntity.getDate();
        dateString = eventEntity.getDateString();
        meetingTimeTV.setText(eventEntity.getTime());
        meetingPersonEditText.setText(eventEntity.getPersonName());
        meetingLocationEditText.setText(eventEntity.getLocation());

    }

    private void showPickerSelected(){
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


            eventType = MEETING_TYPE;
            title = meetingTitleEditText.getText().toString();
            date = meetingDateDate;
            dateString = meetingDateString;
            time = meetingTimeString;
            personName = meetingPersonEditText.getText().toString();
            location = meetingLocationEditText.getText().toString();
            note = " ";
            EventEntity meeting = new EventEntity(eventType, title, date, dateString, time,
                    personName, location, note);

            executors.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(eventId == DEFAULT_ID) {
                        mViewModel.addEvent(meeting);

                    }else {
                        meeting.setId(eventId);
                        mViewModel.updateEvent(meeting);
                    }
                    Objects.requireNonNull(getActivity()).finish();
                }
            });


    }
    private void deleteMeeting() {

    }

}
