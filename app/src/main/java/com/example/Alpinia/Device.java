package com.example.Alpinia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("id")
    @Expose
    protected String id;
    @SerializedName("name")
    @Expose
    protected String name;
    @SerializedName("type")
    @Expose
    protected DeviceType type;
    @SerializedName("state")
    @Expose
    protected State state;
    @SerializedName("meta")
    @Expose
    protected Meta meta;

    public Device(String name, DeviceType type){
        this.name = name;
        this.type = type;
        this.meta = null;
    }
    public Device(String name, DeviceType type, State state){
        this.name = name;
        this.type = type;
        this.state = state;
        this.meta = null;
    }

    public Device() {}

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

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", state=" + state +
                ", meta=" + meta +
                '}';
    }
}