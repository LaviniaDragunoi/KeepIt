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
import com.example.user.keepit.fragment.MeetingFragment;
import com.example.user.keepit.fragment.NoteFragment;

import java.util.List;
import java.util.Objects;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.IS_BIRTHDAY;
import static com.example.user.keepit.activities.AddTodayActivity.IS_MEETING;
import static com.example.user.keepit.activities.AddTodayActivity.IS_NOTE;
import static com.example.user.keepit.adapters.ListAdapter.EVENT_TYPE;
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
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
                                .disallowAddToBackStack()
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


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment instanceof BirthdayFragment) {
                BirthdayFragment.backButtonWasPressed();
            }
        }
    }
}
