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

import static com.example.user.keepit.utils.Constants.DEFAULT_ID;
import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.IS_BIRTHDAY;
import static com.example.user.keepit.utils.Constants.IS_MEETING;
import static com.example.user.keepit.utils.Constants.IS_NOTE;

/**
 * Class that has buttons to add events ( Meetings, Birthdays, Notes)
 */
public class AddTodayActivity extends AppCompatActivity {

    

    @BindView(R.id.meeting_button)
    Button addMeetingButton;
    @BindView(R.id.birthday_button)
    Button addBirthdayButton;
    @BindView(R.id.note_button)
    Button addNoteButton;

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
                intent.putExtra(EVENT_ENTITY_ID, DEFAULT_ID);
                intent.putExtra(IS_MEETING, true);
                startActivity(intent);
            }
        });

        addBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(IS_BIRTHDAY, true);
                intent.putExtra(EVENT_ENTITY_ID, DEFAULT_ID);
                startActivity(intent);
            }
        });

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(IS_NOTE, true);
                intent.putExtra(EVENT_ENTITY_ID, DEFAULT_ID);
                startActivity(intent);
            }
        });
    }
}
