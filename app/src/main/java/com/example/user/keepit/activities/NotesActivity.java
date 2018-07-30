package com.example.user.keepit.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
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
import com.example.user.keepit.adapters.ListAdapter;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventEntity;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;
import com.example.user.keepit.viewModels.EventViewModel;
import com.example.user.keepit.viewModels.EventViewModelFactory;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.activities.AddTodayActivity.DEFAULT_ID;
import static com.example.user.keepit.activities.AddTodayActivity.IS_NOTE;
import static com.example.user.keepit.activities.EditActivity.EVENT_ENTITY_ID;

public class NotesActivity extends AppCompatActivity {
    @BindView(R.id.notes_recycler_view)
    RecyclerView notesRecyclerView;
    @BindView(R.id.empty_note_list_textView)
    TextView emptyNotesListTV;
    @BindView(R.id.notes_adView)
    AdView notesAdView;

    private EventViewModel mViewModel;
    private AppRoomDatabase roomDB;
    private AppExecutors executors;
    private EventViewModelFactory factoryVM;
    private Repository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.notes_name));

        //load the adView
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        notesAdView.loadAd(adRequest);

        //customize the recyclerView appearance
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator_for_recycler));
        notesRecyclerView.addItemDecoration(itemDecoration);
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(this);
       notesRecyclerView.setLayoutManager(layoutManagerReviews);

       //Get the notesList to set the adapter for
        roomDB = AppRoomDatabase.getsInstance(this);
        executors = AppExecutors.getInstance();
        mRepository = Repository.getsInstance(executors,roomDB, roomDB.eventDao());
        factoryVM = new EventViewModelFactory(mRepository);
        mViewModel = ViewModelProviders.of(this,factoryVM).get(EventViewModel.class);
       updateTheList();
    }

    private void updateTheList() {
        mViewModel.getNotesList().observe(this, new Observer<List<EventEntity>>() {
            @Override
            public void onChanged(@Nullable List<EventEntity> eventEntityList) {
                if(eventEntityList != null && eventEntityList.size() > 0){
                    notesRecyclerView.setAdapter(new ListAdapter(NotesActivity.this, eventEntityList));
                    notesRecyclerView.setVisibility(View.VISIBLE);
                    emptyNotesListTV.setVisibility(View.INVISIBLE);
                }else if(eventEntityList == null){
                    notesRecyclerView.setVisibility(View.INVISIBLE);
                    emptyNotesListTV.setVisibility(View.VISIBLE);
                }
            }
        });
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
                //delete notes list
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_add_from_notes:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra(IS_NOTE, true);
                intent.putExtra(EVENT_ENTITY_ID, DEFAULT_ID);
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
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_notes_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the event.
                deleteAllNotes();

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

    private void deleteAllNotes() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                mViewModel.deleteAllNotes();
                updateTheList();

                finish();
            }
        });
    }
}
