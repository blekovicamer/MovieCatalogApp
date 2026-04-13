package com.example.moviecatalogapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviecatalogapp.adapter.MovieAdapter;
import com.example.moviecatalogapp.model.ActorDetails;
import com.example.moviecatalogapp.network.MovieApiService;
import com.example.moviecatalogapp.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorDetailsActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private TextView tvName, tvBio, tvBirthday, tvPlace;
    private RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_details);

        ivProfile = findViewById(R.id.iv_actor_profile);
        tvName = findViewById(R.id.tv_actor_name);
        tvBio = findViewById(R.id.tv_actor_bio);
        tvBirthday = findViewById(R.id.tv_actor_birthday);
        tvPlace = findViewById(R.id.tv_actor_place);
        rvMovies = findViewById(R.id.rv_known_for);
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        int personId = getIntent().getIntExtra("person_id", -1);
        if (personId != -1) {
            fetchActorData(personId);
        }
    }

    private void fetchActorData(int id) {
        MovieApiService apiService = RetrofitClient.getService();
        apiService.getActorDetails(id, RetrofitClient.API_KEY, "movie_credits").enqueue(new Callback<ActorDetails>() {
            @Override
            public void onResponse(Call<ActorDetails> call, Response<ActorDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ActorDetails actor = response.body();
                    tvName.setText(actor.getName());
                    tvBio.setText(actor.getBiography());
                    tvBirthday.setText("Birthday: " + actor.getBirthday());
                    tvPlace.setText(actor.getPlaceOfBirth());

                    Glide.with(ActorDetailsActivity.this).load(actor.getProfileUrl()).into(ivProfile);

                    // Reusing your existing MovieAdapter for the "Known For" list!
                    MovieAdapter adapter = new MovieAdapter(actor.getMovies(), ActorDetailsActivity.this, false);
                    rvMovies.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ActorDetails> call, Throwable t) { /* Log error */ }
        });
    }
}