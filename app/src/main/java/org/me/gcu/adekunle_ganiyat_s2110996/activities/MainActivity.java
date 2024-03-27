package org.me.gcu.adekunle_ganiyat_s2110996.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.adapters.WeatherForecastAdapter;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.CurrentWeatherFragment;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.SearchFragment;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.WeatherForecastFragment;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.viewmodels.WeatherViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnSearchResultListener {
    private static final String LOCATION_ID = "934154";

    private CurrentWeatherFragment currentWeatherFragment;
    private WeatherForecastFragment weatherForecastFragment;
    private WeatherViewModel weatherViewModel;
    private RecyclerView recyclerView;
    private WeatherForecastAdapter forecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        recyclerView = findViewById(R.id.forecastRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        forecastAdapter = new WeatherForecastAdapter(this, new ArrayList<>(), forecast -> {
            // Start DetailedForecastActivity when a card is clicked
            Intent intent = new Intent(MainActivity.this, DetailedForecastActivity.class);
            intent.putExtra("forecast", forecast);
            startActivity(intent);
        });
        recyclerView.setAdapter(forecastAdapter);

        // Observe weatherForecast LiveData and update the adapter
        weatherViewModel.getWeatherForecast(LOCATION_ID).observe(this, forecastList -> {
            forecastAdapter.updateForecastList(forecastList);
        });

        // Initialize fragments with the default location
        initializeFragments(LOCATION_ID);

        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setOnSearchResultListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.search_container, searchFragment)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    // Handle home click
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    // Open SearchActivity
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(searchIntent);
                    return true;
                } else if (itemId == R.id.navigation_map) {
                    // Handle map click
                    return true;
                } else if (itemId == R.id.navigation_settings) {
                    // Handle settings click
                    return true;
                }
                return false;
            }
        });
    }

    private void initializeFragments(String locationId) {
        // Initialize fragments
        currentWeatherFragment = new CurrentWeatherFragment();
        weatherForecastFragment = new WeatherForecastFragment();

        // Set the location for the fragments
        currentWeatherFragment.setLocation(locationId);
        weatherForecastFragment.setLocation(locationId);

        // Add the fragments to the activity layout
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.currentWeatherContainer, currentWeatherFragment);
        transaction.replace(R.id.forecastContainer, weatherForecastFragment);
        transaction.commit();
    }

    @Override
    public void onSearchResultReceived(String locationId) {
        initializeFragments(locationId);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        // Handle menu item selection
//        switch (id) {
//            case R.id.action_refresh:
//                // Refresh weather data
//                currentWeatherFragment.refreshData();
//                weatherForecastFragment.refreshData();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}