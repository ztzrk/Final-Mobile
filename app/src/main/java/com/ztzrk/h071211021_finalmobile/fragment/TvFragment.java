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

import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.adapter.MovieAdapter;
import com.ztzrk.h071211021_finalmobile.adapter.TvAdapter;
import com.ztzrk.h071211021_finalmobile.metwork.ApiInstance;
import com.ztzrk.h071211021_finalmobile.metwork.ApiInterface;
import com.ztzrk.h071211021_finalmobile.model.MovieDataResponse;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;
import com.ztzrk.h071211021_finalmobile.model.TvDataResponse;
import com.ztzrk.h071211021_finalmobile.model.TvResponse;

import java.util.ArrayList;
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

        progressBar = getActivity().findViewById(R.id.progressbar);
        network_error = getActivity().findViewById(R.id.network_error);
        rv_tv = getActivity().findViewById(R.id.rv_tv);
        getTvList();
    }

    public void getTvList() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = ApiInstance.getRetrofit();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<TvDataResponse> client = apiInterface.getTv("edbbdb4bddde7b1048a3ff5d8736ce74", 1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                client.enqueue(new Callback<TvDataResponse>() {
                    @Override
                    public void onResponse(Call<TvDataResponse> call, Response<TvDataResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ArrayList<TvResponse> tvs = (ArrayList<TvResponse>) response.body().getResults();
                                handler.post(() -> {
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
                                    rv_tv.setLayoutManager(gridLayoutManager);
                                    TvAdapter adapter = new TvAdapter(tvs);
                                    rv_tv.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TvDataResponse> call, Throwable t) {
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
                getTvList();
            }
        });
    }
}
