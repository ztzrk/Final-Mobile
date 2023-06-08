package com.ztzrk.h071211021_finalmobile.metwork;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieInstance {

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/3/movie/550?api_key=edbbdb4bddde7b1048a3ff5d8736ce74")
                    .build();
        }
        return retrofit;
    }

}
