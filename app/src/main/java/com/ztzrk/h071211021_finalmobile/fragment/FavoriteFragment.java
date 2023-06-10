package com.ztzrk.h071211021_finalmobile.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.FavoritePagerAdapter;

public class FavoriteFragment extends Fragment {

    private static FavoriteFragment instance;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment getInstance() {
        if (instance == null) {
            instance = new FavoriteFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        // Create an instance of the adapter that will provide the fragment for each tab
        FavoritePagerAdapter adapter = new FavoritePagerAdapter(getChildFragmentManager());

        // Add your favorite fragments to the adapter
        adapter.addFragment(new FavMovieFragment(), "Movie");
        adapter.addFragment(new FavTvFragment(), "Tv Shows");

        // Set the adapter to the ViewPager
        viewPager.setAdapter(adapter);

        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }
}
