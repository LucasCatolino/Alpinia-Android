package com.example.Alpinia.API.objects;

import androidx.annotation.NonNull;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Routine {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("actions")
    @Expose
    private List<DeviceActions> actions;
    @SerializedName("meta")
    @Expose
    private Object meta = new Object();



    public Routine(String name, List<DeviceActions> actions) {
        this.name = name;
        this.actions = actions;
    }


    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<DeviceActions> getActions() {
        return actions;
    }



    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        if (this.getId() != null)
            return String.format("%s - %s", this.getId(), this.getName());
        else
            return this.getName();
    }
}