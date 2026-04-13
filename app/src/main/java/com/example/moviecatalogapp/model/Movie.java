package com.example.moviecatalogapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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

    @SerializedName("budget")
    private long budget;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("production_countries")
    private List<ProductionCountry> productionCountries;

    @SerializedName("credits")
    private Credits credits;

    // --- GETTERS ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public double getVoteAverage() { return voteAverage; }
    public Credits getCredits() { return credits; }

    public String getPosterPath() {
        // This check prevents double-prefixing the URL if it's already saved in full
        if (posterPath != null && posterPath.startsWith("http")) {
            return posterPath;
        }
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public String getBackdropPath() {
        // This check prevents double-prefixing the URL for favorites
        if (backdropPath != null && backdropPath.startsWith("http")) {
            return backdropPath;
        }
        return "https://image.tmdb.org/t/p/w780" + backdropPath;
    }

    public String getReleaseYear() {
        // Check if the date is null or empty first
        if (releaseDate == null || releaseDate.isEmpty()) {
            return "N/A";
        }
        // If it's a full date like 2023-10-12, take the first 4 digits
        if (releaseDate.length() >= 4) {
            return releaseDate.substring(0, 4);
        }
        return releaseDate;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

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

    // --- SETTERS (REQUIRED FOR FAVORITES LOGIC) ---
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    // --- INNER CLASSES ---
    public static class ProductionCountry {
        @SerializedName("name")
        private String name;
        public String getName() { return name; }
    }

    public static class Credits {
        @SerializedName("cast")
        private List<CastMember> cast;
        public List<CastMember> getCast() { return cast; }
    }
}