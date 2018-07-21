package com.example.user.keepit.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.user.keepit.R;
import com.example.user.keepit.fragment.BirthdayFragment;
import com.example.user.keepit.fragment.MeetingFragment;
import com.example.user.keepit.fragment.NoteFragment;

import java.util.Objects;

import static com.example.user.keepit.activities.AddTodayActivity.BIRTHDAY_ID;
import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.MEETING_ID;
import static com.example.user.keepit.activities.AddTodayActivity.NOTE_ID;

public class EditActivity extends AppCompatActivity {

    private static final int DEFAULT_MEETING_ID = -1;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MEETING_ID)) {
                id = intent.getIntExtra(MEETING_ID, DEFAULT_ID);
                if (id == DEFAULT_ID) {
                    setTitle(R.string.add_meeting);
                    MeetingFragment addMeetingFragment = new MeetingFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.add_meeting_container, addMeetingFragment)
                            .commit();
                } else {
                    setTitle(R.string.edit_meeting);

                    //sa deschid fragmentul specific evenimentui( cu toate datele specifice) care urmeaza sa fie modificat
                }
            } else if (intent.hasExtra(BIRTHDAY_ID)) {
                id = intent.getIntExtra(BIRTHDAY_ID, DEFAULT_ID);
                if (id == DEFAULT_ID) {
                    setTitle(R.string.add_birthday);
                    BirthdayFragment addBirthdayFragment = new BirthdayFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.add_birthday_container, addBirthdayFragment)
                            .commit();
                } else {
                    setTitle(R.string.edit_birthday);

                }
            } else if (intent.hasExtra(NOTE_ID)) {
                id = intent.getIntExtra(NOTE_ID, DEFAULT_ID);
                if (id == DEFAULT_ID) {
                    setTitle(R.string.add_note);
                    NoteFragment addNoteFragment = new NoteFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.add_note_container, addNoteFragment)
                            .commit();
                } else {
                    setTitle(R.string.edit_note);

                }
            }
        }
    }

}
