package com.example.Alpinia.API.objects.devices;

import com.example.Alpinia.DeviceState;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaucetState extends DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    public boolean isOpen() {
        return status.equals("opened");
    }

    public boolean isClosed() {
        return status.equals("closed");
    }
}
