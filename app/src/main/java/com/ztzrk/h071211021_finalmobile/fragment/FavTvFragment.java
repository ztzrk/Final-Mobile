package com.ztzrk.h071211021_finalmobile.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztzrk.h071211021_finalmobile.Database.DatabaseContract;
import com.ztzrk.h071211021_finalmobile.Database.DatabaseHelper;
import com.ztzrk.h071211021_finalmobile.Database.MovieMapper;
import com.ztzrk.h071211021_finalmobile.Database.TvShowMapper;
import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.MovieAdapter;
import com.ztzrk.h071211021_finalmobile.adapter.TvAdapter;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;
import com.ztzrk.h071211021_finalmobile.model.TvResponse;

import java.util.ArrayList;
import java.util.List;

public class FavTvFragment extends Fragment {

    private static FavTvFragment instance;
    private RecyclerView rvFavTv;
    private TvAdapter tvAdapter;
    private DatabaseHelper databaseHelper;
    public FavTvFragment() {
        // Required empty public constructor
    }

    public static FavTvFragment getInstance() {
        if (instance == null) {
            instance = new FavTvFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(requireContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavTv = view.findViewById(R.id.rv_fav_tv);
        List<TvResponse> tvList = retrieveTvFromDatabase();

        tvAdapter = new TvAdapter((ArrayList<TvResponse>) tvList);
        // Set up the layout manager and adapter for the RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(),3);
        rvFavTv.setLayoutManager(layoutManager);
        rvFavTv.setAdapter(tvAdapter);

    }
    private List<TvResponse> retrieveTvFromDatabase() {
        ArrayList<TvResponse> tvList = new ArrayList<>();

        // Query the MovieEntry table and retrieve the data
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.TvEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            TvResponse tv = TvShowMapper.fromCursor(cursor);
            tvList.add(tv);
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return tvList;
    }

    public void onResume() {
        super.onResume();
        // Refresh the data when the fragment becomes visible
        loadTvData();
    }

    private void loadTvData() {
        List<TvResponse> tvList = retrieveTvFromDatabase();
        // Update the adapter with the retrieved data
        tvAdapter = new TvAdapter((ArrayList<TvResponse>) tvList);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(),3);
        rvFavTv.setLayoutManager(layoutManager);
        rvFavTv.setAdapter(tvAdapter);
    }
}