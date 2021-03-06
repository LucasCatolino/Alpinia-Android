package com.example.Alpinia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceActivity extends AppCompatActivity {
    List<Device> devicesList;
    Context context;
    private static String roomId; //agrego private static
    RecyclerView recyclerView;
    TextView noDevices;
    FloatingActionButton floatButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room);
        recyclerView = (RecyclerView) findViewById(R.id.rv_devices);
        noDevices = (TextView) findViewById(R.id.no_devices);

        context = this;
        if(getIntent().hasExtra("roomId")) {
            roomId = getIntent().getExtras().get("roomId").toString();
        }
//        else
//            roomId = null;
        floatButton = findViewById(R.id.floatingActionButton2);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AddDeviceActivity.class);
                intent.putExtra("roomId",roomId);
                context.startActivity(intent);
            }
        });

        getRoomDevices();

    }



    private void getRoomDevices() {

        ApiClient.getInstance().getRoomDevices(roomId, new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {

                if (response.isSuccessful()) {

                    Result<List<Device>> result = response.body();
                    devicesList = result.getResult();
                    if (devicesList.size()!= 0) {
                        noDevices.setVisibility(View.GONE);
                    }
                    if(devicesList != null){
                        DeviceAdapter myAdapter = new DeviceAdapter(context,devicesList);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));}

                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Device>>> call, @NonNull Throwable t) {
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
}
