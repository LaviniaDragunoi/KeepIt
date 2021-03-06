package com.example.user.keepit.activities;

import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.keepit.BuildConfig;
import com.example.user.keepit.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.reflect.Method;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.keepit.utils.Constants.EVENT_ANALYTICS_CONTENT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.today_image)
    ImageView todaysLogo;
    @BindView(R.id.meetings_image)
    ImageView meetingsLogo;
    @BindView(R.id.birthday_image)
    ImageView birthdaysLogo;
    @BindView(R.id.notes_image)
    ImageView notesLogo;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Intent intent;

    //Method used for debugging Room Db purposes
    public static void setInMemoryRoomDatabases(SupportSQLiteDatabase... database) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Class[] argTypes = new Class[]{HashMap.class};
                HashMap<String, SupportSQLiteDatabase> inMemoryDatabases = new HashMap<>();
                // set your inMemory databases
                inMemoryDatabases.put("InMemoryOne.db", database[0]);
                Method setRoomInMemoryDatabase = debugDB.getMethod("setInMemoryRoomDatabases", argTypes);
                setRoomInMemoryDatabase.invoke(null, inMemoryDatabases);
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //logEvent for Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, EVENT_ANALYTICS_CONTENT);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, AddTodayActivity.class);
                startActivity(intent);
            }
        });

        todaysLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, TodaysActivity.class);
                startActivity(intent);
            }
        });

        meetingsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, MeetingsActivity.class);
                startActivity(intent);
            }
        });

        birthdaysLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, BirthdaysActivity.class);
                startActivity(intent);
            }
        });

        notesLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        //Method called for debugging Room DB
        setInMemoryRoomDatabases();
    }

    //Inflating the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    //Creating option menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.about_app) {
            showAboutDialog();
        }
        return true;
    }

    /**
     * Method that will pop up an dialog box with details about KeepIt app and the option to go on paid version
     */
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.about_text);
        builder.setPositiveButton(R.string.paid_version, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "You are on paid version", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue with free version of the app.
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
