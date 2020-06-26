package com.example.Alpinia;


import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.devices.DoorDialog;
import com.example.Alpinia.API.objects.devices.Lamp;

import java.util.List;
import java.util.Map;

public class DeviceAdapter  extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    Context context;
    List<Device> devices;

    public DeviceAdapter(Context ct, List<Device> devicesList){
        context = ct;
        devices = devicesList;
    }

    @NonNull
    @Override
    public DeviceAdapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_device,parent,false);
        return new DeviceAdapter.DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.DeviceViewHolder holder, int position) {
        holder.name.setText(devices.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(devices.get(position).getType().getId().equals( "lsf78ly0eqrjbz91")){
                    Intent intent = new Intent(context, DoorDialog.class);
                    intent.putExtra("deviceId",devices.get(position).getId());
                    intent.putExtra("deviceName",devices.get(position).getName());
                    context.startActivity(intent);
                }
                else
                    System.out.println("NO ES UNA PUERTA");
                /*
                Acá vamos a tener que ver de que tipo de device estamos hablando.
                Intent intent = new Intent(context, XXXXX.class);
                intent.putExtra("deviceId",devices.get(position).getId());
                context.startActivity(intent);

                */
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView deviceIcon;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            //Mas complejo para que ponga la foto del device.

            //Mapa para definir imagen que se ve. Hay que buscar todas las imagenes y meterlas en drawable.
            deviceIcon = itemView.findViewById(R.id.deviceImg);
            name = itemView.findViewById(R.id.tvDeviceName);
        }
    }
}
