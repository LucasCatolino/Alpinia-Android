package com.example.Alpinia.API.objects.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
    @SerializedName("song")
    @Expose
    String song;

    @SerializedName("artist")
    @Expose
    String artist;

    @SerializedName("album")
    @Expose
    String album;

    @SerializedName("duration")
    @Expose
    String duration;

}
