package com.example.Alpinia.ui.rooms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.API.objects.Room;
import com.example.Alpinia.DeviceActivity;
import com.example.Alpinia.DeviceAdapter;
import com.example.Alpinia.HomesAdapter;
import com.example.Alpinia.MainActivity;
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
                    strBuilder.append("Devices: " + result.getResult().size());
                    holder.devices.setText(strBuilder.toString());
                }
            }

            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeviceActivity.class);
                intent.putExtra("roomId",rooms.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(context.getResources().getString(R.string.alertwarn)).setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ApiClient.getInstance().deleteRoom(rooms.get(position).getId(), new Callback<Result<Boolean>>() {
                                    @Override
                                    public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                                        if(response.isSuccessful())
                                        {
                                            context.startActivity(new Intent(context, context.getClass()));
                                        }
                                        else{
                                            handleError(response);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                                        handleUnexpectedError(t);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog myAlert = alert.create();
                alert.setTitle("Delete item");
                alert.show();

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
        TextView devices;
        ImageView trash;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomIcon = itemView.findViewById(R.id.roomImg);
            name = itemView.findViewById(R.id.tvRoomName);
            devices = itemView.findViewById(R.id.device_amount);
            trash = itemView.findViewById(R.id.go_to_rooms);
        }
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = context.getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "App";
        Log.e(LOG_TAG, t.toString());
    }
}
