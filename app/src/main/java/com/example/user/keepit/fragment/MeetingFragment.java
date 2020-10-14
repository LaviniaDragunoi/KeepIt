package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.activities.EditActivity;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.entities.EventEntity;
import com.example.user.keepit.networking.ApiClient;
import com.example.user.keepit.networking.ApiInterface;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.utils.Constants.DEFAULT_ID;
import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.EXTRA_EVENT;
import static com.example.user.keepit.utils.Constants.MEETING_TYPE;
import static java.security.AccessController.getContext;

public class MeetingFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected,
        MyTimePickerFragment.OnTimePickerSelected, IOnBackPressed{

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
    private Repository mRepository;
    private EventEntity currentEvent;

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
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors, roomDb, roomDb.eventDao(), apiInterface);

        if (savedInstanceState != null) {
            if (eventId != DEFAULT_ID) {
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.edit_meeting));
            }
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_EVENT)) {
                currentEvent = bundle.getParcelable(EXTRA_EVENT);
                eventId = Objects.requireNonNull(currentEvent).getId();
                populateUI(currentEvent);
            } else {
                eventId = bundle.getInt(EVENT_ENTITY_ID);
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.add_meeting));
            }
        }

        factory = new EditEventModelFactory(mRepository, eventId);
        mViewModel = new ViewModelProvider(this, factory).get(EditEventViewModel.class);
        showPickerSelected();

        showLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.setMeetingLocation(meetingLocationEditText.getText().toString());
                mViewModel.setShowLocation(true);
            }
        });

        final String[] meetingLocation = new String[1];
        mViewModel.getMeetingLocation().observe(getViewLifecycleOwner(), location ->
        {
            if(location != null) {
                meetingLocation[0] = location;
            }
        } );

        mViewModel.getShowLocationOnMap().observe(getViewLifecycleOwner(), show ->
        {
            if(show) {
                Toast.makeText(getActivity(), "You have to meet with your friend here: " + meetingLocation[0], Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    private void populateUI(EventEntity eventEntity) {
        meetingTitleEditText.setText(eventEntity.getTitle());
        meetingDateTV.setText(eventEntity.getDateString());
        meetingDateDate = eventEntity.getDate();
        meetingDateString = eventEntity.getDateString();
        meetingTimeString = eventEntity.getTime();
        meetingTimeTV.setText(meetingTimeString);
        meetingPersonEditText.setText(eventEntity.getPersonName());
        meetingLocationEditText.setText(eventEntity.getMeetingLocation());
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
        String meetingLocation = meetingLocationEditText.getText().toString();
        String note = " ";
        int done = 0;
        int age = 0;
        EventEntity newMeeting = null;
        if(currentEvent != null) {
            mRepository.loadEvent(currentEvent.getId());
            currentEvent = new EventEntity(MEETING_TYPE, title, date, dateString, time,
                    personName, meetingLocation, currentEvent.getLocation(), note, done, age);
            mRepository.loadEvent(currentEvent.getId());
        } else {
            newMeeting = new EventEntity(MEETING_TYPE, title, date, dateString, time,
                    personName, meetingLocation,"", note, done, age);
        }


        EventEntity finalNewMeeting = newMeeting;
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (eventId == DEFAULT_ID) {
                    mViewModel.addEvent(finalNewMeeting);
                } else {
                    currentEvent.setId(eventId);
                    mViewModel.updateEvent(currentEvent);
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    private void deleteMeeting() {
        executors.diskIO().execute(() -> {
            if (eventId == DEFAULT_ID) {
                meetingTitleEditText.setText("");
                meetingDateTV.setText("");
                meetingTimeTV.setText("");
                meetingPersonEditText.setText("");
                meetingLocationEditText.setText("");
            } else {
                mViewModel.deleteEvent(eventId);

            }
            Objects.requireNonNull(getActivity()).finish();
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
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            // User clicked the "Cancel" button, so dismiss the dialog
            // and continue editing the event.
            if (dialog != null) {
                dialog.dismiss();
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
