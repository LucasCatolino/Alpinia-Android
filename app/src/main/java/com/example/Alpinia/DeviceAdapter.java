package com.example.Alpinia;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.devices.AirConditionerDialog;
import com.example.Alpinia.API.objects.devices.DoorDialog;
import com.example.Alpinia.API.objects.devices.FaucetDialog;
import com.example.Alpinia.API.objects.devices.LightsDialog;
import com.example.Alpinia.API.objects.devices.RefrigeratorDialog;
import com.example.Alpinia.API.objects.devices.SpeakerDialog;
import com.example.Alpinia.API.objects.devices.VacuumDialog;

import java.util.List;

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
        final Intent[] intent = new Intent[1];
        holder.name.setText(devices.get(position).getName());
        iconManager(holder, devices.get(position).getType().getId());
        //holder.deviceIcon.setImageDrawable(iconManager(devices.get(position).getType().getId()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(devices.get(position).getType().getId().equals( "lsf78ly0eqrjbz91")){
                   intent[0] = new Intent(context, DoorDialog.class);

                }
                else if(devices.get(position).getType().getId().equals("go46xmbqeomjrsjr")){
                    intent[0] = new Intent(context, LightsDialog.class);
                }
                else if(devices.get(position).getType().getId().equals("rnizejqr2di0okho")){
                    intent[0] = new Intent(context, RefrigeratorDialog.class);

                }
                else if(devices.get(position).getType().getId().equals("c89b94e8581855bc")){
                    intent[0] = new Intent(context, SpeakerDialog.class);
                }

                else if(devices.get(position).getType().getId().equals("li6cbv5sdlatti0j")) {
                     intent[0] = new Intent(context, AirConditionerDialog.class);
                }

                else if(devices.get(position).getType().getId().equals("ofglvd9gqx8yfl3l")) {
                     intent[0] = new Intent(context, VacuumDialog.class);
                }
                else if(devices.get(position).getType().getId().equals("dbrlsh7o5sn8ur4i")) {
                    intent[0] = new Intent(context, FaucetDialog.class);
                }
                else
                    return;


                intent[0].putExtra("deviceId",devices.get(position).getId());
                intent[0].putExtra("deviceName",devices.get(position).getName());
                context.startActivity(intent[0]);

            }
        });
    }

    private void iconManager(DeviceViewHolder holder, String id) {
        switch (id){
            case "lsf78ly0eqrjbz91":
                holder.deviceIcon.setImageResource(R.drawable.ic_door_black);
                break;
            case "go46xmbqeomjrsjr":
                holder.deviceIcon.setImageResource(R.drawable.ic_light_black);
                break;
            case "rnizejqr2di0okho":
                holder.deviceIcon.setImageResource(R.drawable.ic_refrigerator);
                break;
            case "c89b94e8581855bc":
                holder.deviceIcon.setImageResource(R.drawable.ic_speaker_black);
                break;
            case "li6cbv5sdlatti0j":
                holder.deviceIcon.setImageResource(R.drawable.ic_ac_black);
                break;
            case "ofglvd9gqx8yfl3l":
                holder.deviceIcon.setImageResource(R.drawable.ic_vaccum_black);
                break;
            case "dbrlsh7o5sn8ur4i":
                holder.deviceIcon.setImageResource(R.drawable.ic_faucet_black);
                break;
            default: holder.deviceIcon.setImageResource(R.drawable.ic_default_device_black);
        }
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
