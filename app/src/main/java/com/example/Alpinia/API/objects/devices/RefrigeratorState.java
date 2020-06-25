package com.example.Alpinia.API.objects.devices;

import com.example.Alpinia.DeviceState;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefrigeratorState extends DeviceState {

    @SerializedName("freezerTemperature")
    @Expose
    private Integer freezerTemperature;

    @SerializedName("temperature")
    @Expose
    private Integer temperature;

    @SerializedName("mode")
    @Expose
    private String mode;

    public Integer getFreezerTemperature() {
        return freezerTemperature;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public String getMode() {
        return mode;
    }
}
