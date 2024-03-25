package org.me.gcu.adekunle_ganiyat_s2110996.data.models;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Location {
    private String name;

    private static final Map<String, Integer> locationMap = new HashMap<>();

    static {
        // Map locations to their IDs
        locationMap.put("Glasgow", 2648579);
        locationMap.put("London", 2643743);
        locationMap.put("New York", 5128581);
        locationMap.put("Oman", 287286);
        locationMap.put("Mauritius", 934154);
        locationMap.put("Bangladesh", 1185241);
    }

    public static Map<String, Integer> getLocationMap() {
        return locationMap;
    }

    public static int getId(String locationName) {
        Integer id = locationMap.get(locationName);
        return id != null ? id : -1; // Return -1 if location is not found

    }


    public String getName() {
        return name;
    }

}