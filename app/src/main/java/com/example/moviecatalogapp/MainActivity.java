package com.example.moviecatalogapp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviecatalogapp.adapter.MovieAdapter;
import com.example.moviecatalogapp.model.Movie;
import com.example.moviecatalogapp.model.MovieResponse;
import com.example.moviecatalogapp.network.MovieApiService;
import com.example.moviecatalogapp.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Define both RecyclerViews
    private RecyclerView rvMovies, rvTrending;
    private MovieAdapter moviesAdapter, trendingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize "Free to Watch" List
        rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // 2. Initialize "Trending" List
        rvTrending = findViewById(R.id.rv_trending);
        rvTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Fetch data for both sections
        fetchFreeToWatch();
        fetchTrending();
    }

    private void fetchFreeToWatch() {
        MovieApiService apiService = RetrofitClient.getService();
        // Using your existing popular movies call for the first row
        Call<MovieResponse> call = apiService.getPopularMovies(RetrofitClient.API_KEY, 1);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    moviesAdapter = new MovieAdapter(movies);
                    rvMovies.setAdapter(moviesAdapter);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("MainActivity", "Free Movies Error: " + t.getMessage());
            }
        });
    }

    private void fetchTrending() {
        MovieApiService apiService = RetrofitClient.getService();
        // Make sure you added getTrendingMovies to your MovieApiService interface!
        Call<MovieResponse> call = apiService.getTrendingMovies(RetrofitClient.API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    trendingAdapter = new MovieAdapter(movies);
                    rvTrending.setAdapter(trendingAdapter);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("MainActivity", "Trending Error: " + t.getMessage());
            }
        });
    }
}