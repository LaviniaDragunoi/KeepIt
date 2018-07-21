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
import android.widget.TextView;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.viewModels.EditEventViewModel;
import com.example.user.keepit.viewModels.EditEventModelFactory;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.MEETING_ID;


public class MeetingsActivity extends AppCompatActivity {
@BindView(R.id.meetings_recycler_view)
    RecyclerView meetingsRecyclerView;
@BindView(R.id.empty_meeting_list_textView)
    TextView emptyMeetingListTV;
    private int meetingId = DEFAULT_ID;
    private EditEventViewModel mViewModel;
    private Repository mRepository;
    private EditEventModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.meetings_name));
        //customize the recyclerView appearance
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator_for_recycler));
        meetingsRecyclerView.addItemDecoration(itemDecoration);
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(this);
        meetingsRecyclerView.setLayoutManager(layoutManagerReviews);
        //Get the meetingList to set the adapter for
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        AppExecutors executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors, roomDB, roomDB.eventDao());

    }


    //Inflating the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meetings_menu, menu);
        return true;
    }

    //Creating Intents for each menu item.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete_from_meetings:
                //delete items from meetings list
                deleteAll();
                return true;
            case R.id.action_add_from_meetings:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(MEETING_ID, DEFAULT_ID);
                startActivity(intent);
                return true;
            case R.id.action_home_m:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_todays_m:
                intent = new Intent(this, TodaysActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_birthdays_m:
                intent = new Intent(this, BirthdaysActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_notes_m:
                intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAll() {
        mRepository.delete();
    }
}
