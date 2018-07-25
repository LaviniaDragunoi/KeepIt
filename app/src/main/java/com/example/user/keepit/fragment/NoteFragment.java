package com.example.user.keepit.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.EditActivity.EVENT_ENTITY_ID;
import static com.example.user.keepit.adapters.ListAdapter.EXTRA_EVENT;

public class NoteFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected{

    @BindView(R.id.picker_note_deadline)
    TextView noteDeadlineTextView;
    @BindView(R.id.note_name_title)
    EditText noteTitleEditText;
    @BindView(R.id.note_text)
    EditText noteTextEditText;
    private Date noteDeadlineDate;
    private String noteDeadlineString;

    public static final String NOTE_TYPE = "Note";

    private AppRoomDatabase roomDb;
    private EditEventViewModel mViewModel;
    private AppExecutors executors;
    private int eventId;
    private Repository mRepository;
    private EditEventModelFactory factory;
    private EventEntity currentEvent;

    private String eventType;
    private String title;
    private Date date;
    private String dateString;
    private String time;
    private String personName;
    private String location;
    private String note;
    private int done;
    //Empty constructor;
    public NoteFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.note_edit_fragment, container, false);
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

            }else {
                eventId = bundle.getInt(EVENT_ENTITY_ID);

            }

        }

        factory = new EditEventModelFactory(mRepository, eventId);

        updateTheList();
        showPickerSelected();

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

    private void showPickerSelected() {
        noteDeadlineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);

            }
        });
    }

    private void populateUI(EventEntity currentEvent) {
        noteTitleEditText.setText(currentEvent.getTitle());
        noteDeadlineTextView.setText(currentEvent.getDateString());
        noteDeadlineDate = currentEvent.getDate();
        noteDeadlineString = currentEvent.getDateString();
        noteTextEditText.setText(currentEvent.getNote());
    }

    public void showDatePicker(View v) {
        MyDatePickerFragment newFragment = new MyDatePickerFragment();
        newFragment.setListener(this);
        newFragment.show(getFragmentManager(), "date picker");
    }

    @Override
    public void onDateSelected(Date date, String dateString) {
        noteDeadlineDate = date;
        noteDeadlineString = dateString;
        noteDeadlineTextView.setText(dateString);
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
                //delete items from notes list
                showDeleteDialog();
                return true;
            case R.id.action_save:
                saveNote();
                return true;
            case R.id.action_share:
                //share meeting
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteNote() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(eventId == DEFAULT_ID) {
                    noteTitleEditText.setText("");
                    noteDeadlineTextView.setText("");
                    noteTextEditText.setText("");

                }else {
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
                deleteNote();

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
    private void saveNote() {
        eventType = NOTE_TYPE;
        title = noteTitleEditText.getText().toString();
        date = noteDeadlineDate;
        dateString = noteDeadlineString;
        time = "";
        personName = "";
        location = "";
        note = noteTextEditText.getText().toString();
        done = 0;
        EventEntity noteEvent = new EventEntity(eventType, title, date, dateString, time,
                personName, location, note, done);

        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(eventId == DEFAULT_ID) {
                    mViewModel.addEvent(noteEvent);

                }else {
                    noteEvent.setId(eventId);
                    mViewModel.updateEvent(noteEvent);
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });

    }
}
