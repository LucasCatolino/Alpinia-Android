package com.example.Alpinia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.API.objects.Routine;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoutineActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Routine> routinesList;
    Context context = this;

    private List<Routine> routines;

    private ApiClient api;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        recyclerView = findViewById(R.id.recycler_view_routine);

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