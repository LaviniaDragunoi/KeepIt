package com.example.user.keepit.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.entities.EventEntity;
import com.example.user.keepit.networking.ApiClient;
import com.example.user.keepit.networking.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.EXTRA_EVENT;

/**
 * Widget service and RemoteViewFactory that will create views for the widget
 */
public class WidgetService extends RemoteViewsService {
    private List<EventEntity> todayEvents = new ArrayList<>();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new KeepItRemoteViewFactory(this.getApplicationContext(), intent);
    }

    public void createEventsList() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dateString = formatter.format(date);
        AppExecutors executors = AppExecutors.getInstance();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        AppRoomDatabase roomDB = AppRoomDatabase.getsInstance(this);
        Repository repository = Repository.getsInstance(executors,
                roomDB, roomDB.eventDao(), apiInterface);
        todayEvents = repository.getTodaysEventsList(dateString);
    }

    private class KeepItRemoteViewFactory implements RemoteViewsFactory {
        Context mContext;

        public KeepItRemoteViewFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

            createEventsList();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if (todayEvents == null) {
                return 0;
            }
            return todayEvents.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.event_item);
            if (todayEvents != null) {
                views.setViewVisibility(R.id.today_list_widget, View.INVISIBLE);
                int isChecked = todayEvents.get(position).getDone();
                if (isChecked == 0) {
                    views.setImageViewResource(R.id.checked_TV, R.drawable.ic_check_box);
                } else if (isChecked == 1) {
                    views.setImageViewResource(R.id.checked_TV, R.drawable.ic_done_accent);
                }
                views.setTextViewText(R.id.event_type_TV, todayEvents.get(position).getEventType());
                views.setTextViewText(R.id.title_TV, todayEvents.get(position).getTitle());
                views.setTextViewText(R.id.date_TV, todayEvents.get(position).getDateString());
                views.setTextViewText(R.id.person_name_TV, todayEvents.get(position).getPersonName());
                Bundle bundle = new Bundle();
                bundle.putInt(EVENT_ENTITY_ID, todayEvents.get(position).getId());
                bundle.putParcelable(EXTRA_EVENT, todayEvents.get(position));
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(bundle);
                views.setOnClickFillInIntent(R.id.edit_event, fillInIntent);
            } else {
                views.setViewVisibility(R.id.today_list_widget, View.INVISIBLE);
                views.setViewVisibility(R.id.empty_today_widget, View.VISIBLE);
            }

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
