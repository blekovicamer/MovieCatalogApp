package com.example.moviecatalogapp.adapter;

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
import com.example.moviecatalogapp.MovieDetailsActivity;
import com.example.moviecatalogapp.R;
import com.example.moviecatalogapp.model.Movie;
import java.util.List;
import java.util.Locale; // Added for formatting

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private boolean isGrid;

    public MovieAdapter(List<Movie> movieList, Context context, boolean isGrid) {
        this.movieList = movieList;
        this.context = context;
        this.isGrid = isGrid;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isGrid ? R.layout.item_movie_grid : R.layout.item_movie;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        // 1. Set Title
        holder.tvTitle.setText(movie.getTitle());

        // 2. Fix Decimal Display (e.g., 7.58... -> 7.6)
        double rating = movie.getVoteAverage();
        String formattedRating = String.format(Locale.US, "%.1f", rating);
        holder.tvRating.setText(formattedRating);

        // 3. Set Year
        holder.tvYear.setText(movie.getReleaseYear());

        Glide.with(holder.itemView.getContext())
                .load(movie.getPosterPath())
                .into(holder.ivPoster);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
            intent.putExtra("movie_id", movie.getId());
            intent.putExtra("movie_title", movie.getTitle());
            intent.putExtra("movie_overview", movie.getOverview());
            intent.putExtra("movie_poster", movie.getPosterPath());
            intent.putExtra("movie_backdrop", movie.getBackdropPath());

            // CHANGE THIS: Pass the raw releaseDate instead of the formatted getReleaseYear()
            // This ensures MovieDetailsActivity has the full string to work with.
            intent.putExtra("movie_date", movie.getReleaseYear());

            intent.putExtra("movie_rating", movie.getVoteAverage());
            v.getContext().startActivity(intent);
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