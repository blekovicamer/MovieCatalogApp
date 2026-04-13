package com.example.moviecatalogapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviecatalogapp.ActorDetailsActivity;
import com.example.moviecatalogapp.R;
import com.example.moviecatalogapp.model.Person;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private List<Person> personList;
    private Context context;

    public PeopleAdapter(List<Person> personList, Context context) {
        this.personList = personList;
        this.context = context;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_person layout we designed earlier
        View view = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        Person person = personList.get(position);

        holder.tvName.setText(person.getName());
        holder.tvKnownFor.setText(person.getKnownForSummary());

        Glide.with(context)
                .load(person.getProfilePath()) // This now uses the full URL from the model
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(holder.ivProfile);

        // This reuses your working ActorDetailsActivity!
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActorDetailsActivity.class);
            intent.putExtra("person_id", person.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return personList != null ? personList.size() : 0;
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvName, tvKnownFor;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_person_image);
            tvName = itemView.findViewById(R.id.tv_person_name);
            tvKnownFor = itemView.findViewById(R.id.tv_person_works);
        }
    }
}