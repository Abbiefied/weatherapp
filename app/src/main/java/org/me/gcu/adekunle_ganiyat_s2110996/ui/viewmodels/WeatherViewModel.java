package org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels;

import android.app.Application;

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

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private static final String TAG = WeatherRepository.class.getSimpleName();

    private final WeatherRepository weatherRepository;
    private MutableLiveData<List<Forecast>> weatherForecast;
    private MutableLiveData<CurrentWeather> currentWeather;
    private final MutableLiveData<Forecast> selectedForecast;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository(new NetworkDataSource(), new LocalDataSource(application));
        selectedForecast = new MutableLiveData<>();
    }

    public MutableLiveData<List<Forecast>> getWeatherForecast(String location) {
        MutableLiveData<List<Forecast>> weatherForecastLiveData = new MutableLiveData<>();
        weatherRepository.getWeatherForecast(location, new WeatherRepository.WeatherCallback<List<Forecast>>() {
            @Override
            public void onSuccess(List<Forecast> data) {
                weatherForecastLiveData.setValue(data);
            }

            @Override
            public void onFailure(String message) {
                // Handle failure
                Log.e(TAG, "Failed to fetch weather forecast: " + message);
                // Optionally, you can set an empty list or null to weatherForecastLiveData
                // weatherForecastLiveData.setValue(new ArrayList<>());
            }
        }, getApplication().getApplicationContext());
        return weatherForecastLiveData;
    }

    public MutableLiveData<CurrentWeather> getCurrentWeather(String location) {
        MutableLiveData<CurrentWeather> currentWeatherLiveData = new MutableLiveData<>();
        weatherRepository.getCurrentWeather(location, new WeatherRepository.WeatherCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {
                currentWeatherLiveData.setValue(data);
            }

            @Override
            public void onFailure(String message) {
                // Handle failure
                Log.e(TAG, "Failed to fetch current weather: " + message);
                // Optionally, you can set a default CurrentWeather object or null to currentWeatherLiveData
                // currentWeatherLiveData.setValue(null);
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