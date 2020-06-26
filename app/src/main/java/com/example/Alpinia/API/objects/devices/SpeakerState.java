package com.example.Alpinia.API.objects.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeakerState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("volume")
    @Expose
    private Integer volume;
    @SerializedName("genre")
    @Expose
    private String genre;

    List<Song> playlist;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Song> playlist) {
        this.playlist = playlist;
    }
}
