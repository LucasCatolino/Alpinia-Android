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


import com.example.Alpinia.API.objects.Routine;

import java.util.List;


public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>{
    Context context;
    List<Routine> routines;

    public RoutineAdapter(Context ct, List<Routine> routineList){
        context = ct;
        routines = routineList;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_routine,parent,false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        holder.name.setText(routines.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("homeId",routines.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public class RoutineViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView routineIcon;
        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            routineIcon = itemView.findViewById(R.id.roomImg);
            name = itemView.findViewById(R.id.routine_name);
        }
    }

}