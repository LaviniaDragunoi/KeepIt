package com.example.user.keepit.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.example.user.keepit.database.entities.EventEntity;
import com.example.user.keepit.networking.ApiClient;
import com.example.user.keepit.networking.ApiInterface;
import com.example.user.keepit.viewModels.EventViewModel;
import com.example.user.keepit.viewModels.EventViewModelFactory;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.utils.Constants.DEFAULT_ID;
import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.IS_BIRTHDAY;

/**
 * Class that will display the list of birthdays entered by the user
 * Divider solution found here:
 * https://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
 */
public class BirthdaysActivity extends AppCompatActivity {

    @BindView(R.id.birthdays_recycler_view)
    RecyclerView birthdaysRecyclerView;
    @BindView(R.id.empty_birthday_list_textView)
    TextView emptyBirthdayListTV;
//    @BindView(R.id.birthday_adView)
//    AdView adView;

    private EventViewModel mViewModel;
    private AppExecutors executors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthdays);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.birthday_name));

//        //load the adView
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//        adView.loadAd(adRequest);

        //customize the recyclerView appearance
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator_for_recycler));
        birthdaysRecyclerView.addItemDecoration(itemDecoration);
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(this);
        birthdaysRecyclerView.setLayoutManager(layoutManagerReviews);

        //Get the birthdayList to set the adapter for
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        ApiInterface  apiInterface = ApiClient.getClient().create(ApiInterface.class);
        executors = AppExecutors.getInstance();
        Repository mRepository = Repository.getsInstance(executors, roomDB, roomDB.eventDao(), apiInterface);
        EventViewModelFactory factoryVM = new EventViewModelFactory(mRepository);
        mViewModel = ViewModelProviders.of(this, factoryVM).get(EventViewModel.class);

        updateTheList();
    }

    private void updateTheList() {
        mViewModel.getBirthdaysList().observe(this, new Observer<List<EventEntity>>() {
            @Override
            public void onChanged(@Nullable List<EventEntity> eventEntityList) {
                if (eventEntityList != null && eventEntityList.size() > 0) {
                    birthdaysRecyclerView.setAdapter(new ListAdapter(BirthdaysActivity.this, eventEntityList));
                    birthdaysRecyclerView.setVisibility(View.VISIBLE);
                    emptyBirthdayListTV.setVisibility(View.INVISIBLE);
                } else if (eventEntityList == null) {
                    birthdaysRecyclerView.setVisibility(View.INVISIBLE);
                    emptyBirthdayListTV.setVisibility(View.VISIBLE);
                }
            }
        });
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
                //delete the list
                showDeleteConfirmationDialog();
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

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_birthdays_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the event.
                deleteAllBirthdays();
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

    private void deleteAllBirthdays() {
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mViewModel.deleteAllBirthdays();
                updateTheList();
                finish();
            }
        });
    }
}

