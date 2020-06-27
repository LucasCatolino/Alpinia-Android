package com.example.Alpinia.API.objects.devices;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacuumState {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("batteryLevel")
    @Expose
    private Integer batteryLevel;
    @SerializedName("location")
    @Expose
    private Object location;

    public boolean isFunctioning(){
        return status.equals("active");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return
                "Vacuum Status: " + status + '\n' +
                "Vacuum Mode: " + mode + '\n' +
                        "Vacuum Battery: " + batteryLevel + '\n';


    }
}