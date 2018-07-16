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

import static com.example.user.keepit.activities.AddTodayActivity.BIRTHDAY_BOOLEAN;
import static com.example.user.keepit.activities.AddTodayActivity.MEETING_BOOLEAN;
import static com.example.user.keepit.activities.AddTodayActivity.NOTE_BOOLEAN;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra(MEETING_BOOLEAN)){
                setTitle(R.string.add_meeting);
                MeetingFragment addMeetingFragment = new MeetingFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.add_meeting_container,addMeetingFragment)
                        .commit();
            }else if(intent.hasExtra(BIRTHDAY_BOOLEAN)){
                setTitle(R.string.add_birthday);
                BirthdayFragment addBirthdayFragment = new BirthdayFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.add_birthday_container,addBirthdayFragment)
                        .commit();
            }else if(intent.hasExtra(NOTE_BOOLEAN)){
                setTitle(R.string.add_note);
                NoteFragment addNoteFragment = new NoteFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.add_note_container,addNoteFragment)
                        .commit();
            }
        }
    }


}
