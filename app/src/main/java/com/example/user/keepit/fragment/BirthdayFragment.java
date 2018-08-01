package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.activities.EditActivity;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.utils.Constants.BIRTHDAY_TYPE;
import static com.example.user.keepit.utils.Constants.DEFAULT_ID;
import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.EXTRA_EVENT;
import static java.lang.String.valueOf;

public class BirthdayFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected, IOnBackPressed {

    public boolean isChanged = false;
    @BindView(R.id.picker_birth_date)
    TextView birthDate;
    @BindView(R.id.birthday_person_name)
    EditText birthdayPersonNameEditText;
    @BindView(R.id.person_age)
    TextView personAgeTextView;
    @BindView(R.id.send_message_tv)
    TextView sendMessageTextView;
    private Context context;
    private Date birthDateDate;
    private String birthDateString;
    private EditEventViewModel mViewModel;
    private AppExecutors executors;
    private int eventId;
    private int age;
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
    public BirthdayFragment() {
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.birthday_edit_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        birthDate.setOnTouchListener(mTouchListener);
        birthdayPersonNameEditText.setOnTouchListener(mTouchListener);

        AppRoomDatabase roomDb = AppRoomDatabase.getsInstance(getContext());
        executors = AppExecutors.getInstance();
        Repository mRepository = Repository.getsInstance(executors, roomDb, roomDb.eventDao());

        if (savedInstanceState != null) {
            if (eventId != DEFAULT_ID) {
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.edit_birthday));
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
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.add_birthday));
            }
        }
        factory = new EditEventModelFactory(mRepository, eventId);
        updateTheList();
        showPickerSelected();
        sendMessageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage(birthdayPersonNameEditText.getText().toString());
            }
        });
        return rootView;
    }

    private void createMessage(String personName) {
        String mimeType = "text/plain";
        String title = "Happy birthday!";
        String message = personName + " I wish you Happy Birthday!";
        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(Objects.requireNonNull(getActivity()))
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(message)
                .startChooser();
    }

    private void createSharingText(String personName, String dateString) {
        String mimeType = "text/plain";
        String title = "Friend birthday";
        String message = personName + " was born " + " on " + dateString;
        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(Objects.requireNonNull(getActivity()))
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(message)
                .startChooser();
    }

    public void updateTheList() {
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
        newFragment.show(Objects.requireNonNull(getFragmentManager()), "date picker");
    }

    private void populateUI(EventEntity eventEntity) {
        birthDate.setText(eventEntity.getDateString());
        birthDateDate = eventEntity.getDate();
        birthDateString = eventEntity.getDateString();
        birthdayPersonNameEditText.setText(eventEntity.getPersonName());
        personAgeTextView.setText(valueOf(eventEntity.getAge()));
        age = eventEntity.getAge();
    }

    @Override
    public void onDateSelected(Date date, String dateString) {
        birthDateDate = date;
        birthDateString = dateString;
        age = getAge(date);
        personAgeTextView.setText(valueOf(age));
        birthDate.setText(dateString);
    }

    public int getAge(Date date) {
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        int birthYear = calendar.get(Calendar.YEAR);
        return currentYear - birthYear;
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
                showDeleteDialog();
                return true;
            case R.id.action_save:
                saveBirthday();
                return true;
            case R.id.action_share:
                //share meeting
                createSharingText(birthdayPersonNameEditText.getText().toString(), birthDateString);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteBirthday() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (eventId == DEFAULT_ID) {
                    birthDate.setText("");
                    birthdayPersonNameEditText.setText("");
                    personAgeTextView.setText("");
                } else {
                    mViewModel.deleteEvent(eventId);
                    updateTheList();
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    private void saveBirthday() {
        String title = " ";
        Date date = birthDateDate;
        String dateString = birthDateString;
        String time = " ";
        String personName = birthdayPersonNameEditText.getText().toString();
        String location = " ";
        String note = " ";
        int done = 0;
        EventEntity birthday = new EventEntity(BIRTHDAY_TYPE, title, date, dateString, time, personName,
                location, note, done, age);
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (eventId == DEFAULT_ID) {
                    mViewModel.addEvent(birthday);
                } else {
                    birthday.setId(eventId);
                    mViewModel.updateEvent(birthday);
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

    @Override
    public boolean onBackPressed() {
        return isChanged;
    }
}