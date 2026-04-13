package com.example.moviecatalogapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviecatalogapp.adapter.CastAdapter;
import com.example.moviecatalogapp.model.Movie;
import com.example.moviecatalogapp.model.Video;
import com.example.moviecatalogapp.model.VideoResponse;
import com.example.moviecatalogapp.network.RetrofitClient;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView ivBackdrop, ivPoster, btnFavorite;
    private TextView tvTitle, tvDateRuntime, tvRating, tvOverview;
    private TextView tvBudget, tvRevenue, tvCountry, tvUserRatingStatus;
    private RecyclerView rvCast;
    private RatingBar rbUserRating;

    private YouTubePlayerView youtubePlayerView;
    private View cvTrailer, tvLabelTrailer;

    private int movieId;
    private boolean isFavorite = false;
    private SharedPreferences sharedPreferences;

    // Safety variables for Video Playback
    private YouTubePlayer readyYouTubePlayer;
    private String videoKeyToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        sharedPreferences = getSharedPreferences("MoviesApp", Context.MODE_PRIVATE);
        initViews();

        movieId = getIntent().getIntExtra("movie_id", -1);
        setupInitialData();

        if (movieId != -1) {
            fetchFullMovieDetails(movieId);
            fetchMovieTrailer(movieId);
        }
    }

    private void initViews() {
        ivBackdrop = findViewById(R.id.iv_backdrop);
        ivPoster = findViewById(R.id.iv_details_poster);
        btnFavorite = findViewById(R.id.btn_favorite);
        tvTitle = findViewById(R.id.tv_details_title);
        tvDateRuntime = findViewById(R.id.tv_details_date_runtime);
        tvRating = findViewById(R.id.tv_details_rating);
        tvOverview = findViewById(R.id.tv_details_overview);
        tvBudget = findViewById(R.id.tv_budget);
        tvRevenue = findViewById(R.id.tv_revenue);
        tvCountry = findViewById(R.id.tv_country);
        rbUserRating = findViewById(R.id.rb_user_rating);
        tvUserRatingStatus = findViewById(R.id.tv_user_rating_status);

        youtubePlayerView = findViewById(R.id.youtube_player_view);
        cvTrailer = findViewById(R.id.cv_trailer);
        tvLabelTrailer = findViewById(R.id.tv_label_trailer);

        // Crucial: Register the lifecycle observer
        getLifecycle().addObserver(youtubePlayerView);

        // Set up the listener immediately so it's ready when the API returns
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                readyYouTubePlayer = youTubePlayer;
                // If the key was found while the player was still loading, play it now
                if (videoKeyToPlay != null) {
                    readyYouTubePlayer.cueVideo(videoKeyToPlay, 0f);
                }
            }
        });

        rvCast = findViewById(R.id.rv_cast);
        rvCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void fetchMovieTrailer(int id) {
        RetrofitClient.getService().getMovieVideos(id, RetrofitClient.API_KEY).enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Video video : response.body().getResults()) {
                        if (video.getSite().equalsIgnoreCase("YouTube") &&
                                (video.getType().equalsIgnoreCase("Trailer") || video.getType().equalsIgnoreCase("Teaser"))) {

                            videoKeyToPlay = video.getKey();
                            Log.d("DEBUG_VIDEO", "Video Key Found: " + videoKeyToPlay);

                            // If player is already ready, play it. Otherwise, onReady will handle it.
                            if (readyYouTubePlayer != null) {
                                readyYouTubePlayer.cueVideo(videoKeyToPlay, 0f);
                            }
                            return;
                        }
                    }
                    hideTrailer();
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                hideTrailer();
            }
        });
    }

    private void hideTrailer() {
        cvTrailer.setVisibility(View.GONE);
        tvLabelTrailer.setVisibility(View.GONE);
    }

    private void setupInitialData() {
        String title = getIntent().getStringExtra("movie_title");
        String posterUrl = getIntent().getStringExtra("movie_poster");
        String backdropUrl = getIntent().getStringExtra("movie_backdrop");
        double rating = getIntent().getDoubleExtra("movie_rating", 0.0);

        tvTitle.setText(title);
        tvRating.setText("★ " + String.format("%.1f", rating));
        tvDateRuntime.setText(getIntent().getStringExtra("movie_date"));

        Glide.with(this).load(posterUrl).into(ivPoster);
        Glide.with(this).load(backdropUrl).into(ivBackdrop);

        isFavorite = sharedPreferences.contains("fav_id_" + movieId);
        updateStarUI();
        loadUserRating();

        btnFavorite.setOnClickListener(v -> toggleFavorite(title, posterUrl, backdropUrl));
        rbUserRating.setOnRatingBarChangeListener((rb, val, fromUser) -> { if(fromUser) saveUserRating(val); });
    }

    private void toggleFavorite(String title, String poster, String backdrop) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFavorite) {
            editor.remove("fav_id_" + movieId);
            isFavorite = false;
        } else {
            editor.putInt("fav_id_" + movieId, movieId);
            editor.putString("fav_title_" + movieId, title);
            editor.putString("fav_poster_" + movieId, poster);
            editor.putString("fav_backdrop_" + movieId, backdrop);
            isFavorite = true;
        }
        editor.apply();
        updateStarUI();
    }

    private void saveUserRating(float rating) {
        sharedPreferences.edit().putFloat("user_rating_" + movieId, rating).apply();
        updateRatingUI(rating);
    }

    private void loadUserRating() {
        float rating = sharedPreferences.getFloat("user_rating_" + movieId, 0.0f);
        rbUserRating.setRating(rating);
        updateRatingUI(rating);
    }

    private void updateRatingUI(float rating) {
        if (rating > 0) {
            tvUserRatingStatus.setText("Your rating: " + rating);
            tvUserRatingStatus.setTextColor(Color.parseColor("#FFC107"));
        } else {
            tvUserRatingStatus.setText("Rate this movie");
            tvUserRatingStatus.setTextColor(Color.parseColor("#9BA3AF"));
        }
    }

    private void updateStarUI() {
        btnFavorite.setColorFilter(isFavorite ? Color.parseColor("#FFC107") : Color.WHITE);
    }

    private void fetchFullMovieDetails(int id) {
        RetrofitClient.getService().getMovieDetails(id, RetrofitClient.API_KEY, "credits").enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Movie m = response.body();
                    tvOverview.setText(m.getOverview());
                    tvBudget.setText(m.getFormattedBudget());
                    tvRevenue.setText(m.getFormattedRevenue());
                    tvCountry.setText(m.getProductionCountry());
                    if (m.getCredits() != null) rvCast.setAdapter(new CastAdapter(m.getCredits().getCast()));
                }
            }
            @Override public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("DEBUG_API", "Error: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youtubePlayerView.release(); // Clean up memory
    }
}