package com.example.Alpinia;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class HomesAdapter extends RecyclerView.Adapter<HomesAdapter.HomeViewHolder>{
    Context context;
    List<Home> homes;

    public HomesAdapter(Context ct, List<Home> homeList){
        context = ct;
        homes = homeList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.name.setText(homes.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("homeId",homes.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homes.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView homeIcon;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            homeIcon = itemView.findViewById(R.id.homeImg);
            name = itemView.findViewById(R.id.tvName);
        }
    }

}
