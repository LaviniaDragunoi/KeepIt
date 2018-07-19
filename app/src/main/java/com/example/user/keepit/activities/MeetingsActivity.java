package com.example.user.keepit.activities;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Toast;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.adapters.ListsAdapter;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.MeetingsEntity;
import com.example.user.keepit.viewModels.EditMeetingViewModel;
import com.example.user.keepit.viewModels.EditViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.MEETING_ID;


public class MeetingsActivity extends AppCompatActivity {
@BindView(R.id.meetings_recycler_view)
    RecyclerView meetingsRecyclerView;
@BindView(R.id.empty_list_textView)
    TextView emptyListTV;
    private int meetingId = DEFAULT_ID;
    private List<MeetingsEntity> meetingsList;
    private EditMeetingViewModel mViewModel;
    private Repository mRepository;
    private EditViewModelFactory viewModelFactory;

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
        mRepository.getMeetingsLiveDataList().observe(this, meetingsList ->{
            if(meetingsList != null && meetingsList.size() > 0){
                List<Object> objectList =  convertMeetingListToObjectList(meetingsList);
                meetingsRecyclerView.setAdapter(new ListsAdapter(this, objectList));
            }else {
                Toast.makeText(getApplicationContext(), "Nu ai lista pt display", Toast.LENGTH_LONG).show();
                emptyListTV.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * Method that will convert the meetings list into an object list that will be used to bind
     * data into ListsAdapter that is a multi objects(meetings, birthday, note) used adapter
     *
     * @param meetingsEntityList the list to be converted
     * @return the object list, the result of conversion
     */
    public List<Object> convertMeetingListToObjectList(List<MeetingsEntity> meetingsEntityList) {
        return new ArrayList<>(meetingsEntityList);
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
}
