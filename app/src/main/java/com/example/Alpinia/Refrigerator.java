package com.example.Alpinia;

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

