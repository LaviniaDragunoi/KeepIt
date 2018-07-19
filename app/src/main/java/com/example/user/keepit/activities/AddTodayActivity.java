package com.example.user.keepit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.user.keepit.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTodayActivity extends AppCompatActivity {

    @BindView(R.id.meeting_button)
    Button addMeetingButton;
    public static final String MEETING_ID = "meetingId";
    public final static int DEFAULT_ID = -1;
    private int meetingId = DEFAULT_ID;

@BindView(R.id.birthday_button)
Button addBirthdayButton;
    public static final String BIRTHDAY_ID = "birthdayId";
    private int birthdayId = DEFAULT_ID;

    @BindView(R.id.note_button)
    Button addNoteButton;
    public static final String NOTE_ID = "noteId";
    private int noteId = DEFAULT_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_today);
        ButterKnife.bind(this);
        setTitle(R.string.add_today);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        addMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(MEETING_ID, DEFAULT_ID);
                startActivity(intent);
            }
        });
        addBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(BIRTHDAY_ID, DEFAULT_ID);
                startActivity(intent);
            }
        });
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(NOTE_ID, DEFAULT_ID);
                startActivity(intent);
            }
        });
    }
}
