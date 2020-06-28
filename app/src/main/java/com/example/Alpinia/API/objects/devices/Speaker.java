package com.example.Alpinia.API.objects.devices;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.DeviceType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Speaker extends Device {

    public Speaker(String name){
        this.name = name;
        this.type = new DeviceType("c89b94e8581855bc");
    }

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
