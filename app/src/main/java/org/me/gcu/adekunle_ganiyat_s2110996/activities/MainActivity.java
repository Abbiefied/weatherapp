package org.me.gcu.adekunle_ganiyat_s2110996.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels.WeatherViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private static final double DEFAULT_LATITUDE = 51.5074; // Default latitude (London)
    private static final double DEFAULT_LONGITUDE = -0.1278; // Default longitude (London)

    private TextView rawDataDisplay;
    private Button startButton;
    private WeatherViewModel weatherViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        requestLocationPermission();

        rawDataDisplay = findViewById(R.id.rawDataDisplay);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        fetchWeatherData(latitude, longitude);
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
//                        showLocationServicesDialog();
                        fetchWeatherData(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
                    }
                });
    }

//    private void showLocationServicesDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Location services are disabled. Do you want to enable them?")
//                .setPositiveButton("Yes", (dialog, which) -> {
//                    // Open settings to enable location services
//                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                })
//                .setNegativeButton("No", (dialog, which) -> {
//                    // Show a toast or other message indicating that location services are required
//                    Toast.makeText(MainActivity.this, "Location services are required to fetch weather data", Toast.LENGTH_SHORT).show();
//                });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void fetchWeatherData(double latitude, double longitude) {
        String locationId = getLocationIdFromLatLon(latitude, longitude);
        if (locationId != null) {
            weatherViewModel.refreshData(locationId);
        } else {
            Toast.makeText(this, "Location ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private String getLocationIdFromLatLon(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getLocality(); // Or any other relevant address information
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.startButton) {
            getUserLocation();
        }
    }

//    private void displayWeatherForecast(List<Forecast> forecasts) {
//        // Update TextView with weather forecast data
//        if (forecasts != null && !forecasts.isEmpty()) {
//            Forecast forecast = forecasts.get(0); // Displaying the first forecast
//            rawDataDisplay.setText(getString(R.string.weather_forecast_format,
//                    forecast.getLocation(), forecast.getTime(), forecast.getDate(),
//                    forecast.getMinTemperature(), forecast.getMaxTemperature(),
//                    forecast.getHumidity()));
//        }
//    }
}
