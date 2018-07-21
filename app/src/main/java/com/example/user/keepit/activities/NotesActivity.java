package com.example.user.keepit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.adapters.ListAdapter;
import com.example.user.keepit.database.AppRoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.NOTE_ID;

public class NotesActivity extends AppCompatActivity {
    @BindView(R.id.notes_recycler_view)
    RecyclerView notesRecyclerView;
    private Repository mRepository;
    @BindView(R.id.empty_note_list_textView)
    TextView emptyNotesListTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.notes_name));
        //customize the recyclerView appearance
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator_for_recycler));
        notesRecyclerView.addItemDecoration(itemDecoration);
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(this);
       notesRecyclerView.setLayoutManager(layoutManagerReviews);
        //Get the meetingList to set the adapter for
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        AppExecutors executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors, roomDB, roomDB.eventDao());

    }


    //Inflating the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        return true;
    }

    //Creating Intents for each menu item.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete_from_notes:
                //delete from notes list
                return true;
            case R.id.action_add_from_notes:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(NOTE_ID, DEFAULT_ID);
                startActivity(intent);
                return true;
            case R.id.action_home_n:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_todays_n:
                intent = new Intent(this, TodaysActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_meetings_n:
                intent = new Intent(this, MeetingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_birthdays_n:
                intent = new Intent(this, BirthdaysActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
