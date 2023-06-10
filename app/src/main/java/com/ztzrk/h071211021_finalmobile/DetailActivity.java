package com.ztzrk.h071211021_finalmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ztzrk.h071211021_finalmobile.Database.DatabaseContract;
import com.ztzrk.h071211021_finalmobile.Database.DatabaseHelper;
import com.ztzrk.h071211021_finalmobile.Database.MovieMapper;
import com.ztzrk.h071211021_finalmobile.Database.TvShowMapper;
import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;
import com.ztzrk.h071211021_finalmobile.model.TvResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    ImageView ivPost, ivBack, btnFav, btnBack;
    TextView tvTitle, tvRelease, tvScore, tvSynopsis, tvType;
    RatingBar simpleRatingBar;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivBack = findViewById(R.id.iv_detail_back);
        ivPost = findViewById(R.id.iv_detail_post);
        btnBack = findViewById(R.id.btn_back);
        tvRelease = findViewById(R.id.tv_detail_release);
        tvScore = findViewById(R.id.tv_detail_score);
        tvTitle = findViewById(R.id.tv_detail_title);
        tvSynopsis = findViewById(R.id.tv_detail_synopsis);
        tvType = findViewById(R.id.tv_type);
        btnFav = findViewById(R.id.btn_fav);
        simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);

        // Get the movie or TV show data from the intent
        MovieResponse movieResponse = getIntent().getParcelableExtra("movie");
        TvResponse tvResponse = getIntent().getParcelableExtra("tv");

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        if (movieResponse != null) {
            // Load movie data
            loadMovieData(movieResponse);
        } else if (tvResponse != null) {
            // Load TV show data
            loadTvShowData(tvResponse);
        }

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieResponse != null) {
                    if (isFavorite(movieResponse.getId(), DatabaseContract.MovieEntry.TABLE_NAME)) {
                        // Remove from favorites
                        removeFavorite(movieResponse.getId(), DatabaseContract.MovieEntry.TABLE_NAME);
                    } else {
                        // Add to favorites
                        addFavorite(movieResponse, DatabaseContract.MovieEntry.TABLE_NAME);
                    }
                } else if (tvResponse != null) {
                    if (isFavorite(tvResponse.getId(), DatabaseContract.TvEntry.TABLE_NAME)) {
                        // Remove from favorites
                        removeFavorite(tvResponse.getId(), DatabaseContract.TvEntry.TABLE_NAME);
                    } else {
                        // Add to favorites
                        addFavorite(tvResponse, DatabaseContract.TvEntry.TABLE_NAME);
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the favorite button in the previous activity based on the current state
                boolean isFavorite = false;
                if (movieResponse != null) {
                    isFavorite = isFavorite(movieResponse.getId(), DatabaseContract.MovieEntry.TABLE_NAME);
                } else if (tvResponse != null) {
                    isFavorite = isFavorite(tvResponse.getId(), DatabaseContract.TvEntry.TABLE_NAME);
                }

                // Create an intent to pass the favorite state back to the previous activity
                Intent intent = new Intent();
                intent.putExtra("isFavorite", isFavorite);
                setResult(Activity.RESULT_OK, intent);

                // Finish the activity
                finish();
            }
        });


    }

    private boolean isFavorite(int apiId, String tableName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                tableName,
                null,
                DatabaseContract.MovieEntry.COLUMN_NAME_API_ID + "=? OR " +
                        DatabaseContract.TvEntry.COLUMN_NAME_API_ID + "=?",
                new String[]{String.valueOf(apiId), String.valueOf(apiId)},
                null,
                null,
                null
        );
        boolean isFavorite = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return isFavorite;
    }

    private void addFavorite(Object object, String tableName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result;
        if (object instanceof MovieResponse) {
            result = db.insert(tableName, null, MovieMapper.toContentValues((MovieResponse) object));
        } else if (object instanceof TvResponse) {
            result = db.insert(tableName, null, TvShowMapper.toContentValues((TvResponse) object));
        } else {
            result = -1;
        }
        if (result != -1) {
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            btnFav.setImageResource(R.drawable.baseline_favorite_24);
        }
        db.close();
    }

    private void removeFavorite(int apiId, String tableName) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result;

        if (tableName.equals(DatabaseContract.MovieEntry.TABLE_NAME)) {
            result = db.delete(
                    tableName,
                    DatabaseContract.MovieEntry.COLUMN_NAME_API_ID + "=?",
                    new String[]{String.valueOf(apiId)}
            );
        } else if (tableName.equals(DatabaseContract.TvEntry.TABLE_NAME)) {
            result = db.delete(
                    tableName,
                    DatabaseContract.TvEntry.COLUMN_NAME_API_ID + "=?",
                    new String[]{String.valueOf(apiId)}
            );
        } else {
            result = -1;
        }

        if (result > 0) {
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            btnFav.setImageResource(R.drawable.baseline_favorite_border_24);
        }
        db.close();
    }


    private void loadMovieData(MovieResponse movieResponse) {
        // Update UI elements here
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500" + movieResponse.getBackdropPath())
                .into(ivBack);
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500" + movieResponse.getPosterPath())
                .into(ivPost);
        String dt = movieResponse.getReleaseDate();
        tvType.setText("Movie");
        tvRelease.setText(toFormat(dt));
        tvSynopsis.setText(movieResponse.getOverview());
        tvScore.setText(String.valueOf(movieResponse.getVoteAverage()));
        simpleRatingBar.setRating((float) (movieResponse.getVoteAverage()/2.0));
        tvTitle.setText(movieResponse.getTitle());

        System.out.println(movieResponse.getId());
        if (isFavorite(movieResponse.getId(), DatabaseContract.MovieEntry.TABLE_NAME)) {
            btnFav.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            btnFav.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }

    private void loadTvShowData(TvResponse tvResponse) {
        // Update UI elements here
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500" + tvResponse.getBackdropPath())
                .into(ivBack);
        Glide.with(getApplicationContext())
                .load("https://image.tmdb.org/t/p/w500" + tvResponse.getPosterPath())
                .into(ivPost);
        String dt = tvResponse.getFirstAirDate();
        tvType.setText("Tv Shows");
        tvRelease.setText(toFormat(dt));
        tvSynopsis.setText(tvResponse.getOverview());
        tvScore.setText(String.valueOf(tvResponse.getVoteAverage()));
        simpleRatingBar.setRating((float) (tvResponse.getVoteAverage()/2.0));
        tvTitle.setText(tvResponse.getName());

        if (isFavorite(tvResponse.getId(), DatabaseContract.TvEntry.TABLE_NAME)) {
            btnFav.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            btnFav.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }
    private String toFormat(String releaseDate) {
        String outputDate = null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(releaseDate);
            outputDate = outputFormat.format(date); // Output: June 09, 2023
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  outputDate;
    }
}
