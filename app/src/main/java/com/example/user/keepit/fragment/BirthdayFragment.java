package com.example.user.keepit.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import static java.lang.String.valueOf;

public class BirthdayFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected {
    @BindView(R.id.picker_birth_date)
    TextView birthDate;
    @BindView(R.id.birthday_person_name)
    EditText birthdayPersonNameEditText;
    @BindView(R.id.person_age)
    TextView personAgeTextView;
    private Date birthDateDate;
    private String birthDateString;

    public static final String BIRTHDAY_TYPE = "Birthday";

    private AppRoomDatabase roomDb;
    private EditEventViewModel mViewModel;
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
    public BirthdayFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.birthday_edit_fragment, container, false);
        ButterKnife.bind(this, rootView);
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

            }else {
                eventId = bundle.getInt(EVENT_ENTITY_ID);

            }
            }

        factory = new EditEventModelFactory(mRepository, eventId);
       updateTheList();
        showPickerSelected();
        return rootView;
    }

    private void updateTheList(){
        mViewModel = ViewModelProviders.of(this, factory).get(EditEventViewModel.class);
        mViewModel.getEvent().observe(this, new Observer<EventEntity>() {
            @Override
            public void onChanged(@Nullable EventEntity eventEntity) {
                mViewModel.getEvent().removeObserver(this);

            }
        });
    }

    private void showPickerSelected() {
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);

            }
        });
    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "date picker");

    }

    private void populateUI(EventEntity eventEntity) {

        birthDate.setText(eventEntity.getDateString());
        birthDateDate = eventEntity.getDate();
        birthDateString = eventEntity.getDateString();
        birthdayPersonNameEditText.setText(eventEntity.getPersonName());
        personAgeTextView.setText(valueOf(getAge(birthDateDate)));

    }
    @Override
    public void onDateSelected(Date date, String dateString) {
        birthDateDate = date;
        birthDateString = dateString;
        int age = getAge(date);
        personAgeTextView.setText(valueOf(age));
        birthDate.setText(dateString);
    }

    private int getAge(Date date) {
        return 20;
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
                //delete items from birthdays list
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_save:
                saveBirthday();
                return true;
            case R.id.action_share:
                //share meeting
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteBirthday() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(eventId == DEFAULT_ID) {
                    birthDate.setText("");
                    birthdayPersonNameEditText.setText("");
                    personAgeTextView.setText("");

                }else {
                    mViewModel.deleteEvent(eventId);
                    updateTheList();
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });

    }

    private void saveBirthday() {
        eventType = BIRTHDAY_TYPE;
        title = " ";
        date = birthDateDate;
        dateString = birthDateString;
        time = " ";
        personName = birthdayPersonNameEditText.getText().toString();
        location = " ";
        note = " ";
        EventEntity birthday = new EventEntity(eventType, title,date, dateString, time, personName,
                location, note);
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(eventId == DEFAULT_ID) {
                    mViewModel.addEvent(birthday);

                }else {
                    birthday.setId(eventId);
                    mViewModel.updateEvent(birthday);
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setMessage(R.string.delete_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the event.
                deleteBirthday();

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
}
