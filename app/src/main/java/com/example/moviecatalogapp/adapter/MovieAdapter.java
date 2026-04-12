package com.example.moviecatalogapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviecatalogapp.MovieDetailsActivity; // Import your new Activity
import com.example.moviecatalogapp.R;
import com.example.moviecatalogapp.model.Movie;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    public MovieAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvRating.setText(String.valueOf(movie.getVoteAverage()));
        holder.tvYear.setText(movie.getReleaseYear());

        Glide.with(holder.itemView.getContext())
                .load(movie.getPosterPath())
                .into(holder.ivPoster);

        // Handle Click to open Details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);

                // Passing the data to MovieDetailsActivity
                intent.putExtra("movie_title", movie.getTitle());
                intent.putExtra("movie_overview", movie.getOverview());
                intent.putExtra("movie_poster", movie.getPosterPath());
                intent.putExtra("movie_backdrop", movie.getBackdropPath()); // Ensure this is in Movie.java
                intent.putExtra("movie_date", movie.getReleaseYear());
                intent.putExtra("movie_rating", movie.getVoteAverage());
                intent.putExtra("movie_id", movie.getId());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvRating, tvYear;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_movie_poster);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvYear = itemView.findViewById(R.id.tv_year);
        }
    }
}