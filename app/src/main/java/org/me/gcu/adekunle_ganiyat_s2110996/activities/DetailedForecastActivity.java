package org.me.gcu.adekunle_ganiyat_s2110996.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.data.models.Forecast;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.DetailedForecastFragment;

public class DetailedForecastActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_forecast);

        Forecast forecast = getIntent().getParcelableExtra("forecast");

        DetailedForecastFragment detailedForecastFragment = new DetailedForecastFragment();
        Bundle args = new Bundle();
        args.putParcelable("forecast", forecast);
        detailedForecastFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detailedForecastContainer, detailedForecastFragment)
                .commit();
    }
}