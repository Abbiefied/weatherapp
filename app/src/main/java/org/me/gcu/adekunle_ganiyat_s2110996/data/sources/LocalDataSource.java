package org.me.gcu.adekunle_ganiyat_s2110996.data.sources;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.CurrentWeather;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.util.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalDataSource {

    private static final String PREF_NAME = "weather_pref";
    private static final String KEY_FORECAST_LIST = "forecast_list";
    private static final String KEY_CURRENT_WEATHER = "current_weather";

    private Context context;
    public LocalDataSource(Context context) {
        this.context = context;
    }

    public void cacheWeatherForecast(List<Forecast> forecastList, Context context) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                JSONArray jsonArray = new JSONArray();
                for (Forecast forecast : forecastList) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("time", forecast.getTime());
                        jsonObject.put("date", forecast.getDate());
                        jsonObject.put("humidity", forecast.getHumidity());
                        jsonObject.put("minTemperature", forecast.getMinTemperature());
                        jsonObject.put("maxTemperature", forecast.getMaxTemperature());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                editor.putString(KEY_FORECAST_LIST, jsonArray.toString());
                editor.apply();
            }
        });
    }


    public List<Forecast> getCachedWeatherForecast(Context context) {
        List<Forecast> forecastList = new ArrayList<>();
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            try {
                JSONArray jsonArray = new JSONArray(sharedPreferences.getString(KEY_FORECAST_LIST, null));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String date = jsonObject.getString("date");
                    float minTemperature = (float) jsonObject.getDouble("minTemperature");
                    float maxTemperature = (float) jsonObject.getDouble("maxTemperature");
                    String humidity = jsonObject.getString("humidity");
                    Forecast forecast = new Forecast(date, minTemperature, maxTemperature, humidity);
                    forecastList.add(forecast);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            return forecastList;
        }

    public void cacheCurrentWeather(CurrentWeather currentWeather, Context context) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                JSONObject jsonObject = new JSONObject();
                try {
//            jsonObject.put("location", currentWeather.getLocation());
                    jsonObject.put("humidity", currentWeather.getHumidity());
                    jsonObject.put("temperature", currentWeather.getTemperature());
                    editor.putString(KEY_CURRENT_WEATHER, jsonObject.toString());
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CurrentWeather getCachedCurrentWeather(Context context) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            try {
                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(sharedPreferences.getString(KEY_CURRENT_WEATHER, null)));
                float temperature = (float) jsonObject.getDouble("temperature");
                String title = jsonObject.getString("title");
                String humidity = jsonObject.getString("humidity");
                return new CurrentWeather(temperature, title, humidity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


