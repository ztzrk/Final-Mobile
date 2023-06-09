package com.ztzrk.h071211021_finalmobile.Database;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract() {} // Prevent instantiation

    public static class TvEntry implements BaseColumns {
        public static final String TABLE_NAME = "tv_shows";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_FIRST_AIR_DATE = "first_air_date";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_API_ID = "api_id";
    }

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_API_ID = "api_id";
    }
}