package com.example.Alpinia.API.objects.devices;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class VacuumDialog extends AppCompatActivity {

    TextView vacuumName;
    TextView vacuumDetails;
    TextView vacuumBattery;
    Button dockButton;
    Button setModeButton;
    Switch onOffSwitch;
    Spinner modeSpinner;

    ApiClient api;
    String deviceId;
    VacuumState state;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccum_card);
        vacuumName = findViewById(R.id.open_close_switch_faucet);
        vacuumDetails = findViewById(R.id.vacuum_details);
        dockButton = findViewById(R.id.dock_button);
        setModeButton = findViewById(R.id.mode_button);
        onOffSwitch = findViewById(R.id.switch_button);
        modeSpinner = findViewById(R.id.spinner);
        Context context = (Context) this;

        api = ApiClient.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");
        vacuumName.setText(getIntent().getStringExtra("deviceName"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.modes,android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        modeSpinner.setAdapter(adapter);


        getVacuumState();

        dockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dockVacuum();
            }
        });

        onOffSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.isFunctioning()){
                    pauseVacuum();
                }
                else
                    startVacuum();
            }
        });

        setModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVacuumMode(modeSpinner.getSelectedItem().toString());
            }
        });
    }

    public void getVacuumState(){
        api.getVacuumState(deviceId, new Callback<Result<VacuumState>>(){

            @Override
            public void onResponse(Call<Result<VacuumState>> call, Response<Result<VacuumState>> response) {
                if(response.isSuccessful()){
                    Result<VacuumState> result = response.body();
                    state = result.getResult();
                    vacuumDetails.setText(state.toString());
                    onOffSwitch.setChecked(state.isFunctioning());
                    toggleButtons(state.isFunctioning());

                }
                else
                        handleError(response);
            }

            @Override
            public void onFailure(Call<Result<VacuumState>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }


    public void dockVacuum(){
        api.dockVacuum(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    getVacuumState();
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


    public void pauseVacuum(){
        api.pauseVacuum(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    getVacuumState();
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


    public void startVacuum(){
        api.startVacuum(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if(response.isSuccessful()){
                    getVacuumState();
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

    public void setVacuumMode(String mode){
        api.setVacuumMode(deviceId,mode, new Callback<Result<String>>() {
            @Override
            public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                if(response.isSuccessful()){
                    getVacuumState();
                }
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<String>> call, Throwable t) {
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
        dockButton.setEnabled(enabled);
        modeSpinner.setEnabled(enabled);
        setModeButton.setEnabled(enabled);

    }
}
