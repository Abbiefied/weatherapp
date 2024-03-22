package org.me.gcu.adekunle_ganiyat_s2110996.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import org.me.gcu.adekunle_ganiyat_s2110996.R;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.adapters.OnboardingPagerAdapter;
import org.me.gcu.adekunle_ganiyat_s2110996.ui.fragments.OnboardingFragment;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private Button btnSkip;
    private OnboardingPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize views
        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        // Set up ViewPager adapter
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new OnboardingFragment());

        FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompatActivity
        Fragment fragment = new Fragment();
        adapter = new OnboardingPagerAdapter(fragment, fragmentList);
        viewPager.setAdapter(adapter);

        // Add page change listener to ViewPager
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Update buttons based on current page
                updateButtons(position);
            }
        });

        // Button click listeners
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move to the next page
                int nextIndex = viewPager.getCurrentItem() + 1;
                if (nextIndex < adapter.getItemCount()) {
                    viewPager.setCurrentItem(nextIndex);
                } else {
                    // If on the last page, proceed to MainActivity
                    launchMainActivity();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Skip onboarding and proceed to MainActivity
                launchMainActivity();
            }
        });
    }

    private void updateButtons(int position) {
        if (position == adapter.getItemCount() - 1) {
            // Last page reached, change "Next" button text to "Finish"
            btnNext.setText(R.string.finish);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Proceed to MainActivity
                    launchMainActivity();
                }
            });
        } else {
            // Not on the last page, set button text to "Next"
            btnNext.setText(R.string.next);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Move to the next page
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            });
        }
    }

    private void launchMainActivity() {
        // Start MainActivity and finish OnboardingActivity
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}