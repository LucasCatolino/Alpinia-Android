package com.example.Alpinia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lamp extends Device {
    final static private String ID = "go46xmbqeomjrsjr";

    @SerializedName("state")
    @Expose
    LampState state;

    public Lamp(String name) {
        super(name, new DeviceType(ID));
    }


    public LampState getState() {
        return state;
    }

    public void setState(LampState state) {
        this.state = state;
    }
}
