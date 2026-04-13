package com.example.moviecatalogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviecatalogapp.adapter.MovieAdapter;
import com.example.moviecatalogapp.adapter.PeopleAdapter;
import com.example.moviecatalogapp.model.Movie;
import com.example.moviecatalogapp.model.MovieResponse;
import com.example.moviecatalogapp.model.PersonResponse;
import com.example.moviecatalogapp.network.RetrofitClient;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NestedScrollView homeLayout;
    private RecyclerView rvMovies, rvTrending, rvTopRated, rvPeopleGrid, rvFavorites;
    private MovieAdapter moviesAdapter, trendingAdapter, topRatedAdapter, favAdapter;
    private PeopleAdapter peopleAdapter;
    private boolean isPeopleLoaded = false;
    private View navBlurBackground;

    // *** NEW: References for the two backdrops ***
    private ImageView imgBackgroundBlur, imgWelcomeBackdrop;

    private List<Movie> favoriteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        homeLayout = findViewById(R.id.home_layout);
        rvMovies = findViewById(R.id.rv_movies);
        rvTrending = findViewById(R.id.rv_trending);
        rvTopRated = findViewById(R.id.rv_top_rated);
        rvPeopleGrid = findViewById(R.id.rv_people_grid);
        rvFavorites = findViewById(R.id.rv_favorites);
        navBlurBackground = findViewById(R.id.nav_blur_background);

        // *** NEW: Initialize backdrop references ***
        imgBackgroundBlur = findViewById(R.id.img_background_blur);
        imgWelcomeBackdrop = findViewById(R.id.img_welcome_backdrop);

        // Set Layout Managers
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTopRated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPeopleGrid.setLayoutManager(new GridLayoutManager(this, 3));
        rvFavorites.setLayoutManager(new GridLayoutManager(this, 3));

        applyNavBlur();

        // Load Data
        fetchFreeToWatch();
        fetchTrending();
        fetchTopRated();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            homeLayout.setVisibility(View.GONE);
            rvPeopleGrid.setVisibility(View.GONE);
            rvFavorites.setVisibility(View.GONE);

            if (id == R.id.nav_home) {
                homeLayout.setVisibility(View.VISIBLE);
                return true;
            } else if (id == R.id.nav_people) {
                rvPeopleGrid.setVisibility(View.VISIBLE);
                if (!isPeopleLoaded) fetchPeople();
                return true;
            } else if (id == R.id.nav_star) {
                rvFavorites.setVisibility(View.VISIBLE);
                loadFavorites();
                return true;
            } else if (id == R.id.nav_search) {
                homeLayout.setVisibility(View.VISIBLE);
                startActivity(new Intent(this, SearchActivity.class));
                return false;
            }
            return false;
        });
    }

    // *** UPDATED METHOD: Loads Avatar into TWO places ***
    private void updateBackdrops(String backdropPath) {
        if (backdropPath == null || backdropPath.isEmpty()) return;
        String url = "https://image.tmdb.org/t/p/w1280" + backdropPath;

        // Load into the Static Bottom Background
        Glide.with(this).load(url).into(imgBackgroundBlur);

        // Load into the Welcome Card Backdrop
        Glide.with(this).load(url).into(imgWelcomeBackdrop);
    }

    private void fetchFreeToWatch() {
        RetrofitClient.getService().getPopularMovies(RetrofitClient.API_KEY, 1).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    moviesAdapter = new MovieAdapter(movies, MainActivity.this, false);
                    rvMovies.setAdapter(moviesAdapter);

                    // Look for 'Avatar: Fire and Ash' (currently movie index 1 from your screenshot)
                    if (movies.size() > 1) {
                        // Assuming Avatar is the second movie (index 1) from the API.
                        updateBackdrops(movies.get(1).getBackdropPath());
                    } else if (!movies.isEmpty()) {
                        // Fallback: If only one movie, use that one.
                        updateBackdrops(movies.get(0).getBackdropPath());
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) { Log.e("API", t.getMessage()); }
        });
    }

    private void fetchTrending() {
        RetrofitClient.getService().getTrendingMovies(RetrofitClient.API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    trendingAdapter = new MovieAdapter(response.body().getMovies(), MainActivity.this, false);
                    rvTrending.setAdapter(trendingAdapter);
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) { Log.e("API", t.getMessage()); }
        });
    }

    private void fetchTopRated() {
        RetrofitClient.getService().getTopRatedMovies(RetrofitClient.API_KEY, 1).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topRatedAdapter = new MovieAdapter(response.body().getMovies(), MainActivity.this, false);
                    rvTopRated.setAdapter(topRatedAdapter);
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) { Log.e("API", t.getMessage()); }
        });
    }

    private void loadFavorites() {
        SharedPreferences prefs = getSharedPreferences("MoviesApp", MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        favoriteList.clear();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("fav_id_")) {
                String id = entry.getKey().replace("fav_id_", "");
                Movie m = new Movie();
                m.setId(Integer.parseInt(id));
                m.setTitle(prefs.getString("fav_title_" + id, ""));
                m.setPosterPath(prefs.getString("fav_poster_" + id, ""));
                favoriteList.add(m);
            }
        }
        favAdapter = new MovieAdapter(favoriteList, this, true);
        rvFavorites.setAdapter(favAdapter);
    }

    private void fetchPeople() {
        RetrofitClient.getService().getPopularPeople(RetrofitClient.API_KEY, 1).enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    peopleAdapter = new PeopleAdapter(response.body().getResults(), MainActivity.this);
                    rvPeopleGrid.setAdapter(peopleAdapter);
                    isPeopleLoaded = true;
                }
            }
            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) { Log.e("API", t.getMessage()); }
        });
    }

    private void applyNavBlur() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && navBlurBackground != null) {
            navBlurBackground.setRenderEffect(RenderEffect.createBlurEffect(60f, 60f, Shader.TileMode.MIRROR));
        }
    }
}