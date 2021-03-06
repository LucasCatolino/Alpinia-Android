package com.example.Alpinia.API.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Room {
    @SerializedName("home")
    @Expose
    private Home home;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("meta")
    @Expose
    private RoomMeta meta;

    @SerializedName("devices")
    @Expose
    private List<Device> deviceList;



    public Room(String name, RoomMeta meta) {
        this.name = name;
        this.meta = meta;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomMeta getMeta() {
        return meta;
    }

    public void setMeta(RoomMeta meta) {
        this.meta = meta;
    }



    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
    }
}