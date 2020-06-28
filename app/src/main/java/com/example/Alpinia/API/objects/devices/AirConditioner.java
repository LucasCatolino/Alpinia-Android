package com.example.Alpinia.API.objects.devices;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.DeviceType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AirConditioner extends Device {

    @SerializedName("state")
    @Expose
    private AirConditionerState state;

    public AirConditioner(String name) {
        this.name = name;
        this.type = new DeviceType("li6cbv5sdlatti0j");
    }
}