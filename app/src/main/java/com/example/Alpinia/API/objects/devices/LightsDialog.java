package com.example.Alpinia.API.objects.devices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yuku.ambilwarna.AmbilWarnaDialog;

import com.example.Alpinia.HomesActivity;

public class LightsDialog extends AppCompatActivity {

    private static final String CHANNEL_ID = "LightsNotifications";
    private static final int MY_NOTIFICATION_ID = 4;

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
                    sendNotification(getIntent().getStringExtra("deviceName"), getString(R.string.lights_off_notification));
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
                    sendNotification(getIntent().getStringExtra("deviceName"), getString(R.string.lights_on_notification));
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

//Notificaciones
    private void sendNotification(String title, String text){
        CreateNotificationChannel();
        ShowNotification(title, text);
    }
    private void CreateNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channelLights_name);
            String description = getString(R.string.channelLights_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void ShowNotification(String title, String text) {
        // Create the intent to start Activity when notification in action bar is
        // clicked.
        Intent notificationIntent = new Intent(LightsDialog.this, HomesActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomesActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        // Create the pending intent granting the Operating System to launch activity
        // when notification in action bar is clicked.
        final PendingIntent contentIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(LightsDialog.this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_light))
                .setSmallIcon(R.drawable.ic_light)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, builder.build());
    }


}
