package com.ztzrk.h071211021_finalmobile.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ztzrk.h071211021_finalmobile.model.TvResponse;

public class TvShowDatabaseHelper {
    private final DatabaseHelper dbHelper;

    public TvShowDatabaseHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertTvShow(TvResponse tvShow) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = TvShowMapper.toContentValues(tvShow);
        long newRowId = db.insert(DatabaseContract.TvEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public int deleteTvShow(long tvShowId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseContract.TvEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(tvShowId) };
        int deletedRows = db.delete(DatabaseContract.TvEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows;
    }
}