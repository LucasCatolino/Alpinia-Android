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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.Alpinia.HomesActivity;


public class AirConditionerDialog extends AppCompatActivity {
    // creo las variables con las que voy a trabajar
    private static final String CHANNEL_ID = "AirConditionesNotifications";
    private static final int MY_NOTIFICATION_ID = 1;

    Context context;
    private ApiClient api;
    private String deviceId;
    private Spinner spinMode;
    private Spinner spinTemperature;
    private Spinner spinVerticalSwing;
    private Spinner spinHorizontalSwing;
    private Spinner spinSpeed;
    private Switch switchOpenClose;
    TextView acDetails;
    TextView acName;

    ArrayAdapter<CharSequence> tempAdapter;

    ArrayAdapter<CharSequence> speedAdapter;
    AirConditionerState myState;
    ArrayAdapter<CharSequence> modeAdapter;
    ArrayAdapter<CharSequence> horizontalSwingAdapter;
    ArrayAdapter<CharSequence> verticalSwingAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_conditioner_card);
        context = (Context) this;
        api = ApiClient.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");
        acName = findViewById(R.id.ac_title);
        acName.setText(getIntent().getStringExtra("deviceName"));

        switchOpenClose = (Switch) findViewById(R.id.open_close_ac);
        spinTemperature = (Spinner) findViewById(R.id.spin_temperature_ac);
        spinMode = (Spinner) findViewById(R.id.spin_mode_ac);
        spinVerticalSwing = (Spinner) findViewById(R.id.spin_vertical_ac);
        spinHorizontalSwing = (Spinner) findViewById(R.id.spin_horizontal_ac);
        spinSpeed = (Spinner) findViewById(R.id.spin_speed_ac);

        tempAdapter = ArrayAdapter.createFromResource(this, R.array.ac_temperature_options,android.R.layout.simple_list_item_1);
        tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        speedAdapter = ArrayAdapter.createFromResource(this,R.array.ac_speed,android.R.layout.simple_list_item_1);
        speedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        modeAdapter = ArrayAdapter.createFromResource(this,R.array.ac_mode_options,android.R.layout.simple_list_item_1);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        horizontalSwingAdapter = ArrayAdapter.createFromResource(this,R.array.ac_horizontal_options,android.R.layout.simple_list_item_1);
        horizontalSwingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        verticalSwingAdapter = ArrayAdapter.createFromResource(this,R.array.ac_vertical_options,android.R.layout.simple_list_item_1);
        verticalSwingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinTemperature.setAdapter(tempAdapter);
        spinMode.setAdapter(modeAdapter);

        switchOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myState.isOpen()){
                    turnOffAc();
                }
                else
                    turnOnAc();
            }
        });

        spinTemperature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setAcTemperature(Integer.valueOf(spinTemperature.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpinSpeed(spinSpeed.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinHorizontalSwing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setHorizontalSwing(spinHorizontalSwing.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinVerticalSwing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setVerticalSwing(spinVerticalSwing.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        updateState();


    }



    public void setAcTemperature(Integer temp){
        api.setAcTemp(deviceId, temp, new Callback<Result<Integer>>() {
            @Override
            public void onResponse(Call<Result<Integer>> call, Response<Result<Integer>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<Integer>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void setSpinSpeed(String speed){
        api.setFanSpeed(deviceId, speed, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void setHorizontalSwing(String swing){
        api.setHorizontalSwing(deviceId, swing, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    updateState();
                }
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void setVerticalSwing(String swing){
        api.setVerticalSwing(deviceId, swing, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    updateState();
                }
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void turnOnAc(){
        api.turnOnAc(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void turnOffAc(){
        api.turnOffAc(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    updateState();
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void updateState(){
        api.getAirConditionerState(deviceId, new Callback<Result<AirConditionerState>>() {
            @Override
            public void onResponse(Call<Result<AirConditionerState>> call, Response<Result<AirConditionerState>> response) {
                if(response.isSuccessful()){
                    Result<AirConditionerState> result = response.body();
                    myState = result.getResult();

                    spinTemperature.setSelection(tempAdapter.getPosition(myState.getTemperature().toString()));
                    spinMode.setSelection(modeAdapter.getPosition(myState.getMode()));
                    spinVerticalSwing.setSelection(verticalSwingAdapter.getPosition(myState.getVerticalSwing()));
                    spinHorizontalSwing.setSelection(horizontalSwingAdapter.getPosition(myState.getHorizontalSwing()));
                    spinSpeed.setSelection(speedAdapter.getPosition(myState.getFanSpeed()));

                    switchOpenClose.setChecked(myState.isOpen());
                    toggleButtons(myState.isOpen());
                    airConditionerSendNotification();
                }
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<AirConditionerState>> call, Throwable t) {
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

    private void toggleButtons(boolean enabled){

        spinTemperature.setEnabled(enabled);
        spinMode.setEnabled(enabled);
        spinVerticalSwing.setEnabled(enabled);
        spinHorizontalSwing.setEnabled(enabled);
        spinSpeed.setEnabled(enabled);

    }

//Notificaciones
    private void airConditionerSendNotification(){
        if(myState.isOpen() && myState.getTemperature().toString() != null) {
            sendNotification(getIntent().getStringExtra("deviceName"), myState.getMode() + ": " + myState.getTemperature().toString() + "°C");
        }
    }

    private void sendNotification(String title, String text){
        CreateNotificationChannel();
        ShowNotification(title, text);
    }
    private void CreateNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channelAirConditioner_name);
            String description = getString(R.string.channelAirConditioner_description);
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
        Intent notificationIntent = new Intent(AirConditionerDialog.this, HomesActivity.class);

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(AirConditionerDialog.this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_ac))
                .setSmallIcon(R.drawable.ic_ac)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, builder.build());
    }

}