package com.ztzrk.h071211021_finalmobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "entertainment.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TV_SHOWS_TABLE = "CREATE TABLE " + DatabaseContract.TvEntry.TABLE_NAME + " (" +
                DatabaseContract.TvEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.TvEntry.COLUMN_NAME_API_ID + " INTEGER, " +
                DatabaseContract.TvEntry.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                DatabaseContract.TvEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                DatabaseContract.TvEntry.COLUMN_NAME_FIRST_AIR_DATE + " TEXT, " +
                DatabaseContract.TvEntry.COLUMN_NAME_NAME + " TEXT, " +
                DatabaseContract.TvEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
                DatabaseContract.TvEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL);";

        String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + DatabaseContract.MovieEntry.TABLE_NAME + " (" +
                DatabaseContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.MovieEntry.COLUMN_NAME_API_ID + " INTEGER, " +
                DatabaseContract.MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                DatabaseContract.MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                DatabaseContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                DatabaseContract.MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
                DatabaseContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
                DatabaseContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL);";

        db.execSQL(SQL_CREATE_TV_SHOWS_TABLE);
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }
}
