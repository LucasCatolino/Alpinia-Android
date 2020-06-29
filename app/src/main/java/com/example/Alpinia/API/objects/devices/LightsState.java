package com.example.Alpinia.API.objects.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LightsState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("brightness")
    @Expose
    private int intensity;

    public String getStatus() {
        return status;
    }

    public String getColor() {
        return color;
    }

    public int getIntensity() {
        return intensity;
    }

    public boolean isOn() {
        return status.equals("on");
    }

    public boolean isOff() {
        return status.equals("off");
    }
}
