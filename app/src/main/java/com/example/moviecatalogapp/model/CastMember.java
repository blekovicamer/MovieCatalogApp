package com.example.moviecatalogapp.model;

import com.google.gson.annotations.SerializedName;

public class CastMember {

    // 1. Add the ID field
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("character")
    private String character;

    @SerializedName("profile_path")
    private String profilePath;

    // 2. Add this getter method (This fixes the "cannot find symbol" error)
    public int getId() {
        return id;
    }

    public String getName() { return name; }
    public String getCharacter() { return character; }
    public String getProfileUrl() {
        return "https://image.tmdb.org/t/p/w185" + profilePath;
    }
}