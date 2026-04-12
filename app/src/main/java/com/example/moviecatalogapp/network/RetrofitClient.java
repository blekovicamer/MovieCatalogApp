package com.example.moviecatalogapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory; // Changed colon to dot
import com.example.moviecatalogapp.network.MovieApiService; // Ensure this is imported

public class RetrofitClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    // Your API Key stored here
    public static final String API_KEY = "b7cd25326d48819cbc5a4ff31232c87c";

    private static Retrofit retrofit = null;

    public static MovieApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MovieApiService.class);
    }
}