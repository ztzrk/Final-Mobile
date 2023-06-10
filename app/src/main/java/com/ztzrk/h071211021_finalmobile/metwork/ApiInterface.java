package com.ztzrk.h071211021_finalmobile.metwork;
import com.ztzrk.h071211021_finalmobile.model.MovieDataResponse;
import com.ztzrk.h071211021_finalmobile.model.TvDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("3/discover/movie?language=en-US")
    Call<MovieDataResponse>
    getMovie(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex,
            @Query("sort_by") String sortBy
    );

    @GET("3/discover/tv?language=en-US")
    Call<TvDataResponse>
    getTv(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex,
            @Query("sort_by") String sortBy
    );
}