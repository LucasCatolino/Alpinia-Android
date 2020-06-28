package com.example.Alpinia.API.objects.devices;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.DeviceType;

public class Vacuum extends Device {
    public Vacuum(String name){
        this.name = name;
        this.type = new DeviceType("ofglvd9gqx8yfl3l");
    }
}
