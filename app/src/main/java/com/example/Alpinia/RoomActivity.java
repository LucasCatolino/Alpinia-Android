package com.example.Alpinia;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.bind.util.ISO8601Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity {
    List<Device> devicesList;
    Context context;
    String roomId;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room);
        recyclerView = (RecyclerView) findViewById(R.id.rv_devices);

        context = this;
        if(getIntent().hasExtra("roomId"))
            roomId = getIntent().getExtras().get("roomId").toString();
        else
            roomId = null;

        getRoomDevices();

    }



    private void getRoomDevices() {

        ApiClient.getInstance().getRoomDevices(roomId, new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {
                System.out.println("ENTRO EN GET ROOM DEVICES");
                if (response.isSuccessful()) {
                    System.out.println("EXITO");
                    Result<List<Device>> result = response.body();
                    devicesList = result.getResult();
                    Toast.makeText(context,"holaaa",Toast.LENGTH_LONG);
                    System.out.println(result.getResult().toString());
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
