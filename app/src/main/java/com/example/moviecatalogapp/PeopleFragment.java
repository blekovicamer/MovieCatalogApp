package com.example.moviecatalogapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviecatalogapp.adapter.PeopleAdapter;
import com.example.moviecatalogapp.model.PersonResponse;
import com.example.moviecatalogapp.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleFragment extends Fragment {

    private RecyclerView rvPeople;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        rvPeople = view.findViewById(R.id.rv_people);
        // Setting a grid with 3 columns as seen in your screenshot
        rvPeople.setLayoutManager(new GridLayoutManager(getContext(), 3));

        fetchPopularPeople();
        return view;
    }

    private void fetchPopularPeople() {
        RetrofitClient.getService().getPopularPeople(RetrofitClient.API_KEY, 1).enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PeopleAdapter adapter = new PeopleAdapter(response.body().getResults(), getContext());
                    rvPeople.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) { /* Log error */ }
        });
    }
}