package com.example.moviecatalogapp.model;

import com.google.gson.annotations.SerializedName;

public class CastMember {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("character")
    private String character;

    @SerializedName("profile_path")
    private String profilePath;

    public String getName() { return name; }
    public String getCharacter() { return character; }
    public String getProfileUrl() {
        return "https://image.tmdb.org/t/p/w185" + profilePath;
    }
}