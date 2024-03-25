package org.me.gcu.adekunle_ganiyat_s2110996.data.sources;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.me.gcu.adekunle_ganiyat_s2110996.data.models.CurrentWeather;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;

import org.me.gcu.adekunle_ganiyat_s2110996.util.AppExecutors;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.me.gcu.adekunle_ganiyat_s2110996.util.NetworkUtils;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;

public class NetworkDataSource {

    private static final String TAG = NetworkDataSource.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public void fetchWeatherForecast(Context context, String locationId, WeatherCallback<List<Forecast>> callback) {
        Log.d(TAG, "Fetching weather forecast for locationId - network1: " + locationId);
        if (!isNetworkAvailable(context)) {
            callback.onFailure("Network unavailable");
            return;
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                String url = NetworkUtils.buildForecastUrl(locationId);
                String xmlData = NetworkUtils.fetchData(url);
                // Parse XML data into a list of forecast objects
                List<Forecast> forecasts = parseForecastData(xmlData);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (forecasts != null) {
                        callback.onSuccess(forecasts);
                    } else {
                        callback.onFailure("Failed to parse forecast data");
                    }
                });
            } catch (IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onFailure("Error fetching weather forecast data");
                });
                Log.e(TAG, "Error fetching weather forecast data: " + e.getMessage());
            }
        });
    }

    public void fetchCurrentWeather(Context context, String locationId, WeatherCallback<CurrentWeather> callback) {
        Log.d(TAG, "Fetching weather forecast for locationId - network2: " + locationId);
        if (!isNetworkAvailable(context)) {
            callback.onFailure("Network unavailable");
            return;
        }
        AppExecutors.getInstance().diskIO().execute(() -> {
            try {
                String url = NetworkUtils.buildObservationUrl(locationId);
                Log.d(TAG, "Fetching current weather data from: " + url);
                String xmlData = NetworkUtils.fetchData(url);
                if (xmlData == null || xmlData.isEmpty()) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.onFailure("Empty or null data received");
                    });
                    return;
                }
                Log.d(TAG, "Received XML data: " + xmlData); // Log the raw XML data
                // Parse XML data into current weather object
                CurrentWeather currentWeather = parseCurrentWeatherData(xmlData);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (currentWeather != null) {
                        callback.onSuccess(currentWeather);
                    } else {
                        callback.onFailure("Failed to parse current weather data");
                    }
                });
            } catch (IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onFailure("Error fetching current weather data");
                });
                Log.e(TAG, "Error fetching current weather data: " + e.getMessage());
            }
        });
    }

    private List<Forecast> parseForecastData(String xmlData) {
        List<Forecast> forecastList = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData));
            int eventType = parser.getEventType();
            String title = null;
            String minTemp = null;
            String maxTemp = null;
            String windDirection = null;
            String windSpeed = null;
            String visibility = null;
            String pressure = null;
            String humidity = null;
            String uvRisk = null;
            String pollution = null;
            String sunrise = null;
            String sunset = null;
            String pubDate = null; // New variable to store pubDate

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("item".equalsIgnoreCase(tagName)) {
                            // Reset parameters for each new forecast item
                            title = null;
                            minTemp = null;
                            maxTemp = null;
                            windDirection = null;
                            windSpeed = null;
                            visibility = null;
                            pressure = null;
                            humidity = null;
                            uvRisk = null;
                            pollution = null;
                            sunrise = null;
                            sunset = null;
                            pubDate = null; // Initialize pubDate
                        } else if ("title".equalsIgnoreCase(tagName)) {
                            title = parser.nextText();
                            // Extract minTemp and maxTemp from title
                            if (title != null) {
                                String[] tempParts = title.split("Maximum Temperature: | Minimum Temperature: ");
                                if (tempParts.length >= 3) {
                                    minTemp = tempParts[1].substring(0, tempParts[1].indexOf('°'));
                                    maxTemp = tempParts[2].substring(0, tempParts[2].indexOf('°'));
                                }
                            }
                        } else if ("description".equalsIgnoreCase(tagName)) {
                            String description = parser.nextText();
                            // Parse other forecast details from description
                            String[] details = description.split(", ");
                            for (String detail : details) {
                                String[] keyValue = detail.split(": ");
                                if (keyValue.length == 2) {
                                    String key = keyValue[0];
                                    String value = keyValue[1];
                                    switch (key) {
                                        case "Wind Direction":
                                            windDirection = value;
                                            break;
                                        case "Wind Speed":
                                            windSpeed = value.substring(0, value.indexOf("mph"));
                                            break;
                                        case "Visibility":
                                            visibility = value;
                                            break;
                                        case "Pressure":
                                            pressure = value.substring(0, value.indexOf("mb"));
                                            break;
                                        case "Humidity":
                                            humidity = value.substring(0, value.indexOf("%"));
                                            break;
                                        case "UV Risk":
                                            uvRisk = value;
                                            break;
                                        case "Pollution":
                                            pollution = value;
                                            break;
                                        case "Sunrise":
                                            sunrise = value;
                                            break;
                                        case "Sunset":
                                            sunset = value;
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        } else if ("pubDate".equalsIgnoreCase(tagName)) {
                            pubDate = parser.nextText(); // Extract pubDate
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equalsIgnoreCase(tagName)) {
                            // Create a new Forecast object with the parsed parameters
                            Forecast forecast = new Forecast();
                            forecast.setTitle(title);
                            forecast.setMinTemperature(minTemp != null ? Float.parseFloat(minTemp) : -1); // Set to -1 if null
                            forecast.setMaxTemperature(maxTemp != null ? Float.parseFloat(maxTemp) : -1); // Set to -1 if null
                            forecast.setWindDirection(windDirection != null ? windDirection : "Not available"); // Set to "Not available" if null
                            forecast.setWindSpeed(windSpeed != null ? Float.parseFloat(windSpeed) : -1); // Set to -1 if null
                            forecast.setVisibility(visibility != null ? visibility : "Not available"); // Set to "Not available" if null
                            forecast.setPressure(pressure != null ? pressure : "Not available"); // Set to "Not available" if null
                            forecast.setHumidity(humidity != null ? humidity : "Not available"); // Set to "Not available" if null
                            forecast.setUvRisk(uvRisk != null ? uvRisk : "Not available"); // Set to "Not available" if null
                            forecast.setPollution(pollution != null ? pollution : "Not available"); // Set to "Not available" if null
                            forecast.setSunrise(sunrise != null ? sunrise : "Not available"); // Set to "Not available" if null
                            forecast.setSunset(sunset != null ? sunset : "Not available"); // Set to "Not available" if null
                            // Extract day of the week, date, and time from pubDate
                            if (pubDate != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                                try {
                                    Date date = sdf.parse(pubDate);
                                    // Extracting day of the week, date, and time
                                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                    if (date != null) {
                                        forecast.setDayOfWeek(dayFormat.format(date));
                                        forecast.setDate(dateFormat.format(date));
                                        forecast.setTime(timeFormat.format(date));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            forecastList.add(forecast);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error parsing forecast data: " + e.getMessage());
        }
        return forecastList;
    }




    private CurrentWeather parseCurrentWeatherData(String xmlData) {
        CurrentWeather currentWeather = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("title".equalsIgnoreCase(tagName)) {
                            currentWeather = new CurrentWeather();
                            String title = parser.nextText();
                            // Extract temperature from title
                            String[] tempParts = title.split(",");
                            if (tempParts.length >= 2) {
                                String temperatureStr = tempParts[1].trim().split(" ")[0];
                                float temperature = Float.parseFloat(temperatureStr.replace("°C", "").trim());
                                currentWeather.setTemperature(temperature);
                            }
                        } else if ("description".equalsIgnoreCase(tagName)) {
                            // Parse other current weather details from description
                            String description = parser.nextText();
                            String[] parts = description.split(",");
                            for (String part : parts) {
                                String[] keyValue = part.trim().split(":");
                                if (keyValue.length == 2) {
                                    String key = keyValue[0].trim();
                                    String value = keyValue[1].trim();
                                    switch (key) {
                                        case "Wind Direction":
                                            currentWeather.setWindDirection(value);
                                            break;
                                        case "Wind Speed":
                                            currentWeather.setWindSpeed(value);
                                            break;
                                        case "Humidity":
                                            currentWeather.setHumidity(value);
                                            break;
                                        case "Pressure":
                                            currentWeather.setPressure(value);
                                            break;
                                        case "Visibility":
                                            currentWeather.setVisibility(value);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error parsing current weather data: " + e.getMessage());
        }
        return currentWeather;
    }


    public interface WeatherCallback<T> {
        void onSuccess(T data);
        void onFailure(String message);
    }
}