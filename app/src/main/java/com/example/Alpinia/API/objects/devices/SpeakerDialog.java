package com.example.Alpinia.API.objects.devices;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class SpeakerDialog extends AppCompatActivity {
    String deviceId;
    String deviceName;
    SpeakerState state;
    ApiClient api;

    SeekBar volumeBar;
    SeekBar progressBar;
    ImageView previousButton;
    ImageView nextButton;
    ImageView playButton;
    TextView songDetails;
    TextView speakerName;
    Switch onOffSwitch;

    ProgressThread progressThread;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker_card);
        api = ApiClient.getInstance();
        volumeBar = (SeekBar) findViewById(R.id.volume_bar);
        volumeBar.setRight(10);
        volumeBar.setLeft(0);

        progressBar = (SeekBar) findViewById(R.id.progress_bar);

        onOffSwitch = (Switch) findViewById(R.id.onoff_switch);
        playButton = (ImageView) findViewById(R.id.play_button);
        nextButton = (ImageView) findViewById(R.id.next_button);
        previousButton = (ImageView) findViewById(R.id.prev_button);
        songDetails = (TextView) findViewById(R.id.song_details);
        speakerName = (TextView) findViewById(R.id.speaker_name);

        deviceId = getIntent().getStringExtra("deviceId");
        speakerName.setText( getIntent().getStringExtra("deviceName"));



        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setVolume((Integer) progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ;

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSong();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.isPaused()){
                    resume();
                }
                else if(state.isPlaying()){
                    pause();
                }
            }
        });
        onOffSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.isStopped()){
                    toggleButtons(true);
                    play();
                }
                else{
                    toggleButtons(false);
                    stop();
                }
            }
        });




        updateState();
    }


    public void setVolume(int vol){
        api.setSpeakerVol(deviceId,vol, new Callback<Result<Integer>>() {
            @Override
            public void onResponse(Call<Result<Integer>> call, Response<Result<Integer>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Integer>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void nextSong(){
        api.nextSong(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void prevSong(){
        api.previousSong(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void pause(){
        api.pause(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void play(){
        api.play(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void stop(){
        api.stop(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void resume(){
        api.resume(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                    progressThread = new ProgressThread(getSeconds(state.getSong().getDuration())-getSeconds(state.getSong().getProgress()));
                    progressThread.start();
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void updateState(){
        api.getSpeakerState(deviceId, new Callback<Result<SpeakerState>>() {
            @Override
            public void onResponse(Call<Result<SpeakerState>> call, Response<Result<SpeakerState>> response) {
                if(response.isSuccessful()){
                    Result<SpeakerState> result = response.body();
                    state = result.getResult();

                    onOffSwitch.setChecked(!state.isStopped());
                    if(state.getSong() != null) {
                        songDetails.setText(state.getSong().getTitle() + " by " + state.getSong().getArtist());
                        progressBar.setLeft(0);
                        progressBar.setRight(getSeconds(state.getSong().getDuration()));
                        progressBar.setProgress(getSeconds(state.getSong().getProgress()));
                    }
                    else{
                        progressBar.setProgress(0);
                    }

                    volumeBar.setProgress(state.getVolume());

                    if(state.isPaused()){
                        playButton.setImageResource(R.drawable.ic_play_sound);
                    }
                    else if(state.isPlaying()){
                        //cambiar de imagen acá
                        playButton.setImageResource(R.drawable.ic_reloj);
                    }


                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<SpeakerState>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });

    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "App";
        Log.e(LOG_TAG, t.toString());
    }

    public Integer getSeconds(String time){
        String[] h1 = time.split(":");
        int minute = Integer.parseInt(h1[0]);
        int second = Integer.parseInt(h1[1]);
        return (second + 60*minute);
    }

    private void toggleButtons(boolean enabled) {
        volumeBar.setEnabled(enabled);
        progressBar.setEnabled(enabled);
        playButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
        previousButton.setEnabled(enabled);
        if(!enabled){
            songDetails.setText("");
        }
    }

    public class ProgressThread extends Thread{
        int seconds;
        ProgressThread(int secs){
            seconds = secs;
        }
        @Override
        public void run(){
            for(int i = 0; i<seconds; i++){
                try {
                    Thread.sleep(1000);
                    updateState();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
