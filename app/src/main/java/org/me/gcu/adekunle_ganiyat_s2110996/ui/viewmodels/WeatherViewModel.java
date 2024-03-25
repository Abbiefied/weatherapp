package org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels;

import android.app.Application;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.me.gcu.adekunle_ganiyat_s2110996.data.models.CurrentWeather;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.data.repositories.WeatherRepository;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.LocalDataSource;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.NetworkDataSource;
import org.me.gcu.adekunle_ganiyat_s2110996.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private static final String TAG = WeatherRepository.class.getSimpleName();

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final WeatherRepository weatherRepository;

    private MutableLiveData<List<Forecast>> weatherForecast;
    private MutableLiveData<CurrentWeather> currentWeather;
    private final MutableLiveData<Forecast> selectedForecast;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository(new NetworkDataSource(), new LocalDataSource(application));
        weatherForecast = new MutableLiveData<>();
        currentWeather = new MutableLiveData<>();
        selectedForecast = new MutableLiveData<>();
    }

    public MutableLiveData<List<Forecast>> getWeatherForecast(String locationId) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            weatherRepository.getWeatherForecast(locationId, new WeatherRepository.WeatherCallback<List<Forecast>>() {
                @Override
                public void onSuccess(List<Forecast> data) {

                    mainHandler.post(() -> weatherForecast.postValue(data));
                }

                @Override
                public void onFailure(String message) {
                    // Handle failure
                    mainHandler.post(() -> {
                        Log.e(TAG, "Failed to fetch weather forecast: " + message);
                        weatherForecast.setValue(new ArrayList<>());
                    });
                }
            }, getApplication().getApplicationContext());
        });
        return weatherForecast;
    }

    public MutableLiveData<CurrentWeather> getCurrentWeather(String locationId) {
        weatherRepository.getCurrentWeather(locationId, new WeatherRepository.WeatherCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {

                mainHandler.post(() ->  currentWeather.setValue(data));
            }

            @Override
            public void onFailure(String message) {
                mainHandler.post(() -> {
                    // Handle failure
                    Log.e(TAG, "Failed to fetch current weather: " + message);
                    // Optionally, you can set a default CurrentWeather object or null to currentWeatherLiveData
                    // currentWeatherLiveData.setValue(null);
                });
            }
        }, getApplication().getApplicationContext());
        return currentWeather;
    }

//    public LiveData<List<String>> searchLocations(String query) {
//        MutableLiveData<List<String>> searchResults = new MutableLiveData<>();
//        weatherRepository.searchLocations(query, new WeatherRepository.WeatherCallback<List<String>>() {
//            @Override
//            public void onSuccess(List<String> data) {
//                searchResults.setValue(data);
//            }
//
//            @Override
//            public void onFailure(String message) {
//                // Handle failure
//            }
//        });
//        return searchResults;
//    }

    public LiveData<Forecast> getSelectedForecast() {
        return selectedForecast;
    }

    public void selectForecast(Forecast forecast) {
        selectedForecast.setValue(forecast);
    }

    public void refreshData(String locationId) {
        weatherRepository.refreshData(locationId, getApplication().getApplicationContext());
    }
}