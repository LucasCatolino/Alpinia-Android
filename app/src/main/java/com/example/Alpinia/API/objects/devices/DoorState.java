package com.example.Alpinia.API.objects.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoorState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("lock")
    @Expose
    private String lock;

    public boolean isLocked() {
        return lock.equals("locked");
    }

    public boolean isOpen() {
        return status.equals("opened");
    }


}
