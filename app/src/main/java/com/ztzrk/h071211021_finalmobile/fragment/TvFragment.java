package com.ztzrk.h071211021_finalmobile.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.TvAdapter;
import com.ztzrk.h071211021_finalmobile.metwork.ApiInstance;
import com.ztzrk.h071211021_finalmobile.metwork.ApiInterface;
import com.ztzrk.h071211021_finalmobile.model.TvDataResponse;
import com.ztzrk.h071211021_finalmobile.model.TvResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TvFragment extends Fragment {
    private static TvFragment instance;
    ProgressBar progressBar;
    LinearLayout network_error;
    RecyclerView rv_tv;
    private int currentPage = 1;
    private int totalPages = 0;
    String formattedDate;

    RadioGroup sortRadioGroup;
    String selectedSortOption = "popularity.desc";
    private TvAdapter adapter; // Declare the adapter as a member variable
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isLoading = false; // Add a flag to prevent multiple simultaneous API calls


    public TvFragment() {
        // Required empty public constructor
    }

    public static TvFragment getInstance() {
        if (instance == null) {
            instance = new TvFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date currentDate = new Date();

        // Format the date as "YYYY-MM-DD"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = formatter.format(currentDate);

        progressBar = view.findViewById(R.id.progressbar);
        network_error = view.findViewById(R.id.network_error);
        rv_tv = view.findViewById(R.id.rv_tv);
        sortRadioGroup = view.findViewById(R.id.radioGroupSort);
        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedSortOption = getSelectedSortOption(checkedId);
                resetCurrentPage();
                setupRecyclerView();
                getTvList();
            }
        });

        setupRecyclerView();
        getTvList();
    }
    private String getSelectedSortOption(int checkedId) {
        if (checkedId == R.id.radioButtonPopularity) {
            return "popularity.desc";
        } else if (checkedId == R.id.radioButtonRating) {
            return "vote_average.desc";
        } else if (checkedId == R.id.radioButtonReleaseDate) {
            return "first_air_date.desc";
        } else {
            return "popularity.desc"; // Default sort option
        }
    }

    private void setupRecyclerView() {
        // Create the adapter instance
        adapter = new TvAdapter(new ArrayList<>());

        // Set the layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_tv.setLayoutManager(gridLayoutManager);

        // Set the adapter on the RecyclerView
        rv_tv.setAdapter(adapter);
    }


    public void getTvList() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = ApiInstance.getRetrofit();
        ApiInterface tvInterface = retrofit.create(ApiInterface.class);

        // Load the first page of data
        loadTvData(tvInterface, 1);

        // Set up scroll listener for infinite scrolling
        rv_tv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Reached the end of the list, load the next page
                    loadNextPage(tvInterface);
                }
            }
        });
    }
    private void loadTvData(ApiInterface tvInterface, int page) {
        isLoading = true;
        Call<TvDataResponse> client = null;
        if (selectedSortOption.equals("popular.desc")) {
            client = tvInterface.getPopularTv("edbbdb4bddde7b1048a3ff5d8736ce74", page);
        }
        else {
            client = tvInterface.getTv("edbbdb4bddde7b1048a3ff5d8736ce74", page, selectedSortOption, formattedDate);
        }
        client.enqueue(new Callback<TvDataResponse>() {
            @Override
            public void onResponse(Call<TvDataResponse> call, Response<TvDataResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        TvDataResponse dataResponse = response.body();
                        ArrayList<TvResponse> tvs = (ArrayList<TvResponse>) dataResponse.getResults();

                        // Check if the data is being retrieved correctly
                        for (TvResponse tv : tvs) {
                            Log.d("TvFragment", "TV Title: " + tv.getName());
                        }

                        // Update the UI on the main thread using a Handler
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (currentPage == 1) {
                                    // Set the totalPages value only for the first page
                                    totalPages = dataResponse.getTotalPages();
                                }

                                adapter.addAll(tvs);
                                isLoading = false;
                                progressBar.setVisibility(View.GONE);
                                sortRadioGroup.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<TvDataResponse> call, Throwable t) {
                showNetworkErrorInfo();
                isLoading = false;
            }
        });
    }



    private void loadNextPage(ApiInterface tvInterface) {
        if (currentPage < totalPages) {
            currentPage++;
            loadTvData(tvInterface, currentPage);
        }
    }

    private void showNetworkErrorInfo() {
        progressBar.setVisibility(View.GONE);
        network_error.setVisibility(View.VISIBLE);
        sortRadioGroup.setVisibility(View.GONE);

        network_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                network_error.setVisibility(View.GONE);
                getTvList();
            }
        });
    }

    public void resetCurrentPage() {
        currentPage = 1;
        totalPages = 0;
    }
}