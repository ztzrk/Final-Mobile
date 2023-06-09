package com.ztzrk.h071211021_finalmobile.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ztzrk.h071211021_finalmobile.model.MovieResponse;

public class MovieDatabaseHelper {
    private final DatabaseHelper dbHelper;

    public MovieDatabaseHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertMovie(MovieResponse movie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = MovieMapper.toContentValues(movie);
        long newRowId = db.insert(DatabaseContract.MovieEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public int deleteMovie(long movieId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.MovieEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(movieId) };
        int deletedRows = db.delete(DatabaseContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows;
    }
}