package com.example.moviecatalogapp.network;

import com.example.moviecatalogapp.model.Movie;
import com.example.moviecatalogapp.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    // Add @Query("page") to match your MainActivity call
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    // Add this missing method for Trending
    @GET("trending/movie/day")
    Call<MovieResponse> getTrendingMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String append
    );
}