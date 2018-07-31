package com.example.user.keepit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.keepit.R;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.fragment.BirthdayFragment;
import com.example.user.keepit.fragment.IOnBackPressed;
import com.example.user.keepit.fragment.MeetingFragment;
import com.example.user.keepit.fragment.NoteFragment;

import java.util.List;
import java.util.Objects;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.IS_BIRTHDAY;
import static com.example.user.keepit.activities.AddTodayActivity.IS_MEETING;
import static com.example.user.keepit.activities.AddTodayActivity.IS_NOTE;
import static com.example.user.keepit.adapters.ListAdapter.EXTRA_EVENT;
import static com.example.user.keepit.fragment.BirthdayFragment.BIRTHDAY_TYPE;
import static com.example.user.keepit.fragment.MeetingFragment.MEETING_TYPE;
import static com.example.user.keepit.fragment.NoteFragment.NOTE_TYPE;

public class EditActivity extends AppCompatActivity {

    public static final String EVENT_ENTITY_ID = "id";
    private int id = DEFAULT_ID;
    private EventEntity currentEvent;
    public interface OnBackPressed {
        void onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        if(savedInstanceState == null) {
            if (intent != null) {
                if (intent.hasExtra(EVENT_ENTITY_ID)) {
                    id = intent.getIntExtra(EVENT_ENTITY_ID, DEFAULT_ID);
                    if (id == DEFAULT_ID) {
                        if (intent.hasExtra(IS_MEETING)) {
                            setTitle(R.string.add_meeting);
                            MeetingFragment addMeetingFragment = new MeetingFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EVENT_ENTITY_ID, id);
                            addMeetingFragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.add_meeting_container, addMeetingFragment)
                                    .commit();
                        } else if (intent.hasExtra(IS_BIRTHDAY)) {
                            setTitle(R.string.add_birthday);
                            BirthdayFragment addBirthdayFragment = new BirthdayFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EVENT_ENTITY_ID, id);
                            addBirthdayFragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.add_birthday_container, addBirthdayFragment)
                                    .commit();
                        } else if (intent.hasExtra(IS_NOTE)) {
                            setTitle(R.string.add_note);
                            NoteFragment addNoteFragment = new NoteFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EVENT_ENTITY_ID, id);
                            addNoteFragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.add_note_container, addNoteFragment)
                                    .commit();
                        }
                    } else if (intent.hasExtra(EXTRA_EVENT)) {
                        currentEvent = intent.getParcelableExtra(EXTRA_EVENT);
                        String eventType = currentEvent.getEventType();
                        switch (eventType) {
                            case MEETING_TYPE: {
                               setTitle(R.string.edit_meeting);
                                MeetingFragment addMeetingFragment = new MeetingFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt(EVENT_ENTITY_ID, id);
                                bundle.putParcelable(EXTRA_EVENT, currentEvent);
                                addMeetingFragment.setArguments(bundle);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.add_meeting_container, addMeetingFragment)
                                        .commit();
                                break;
                            }
                            case BIRTHDAY_TYPE: {
                                setTitle(R.string.edit_birthday);
                                BirthdayFragment addBirthdayFragment = new BirthdayFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt(EVENT_ENTITY_ID, id);
                                bundle.putParcelable(EXTRA_EVENT, currentEvent);
                                addBirthdayFragment.setArguments(bundle);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.add_birthday_container, addBirthdayFragment)
                                        .commit();
                                break;
                            }
                            case NOTE_TYPE: {
                                setTitle(R.string.edit_note);
                                NoteFragment addNoteFragment = new NoteFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt(EVENT_ENTITY_ID, id);
                                bundle.putParcelable(EXTRA_EVENT, currentEvent);
                                addNoteFragment.setArguments(bundle);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.add_note_container, addNoteFragment)
                                        .commit();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    //Set title on ACtionBar that is received from fragment
    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }
    @Override
    public void onBackPressed() {

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
                super.onBackPressed();
            }
            //setup the dialog to warn the user about losing changes already made
            DialogInterface.OnClickListener discard = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                    finish();
                }
            };
            showUnsavedDialog(discard);
        }
    }

    public void showUnsavedDialog(DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_message);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( dialog!=null ){
                    dialog.dismiss();

                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
