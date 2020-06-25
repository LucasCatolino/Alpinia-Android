package com.example.Alpinia.API.objects.devices;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.DeviceType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Refrigerator extends Device {

    @SerializedName("state")
    @Expose
    private RefrigeratorState state;

    public Refrigerator(String name, DeviceType type) {
        this.name = name;
        this.type = type;
    }
}

