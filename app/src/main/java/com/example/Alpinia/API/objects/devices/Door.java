package com.example.Alpinia.API.objects.devices;


import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.DeviceType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Door extends Device {

    @SerializedName("state")
    @Expose
    private DoorState state;

    public Door(String name) {
        this.name = name;
        this.type = new DeviceType("lsf78ly0eqrjbz91");
    }
}
