package org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.CurrentWeather;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Location;
import org.me.gcu.adekunle_ganiyat_s2110996.data.repositories.WeatherRepository;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.LocalDataSource;
import org.me.gcu.adekunle_ganiyat_s2110996.data.sources.NetworkDataSource;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.adapters.LocationAdapter;
import org.me.gcu.adekunle_ganiyat_s2110996.util.AppExecutors;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements LocationAdapter.OnLocationClickListener {
    private Context context;
    private SearchView searchView;
    private WeatherRepository weatherRepository;
    private NetworkDataSource networkDataSource;
    private LocalDataSource localDataSource;
    private CurrentWeatherFragment currentWeatherFragment;
    private WeatherForecastFragment weatherForecastFragment;
    private RecyclerView searchResultsRecyclerView;
    private LocationAdapter locationAdapter;
    private List<String> locationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view);

        locationList = new ArrayList<>();
        locationList.add("Glasgow");
        locationList.add("London");
        locationList.add("New York");
        locationList.add("Oman");
        locationList.add("Mauritius");
        locationList.add("Bangladesh");

        locationAdapter = new LocationAdapter(locationList, this);

        searchResultsRecyclerView.setAdapter(locationAdapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLocations(newText);
                return true;
            }
        });
        networkDataSource = new NetworkDataSource();
        localDataSource = new LocalDataSource(context);
        weatherRepository = new WeatherRepository(networkDataSource, localDataSource);

        return view;
    }

    @Override
    public void onLocationClick(String location) {
        // Fetch weather data for the selected location
        searchLocation(location);
    }

    // Method to search locations based on the query string
    private void searchLocation(String location) {
        Log.d(TAG, "Fetching weather forecast for locationId-search0: " + location);
//         Check if the query is empty or null
        if (location == null || location.trim().isEmpty()) {
            showToast("Please enter a valid location.");
            return;
        }

        // Convert the query to lowercase for case-insensitive comparison
        String lowercaseQuery = location.toLowerCase();

        // Initialize a list to store matching location IDs
        List<String> matchingLocationIds = new ArrayList<>();

        // Iterate through all locations to find matches
        for (Map.Entry<String, Integer> entry : Location.getLocationMap().entrySet()) {
            String locationName = entry.getKey();
            // Convert location name to lowercase for case-insensitive comparison
            if (locationName.equalsIgnoreCase(location)) {
                String locationId = Integer.toString(entry.getValue());
                matchingLocationIds.add(locationId);
                break;
            }
        }

        // Check if any matching locations were found
        if (matchingLocationIds.isEmpty()) {
            showToast("No matching locations found.");
            return;
        }

        // Use the first matching location ID
        String locationId = matchingLocationIds.get(0);

//        int locationId = Location.getId(location);
//        if (locationId == -1) {
//            showToast("Invalid location: " + location);
//            return;
//        }

        // Execute fetching weather data in background using AppExecutors
        AppExecutors.getInstance().diskIO().execute(() -> {
            // Fetch weather forecast for the location ID
            weatherRepository.getWeatherForecast(locationId, new WeatherRepository.WeatherCallback<List<Forecast>>() {
                @Override
                public void onSuccess(List<Forecast> data) {
                    // Update UI with weather forecast data
                    updateWeatherForecastUI(locationId);
                }

                @Override
                public void onFailure(String message) {
                    // Handle error for weather forecast retrieval
                    showToast("Failed to retrieve weather forecast: " + message);
                }
            }, requireContext());

            // Fetch current weather for the location ID
            weatherRepository.getCurrentWeather(locationId, new WeatherRepository.WeatherCallback<CurrentWeather>() {
                @Override
                public void onSuccess(CurrentWeather data) {
                    // Update UI with current weather data
                    updateCurrentWeatherUI(locationId);
                }

                @Override
                public void onFailure(String message) {
                    // Handle error for current weather retrieval
                    showToast("Failed to retrieve current weather: " + message);
                }
            }, requireContext());
        });
    }
    // Method to show a toast message
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }



    private void updateCurrentWeatherUI(String locationId) {
        // Create instances of CurrentWeatherFragment and WeatherForecastFragment
        CurrentWeatherFragment currentWeatherFragment = new CurrentWeatherFragment();

        // Set the location for the fragments
        Bundle bundle = new Bundle();
        bundle.putString("locationId", locationId);
        currentWeatherFragment.setArguments(bundle);

        // Add the fragment to the respective containers
        getChildFragmentManager().beginTransaction()
                .replace(R.id.currentWeatherContainer, currentWeatherFragment)
                .commit();

        if (onSearchResultListener != null) {
            onSearchResultListener.onSearchResultReceived(locationId);
        }
    }

    private void updateWeatherForecastUI(String locationId) {
        // Create instances of WeatherForecastFragment
        WeatherForecastFragment weatherForecastFragment = new WeatherForecastFragment();

        // Set the location for the fragments
        Bundle bundle = new Bundle();
        bundle.putString("locationId", locationId);
        weatherForecastFragment.setArguments(bundle);

        // Add the fragments to the respective containers
        getChildFragmentManager().beginTransaction()
                .replace(R.id.forecastContainer, weatherForecastFragment)
                .commit();

        if (onSearchResultListener != null) {
            onSearchResultListener.onSearchResultReceived(locationId);
        }
    }

    private void filterLocations(String query) {
        List<String> filteredList = new ArrayList<>();
        for (String location : locationList) {
            if (location.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(location);
            }
        }
        locationAdapter.filterList(filteredList);
        locationAdapter.notifyDataSetChanged();
    }

    public interface OnSearchResultListener {
        void onSearchResultReceived(String locationId);
    }

    private OnSearchResultListener onSearchResultListener;

    public void setOnSearchResultListener(OnSearchResultListener listener) {
        this.onSearchResultListener = listener;
    }
}