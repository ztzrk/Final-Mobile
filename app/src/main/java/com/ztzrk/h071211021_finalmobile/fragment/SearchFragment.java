package com.ztzrk.h071211021_finalmobile.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.MovieAdapter;
import com.ztzrk.h071211021_finalmobile.adapter.TvAdapter;
import com.ztzrk.h071211021_finalmobile.model.TvDataResponse;
import com.ztzrk.h071211021_finalmobile.model.TvResponse;
import com.ztzrk.h071211021_finalmobile.network.ApiInstance;
import com.ztzrk.h071211021_finalmobile.network.ApiInterface;
import com.ztzrk.h071211021_finalmobile.model.MovieDataResponse;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {
    private static SearchFragment instance;
    private ProgressBar progressBar;
    private RecyclerView rv_movie;
    private MovieAdapter movieAdapter;
    LinearLayout network_error;
    private TvAdapter tvAdapter;
    private ApiInterface apiInterface;
    private TextView tvSearch;
    RadioGroup radioGroup;
    private RadioButton radioButtonMovie;
    private RadioButton radioButtonTV;
    SearchView searchView;
    private String searchType = "movie";

    private int currentPage = 1;
    private boolean isLoading = false;
    private List<MovieResponse> movies;
    private List<TvResponse> tvShows;

    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment getInstance() {
        if (instance == null) {
            instance = new SearchFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressbar);
        network_error = view.findViewById(R.id.network_error);
        rv_movie = view.findViewById(R.id.rv_movie);
        tvSearch = view.findViewById(R.id.tvSearch);

        movieAdapter = new MovieAdapter(new ArrayList<>());
        tvAdapter = new TvAdapter(new ArrayList<>());

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rv_movie.setLayoutManager(layoutManager);
        rv_movie.setAdapter(movieAdapter);

        Retrofit retrofit = ApiInstance.getRetrofit();
        apiInterface = retrofit.create(ApiInterface.class);

        radioButtonMovie = view.findViewById(R.id.radioButtonMovie);
        radioButtonTV = view.findViewById(R.id.radioButtonTV);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonMovie) {
                    searchType = "movie";
                    tvSearch.setText("Search Your Movie");
                    resetAdapter();
                } else if (checkedId == R.id.radioButtonTV) {
                    searchType = "tv";
                    tvSearch.setText("Search Your Tv Show");
                    resetAdapter();
                }
                resetSearchView();
                resetCurrentPage();
            }
        });

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                resetCurrentPage();
                searchMedia(query);
                if (query.equals("")) {
                    tvSearch.setVisibility(View.VISIBLE);
                } else {
                    tvSearch.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resetCurrentPage();
                searchMedia(newText);
                if (newText.equals("")) {
                    tvSearch.setVisibility(View.VISIBLE);
                } else {
                    tvSearch.setVisibility(View.GONE);
                }
                return true;
            }
        });

        // Set up scroll listener for infinite scrolling
        rv_movie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Reached the end of the list, load the next page
                    loadNextPage();
                }
            }
        });
    }
    private void searchMedia(String query) {
        progressBar.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        tvSearch.setVisibility(View.VISIBLE);

        if (searchType.equals("movie")) {
            Call<MovieDataResponse> movieCall = apiInterface.searchMovies("edbbdb4bddde7b1048a3ff5d8736ce74", query, currentPage);
            movieCall.enqueue(new Callback<MovieDataResponse>() {
                @Override
                public void onResponse(Call<MovieDataResponse> call, Response<MovieDataResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        MovieDataResponse dataResponse = response.body();
                        if (dataResponse != null) {
                            movies = dataResponse.getResults();
                            if (currentPage == 1) {
                                movieAdapter.setMovies(movies);
                            } else {
                                movieAdapter.addMovies(movies);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieDataResponse> call, Throwable t) {
                    showNetworkErrorInfo();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else if (searchType.equals("tv")) {
            Call<TvDataResponse> tvCall = apiInterface.searchTVShows("edbbdb4bddde7b1048a3ff5d8736ce74", query, currentPage);
            tvCall.enqueue(new Callback<TvDataResponse>() {
                @Override
                public void onResponse(Call<TvDataResponse> call, Response<TvDataResponse> response) {
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        TvDataResponse dataResponse = response.body();
                        if (dataResponse != null) {
                            tvShows = dataResponse.getResults();
                            if (currentPage == 1) {
                                tvAdapter.setTvs(tvShows);
                            } else {
                                tvAdapter.addAll((ArrayList<TvResponse>) tvShows);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TvDataResponse> call, Throwable t) {
                    showNetworkErrorInfo();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    private void loadNextPage() {
        currentPage++;
        searchMedia(((SearchView) getView().findViewById(R.id.searchView)).getQuery().toString());
    }

    private void resetCurrentPage() {
        currentPage = 1;
    }

    private void resetSearchView() {
        SearchView searchView = getView().findViewById(R.id.searchView);
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    private void resetAdapter() {
        if (searchType.equals("movie")) {
            rv_movie.setAdapter(movieAdapter);
            movieAdapter.setMovies(new ArrayList<>());
        } else if (searchType.equals("tv")) {
            rv_movie.setAdapter(tvAdapter);
            tvAdapter.setTvs(new ArrayList<>());
        }
    }
    private void showNetworkErrorInfo() {
        progressBar.setVisibility(View.GONE);
        network_error.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);
        tvSearch.setVisibility(View.GONE);

        network_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                network_error.setVisibility(View.GONE);
                if (searchType.equals("movie")) {
                    radioGroup.check(R.id.radioButtonMovie);
                } else if (searchType.equals("tv")) {
                    radioGroup.check(R.id.radioButtonTV);
                }
                String query = searchView.getQuery().toString();
                searchMedia(query);
            }
        });
    }

}
