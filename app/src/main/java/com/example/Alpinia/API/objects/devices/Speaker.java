package com.example.Alpinia.API.objects.devices;

import com.example.Alpinia.API.objects.Device;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Speaker extends Device {

    @SerializedName("state")
    @Expose
    private SpeakerState state;

    public SpeakerState getState() {
        return state;
    }

    public void setState(SpeakerState state) {
        this.state = state;
    }


}
