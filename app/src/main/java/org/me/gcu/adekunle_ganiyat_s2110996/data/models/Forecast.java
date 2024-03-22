package org.me.gcu.adekunle_ganiyat_s2110996.data.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Forecast {
    private String title;
    private String date;
    private String time;
    private float minTemperature;
    private float maxTemperature;
    private String windDirection;
    private float windSpeed;
    private String visibility;
    private String pressure;
    private String humidity;
    private String uvRisk;
    private String pollution;
    private String sunrise;
    private String sunset;

    public Forecast() {
        // Default constructor
    }

    // Constructor with all fields
    public Forecast(String title, String date, String time, float minTemperature, float maxTemperature, String windDirection, float windSpeed, String visibility, String pressure, String humidity, String uvRisk, String pollution, String sunrise, String sunset) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.pressure = pressure;
        this.humidity = humidity;
        this.uvRisk = uvRisk;
        this.pollution = pollution;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public Forecast(String date, float minTemperature, float maxTemperature, String humidity) {
        this.date = date;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.humidity = humidity;

    }

    // Getters and setters for all fields

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
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

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getUvRisk() {
        return uvRisk;
    }

    public void setUvRisk(String uvRisk) {
        this.uvRisk = uvRisk;
    }

    public String getPollution() {
        return pollution;
    }

    public void setPollution(String pollution) {
        this.pollution = pollution;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    // Serialize Forecast object to JSON string
    public String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", title);
            jsonObject.put("date", date);
            jsonObject.put("time", time);
            jsonObject.put("minTemperature", minTemperature);
            jsonObject.put("maxTemperature", maxTemperature);
            jsonObject.put("windDirection", windDirection);
            jsonObject.put("windSpeed", windSpeed);
            jsonObject.put("visibility", visibility);
            jsonObject.put("pressure", pressure);
            jsonObject.put("humidity", humidity);
            jsonObject.put("uvRisk", uvRisk);
            jsonObject.put("pollution", pollution);
            jsonObject.put("sunrise", sunrise);
            jsonObject.put("sunset", sunset);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Deserialize JSON string to Forecast object
    public static Forecast fromJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String title = jsonObject.getString("title");
            String date = jsonObject.getString("date");
            String time = jsonObject.getString("time");
            float minTemperature = (float) jsonObject.getDouble("minTemperature");
            float maxTemperature = (float) jsonObject.getDouble("maxTemperature");
            String windDirection = jsonObject.getString("windDirection");
            float windSpeed = (float) jsonObject.getDouble("windSpeed");
            String visibility = jsonObject.getString("visibility");
            String pressure = jsonObject.getString("pressure");
            String humidity = jsonObject.getString("humidity");
            String uvRisk = jsonObject.getString("uvRisk");
            String pollution = jsonObject.getString("pollution");
            String sunrise = jsonObject.getString("sunrise");
            String sunset = jsonObject.getString("sunset");
            return new Forecast(title, date, time, minTemperature, maxTemperature, windDirection, windSpeed, visibility, pressure, humidity, uvRisk, pollution, sunrise, sunset);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
