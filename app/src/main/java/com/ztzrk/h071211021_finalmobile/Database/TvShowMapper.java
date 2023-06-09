package com.ztzrk.h071211021_finalmobile.Database;

import android.content.ContentValues;
import android.database.Cursor;

import com.ztzrk.h071211021_finalmobile.model.TvResponse;

public class TvShowMapper {
    public static ContentValues toContentValues(TvResponse tvShow) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TvEntry.COLUMN_NAME_API_ID, tvShow.getId());
        values.put(DatabaseContract.TvEntry.COLUMN_NAME_POSTER_PATH, tvShow.getPosterPath());
        values.put(DatabaseContract.TvEntry.COLUMN_NAME_OVERVIEW, tvShow.getOverview());
        values.put(DatabaseContract.TvEntry.COLUMN_NAME_FIRST_AIR_DATE, tvShow.getFirstAirDate());
        values.put(DatabaseContract.TvEntry.COLUMN_NAME_NAME, tvShow.getName());
        values.put(DatabaseContract.TvEntry.COLUMN_NAME_BACKDROP_PATH, tvShow.getBackdropPath());
        values.put(DatabaseContract.TvEntry.COLUMN_NAME_VOTE_AVERAGE, tvShow.getVoteAverage());
        return values;
    }


    public static TvResponse fromCursor(Cursor cursor) {
        TvResponse tvShow = new TvResponse();
        tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvEntry.COLUMN_NAME_API_ID)));
        tvShow.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvEntry.COLUMN_NAME_POSTER_PATH)));
        tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvEntry.COLUMN_NAME_OVERVIEW)));
        tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvEntry.COLUMN_NAME_FIRST_AIR_DATE)));
        tvShow.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvEntry.COLUMN_NAME_NAME)));
        tvShow.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvEntry.COLUMN_NAME_BACKDROP_PATH)));
        tvShow.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.TvEntry.COLUMN_NAME_VOTE_AVERAGE)));
        return tvShow;
    }

}