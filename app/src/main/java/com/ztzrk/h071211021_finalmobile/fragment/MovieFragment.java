package com.ztzrk.h071211021_finalmobile.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.MovieAdapter;
import com.ztzrk.h071211021_finalmobile.metwork.MovieInstance;
import com.ztzrk.h071211021_finalmobile.metwork.MovieInterface;
import com.ztzrk.h071211021_finalmobile.model.MovieDataResponse;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieFragment extends Fragment {
    private static MovieFragment instance;
    ProgressBar progressBar;
    LinearLayout network_error;
    RecyclerView rv_movie;

    public MovieFragment() {
        // Required empty public constructor
    }

    public static MovieFragment getInstance() {
        if (instance == null) {
            instance = new MovieFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = getActivity().findViewById(R.id.progressbar);
        network_error = getActivity().findViewById(R.id.network_error);
        rv_movie= getActivity().findViewById(R.id.rv_movie);
        getMovieList();
    }

    public void getMovieList() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = MovieInstance.getRetrofit();
        MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Call<MovieDataResponse> client = movieInterface.getMovie("edbbdb4bddde7b1048a3ff5d8736ce74", 12);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                client.enqueue(new Callback<MovieDataResponse>() {
                    @Override
                    public void onResponse(Call<MovieDataResponse> call, Response<MovieDataResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ArrayList<MovieResponse> movies = (ArrayList<MovieResponse>) response.body().getResults();
                                handler.post(() -> {
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
                                    rv_movie.setLayoutManager(gridLayoutManager);
                                    MovieAdapter adapter = new MovieAdapter(movies);
                                    rv_movie.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDataResponse> call, Throwable t) {
                        showNetworkErrorInfo();
                    }
                });

            }
        });

    }

    private void showNetworkErrorInfo() {
        progressBar.setVisibility(View.GONE);
        network_error.setVisibility(View.VISIBLE);

        network_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                network_error.setVisibility(View.GONE);
                getMovieList();
            }
        });
    }
}