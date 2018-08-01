package com.example.user.keepit.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.user.keepit.R;
import com.example.user.keepit.activities.AddTodayActivity;
import com.example.user.keepit.activities.EditActivity;

/**
 * Implementation of App Widget functionality.
 */
public class KeepItWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = setRemoteAdapter(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Sets the remote adapter used to fill in the grid items
     */
    private static RemoteViews setRemoteAdapter(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.keep_it_widget);
        views.setRemoteAdapter(R.id.today_list_widget, new Intent(context, WidgetService.class));
        Intent addIntent = new Intent(context, AddTodayActivity.class);
        PendingIntent addPendingIntent = PendingIntent.getActivity(context, 0, addIntent, 0);
        views.setOnClickPendingIntent(R.id.add_event_widget, addPendingIntent);

        Intent editIntent = new Intent(context, EditActivity.class);
        PendingIntent pendingEditIntent = PendingIntent.getActivity(context, 0,
                editIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.today_list_widget, pendingEditIntent);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
