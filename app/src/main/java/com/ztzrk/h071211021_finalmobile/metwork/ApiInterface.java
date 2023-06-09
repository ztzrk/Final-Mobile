package com.ztzrk.h071211021_finalmobile.metwork;
import com.ztzrk.h071211021_finalmobile.model.MovieDataResponse;
import com.ztzrk.h071211021_finalmobile.model.TvDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("3/movie/top_rated?language=en-US")
    Call<MovieDataResponse>
    getMovie(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex
    );

    @GET("3/tv/top_rated?language=en-US")
    Call<TvDataResponse>
    getTv(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex
    );

}