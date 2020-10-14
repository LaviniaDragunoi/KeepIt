package com.example.user.keepit.database.convertors;

import androidx.room.TypeConverter;

import com.example.user.keepit.database.StepEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MyStepEntityTypeConverter {
    public static Gson gson = new Gson();

    @TypeConverter
    public static List<StepEntity> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<StepEntity>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<StepEntity> someObjects) {
        return gson.toJson(someObjects);
    }
}
