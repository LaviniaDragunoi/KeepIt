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
    public static final String BIRTHDAY_BOOLEAN = "birthdayBoolean";
    @BindView(R.id.meeting_button)
    Button addMeetingButton;
@BindView(R.id.birthday_button)
Button addBirthdayButton;

    @BindView(R.id.note_button)
    Button addNoteButton;
    public static final String NOTE_BOOLEAN = "noteBoolean";
    boolean noteBoolean = false;

public static final String MEETING_BOOLEAN = "meetingBoolean";
boolean meetingBoolean = false;
    private boolean birthdayBoolean = false;

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
                meetingBoolean = true;
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(MEETING_BOOLEAN, meetingBoolean);
                startActivity(intent);
            }
        });
        addBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayBoolean = true;
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(BIRTHDAY_BOOLEAN, birthdayBoolean);
                startActivity(intent);
            }
        });
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteBoolean = true;
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(NOTE_BOOLEAN, noteBoolean);
                startActivity(intent);
            }
        });
    }
}
