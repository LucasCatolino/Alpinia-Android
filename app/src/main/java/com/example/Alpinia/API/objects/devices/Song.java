package com.example.Alpinia.API.objects.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("artist")
    @Expose
    String artist;

    @SerializedName("album")
    @Expose
    String album;

    @SerializedName("duration")
    @Expose
    String duration;

    @SerializedName("progress")
    String progress;

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public String getProgress() {
        return progress;
    }
}
