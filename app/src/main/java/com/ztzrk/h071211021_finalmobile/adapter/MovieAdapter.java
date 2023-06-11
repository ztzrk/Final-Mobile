package com.ztzrk.h071211021_finalmobile.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ztzrk.h071211021_finalmobile.DetailActivity;
import com.ztzrk.h071211021_finalmobile.R;
import com.ztzrk.h071211021_finalmobile.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public ArrayList<MovieResponse> movies;
    public MovieAdapter(ArrayList<MovieResponse> movies) {
        this.movies = movies;
    }
    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        MovieResponse movie = movies.get(position);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500"+movie.getPosterPath())
                .into(holder.ivPostImg);
        holder.tvTitle.setText(movie.getTitle());
        holder.ivPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("movie", movie);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addAll(ArrayList<MovieResponse> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }
    public void setMovies(List<MovieResponse> movies) {
        this.movies = (ArrayList<MovieResponse>) movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<MovieResponse> newMovies) {
        int startPosition = movies.size();
        movies.addAll(newMovies);
        notifyItemRangeInserted(startPosition, newMovies.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivPostImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            ivPostImg = itemView.findViewById(R.id.iv_item_image);
        }
    }
}
