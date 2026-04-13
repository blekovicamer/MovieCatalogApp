package com.example.moviecatalogapp.adapter;

// ADD THESE TWO IMPORTS
import android.content.Intent;
import com.example.moviecatalogapp.ActorDetailsActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviecatalogapp.R;
import com.example.moviecatalogapp.model.CastMember;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<CastMember> castList;

    public CastAdapter(List<CastMember> castList) {
        this.castList = castList;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        CastMember member = castList.get(position);
        holder.tvName.setText(member.getName());

        Glide.with(holder.itemView.getContext())
                .load(member.getProfileUrl())
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(holder.ivPhoto);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ActorDetailsActivity.class);
            intent.putExtra("person_id", member.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return castList != null ? castList.size() : 0;
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvName;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_cast_image);
            tvName = itemView.findViewById(R.id.tv_cast_name);
        }
    }
}