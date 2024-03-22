package org.me.gcu.adekunle_ganiyat_s2110996.data.repositories;

import android.content.Context;
import android.util.Log;

import org.me.gcu.adekunle_ganiyat_s2110996.data.models.CurrentWeather;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.LocalDataSource;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.NetworkDataSource;

import java.util.List;
import org.me.gcu.adekunle_ganiyat_s2110996.util.AppExecutors;

public class WeatherRepository {

    private static final String TAG = WeatherRepository.class.getSimpleName();

    private final NetworkDataSource networkDataSource;
    private final LocalDataSource localDataSource;

    public WeatherRepository(NetworkDataSource networkDataSource, LocalDataSource localDataSource) {
        this.networkDataSource = networkDataSource;
        this.localDataSource = localDataSource;
    }

    public void getWeatherForecast(String location, WeatherCallback<List<Forecast>> callback, Context context) {
        networkDataSource.fetchWeatherForecast(location, new NetworkDataSource.WeatherCallback<List<Forecast>>() {
            @Override
            public void onSuccess(List<Forecast> data) {
                callback.onSuccess(data);
                // Cache the forecast data locally
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

    public void getCurrentWeather(String location, WeatherCallback<CurrentWeather> callback, Context context) {
        networkDataSource.fetchCurrentWeather(location, new NetworkDataSource.WeatherCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {
                callback.onSuccess(data);
                // Cache the current weather data locally
                AppExecutors.getInstance().diskIO().execute(() -> localDataSource.cacheCurrentWeather(data, context));
            }

            @Override
            public void onFailure(String message) {
                Log.e(TAG, "Failed to fetch current weather: " + message);
                // If network call fails, fallback to local data source if available
                CurrentWeather cachedData = localDataSource.getCachedCurrentWeather(context);
                if (cachedData != null) {
                    callback.onSuccess(cachedData);
                } else {
                    callback.onFailure(message);
                }
            }
        });
    }

    public void refreshData(String location, Context context) {
        // Refresh weather forecast data
        getWeatherForecast(location, new WeatherCallback<List<Forecast>>() {
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
        getCurrentWeather(location, new WeatherCallback<CurrentWeather>() {
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