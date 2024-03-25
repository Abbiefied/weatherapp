package org.me.gcu.adekunle_ganiyat_s2110996.data.repositories;

import android.content.Context;
import android.util.Log;

import org.me.gcu.adekunle_ganiyat_s2110996.data.models.CurrentWeather;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Location;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.LocalDataSource;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.NetworkDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.me.gcu.adekunle_ganiyat_s2110996.util.AppExecutors;

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();
    private final NetworkDataSource networkDataSource;
    private final LocalDataSource localDataSource;
    private final Map<String, Long> lastRequestTimes = new HashMap<>();
    private static final long REQUEST_INTERVAL_MILLIS = 60000; // 1 minute

    public WeatherRepository(NetworkDataSource networkDataSource, LocalDataSource localDataSource) {
        this.networkDataSource = networkDataSource;
        this.localDataSource = localDataSource;
    }

    public void getWeatherForecast(String locationId, WeatherCallback<List<Forecast>> callback, Context context) {
        Long lastRequestTime = lastRequestTimes.get(locationId);
        if (lastRequestTime != null && System.currentTimeMillis() - lastRequestTime < REQUEST_INTERVAL_MILLIS) {
            Log.d(TAG, "Skipping weather forecast request for locationId " + locationId + " (too soon)");
            return;
        }

        Log.d(TAG, "Fetching weather forecast for locationId -weather repo1: " + locationId);
        networkDataSource.fetchWeatherForecast(context, locationId, new NetworkDataSource.WeatherCallback<List<Forecast>>() {
            @Override
            public void onSuccess(List<Forecast> data) {
                callback.onSuccess(data);
                // Cache the forecast data locally
                lastRequestTimes.put(locationId, System.currentTimeMillis());
                AppExecutors.getInstance().diskIO().execute(() -> localDataSource.cacheWeatherForecast(data, context));
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "Failed to fetch weather forecast: " + message);
                // If network call fails, fallback to local data source if available
                List<Forecast> cachedData = localDataSource.getCachedWeatherForecast(context);
                if (cachedData != null && !cachedData.isEmpty()) {
                    callback.onSuccess(cachedData);
                } else {
                    callback.onFailure(message);
                }
            }
        });
    }

    public void getCurrentWeather(String locationId, WeatherCallback<CurrentWeather> callback, Context context) {
        Long lastRequestTime = lastRequestTimes.get(locationId);
        if (lastRequestTime != null && System.currentTimeMillis() - lastRequestTime < REQUEST_INTERVAL_MILLIS) {
            Log.d(TAG, "Skipping current weather request for locationId " + locationId + " (too soon)");
            return;
        }

        Log.d(TAG, "Fetching weather forecast for locationId -weatherrepo2: " + locationId);
        networkDataSource.fetchCurrentWeather(context, locationId, new NetworkDataSource.WeatherCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {
                callback.onSuccess(data);

                lastRequestTimes.put(locationId, System.currentTimeMillis());
                // Cache the current weather data locally
                AppExecutors.getInstance().diskIO().execute(() -> localDataSource.cacheCurrentWeather(data, context));
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "Failed to fetch current weather: " + message);
                // If network call fails, fallback to local data source if available
                CurrentWeather cachedData = localDataSource.getCachedCurrentWeather(context);
                if (cachedData != null) {
                    Log.w(TAG, "Falling back to cached current weather data");
                    callback.onSuccess(cachedData);
                } else {
                    Log.e(TAG, "No cached current weather data available");
                    callback.onFailure("Failed to fetch current weather data. No cached data available.");
                }
            }
        });
    }

    public void refreshData(String locationId, Context context) {
        // Refresh weather forecast data
        getWeatherForecast(locationId, new WeatherCallback<List<Forecast>>() {
            @Override
            public void onSuccess(List<Forecast> data) {
                // Data already cached in onSuccess method of getWeatherForecast
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "Failed to refresh weather forecast data: " + message);
            }
        }, context);

        // Refresh current weather data
        getCurrentWeather(locationId, new WeatherCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {
                // Data already cached in onSuccess method of getCurrentWeather
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "Failed to refresh current weather data: " + message);
            }
        }, context);
    }


    public interface WeatherCallback<T> {
        void onSuccess(T data);

        void onFailure(String message);
    }
}