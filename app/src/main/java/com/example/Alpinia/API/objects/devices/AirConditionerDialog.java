package com.example.Alpinia.API.objects.devices;

import android.content.Context;
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

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirConditionerDialog extends AppCompatActivity {
    // creo las variables con las que voy a trabajar
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

    ArrayAdapter<CharSequence> tempAdapter;

    ArrayAdapter<CharSequence> speedAdapter;
    AirConditionerState myState;
    ArrayAdapter<CharSequence> modeAdapter;
    ArrayAdapter<CharSequence> horizontalSwingAdapter;
    ArrayAdapter<CharSequence> verticalSwingAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_card);
        context = (Context) this;
        api = ApiClient.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");

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


}