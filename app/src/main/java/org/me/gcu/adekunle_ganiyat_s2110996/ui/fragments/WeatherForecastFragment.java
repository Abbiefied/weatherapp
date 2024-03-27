package org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.adekunle_ganiyat_s2110996.activities.DetailedForecastActivity;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.adapters.WeatherForecastAdapter;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.util.AppExecutors;

public class WeatherForecastFragment extends Fragment {

    private boolean resumedState = false;
    private String location;
    private WeatherViewModel weatherViewModel;
    private RecyclerView recyclerView;
    private WeatherForecastAdapter forecastAdapter;

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather_forecast, container, false);

        // Initialize RecyclerView
        recyclerView = root.findViewById(R.id.forecastRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        forecastAdapter = new WeatherForecastAdapter(requireContext(), new ArrayList<>(), forecast -> {
            // Handle item click event
            // You can navigate to the DetailedForecastActivity from here
            Intent intent = new Intent(requireContext(), DetailedForecastActivity.class);
            intent.putExtra("forecast", forecast);
            startActivity(intent);
        });
        recyclerView.setAdapter(forecastAdapter);

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

        if (location != null) {
            fetchWeatherForecastData();
        }
    }

        private void fetchWeatherForecastData() {
            weatherViewModel.getWeatherForecast(location).observe(getViewLifecycleOwner(), forecastList -> {
                // Update UI on the main thread
                requireActivity().runOnUiThread(() -> {
                    updateForecastList(forecastList);
                });
            });
        }


    @Override
    public void onPause() {
        super.onPause();
        resumedState = false;

        // Remove observers when the fragment is paused
        weatherViewModel.getCurrentWeather(location).removeObservers(getViewLifecycleOwner());
    }

    private void updateForecastList(List<Forecast> forecastList) {
        // Update RecyclerView with new data
        forecastAdapter.updateForecastList(forecastList);
    }
}
