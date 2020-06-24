package com.example.Alpinia;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lights extends Device {

    @SerializedName("state")
    @Expose
    private LightsState state;

    public Lights(String name, DeviceType type) {
        this.name = name;
        this.type = type;
    }
}