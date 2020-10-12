package com.example.user.keepit.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.user.keepit.AppExecutors;
import com.example.user.keepit.R;
import com.example.user.keepit.Repository;
import com.example.user.keepit.database.AppRoomDatabase;
import com.example.user.keepit.database.entities.EventEntity;
import com.example.user.keepit.networking.ApiClient;
import com.example.user.keepit.networking.ApiInterface;
import com.example.user.keepit.viewModels.EditEventModelFactory;
import com.example.user.keepit.viewModels.EditEventViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.user.keepit.utils.Constants.EVENT_ENTITY_ID;
import static com.example.user.keepit.utils.Constants.EXTRA_EVENT;

public class MapTestMeetingFragment extends Fragment implements OnMapReadyCallback {

    private static GoogleMap mMap;
    private ArrayList markerPoints = new ArrayList();
    private CameraPosition cameraPosition;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location lastKnownLocation;
    private EditEventViewModel viewModel;
    private EditEventModelFactory factory;
    private int eventId;
    private String meetingLocation;
    private Repository mRepository;
    private EventEntity currentEvent;
    PolylineOptions lineOptions = null;
    MarkerOptions markerOptions = new MarkerOptions();

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private String TAG = MapTestMeetingFragment.class.getName();
    private AppExecutors executors;
    private boolean updateEvent;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);

        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_EVENT)) {
                currentEvent = bundle.getParcelable(EXTRA_EVENT);
                eventId = Objects.requireNonNull(currentEvent).getId();
                updateEvent = true;
            } else {
                eventId = bundle.getInt(EVENT_ENTITY_ID);
            }
        }
        AppRoomDatabase roomDb = AppRoomDatabase.getsInstance(getContext());
        executors = AppExecutors.getInstance();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mRepository = Repository.getsInstance(executors, roomDb, roomDb.eventDao(), apiInterface);
        factory = new EditEventModelFactory(mRepository, eventId);
        viewModel = new ViewModelProvider(this, factory).get(EditEventViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("Range")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        lineOptions = new PolylineOptions();
        mMap = googleMap;
        getLocationPermission();
        viewModel.getPermissionsUpdated().observe(getViewLifecycleOwner(), it -> {
            getDeviceLocation();
            updateLocationUI();
        });

        markerPoints.add(mRepository.getMeetingPoints()); 


        lineOptions.addAll(mRepository.getMeetingPoints());
        lineOptions.width(7);
        lineOptions.color(Color.GRAY);
        lineOptions.geodesic(true);
        mMap.addPolyline(lineOptions);


    }

    private void getDeviceLocation() {

        try {
            viewModel.getPermissionsUpdated().observe(getViewLifecycleOwner(), it ->
            {
                if (it) {
                    @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                    locationTask.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                lastKnownLocation = task.getResult();
                                if (lastKnownLocation != null) {
                                    LatLng coordinates = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                    addMarkers(coordinates, "You are here");
                                    if (mRepository.getMeetingCoordinates() != null) {
                                        addMarkers(mRepository.getMeetingCoordinates(), "Meet here");
                                    }
                                    String stringCoordinates = coordinates.latitude + "," + coordinates.longitude;
                                    if (updateEvent) {
                                        viewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
                                            currentEvent = new EventEntity(eventId, event.getEventType(), event.getTitle(),
                                                    event.getDate(), event.getDateString(), event.getTime(), event.getPersonName(),
                                                    event.getMeetingLocation(), stringCoordinates, event.getNote(), event.getDone(),
                                                    event.getAge());
                                            Log.d("LaviniaUpdateEvent", currentEvent.getLocation());
                                        });

                                    }
                                    if (currentEvent != null) {
                                        mRepository.addRouteToMeeting(eventId, stringCoordinates, currentEvent.getMeetingLocation());
                                    } else {
                                        mRepository.addRouteToMeeting(eventId, stringCoordinates, null);
                                    }

                                }
                            } else {
                                Log.d(TAG, "Current location is null. You will see default values");
                                Log.e(TAG, "Exceptions: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                } else {
                    getLocationPermission();
                }
            });
        } catch (SecurityException e) {
            Log.e("Exceptions: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            viewModel.setPermissionsUpdate(true);
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        viewModel.setPermissionsUpdate(false);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.setPermissionsUpdate(true);
                }

            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            viewModel.getPermissionsUpdated().observe(getViewLifecycleOwner(), it -> {
                if (it) {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);

                } else {
                    mMap.setMyLocationEnabled(false);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    lastKnownLocation = null;
                    getLocationPermission();
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public static void addMarkers(LatLng coordinates, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, DEFAULT_ZOOM));
        mMap.addMarker(new MarkerOptions().position(coordinates).title(title));
    }
}
