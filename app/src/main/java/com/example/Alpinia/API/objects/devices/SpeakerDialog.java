package com.example.Alpinia.API.objects.devices;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker_card);
        api = ApiClient.getInstance();
        volumeBar = (SeekBar) findViewById(R.id.volume_bar);
        volumeBar.setRight(10);
        volumeBar.setLeft(0);

        progressBar = (SeekBar) findViewById(R.id.progress_bar);


        playButton = (ImageView) findViewById(R.id.play_button);
        nextButton = (ImageView) findViewById(R.id.next_button);
        previousButton = (ImageView) findViewById(R.id.prev_button);
        songDetails = (TextView) findViewById(R.id.song_details);
        speakerName = (TextView) findViewById(R.id.speaker_name);

        deviceId = getIntent().getStringExtra("deviceId");
        deviceName = getIntent().getStringExtra("deviceName");

    }

    public void updateState(){
        api.getSpeakerState(deviceId, new Callback<Result<SpeakerState>>() {
            @Override
            public void onResponse(Call<Result<SpeakerState>> call, Response<Result<SpeakerState>> response) {
                if(response.isSuccessful()){
                    Result<SpeakerState> result = response.body();
                    state = result.getResult();

                    progressBar.setLeft(0);
                    progressBar.setRight(getSeconds(state.getSong().getDuration()));
                    progressBar.setProgress(getSeconds(state.getSong().getProgress()));

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

}
