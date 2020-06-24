package com.example.Alpinia;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Door extends Device {

    @SerializedName("state")
    @Expose
    private DoorState state;

    public Door(String name, DeviceType type) {
        this.name = name;
        this.type = type;
    }
}
