package com.ztzrk.h071211021_finalmobile.network;
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
            @Query("sort_by") String sortBy,
            @Query("primary_release_date.lte") String currentDate
    );
    @GET("3/movie/popular?language=en-US")
    Call<MovieDataResponse>
    getPopularMovie(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex
    );

    @GET("3/tv/popular?language=en-US")
    Call<TvDataResponse>
    getPopularTv(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex
    );

    @GET("3/search/movie")
    Call<MovieDataResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int currentPage);

    @GET("3/search/tv")
    Call<TvDataResponse> searchTVShows(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int currentPage);

    @GET("3/discover/tv?language=en-US")
    Call<TvDataResponse>
    getTv(
            @Query("api_key") String apiKey,
            @Query("page") int pageIndex,
            @Query("sort_by") String sortBy,
            @Query("first_air_date.lte") String currentDate
    );
}