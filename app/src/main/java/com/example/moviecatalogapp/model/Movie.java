package com.example.moviecatalogapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List; // FIX: This solves the "cannot find symbol class List" error

public class Movie {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("release_date")
    private String releaseDate;

    // New fields for the "About movie" section
    @SerializedName("budget")
    private long budget;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("production_countries")
    private List<ProductionCountry> productionCountries;

    // New field for the Cast section
    @SerializedName("credits")
    private Credits credits;

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public double getVoteAverage() { return voteAverage; }
    public Credits getCredits() { return credits; }

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public String getBackdropPath() {
        return "https://image.tmdb.org/t/p/w780" + backdropPath;
    }

    public String getReleaseYear() {
        if (releaseDate != null && releaseDate.length() >= 4) {
            return releaseDate.substring(0, 4);
        }
        return "N/A";
    }

    // Formatting methods for Budget and Revenue
    public String getFormattedBudget() {
        return budget == 0 ? "N/A" : "$" + String.format("%,d", budget);
    }

    public String getFormattedRevenue() {
        return revenue == 0 ? "N/A" : "$" + String.format("%,d", revenue);
    }

    public String getProductionCountry() {
        if (productionCountries != null && !productionCountries.isEmpty()) {
            return productionCountries.get(0).getName();
        }
        return "N/A";
    }

    // --- Inner Classes for Nested Data ---

    public static class ProductionCountry {
        @SerializedName("name")
        private String name;
        public String getName() { return name; }
    }

    public static class Credits {
        @SerializedName("cast")
        private List<CastMember> cast; // Ensure CastMember.java exists in your model folder
        public List<CastMember> getCast() { return cast; }
    }
}