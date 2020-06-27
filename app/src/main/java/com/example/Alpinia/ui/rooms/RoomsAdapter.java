package com.example.Alpinia.ui.rooms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.API.objects.Room;
import com.example.Alpinia.DeviceActivity;
import com.example.Alpinia.DeviceAdapter;
import com.example.Alpinia.HomesAdapter;
import com.example.Alpinia.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        StringBuilder strBuilder = new StringBuilder();

        ApiClient.getInstance().getRoomDevices(rooms.get(position).getId(), new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){
                    Result<List<Device>> result = response.body();
                    if(result.getResult() != null){
                        DeviceAdapter myAdapter = new DeviceAdapter(context,result.getResult());
                        holder.devices.setAdapter(myAdapter);
                        holder.devices.setLayoutManager(new LinearLayoutManager(context));
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

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
        RecyclerView devices;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomIcon = itemView.findViewById(R.id.roomImg);
            name = itemView.findViewById(R.id.tvRoomName);
            devices = itemView.findViewById(R.id.recyclerdevices);
        }
    }
}
