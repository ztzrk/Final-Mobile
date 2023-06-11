package com.ztzrk.h071211021_finalmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.fragment.FavoriteFragment;
import com.ztzrk.h071211021_finalmobile.fragment.MovieFragment;
import com.ztzrk.h071211021_finalmobile.fragment.SearchFragment;
import com.ztzrk.h071211021_finalmobile.fragment.TvFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the activity is launched by pressing the back button from the main activity
        if (isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        MovieFragment homeFragment = MovieFragment.getInstance();
        Fragment fragment = fragmentManager.findFragmentByTag(MovieFragment.class.getSimpleName());
        if (!(fragment instanceof MovieFragment)) {
            fragmentManager.beginTransaction()
                    .add(R.id.fl_container, homeFragment, MovieFragment.class.getSimpleName())
                    .commit();
        }

        bottomNavigationView = findViewById(R.id.bottonnav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.item_movie) {
                    selectedFragment = MovieFragment.getInstance();
                } else if (item.getItemId() == R.id.item_tv) {
                    selectedFragment = TvFragment.getInstance();
                } else if (item.getItemId() == R.id.item_fav) {
                    selectedFragment = FavoriteFragment.getInstance();
                } else if (item.getItemId() == R.id.item_search) {
                    selectedFragment = SearchFragment.getInstance();
                }

                if (selectedFragment != null) {
                    switchFragment(selectedFragment);
                    return true;
                }

                return false;
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragment instanceof MovieFragment) {
            transaction.replace(R.id.fl_container, fragment, MovieFragment.class.getSimpleName());
            MovieFragment movieFragment = (MovieFragment) fragment;
            movieFragment.resetCurrentPage();
        } else if (fragment instanceof TvFragment) {
            transaction.replace(R.id.fl_container, fragment, TvFragment.class.getSimpleName());
            TvFragment tvFragment = (TvFragment) fragment;
            tvFragment.resetCurrentPage();
        } else if (fragment instanceof FavoriteFragment) {
            transaction.replace(R.id.fl_container, fragment, FavoriteFragment.class.getSimpleName());
        } else if (fragment instanceof SearchFragment) {
            transaction.replace(R.id.fl_container, fragment, SearchFragment.class.getSimpleName());
        }

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            if (!isBackPressed) {
                isBackPressed = true;
                moveTaskToBack(true);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackPressed = false;
    }
}
