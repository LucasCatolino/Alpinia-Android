package com.example.Alpinia.API.objects.devices;

import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.R;
import com.example.Alpinia.API.objects.Result;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoorDialog extends AppCompatActivity {


    private ApiClient api;
    private String deviceId;
    private Switch switchOpenClose, switchLockUnlock;
    TextView doorName;

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

        updateState();
        //toggleButtons(false);
        switchLockUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myState.isLocked()){
                    unlockDoor();
                }
                else{
                    lockDoor();
                }
            }
        });

        switchOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myState.isOpen()){
                    closeDoor();
                }
                else{
                    openDoor();
                }
            }
        });

    }

    private void updateState() {
        api.getDoorState(deviceId, new Callback<Result<DoorState>>() {
            @Override
            public void onResponse(Call<Result<DoorState>> call, Response<Result<DoorState>> response) {
                if(response.isSuccessful()){
                    Result<DoorState> result = response.body();
                    myState = result.getResult();
                    switchOpenClose.setChecked(myState.isOpen());
                    switchLockUnlock.setChecked(myState.isLocked());

                    switchOpenClose.setEnabled(!myState.isLocked());
                    switchLockUnlock.setEnabled(!myState.isOpen());
                }
                else{
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
                if(response.isSuccessful()) {
                    updateState();
                }
                else {
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
                if(response.isSuccessful()) {
                    updateState();
                }
                else {
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
                if(response.isSuccessful()) {
                    updateState();
                }
                else {
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
                if(response.isSuccessful()) {
                    updateState();
                }
                else {
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


}
