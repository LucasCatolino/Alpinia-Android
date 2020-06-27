package com.example.Alpinia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
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

    private List<String> routineNames;
    private List<String> routineIds;
    private List<Routine> routines;

    private ApiClient api;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        recyclerView = findViewById(R.id.recycler_view_routine);
        updateView();
    }

    //Mostrar el overflow
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    //Asignar las funciones al overflow
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.overflow_refresh) {
            updateView();
            Toast.makeText(getApplicationContext(), R.string.toast_Refresh, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.overflow_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);

            Toast.makeText(getApplicationContext(), R.string.toast_Settings, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateView() {
        getRoutines();
    }

    private void getRoutines() {
        api.getRoutines(new Callback<Result<List<Routine>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Routine>>> call, @NonNull Response<Result<List<Routine>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Routine>> result = response.body();
                    if (result != null) {
                        List<Routine> routineList = result.getResult();
                        for (Routine h : routineList) {
                            routineIds.add(h.getId());
                            routineNames.add(h.getName());
                            routines.add(h);
                        }
                        RoutineAdapter myAdapter = new RoutineAdapter(context,routineList);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));}
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Routine>>> call, @NonNull Throwable t) {

                }
        });
    }











}