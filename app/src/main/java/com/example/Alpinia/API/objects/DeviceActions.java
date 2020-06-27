package com.example.Alpinia.API.objects;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DeviceActions {

    @SerializedName("device")
    @Expose
    private Device device;

    @SerializedName("actionName")
    @Expose
    private String actionName;

    @SerializedName("params")
    @Expose
    private List<String> params = new ArrayList<>();

    @SerializedName("meta")
    @Expose
    private Object meta = new Object();

    public Device getDevice() {
        return device;
    }

    public String getActionName() {
        return actionName;
    }

    @Override
    public String toString() {
        return actionName + " " + device.getName();
    }
}