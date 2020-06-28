package com.example.Alpinia.API.objects.devices;



import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.DeviceType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lights extends Device {

    @SerializedName("state")
    @Expose
    private LightsState state;

    public Lights(String name) {
        this.name = name;
        this.type = new DeviceType("go46xmbqeomjrsjr");
    }
}