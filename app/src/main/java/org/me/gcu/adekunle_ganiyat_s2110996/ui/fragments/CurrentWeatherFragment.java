package org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.me.gcu.adekunle_ganiyat_s2110996.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import org.me.gcu.adekunle_ganiyat_s2110996.data.models.CurrentWeather;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels.WeatherViewModel;


public class CurrentWeatherFragment extends Fragment {

    private String location;
    private WeatherViewModel weatherViewModel;
    private TextView titleTextView;
    private TextView humidityTextView;
    private TextView temperatureTextView;

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_current_weather, container, false);

        // Initialize Views
        titleTextView = root.findViewById(R.id.titleTextView);
        humidityTextView = root.findViewById(R.id.humidityTextView);
        temperatureTextView = root.findViewById(R.id.temperatureTextView);

        // Set the location
        if (getArguments() != null) {
            location = getArguments().getString("location");
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel using requireActivity()
        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);

        // Observe current weather data only if location is not null
        if (location != null) {
            weatherViewModel.getCurrentWeather(location).observe(getViewLifecycleOwner(), currentWeather -> {
                updateCurrentWeather(currentWeather);
            });
        }
    }

    private void updateCurrentWeather(CurrentWeather currentWeather) {
        // Update views with current weather data
        if (currentWeather != null) {
            titleTextView.setText(currentWeather.getTitle());
            humidityTextView.setText(currentWeather.getHumidity());
            temperatureTextView.setText(getString(R.string.temperature, currentWeather.getTemperature()));
        }
    }
}


