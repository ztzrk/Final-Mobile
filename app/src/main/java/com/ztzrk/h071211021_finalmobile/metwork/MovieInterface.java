package com.ztzrk.h071211021_finalmobile.metwork;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieInterface {

    @GET("tv/popular")
    Call<MovieInterface>
    getMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );

}