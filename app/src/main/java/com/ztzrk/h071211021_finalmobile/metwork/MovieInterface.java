package com.ztzrk.h071211021_finalmobile.metwork;
import com.ztzrk.h071211021_finalmobile.model.MovieDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieInterface {
    @GET("3/movie/now_playing")
    Call<MovieDataResponse>
    getMovie(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex
    );

}