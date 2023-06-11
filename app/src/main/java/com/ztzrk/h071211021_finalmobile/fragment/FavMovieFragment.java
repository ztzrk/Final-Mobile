package com.ztzrk.h071211021_finalmobile.fragment;

import static okhttp3.internal.Internal.instance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztzrk.h071211021_finalmobile.Database.DatabaseContract;
import com.ztzrk.h071211021_finalmobile.Database.DatabaseHelper;
import com.ztzrk.h071211021_finalmobile.Database.MovieMapper;
import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.FavoritePagerAdapter;
import com.ztzrk.h071211021_finalmobile.adapter.MovieAdapter;
import com.ztzrk.h071211021_finalmobile.adapter.TvAdapter;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;
import com.ztzrk.h071211021_finalmobile.model.TvResponse;

import java.util.ArrayList;
import java.util.List;

public class FavMovieFragment extends Fragment {
    private static FavMovieFragment instance;
    private RecyclerView rvFavMovie;
    private MovieAdapter movieAdapter;
    private DatabaseHelper databaseHelper;
    private TextView tvEmpty;

    public FavMovieFragment() {
        // Required empty public constructor
    }

    public static FavMovieFragment getInstance() {
        if (instance == null) {
            instance = new FavMovieFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(requireContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavMovie = view.findViewById(R.id.rv_fav_movie);
        tvEmpty = view.findViewById(R.id.tv_empty);

        ArrayList<MovieResponse> movieList = (ArrayList<MovieResponse>) retrieveMoviesFromDatabase();
        if (movieList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            tvEmpty.setVisibility(View.GONE);
        }
        movieAdapter = new MovieAdapter(movieList);

        // Set up the layout manager and adapter for the RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
        rvFavMovie.setLayoutManager(layoutManager);

        rvFavMovie.setAdapter(movieAdapter);

    }

    private List<MovieResponse> retrieveMoviesFromDatabase() {
        ArrayList<MovieResponse> movieList = new ArrayList<>();

        // Query the MovieEntry table and retrieve the data
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            MovieResponse movie = MovieMapper.fromCursor(cursor);
            movieList.add(movie);
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return movieList;
    }
    public void onResume() {
        super.onResume();
        // Refresh the data when the fragment becomes visible
        loadMovieData();
    }

    private void loadMovieData() {
        List<MovieResponse> movieList = retrieveMoviesFromDatabase();
        // Update the adapter with the retrieved data
        movieAdapter = new MovieAdapter((ArrayList<MovieResponse>) movieList);

        if (movieList.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            tvEmpty.setVisibility(View.GONE);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(),3);
        rvFavMovie.setLayoutManager(layoutManager);
        rvFavMovie.setAdapter(movieAdapter);
    }
}