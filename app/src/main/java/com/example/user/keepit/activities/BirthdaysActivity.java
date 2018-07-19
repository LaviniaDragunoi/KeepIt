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
import android.widget.Toast;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.adapters.ListsAdapter;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.BirthdayEntity;
import com.example.user.keepit.database.MeetingsEntity;
import com.example.user.keepit.fragment.BirthdayFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.example.user.keepit.activities.AddTodayActivity.BIRTHDAY_ID;
import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;

public class BirthdaysActivity extends AppCompatActivity {
@BindView(R.id.birthdays_recycler_view)
    RecyclerView birthdaysRecyclerView;
private Repository mRepository;
   @BindView(R.id.empty_birthday_list_textView)
   TextView emptyBirthdayListTV;

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
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        AppExecutors executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors, roomDB, roomDB.eventDao());
        mRepository.getBirthdaysLiveDataList().observe(this, birthdayList ->{
            if(birthdayList != null && birthdayList.size() > 0){
                List<Object> objectList =  convertBirthdayListToObjectList(birthdayList);
               birthdaysRecyclerView.setAdapter(new ListsAdapter(this, objectList));
            }else {
                Toast.makeText(getApplicationContext(), "Nu ai lista pt display", Toast.LENGTH_LONG).show();
                emptyBirthdayListTV.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Method that will convert the meetings list into an object list that will be used to bind
     * data into ListsAdapter that is a multi objects(meetings, birthday, note) used adapter
     *
     * @param birthdayEntityList the list to be converted
     * @return the object list, the result of conversion
     */
    public List<Object> convertBirthdayListToObjectList(List<BirthdayEntity> birthdayEntityList) {
        return new ArrayList<>(birthdayEntityList);
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
                intent.putExtra(BIRTHDAY_ID, DEFAULT_ID);
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

