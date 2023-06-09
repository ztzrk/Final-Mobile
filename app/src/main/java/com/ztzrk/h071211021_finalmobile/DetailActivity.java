package com.ztzrk.h071211021_finalmobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;
import com.ztzrk.h071211021_finalmobile.model.TvResponse;

public class DetailActivity extends AppCompatActivity {
    ImageView ivPost, ivBack;
    ImageButton btnBack;
    TextView tvTitle, tvRelease, tvScore, tvSynopsis;
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

        MovieResponse movieResponse = getIntent().getParcelableExtra("movie");
        TvResponse tvResponse = getIntent().getParcelableExtra("tv");
        if (movieResponse != null) {
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+movieResponse.getBackdropPath())
                    .into(ivBack);
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+movieResponse.getPosterPath())
                    .into(ivPost);
            tvRelease.setText(movieResponse.getReleaseDate());
            tvSynopsis.setText(movieResponse.getOverview());
            tvScore.setText(String.valueOf(movieResponse.getVoteAverage()));
            tvTitle.setText(movieResponse.getTitle());
        }
        else if (tvResponse != null) {
            tvRelease.setText(tvResponse.getFirstAirDate());
            tvSynopsis.setText(tvResponse.getOverview());
            tvScore.setText(String.valueOf(tvResponse.getVoteAverage()));
            tvTitle.setText(tvResponse.getName());
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+tvResponse.getBackdropPath())
                    .into(ivBack);
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500"+tvResponse.getPosterPath())
                    .into(ivPost);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}