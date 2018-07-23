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

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.adapters.ListAdapter;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;
import com.example.user.keepit.viewModels.EventViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.IS_BIRTHDAY;
import static com.example.user.keepit.activities.EditActivity.EVENT_ENTITY_ID;

public class BirthdaysActivity extends AppCompatActivity {
    @BindView(R.id.birthdays_recycler_view)
    RecyclerView birthdaysRecyclerView;
   @BindView(R.id.empty_birthday_list_textView)
   TextView emptyBirthdayListTV;
    private EventViewModel mViewModel;
    private AppRoomDatabase roomDB;
    private AppExecutors executors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdays);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.birthday_name));
        //customize the recyclerView appearance
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator_for_recycler));
        birthdaysRecyclerView.addItemDecoration(itemDecoration);
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(this);
        birthdaysRecyclerView.setLayoutManager(layoutManagerReviews);
        //Get the meetingList to set the adapter for
        roomDB = AppRoomDatabase.getsInstance(this);
        executors = AppExecutors.getInstance();
        mViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
//        mViewModel.getEventListByType(BIRTHDAY_TYPE).observe(this, eventEntityList -> {
//            if(eventEntityList != null && eventEntityList.size() > 0){
//                birthdaysRecyclerView.setAdapter(new ListAdapter(this, eventEntityList));
//            }else {
//                emptyBirthdayListTV.setVisibility(View.VISIBLE);
//            }
//        });
    }


    //Inflating the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.birthdays_menu, menu);
        return true;
    }

    //Creating Intents for each menu item.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete_from_birthdays:
                //delete from list what was selected
                return true;
            case R.id.action_add_from_birthdays:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(IS_BIRTHDAY, true);
                intent.putExtra(EVENT_ENTITY_ID, DEFAULT_ID);
                startActivity(intent);
                return true;
            case R.id.action_home_b:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_todays_b:
                intent = new Intent(this, TodaysActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_meetings_b:
                intent = new Intent(this, MeetingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_notes_b:
                intent = new Intent(this, NotesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

