package com.example.user.keepit;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.EventDao;
import com.example.user.keepit.database.LegEntity;
import com.example.user.keepit.database.RouteEntity;
import com.example.user.keepit.database.StepEntity;
import com.example.user.keepit.database.entities.EventEntity;
import com.example.user.keepit.models.DirectionResponse;
import com.example.user.keepit.networking.ApiClient;
import com.example.user.keepit.networking.ApiInterface;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.user.keepit.utils.Constants.BIRTHDAY_TYPE;
import static com.example.user.keepit.utils.Constants.MEETING_TYPE;
import static com.example.user.keepit.utils.Constants.NOTE_TYPE;

/**
 * Repository class that will mediate the ViewModel and the RoomDb
 */
public class Repository {
    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final AppRoomDatabase mRoomDB;
    private final EventDao mEventDao;
    private final AppExecutors mAppExecutors;
    private ApiInterface mApiInterface;
    private int eventId;
    private List<LatLng> meetingPoints = new ArrayList<>();
    private LatLng meetingCoordinates;

    public Repository(AppExecutors appExecutors, AppRoomDatabase roomDB, EventDao eventDao, ApiInterface apiInterface) {
        mAppExecutors = appExecutors;
        mRoomDB = roomDB;
        mEventDao = eventDao;
        mApiInterface = apiInterface;
    }

    public synchronized static Repository getsInstance(AppExecutors appExecutors,
                                                       AppRoomDatabase roomDB, EventDao eventDao, ApiInterface apiInterface) {
        if (sInstance == null) {
            synchronized (LOCK) {
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                sInstance = new Repository(appExecutors, roomDB, eventDao, apiInterface);
            }
        }
        return sInstance;
    }

    public LiveData<EventEntity> loadEvent(int id) {
        eventId = id;
        return mEventDao.loadEventById(id);
    }


    public LatLng getMeetingCoordinates() {
        return meetingCoordinates;
    }

    public void getRetrofitRoutes(String origin, String meetingLocation, String key) {
        final DirectionResponse[] directionResponses = new DirectionResponse[1];
        final List<LatLng>[] points = new List[1];
        meetingPoints.clear();
        mApiInterface.getDirections(origin, meetingLocation, key).enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                if (response.isSuccessful()) {
                    directionResponses[0] = response.body();
                    List<RouteEntity> routeEntities = new ArrayList<>();
                    for (RouteEntity route : directionResponses[0].getRoutes()) {
                        routeEntities.add(route);
                        points[0] = new ArrayList<>();
                        points[0].add(new LatLng(route.getLegs().get(0).getStartLocation().getLatitude(),
                                route.getLegs().get(0).getStartLocation().getLongitude() ));
                        for (LegEntity legEntity : route.getLegs()) {
                            meetingCoordinates =
                                    new LatLng(legEntity.getEndLocation().getLatitude(), legEntity.getEndLocation().getLongitude());
                            for (StepEntity stepEntity : legEntity.getSteps()) {
                                LatLng endCoordinates = new LatLng(stepEntity.getEndLocation().getLatitude(),
                                        stepEntity.getEndLocation().getLongitude());
                                points[0].add(endCoordinates);
                            }
                            Log.d("LaviniaCoordinates", "meetingCoordinates are : " + meetingCoordinates + " points[0] size: " + points[0].size());
                        }
                        meetingPoints.addAll(points[0]);
                        Log.d("LaviniaPointsList", "Points list size is: " + meetingPoints.size());
                    }

                    mAppExecutors.diskIO().execute(() -> {
                        EventEntity updateEvent = mEventDao.loadEventEntityById(eventId);
                        updateEvent.setRoutes(directionResponses[0].getRoutes());

                    });
                    //     Log.d("LaviniaResponse", "EndLocation coordinates are:" + response.body().getRoutes().get(0).getLegs().get(0).getEndLocation());
                } else {
                    Log.d("LaviniaResponse", "Routes response is not successful");
                }
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                Log.d("LaviniaResponseError", t.getMessage());
            }
        });
    }

    public List<LatLng> getMeetingPoints() {
        return meetingPoints;
    }

    public void addRouteToMeeting(int eventId, String originLocation, String meetingLocation) {
        getRetrofitRoutes(originLocation,
                meetingLocation, "AIzaSyCzTRbicswDKeBpFwLnyBUOKOztTrsmpr0");
    }

    public void insertNewEvent(EventEntity event) {
        mEventDao.insertEvent(event);
    }

    public void updateExistingEvent(EventEntity event) {
        mAppExecutors.diskIO().execute(() -> mEventDao.updateEvent(event));
    }

    public LiveData<List<EventEntity>> getInitialEventsList() {

        return mEventDao.loadAllEvents();
    }

    public LiveData<List<EventEntity>> getMeetings() {

        return mEventDao.getEventsByEventType(MEETING_TYPE);
    }

    public LiveData<List<EventEntity>> getTodaysEvents(String dateString) {
        return mEventDao.getEventsByDate(dateString);
    }

    public List<EventEntity> getTodaysEventsList(String dateString) {
        return mEventDao.getEventsByDateList(dateString);
    }

    public LiveData<List<EventEntity>> getBirthdays() {
        return mEventDao.getEventsByEventType(BIRTHDAY_TYPE);
    }

    public List<EventEntity> getBirthdaysList() {
        return mEventDao.getEventsListByEventType(BIRTHDAY_TYPE);
    }

    public LiveData<List<EventEntity>> getNotes() {
        return mEventDao.getEventsByEventType(NOTE_TYPE);
    }

    public List<EventEntity> getNotesList() {
        return mEventDao.getEventsListByEventType(NOTE_TYPE);
    }

    public List<EventEntity> getMeetingsList() {

        return mEventDao.getEventsListByEventType(MEETING_TYPE);
    }

    public void deleteEventById(int eventId) {
        EventEntity event = mEventDao.loadEventEntityById(eventId);
        mEventDao.deleteEvent(event);
    }

    public void deleteEvents(List<EventEntity> events) {
        mEventDao.deleteEventsList(events);
    }

}