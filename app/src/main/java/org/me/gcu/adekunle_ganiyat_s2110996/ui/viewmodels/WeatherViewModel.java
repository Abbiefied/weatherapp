package org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels;

import android.app.Application;

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

    public MutableLiveData<List<Forecast>> getWeatherForecast(String location) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            weatherRepository.getWeatherForecast(location, new WeatherRepository.WeatherCallback<List<Forecast>>() {
                @Override
                public void onSuccess(List<Forecast> data) {

                    weatherForecast.postValue(data);
                }

                @Override
                public void onFailure(String message) {
                    // Handle failure
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.e(TAG, "Failed to fetch weather forecast: " + message);
                        weatherForecast.setValue(new ArrayList<>());
                    });
                }
            }, getApplication().getApplicationContext());
        });
        return weatherForecast;
    }

    public MutableLiveData<CurrentWeather> getCurrentWeather(String location) {
        MutableLiveData<CurrentWeather> currentWeatherLiveData = new MutableLiveData<>();
        weatherRepository.getCurrentWeather(location, new WeatherRepository.WeatherCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {

                new Handler(Looper.getMainLooper()).post(() ->  currentWeatherLiveData.setValue(data));
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
        return currentWeatherLiveData;
    }


    public LiveData<Forecast> getSelectedForecast() {
        return selectedForecast;
    }

    public void selectForecast(Forecast forecast) {
        selectedForecast.setValue(forecast);
    }

    public void refreshData(String location) {
        weatherRepository.refreshData(location, getApplication().getApplicationContext());
    }
}