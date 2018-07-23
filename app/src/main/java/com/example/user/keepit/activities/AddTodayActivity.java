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

import static android.provider.CalendarContract.Instances.EVENT_ID;
import static com.example.user.keepit.activities.EditActivity.EVENT_ENTITY_ID;

public class AddTodayActivity extends AppCompatActivity {

    @BindView(R.id.meeting_button)
    Button addMeetingButton;
    public final static int DEFAULT_ID = -1;
    @BindView(R.id.birthday_button)
    Button addBirthdayButton;
    @BindView(R.id.note_button)
    Button addNoteButton;
    public static final String IS_MEETING = "Meeting";
    public static final String IS_BIRTHDAY = "Birthday";
    public static final String IS_NOTE = "Note";

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
                intent.putExtra(IS_MEETING, true);
                startActivity(intent);
            }
        });
        addBirthdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(IS_BIRTHDAY, true);
                startActivity(intent);
            }
        });
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTodayActivity.this, EditActivity.class);
                intent.putExtra(IS_NOTE, true);
                startActivity(intent);
            }
        });
    }
}
