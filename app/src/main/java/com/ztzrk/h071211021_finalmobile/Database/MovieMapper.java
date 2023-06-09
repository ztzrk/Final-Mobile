package com.ztzrk.h071211021_finalmobile.Database;

import android.content.ContentValues;
import android.database.Cursor;

import com.ztzrk.h071211021_finalmobile.model.MovieResponse;

public class MovieMapper {
    public static ContentValues toContentValues(MovieResponse movie) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_API_ID, movie.getId());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getBackdropPath());
        values.put(DatabaseContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());
        return values;
    }


    public static MovieResponse fromCursor(Cursor cursor) {
        MovieResponse movie = new MovieResponse();
        movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieEntry.COLUMN_NAME_API_ID)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieEntry.COLUMN_NAME_POSTER_PATH)));
        movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieEntry.COLUMN_NAME_OVERVIEW)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieEntry.COLUMN_NAME_RELEASE_DATE)));
        movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieEntry.COLUMN_NAME_TITLE)));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH)));
        movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE)));
        return movie;
    }

}