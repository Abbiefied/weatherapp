package org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels.WeatherViewModel;

import org.me.gcu.adekunle_ganiyat_s2110996.R;

public class DetailedForecastFragment extends Fragment {

    private WeatherViewModel weatherViewModel;
    private TextView timeTextView;
    private TextView dateTextView;
    private TextView titleTextView;
    private TextView temperatureTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel using requireActivity()
        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detailed_forecast, container, false);

        // Initialize Views
        timeTextView = root.findViewById(R.id.detailedTimeTextView);
        dateTextView = root.findViewById(R.id.detailedDateTextView);
        titleTextView = root.findViewById(R.id.detailedTitleTextView);
        temperatureTextView = root.findViewById(R.id.detailedTemperatureTextView);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe selected forecast data using getViewLifecycleOwner()
        weatherViewModel.getSelectedForecast().observe(getViewLifecycleOwner(), forecast -> {
            updateSelectedForecast(forecast);
        });
    }

    private void updateSelectedForecast(Forecast forecast) {
        // Update views with selected forecast data
        if (forecast != null) {
            timeTextView.setText(forecast.getTime());
            dateTextView.setText(forecast.getDate());
            titleTextView.setText(forecast.getHumidity());
            temperatureTextView.setText(getString(R.string.temperature_range, forecast.getMinTemperature(), forecast.getMaxTemperature()));
        }
    }
}
