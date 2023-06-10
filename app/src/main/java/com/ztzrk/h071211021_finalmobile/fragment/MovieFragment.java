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
import android.widget.RadioGroup;

import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.MovieAdapter;
import com.ztzrk.h071211021_finalmobile.metwork.ApiInstance;
import com.ztzrk.h071211021_finalmobile.metwork.ApiInterface;
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
    private int currentPage = 1;
    private int totalPages = 0;
    RadioGroup sortRadioGroup;


    private String selectedSortOption = "popularity.desc"; // Default sort option

    private MovieAdapter adapter; // Declare the adapter as a member variable
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isLoading = false; // Add a flag to prevent multiple simultaneous API calls


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

        progressBar = view.findViewById(R.id.progressbar);
        network_error = view.findViewById(R.id.network_error);
        rv_movie = view.findViewById(R.id.rv_movie);


        sortRadioGroup = view.findViewById(R.id.radioGroupSort);
        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedSortOption = getSelectedSortOption(checkedId);
                resetCurrentPage();
                setupRecyclerView();// Reset the page count when sorting changes
                getMovieList(); // Reload the movie list with the new sort option
            }
        });
        setupRecyclerView();
        getMovieList();
    }

    private String getSelectedSortOption(int checkedId) {
        if (checkedId == R.id.radioButtonPopularity) {
            return "popularity.desc";
        } else if (checkedId == R.id.radioButtonRating) {
            return "vote_average.desc";
        } else if (checkedId == R.id.radioButtonReleaseDate) {
            return "release_date.desc";
        } else {
            return "popularity.desc"; // Default sort option
        }
    }


    private void setupRecyclerView() {
        // Create the adapter instance
        adapter = new MovieAdapter(new ArrayList<>());

        // Set the adapter on the RecyclerView
        rv_movie.setAdapter(adapter);

        // Set the layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_movie.setLayoutManager(gridLayoutManager);
    }

    public void getMovieList() {
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = ApiInstance.getRetrofit();
        ApiInterface movieInterface = retrofit.create(ApiInterface.class);

        // Load the first page of data
        loadMovieData(movieInterface, 1);

        // Set up scroll listener for infinite scrolling
        rv_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Reached the end of the list, load the next page
                    loadNextPage(movieInterface);
                }
            }
        });
    }

    private void loadMovieData(ApiInterface movieInterface, int page) {
        isLoading = true;
        Call<MovieDataResponse> client = client = movieInterface.getMovie("edbbdb4bddde7b1048a3ff5d8736ce74", page, selectedSortOption);

        client.enqueue(new Callback<MovieDataResponse>() {
            @Override
            public void onResponse(Call<MovieDataResponse> call, Response<MovieDataResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        MovieDataResponse dataResponse = response.body();
                        ArrayList<MovieResponse> movies = (ArrayList<MovieResponse>) dataResponse.getResults();

                        // Update the UI on the main thread using a Handler
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (currentPage == 1) {
                                    // Set the totalPages value only for the first page
                                    totalPages = dataResponse.getTotalPages();
                                }

                                // Add the new movies to the adapter
                                adapter.addAll(movies);
                                isLoading = false;
                                progressBar.setVisibility(View.GONE);
                                sortRadioGroup.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDataResponse> call, Throwable t) {
                showNetworkErrorInfo();
                isLoading = false;
            }
        });
    }


    private void loadNextPage(ApiInterface movieInterface) {
        if (currentPage < totalPages) {
            currentPage++;
            loadMovieData(movieInterface, currentPage);
        }
    }




    private void showNetworkErrorInfo() {
        sortRadioGroup.setVisibility(View.GONE);
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
    public void resetCurrentPage() {
        currentPage = 1;
        totalPages = 0;
    }

}