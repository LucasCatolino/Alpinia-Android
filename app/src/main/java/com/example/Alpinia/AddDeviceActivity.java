package com.example.Alpinia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.API.objects.devices.AirConditioner;
import com.example.Alpinia.API.objects.devices.Door;
import com.example.Alpinia.API.objects.devices.Faucet;
import com.example.Alpinia.API.objects.devices.Lights;
import com.example.Alpinia.API.objects.devices.Refrigerator;
import com.example.Alpinia.API.objects.devices.Speaker;
import com.example.Alpinia.API.objects.devices.Vacuum;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDeviceActivity extends AppCompatActivity {
    Button addButton;
    Spinner typeSpinner;
    EditText deviceName;
    Context context;

    ApiClient api;
    String roomId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device_layout);
        typeSpinner = findViewById(R.id.spinner2);
        deviceName = findViewById(R.id.editTextTextPersonName);
        addButton = findViewById(R.id.button);

        api = ApiClient.getInstance();
        roomId = getIntent().getStringExtra("roomId");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.device_types,android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device;
                if(typeSpinner.getSelectedItem().equals("Faucet")){
                    device = new Faucet(deviceName.getText().toString());
                }
                else if(typeSpinner.getSelectedItem().equals("Door")){
                    device = new Door(deviceName.getText().toString());
                }
                else if(typeSpinner.getSelectedItem().equals("Lights")){
                    device = new Lights(deviceName.getText().toString());
                }
                else if(typeSpinner.getSelectedItem().equals("Speaker")){
                    device = new Speaker(deviceName.getText().toString());
                }
                else if(typeSpinner.getSelectedItem().equals("Fridge")){
                    device = new Refrigerator(deviceName.getText().toString());
                }
                else if(typeSpinner.getSelectedItem().equals("Air Conditioner")){
                    device = new AirConditioner(deviceName.getText().toString());
                }
                else if(typeSpinner.getSelectedItem().equals("Vacuum")){
                    device = new Vacuum(deviceName.getText().toString());
                }
                else return;
                api.addDevice(device, new Callback<Result<Device>>() {
                    @Override
                    public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                        if(response.isSuccessful()){
                            api.addDeviceToRoom(roomId, response.body().getResult().getId(), new Callback<Result<Boolean>>() {
                                @Override
                                public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                                    if(response.isSuccessful()){
                                        Intent intent = new Intent(context,DeviceActivity.class);
                                        intent.putExtra("roomId", roomId);
                                        context.startActivity(intent);
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
                        else
                            handleError(response);
                    }

                    @Override
                    public void onFailure(Call<Result<Device>> call, Throwable t) {
                        handleUnexpectedError(t);
                    }
                });
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
}
