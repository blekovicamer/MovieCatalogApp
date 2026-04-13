package com.example.moviecatalogapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ActorDetails {
    @SerializedName("name") private String name;
    @SerializedName("biography") private String biography;
    @SerializedName("birthday") private String birthday;
    @SerializedName("place_of_birth") private String placeOfBirth;
    @SerializedName("profile_path") private String profilePath;
    @SerializedName("movie_credits") private MovieCredits movieCredits;

    public String getName() { return name != null ? name : "Unknown"; }
    public String getBiography() { return biography != null && !biography.isEmpty() ? biography : "No biography available."; }
    public String getBirthday() { return birthday != null ? birthday : "N/A"; }
    public String getPlaceOfBirth() { return placeOfBirth != null ? placeOfBirth : "N/A"; }

    public String getProfileUrl() {
        return "https://image.tmdb.org/t/p/w500" + profilePath;
    }

    // FIX: Added a null check so the app doesn't crash if an actor has no movies listed
    public List<Movie> getMovies() {
        return (movieCredits != null) ? movieCredits.cast : null;
    }

    public static class MovieCredits {
        @SerializedName("cast") public List<Movie> cast;
    }
}