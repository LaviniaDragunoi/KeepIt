package com.example.user.keepit.networking;

import com.example.user.keepit.models.DirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //https://maps.googleapis.com/maps/api/directions/json?origin=44.4286358%2C26.0317354&destination=Moghioros%20Bucharest&key=AIzaSyCzTRbicswDKeBpFwLnyBUOKOztTrsmpr0
    @GET("json")
    Call<DirectionResponse> getDirections(
            @Query("origin") String coordinates,
            @Query("destination") String meetingLocation,
            @Query("key") String key
    );

    //nu merege , am eroare at cand pun Path
    // https://maps.googleapis.com/maps/api/directions/json/44.4286358,26.0317354/Moghioros%20Bucharest/AIzaSyCzTRbicswDKeBpFwLnyBUOKOztTrsmpr0
    // => @GET("json/{origin}/{destination}/{key}")
    @GET("json?{origin}?{destination}?{key}")
    Call<DirectionResponse> getRoutes(
            @Path("origin") String origin,
            @Path("destination") String destination,
            @Path("key") String key
    );


//    @GET("json?origin={origin}&destination={destination}&key={key}")
//    Call<List<Route>> getRoutesWithBody(
//            @Body("origin") String origin,
//            @Path("destination") String destination,
//            @Path("key") String key
//    );

}
