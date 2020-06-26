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

    @SerializedName("song")
    private Song song;

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



    public boolean isPlaying(){
        return status.equals("playing");
    }
    public boolean isStopped(){
        return status.equals("stopped");
    }
    public boolean isPaused(){
        return status.equals("paused");
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
