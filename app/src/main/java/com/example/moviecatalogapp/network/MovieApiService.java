package com.example.moviecatalogapp.network;

// ADD THIS LINE TO FIX THE ERROR
import com.example.moviecatalogapp.model.ActorDetails;
import com.example.moviecatalogapp.model.Movie;
import com.example.moviecatalogapp.model.MovieResponse;
import com.example.moviecatalogapp.model.PersonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import com.example.moviecatalogapp.model.VideoResponse;

public interface MovieApiService {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("trending/movie/day")
    Call<MovieResponse> getTrendingMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String append
    );

    @GET("person/{person_id}")
    Call<ActorDetails> getActorDetails(
            @Path("person_id") int personId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String append
    );

    @GET("person/popular")
    Call<PersonResponse> getPopularPeople(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<MovieResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getMovieVideos(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);
}