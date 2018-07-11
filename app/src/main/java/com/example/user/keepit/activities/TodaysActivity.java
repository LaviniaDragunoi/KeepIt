package com.example.user.keepit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.user.keepit.R;
import com.example.user.keepit.activities.AddTodayActivity;
import com.example.user.keepit.activities.BirthdaysActivity;
import com.example.user.keepit.activities.MainActivity;
import com.example.user.keepit.activities.MeetingsActivity;
import com.example.user.keepit.activities.NotesActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodaysActivity extends AppCompatActivity {

    @BindView(R.id.todays_recycler_view)
    RecyclerView todaysRecylerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.todays_name));
        //customize the recyclerView appearance
         DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
         itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator_for_recycler));
         todaysRecylerView.addItemDecoration(itemDecoration);




    }

    //Inflating the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todays_menu, menu);
        return true;
    }
    //Creating Intents for each menu item.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home_t:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_meetings_t:
                intent = new Intent(this, MeetingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_birthdays_t:
                intent = new Intent(this, BirthdaysActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_notes_t:
                intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add_from_todays:
                intent = new Intent(this, AddTodayActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
