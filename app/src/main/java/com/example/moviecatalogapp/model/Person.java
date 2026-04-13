package com.example.moviecatalogapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class Person {
    @SerializedName("id") private int id;
    @SerializedName("name") private String name;
    @SerializedName("profile_path") private String profilePath;
    @SerializedName("known_for") private List<Movie> knownFor;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getProfilePath() { return "https://image.tmdb.org/t/p/w500" + profilePath; }

    // Creates that "Rush Hour, Rush Hour 2" text string under the name
    public String getKnownForSummary() {
        if (knownFor == null || knownFor.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(knownFor.size(), 2); i++) {
            sb.append(knownFor.get(i).getTitle());
            if (i == 0 && knownFor.size() > 1) sb.append(", ");
        }
        return sb.toString();
    }
}