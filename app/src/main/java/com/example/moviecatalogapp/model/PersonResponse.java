package com.example.moviecatalogapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PersonResponse {
    @SerializedName("results")
    private List<Person> results;

    public List<Person> getResults() {
        return results;
    }
}