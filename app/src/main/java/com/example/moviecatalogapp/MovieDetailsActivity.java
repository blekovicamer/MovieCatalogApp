package com.example.moviecatalogapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviecatalogapp.adapter.CastAdapter;
import com.example.moviecatalogapp.model.Movie;
import com.example.moviecatalogapp.network.MovieApiService;
import com.example.moviecatalogapp.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView ivBackdrop, ivPoster;
    private TextView tvTitle, tvDateRuntime, tvRating, tvOverview;
    private TextView tvBudget, tvRevenue, tvCountry;
    private RecyclerView rvCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // 1. Initialize Basic Views
        ivBackdrop = findViewById(R.id.iv_backdrop);
        ivPoster = findViewById(R.id.iv_details_poster);
        tvTitle = findViewById(R.id.tv_details_title);
        tvDateRuntime = findViewById(R.id.tv_details_date_runtime);
        tvRating = findViewById(R.id.tv_details_rating);
        tvOverview = findViewById(R.id.tv_details_overview);

        // 2. Initialize "About Movie" Views
        tvBudget = findViewById(R.id.tv_budget);
        tvRevenue = findViewById(R.id.tv_revenue);
        tvCountry = findViewById(R.id.tv_country);

        // 3. Setup Cast RecyclerView
        rvCast = findViewById(R.id.rv_cast);
        rvCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // 4. Get Initial Data from Intent
        int movieId = getIntent().getIntExtra("movie_id", -1);
        String title = getIntent().getStringExtra("movie_title");
        String overview = getIntent().getStringExtra("movie_overview");
        String posterUrl = getIntent().getStringExtra("movie_poster");
        String backdropUrl = getIntent().getStringExtra("movie_backdrop");

        // FIX: Getting the missing Rating and Year strings
        String releaseYear = getIntent().getStringExtra("movie_date");
        double rating = getIntent().getDoubleExtra("movie_rating", 0.0);

        // Set Data to UI
        tvTitle.setText(title);
        tvOverview.setText(overview);

        // FIX: Setting the missing text (This fixes the "yellow block" issue)
        tvDateRuntime.setText(releaseYear);
        tvRating.setText("★ " + String.format("%.1f", rating));

        Glide.with(this).load(posterUrl).into(ivPoster);
        Glide.with(this).load(backdropUrl).into(ivBackdrop);

        // 5. Fetch Full Details (Budget, Revenue, Cast)
        if (movieId != -1) {
            fetchFullMovieDetails(movieId);
        }
    }

    private void fetchFullMovieDetails(int movieId) {
        MovieApiService apiService = RetrofitClient.getService();

        // Using "credits" to get the cast list in the same call
        apiService.getMovieDetails(movieId, RetrofitClient.API_KEY, "credits").enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Movie movie = response.body();

                    // Set the budget, revenue, and country data
                    tvBudget.setText(movie.getFormattedBudget());
                    tvRevenue.setText(movie.getFormattedRevenue());
                    tvCountry.setText(movie.getProductionCountry());

                    if (movie.getCredits() != null && movie.getCredits().getCast() != null) {
                        CastAdapter castAdapter = new CastAdapter(movie.getCredits().getCast());
                        rvCast.setAdapter(castAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("API_ERROR", "Could not fetch details: " + t.getMessage());
            }
        });
    }
}