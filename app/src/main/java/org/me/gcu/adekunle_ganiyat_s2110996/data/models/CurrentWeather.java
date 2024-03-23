package org.me.gcu.adekunle_ganiyat_s2110996.data.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentWeather {
    private String title;
    private float temperature;
    private String windDirection;
    private String windSpeed;
    private String humidity;
    private String pressure;
    private String visibility;

    public CurrentWeather() {
        // Default constructor
    }

    public CurrentWeather(String title, float temperature, String windDirection, String windSpeed, String humidity, String pressure, String visibility) {
        this.title = title;
        this.temperature = temperature;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    public CurrentWeather(float temperature, String title, String humidity) {
        this.temperature = temperature;
        this.title = title;
        this.humidity = humidity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    // Serialize CurrentWeather object to JSON string
    public String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("temperature", temperature);
            jsonObject.put("windDirection", windDirection);
            jsonObject.put("windSpeed", windSpeed);
            jsonObject.put("humidity", humidity);
            jsonObject.put("pressure", pressure);
            jsonObject.put("visibility", visibility);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Deserialize JSON string to CurrentWeather object
    public static CurrentWeather fromJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            // Add null checks for each field before accessing them
            String title = jsonObject.optString("title", null);
            float temperature = (float) jsonObject.optDouble("temperature", 0.0);
            String windDirection = jsonObject.optString("windDirection", null);
            String windSpeed = jsonObject.optString("windSpeed", null);
            String humidity = jsonObject.optString("humidity", null);
            String pressure = jsonObject.optString("pressure", null);
            String visibility = jsonObject.optString("visibility", null);

            // Check if any required field is null
//            if (title == null || windDirection == null || windSpeed == null || humidity == null || pressure == null || visibility == null) {
//                return null; // Return null if any required field is missing
//            }

            // Create and return CurrentWeather object
            return new CurrentWeather(title, temperature, windDirection, windSpeed, humidity, pressure, visibility);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
