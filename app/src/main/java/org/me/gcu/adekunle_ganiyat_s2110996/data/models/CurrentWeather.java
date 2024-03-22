package org.me.gcu.adekunle_ganiyat_s2110996.data.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentWeather {
    private String title;
    private float temperature;
    private String windDirection;
    private float windSpeed;
    private String humidity;
    private String pressure;
    private String visibility;

    public CurrentWeather() {
        // Default constructor
    }

    public CurrentWeather(String title, float temperature, String windDirection, float windSpeed, String humidity, String pressure, String visibility) {
        this.title = title;
        this.temperature = temperature;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    public CurrentWeather(float temperature, String title, String humidity) {
        this.temperature =temperature;
        this.title = title;
        this.humidity =humidity;
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

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
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
            String title = jsonObject.getString("title");
            float temperature = (float) jsonObject.getDouble("temperature");
            String windDirection = jsonObject.getString("windDirection");
            float windSpeed = (float) jsonObject.getDouble("windSpeed");
            String humidity = jsonObject.getString("humidity");
            String pressure = jsonObject.getString("pressure");
            String visibility = jsonObject.getString("visibility");
            return new CurrentWeather(title, temperature, windDirection, windSpeed, humidity, pressure, visibility);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
