package org.me.gcu.adekunle_ganiyat_s2110996.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.CurrentWeatherFragment;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.SearchFragment;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.WeatherForecastFragment;

public class MainActivity extends AppCompatActivity implements SearchFragment.OnSearchResultListener {

    private static final String LOCATION_ID = "2648579";

    private CurrentWeatherFragment currentWeatherFragment;
    private WeatherForecastFragment weatherForecastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fragments with the default location
        initializeFragments(LOCATION_ID);

        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setOnSearchResultListener(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.search_container, searchFragment)
                .commit();
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