package com.example.user.keepit.database.convertors;

import androidx.room.TypeConverter;

import com.example.user.keepit.database.RouteEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MyRouteEntityTypeConverter {

    public static Gson gson = new Gson();

    @TypeConverter
    public static List<RouteEntity> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<RouteEntity>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<RouteEntity> someObjects) {
        return gson.toJson(someObjects);
    }
}