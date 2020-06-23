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

public class RoomsAdapter  extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {
    Context context;
    List<Room> rooms;

    public RoomsAdapter(Context ct, List<Room> roomsList){
        context = ct;
        rooms = roomsList;
    }

    @NonNull
    @Override
    public RoomsAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_room,parent,false);
        return new RoomsAdapter.RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.RoomViewHolder holder, int position) {
        holder.name.setText(rooms.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoomActivity.class);
                intent.putExtra("roomId",rooms.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView roomIcon;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomIcon = itemView.findViewById(R.id.roomImg);
            name = itemView.findViewById(R.id.tvRoomName);
        }
    }
}
