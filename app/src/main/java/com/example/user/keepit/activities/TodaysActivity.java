package com.example.user.keepit.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.example.user.keepit.activities.AddTodayActivity;
import com.example.user.keepit.activities.BirthdaysActivity;
import com.example.user.keepit.activities.MainActivity;
import com.example.user.keepit.activities.MeetingsActivity;
import com.example.user.keepit.activities.NotesActivity;
import com.example.user.keepit.adapters.ListAdapter;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.utils.Utils;
import com.example.user.keepit.viewModels.EventViewModel;
import com.example.user.keepit.viewModels.EventViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;

public class TodaysActivity extends AppCompatActivity {

    @BindView(R.id.todays_recycler_view)
    RecyclerView todaysRecylerView;
    @BindView(R.id.todays_date_text_view)
    TextView todayDate;
    private AppRoomDatabase roomDB;
    private AppExecutors executors;
    private EventViewModelFactory factoryVM;
    private Repository repository;
    private EventViewModel eventVM;
    @BindView(R.id.empty_today_list_textView)
    TextView emptyTextViewToday;
    private String todayDateString;

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

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        todayDateString = formatter.format(date);
        todayDate.setText(todayDateString);

        roomDB = AppRoomDatabase.getsInstance(this);
        executors = AppExecutors.getInstance();
        repository = Repository.getsInstance(executors,roomDB,roomDB.eventDao());
        factoryVM = new EventViewModelFactory(repository);
        eventVM = ViewModelProviders.of(this, factoryVM).get(EventViewModel.class);
       updateTheList();

    }
    private void updateTheList() {
        eventVM.getEventsOfToday(todayDateString).observe(this, eventEntityList -> {
            if(eventEntityList != null && eventEntityList.size() > 0 ){

                RecyclerView.LayoutManager layoutManagerReviews = new
                        LinearLayoutManager(this);
                todaysRecylerView.setLayoutManager(layoutManagerReviews);
                todaysRecylerView.setAdapter(new ListAdapter(this, eventEntityList));
                emptyTextViewToday.setVisibility(View.INVISIBLE);

            }else if(eventEntityList == null){
                todaysRecylerView.setVisibility(View.INVISIBLE);
                emptyTextViewToday.setVisibility(View.VISIBLE);
            }

        });
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
            case R.id.action_delete_from_todays:
                showDeleteConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllForToday() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                eventVM.deleteTodaysEvents(todayDateString);
                    updateTheList();

                finish();
            }
        });

    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_today_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the event.
                deleteAllForToday();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the event.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

