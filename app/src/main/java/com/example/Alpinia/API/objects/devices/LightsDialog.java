package com.example.Alpinia.API.objects.devices;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yuku.ambilwarna.AmbilWarnaDialog;

public class LightsDialog extends AppCompatActivity {
    Context context;
    private ApiClient api;
    private String deviceId;
    private Switch switchOnOff;
    private Button colorChange;
    LightsState state;
    SeekBar brightnessBar;
    int mDefaultColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = (Context) this;
        setContentView(R.layout.light_card);
        api = ApiClient.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");
        mDefaultColor = ContextCompat.getColor(this,R.color.colorPrimary);
        switchOnOff = findViewById(R.id.lights_switch);

        colorChange = findViewById(R.id.color_picker);

        colorChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        switchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.isOn()){
                    turnOffLights();
                }
                else
                    turnOnLights();
            }
        });

        brightnessBar = findViewById(R.id.brightness_slider);
        brightnessBar.setRight(100);
        brightnessBar.setLeft(0);
        updateState();

        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBrightness((Integer) progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ;

            }
        });



    }

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(context,mDefaultColor ,new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                setColor(color);
            }
        });
        colorPicker.show();
    }
    public void setColor(int color){
        api.setLightColor(deviceId, Integer.toHexString(color), new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                if(response.isSuccessful()){

                }
                else{
                    handleError(response);
                }

            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void setBrightness(Integer brightness){
        api.setLightIntensity(deviceId, brightness, new Callback<Result<Integer>>() {
            @Override
            public void onResponse(Call<Result<Integer>> call, Response<Result<Integer>> response) {
                if(response.isSuccessful()){
                    Result<Integer> result = response.body();

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

    public void turnOffLights(){
        api.turnLightsOff(deviceId, new Callback<Result<Boolean>>() {
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


    public void turnOnLights(){
        api.turnLightsOn(deviceId, new Callback<Result<Boolean>>() {
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


    public void updateState(){
        api.getLightState(deviceId, new Callback<Result<LightsState>>() {
            @Override
            public void onResponse(Call<Result<LightsState>> call, Response<Result<LightsState>> response) {
                if(response.isSuccessful()){
                    Result<LightsState> result = response.body();
                    if(result.getResult() != null) {
                        state = result.getResult();
                        brightnessBar.setProgress(state.getIntensity());
                        switchOnOff.setChecked(state.isOn());

                        toggleButtons(state.getStatus().equals("on"));
                    }
                }
                else{
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<LightsState>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }



    private void toggleButtons(boolean enabled) {
        brightnessBar.setEnabled(enabled);
        colorChange.setEnabled(enabled);
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

}
