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
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.utils.Constants.DEFAULT_ID;
import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.EXTRA_EVENT;
import static com.example.user.keepit.utils.Constants.NOTE_TYPE;

public class NoteFragment extends Fragment implements MyDatePickerFragment.OnDatePickerSelected, IOnBackPressed {

    public boolean isChanged = false;
    @BindView(R.id.picker_note_deadline)
    TextView noteDeadlineTextView;
    @BindView(R.id.note_name_title)
    EditText noteTitleEditText;
    @BindView(R.id.note_text)
    EditText noteTextEditText;
    private Date noteDeadlineDate;
    private String noteDeadlineString;
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
    public NoteFragment() {
    }

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_edit_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        noteTitleEditText.setOnTouchListener(mTouchListener);
        noteDeadlineTextView.setOnTouchListener(mTouchListener);
        noteTextEditText.setOnTouchListener(mTouchListener);
        AppRoomDatabase roomDb = AppRoomDatabase.getsInstance(getContext());
        executors = AppExecutors.getInstance();
        Repository mRepository = Repository.getsInstance(executors, roomDb, roomDb.eventDao());

        if (savedInstanceState != null) {
            if (eventId != DEFAULT_ID) {
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.edit_note));
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
                ((EditActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getString(R.string.add_note));
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
        newFragment.show(Objects.requireNonNull(getFragmentManager()), "date picker");
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

    private void createSharingText(String deadlineString, String noteText) {
        String mimeType = "text/plain";
        String title = "Note";
        String message = "This note has it's deadline on " + deadlineString + " \n" + noteText;
        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(Objects.requireNonNull(getActivity()))
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(message)
                .startChooser();
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
                createSharingText(noteDeadlineString, noteTextEditText.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteNote() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (eventId == DEFAULT_ID) {
                    noteTitleEditText.setText("");
                    noteDeadlineTextView.setText("");
                    noteTextEditText.setText("");
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
        String title = noteTitleEditText.getText().toString();
        Date date = noteDeadlineDate;
        String dateString = noteDeadlineString;
        String time = "";
        String personName = "";
        String location = "";
        String note = noteTextEditText.getText().toString();
        int done = 0;
        int age = 0;
        EventEntity noteEvent = new EventEntity(NOTE_TYPE, title, date, dateString, time,
                personName, location, note, done, age);

        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (eventId == DEFAULT_ID) {
                    mViewModel.addEvent(noteEvent);
                } else {
                    noteEvent.setId(eventId);
                    mViewModel.updateEvent(noteEvent);
                }
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return isChanged;
    }
}
