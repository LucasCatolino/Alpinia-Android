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

import android.util.Log;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.MainActivity;
import com.example.Alpinia.R;
import com.example.Alpinia.API.objects.Result;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.Alpinia.HomesActivity;

public class DoorDialog extends AppCompatActivity {

    private static final String CHANNEL_ID = "DoorNotifications";
    private static final int MY_NOTIFICATION_ID = 2;

    private ApiClient api;
    private String deviceId;
    private Switch switchOpenClose, switchLockUnlock;
    TextView doorName;
    Context context;

    DoorState myState;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_card);

        deviceId = getIntent().getStringExtra("deviceId");

        api = ApiClient.getInstance();
        doorName = findViewById(R.id.door_name);
        doorName.setText(getIntent().getStringExtra("deviceName"));
        switchOpenClose = findViewById(R.id.open_close_door);
        switchLockUnlock = findViewById(R.id.lock_unlock_switch);
        context = (Context) this;

        updateState();
        //toggleButtons(false);
        switchLockUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myState.isLocked()) {
                    unlockDoor();
                } else {
                    lockDoor();
                }
            }
        });

        switchOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myState.isOpen()) {
                    closeDoor();
                } else {
                    openDoor();
                }
            }
        });

    }

    private void updateState() {
        api.getDoorState(deviceId, new Callback<Result<DoorState>>() {
            @Override
            public void onResponse(Call<Result<DoorState>> call, Response<Result<DoorState>> response) {
                if (response.isSuccessful()) {
                    Result<DoorState> result = response.body();
                    myState = result.getResult();
                    switchOpenClose.setChecked(myState.isOpen());
                    switchLockUnlock.setChecked(myState.isLocked());

                    switchOpenClose.setEnabled(!myState.isLocked());
                    switchLockUnlock.setEnabled(!myState.isOpen());
                } else {
                    handleError(response);
                }

            }

            @Override
            public void onFailure(Call<Result<DoorState>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }


    // ABRO LA PUERTA
    private void openDoor() {
        api.openDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if (response.isSuccessful()) {
                    updateState();
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    //CIERRO LA PUERTA
    private void closeDoor() {
        api.closeDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if (response.isSuccessful()) {
                    updateState();
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }


    //BLOQUEO LA PUERTA
    private void lockDoor() {
        api.lockDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if (response.isSuccessful()) {
                    updateState();
                    sendNotification(getIntent().getStringExtra("deviceName"), getString(R.string.door_locked_notification));
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }


    //DESBLOQUEO LA PUERTA
    private void unlockDoor() {
        api.unlockDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if (response.isSuccessful()) {
                    updateState();
                    sendNotification(getIntent().getStringExtra("deviceName"), getString(R.string.door_unlocked_notification));
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
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

    private void toggleButtons(boolean enabled) {
        switchLockUnlock.setEnabled(enabled);
        switchOpenClose.setEnabled(enabled);
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
            CharSequence name = "Notifications";
            String description = "Notifications Description";
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
        Intent notificationIntent = new Intent(DoorDialog.this, HomesActivity.class);

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(DoorDialog.this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_sys_download_done))
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, builder.build());
    }
}

