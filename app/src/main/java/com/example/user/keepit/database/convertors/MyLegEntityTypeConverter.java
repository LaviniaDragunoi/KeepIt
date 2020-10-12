package com.example.user.keepit.database.convertors;

import androidx.room.TypeConverter;

import com.example.user.keepit.database.LegEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MyLegEntityTypeConverter {

        public static Gson gson = new Gson();

        @TypeConverter
        public static List<LegEntity> stringToSomeObjectList(String data) {
            if (data == null) {
                return Collections.emptyList();
            }

            Type listType = new TypeToken<List<LegEntity>>() {}.getType();

            return gson.fromJson(data, listType);
        }

        @TypeConverter
        public static String someObjectListToString(List<LegEntity> someObjects) {
            return gson.toJson(someObjects);
        }
    }
