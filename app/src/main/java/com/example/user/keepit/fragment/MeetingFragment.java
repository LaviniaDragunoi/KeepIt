package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.activities.EditActivity;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;

import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.utils.Constants.DEFAULT_ID;
import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.EXTRA_EVENT;
import static com.example.user.keepit.utils.Constants.MEETING_TYPE;

public class MeetingFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected,
        MyTimePickerFragment.OnTimePickerSelected, IOnBackPressed {

    public boolean isChanged = false;
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
    @BindView(R.id.location_map)
    TextView showLocationTextView;
    private Date meetingDateDate;
    private String meetingDateString;
    private String meetingTimeString;
    private EditEventViewModel mViewModel;
    private AppExecutors executors;
    private int eventId;
    private EditEventModelFactory factory;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isChanged = true;
            return false;
        }
    };

    //Empty constructor;
    public MeetingFragment() {
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.meeting_edit_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        meetingDateTV.setOnTouchListener(mTouchListener);
        meetingTitleEditText.setOnTouchListener(mTouchListener);
        meetingTimeTV.setOnTouchListener(mTouchListener);
        meetingPersonEditText.setOnTouchListener(mTouchListener);
        meetingLocationEditText.setOnTouchListener(mTouchListener);

        AppRoomDatabase roomDb = AppRoomDatabase.getsInstance(getContext());
        executors = AppExecutors.getInstance();
        Repository mRepository = Repository.getsInstance(executors, roomDb, roomDb.eventDao());

        if (savedInstanceState != null) {
            if (eventId != DEFAULT_ID) {
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.edit_meeting));
            }
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_EVENT)) {
                EventEntity currentEvent = bundle.getParcelable(EXTRA_EVENT);
                eventId = Objects.requireNonNull(currentEvent).getId();
                populateUI(currentEvent);
            } else {
                eventId = bundle.getInt(EVENT_ENTITY_ID);
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.add_meeting));
            }
        }

        factory = new EditEventModelFactory(mRepository, eventId);
        updateTheList();
        showPickerSelected();

        showLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationOnMap(meetingLocationEditText.getText().toString());
            }
        });
        return rootView;
    }

    private void updateTheList() {
        mViewModel = ViewModelProviders.of(this, factory).get(EditEventViewModel.class);
        mViewModel.getEvent().observe(this, new Observer<EventEntity>() {
            @Override
            public void onChanged(@Nullable EventEntity eventEntity) {
                mViewModel.getEvent().removeObserver(this);
            }
        });
    }

    private void populateUI(EventEntity eventEntity) {
        meetingTitleEditText.setText(eventEntity.getTitle());
        meetingDateTV.setText(eventEntity.getDateString());
        meetingDateDate = eventEntity.getDate();
        meetingDateString = eventEntity.getDateString();
        meetingTimeString = eventEntity.getTime();
        meetingTimeTV.setText(meetingTimeString);
        meetingPersonEditText.setText(eventEntity.getPersonName());
        meetingLocationEditText.setText(eventEntity.getLocation());
    }

    private void showPickerSelected() {
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
        newFragment.show(Objects.requireNonNull(getFragmentManager()), "date picker");
    }

    public void showTimePickerDialog(View v) {
        MyTimePickerFragment newFragment = new MyTimePickerFragment();
        newFragment.setListener(this);
        newFragment.show(Objects.requireNonNull(getFragmentManager()), "time picker");
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

    private void showLocationOnMap(String location) {
        //get the location to String
        location = meetingLocationEditText.getText().toString();
        //build the address Uri
        Uri locationUri = new Uri.Builder()
                .scheme("geo")
                .path("0,0")
                .appendQueryParameter("q", location)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(locationUri);
        if (intent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //Creating Intents for each menu item.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                //delete items from meetings list
                showDeleteDialog();
                return true;
            case R.id.action_save:
                saveMeeting();
                return true;
            case R.id.action_share:
                //share meeting
                String name = meetingPersonEditText.getText().toString();
                String locationMeeting = meetingLocationEditText.getText().toString();
                createSharingText(name, meetingDateString, meetingTimeString, locationMeeting);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createSharingText(String personName, String dateString, String time, String location) {
        String mimeType = "text/plain";
        String title = "Meeting";
        String message = personName + " ,please don't forget of our meeting schedule on " + dateString + " at " + time;
        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(Objects.requireNonNull(getActivity()))
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(message)
                .startChooser();
    }

    private void saveMeeting() {
        String title = meetingTitleEditText.getText().toString();
        Date date = meetingDateDate;
        String dateString = meetingDateString;
        String time = meetingTimeString;
        String personName = meetingPersonEditText.getText().toString();
        String location = meetingLocationEditText.getText().toString();
        String note = " ";
        int done = 0;
        int age = 0;
        EventEntity meeting = new EventEntity(MEETING_TYPE, title, date, dateString, time,
                personName, location, note, done, age);
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (eventId == DEFAULT_ID) {
                    mViewModel.addEvent(meeting);
                } else {
                    meeting.setId(eventId);
                    mViewModel.updateEvent(meeting);
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    private void deleteMeeting() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (eventId == DEFAULT_ID) {
                    meetingTitleEditText.setText("");
                    meetingDateTV.setText("");
                    meetingTimeTV.setText("");
                    meetingPersonEditText.setText("");
                    meetingLocationEditText.setText("");
                } else {
                    mViewModel.deleteEvent(eventId);
                    updateTheList();
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    private void showDeleteDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage(R.string.delete_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the event.
                deleteMeeting();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the event.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onBackPressed() {
        return isChanged;
    }
}
