package org.me.gcu.adekunle_ganiyat_s2110996.util;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "https://weather-broker-cdn.api.bbci.co.uk/en/";
    private static final String FORECAST_ENDPOINT = "forecast/rss/3day/";
    private static final String OBSERVATION_ENDPOINT = "observation/rss/";

    // Timeout values in milliseconds
    private static final int CONNECTION_TIMEOUT = 10000; // 10 seconds
    private static final int READ_TIMEOUT = 10000; // 10 seconds

    public static String buildForecastUrl(String locationId) {
        return Uri.parse(BASE_URL)
                .buildUpon()
                .appendEncodedPath(FORECAST_ENDPOINT + locationId)
                .toString();
    }

    public static String buildObservationUrl(String locationId) {
        return Uri.parse(BASE_URL)
                .buildUpon()
                .appendEncodedPath(OBSERVATION_ENDPOINT + locationId)
                .toString();
    }

    public static String fetchData(String urlString) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(READ_TIMEOUT);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    return builder.toString();
                }
            } else {
                Log.e(TAG, "HTTP error response: " + responseCode);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing BufferedReader", e);
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
