package com.seba.inventariado.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.seba.inventariado.R;
import com.seba.inventariado.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    SessionManager session;
    private static final long MIN_DELAY_MS = 200;
    private long mLastClickTime = 0;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            long lastClickTime = mLastClickTime;
            long now = System.currentTimeMillis();
            mLastClickTime = now;
            if (!(now - lastClickTime < MIN_DELAY_MS)) {
                switch (item.getItemId()) {
                    case R.id.navigationDashboard:
                        addFragment(new DashboardFragment());
                        return true;
                    case R.id.navigationItems:
                        addFragment(new ProductListFragment());
                        return true;
                    case R.id.navigationTags:
                        addFragment(new TagFragment());
                        return true;
                    case R.id.navigationAlerts:
                        addFragment(new AlertsFragment());
                        return true;
                    case R.id.navigationSettings:
                        addFragment(new ProfileFragment());
                        return true;
                }
            }

            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigationDashboard);
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finishAffinity();
    }
}