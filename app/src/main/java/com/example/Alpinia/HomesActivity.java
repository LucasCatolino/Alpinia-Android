package com.example.Alpinia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Home;
import com.example.Alpinia.API.objects.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;


public class HomesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Home> homesList;
    Context context = this;
    TextView noHomes;
    Button addHomeBttn;
    EditText newHomeName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addHomeBttn = findViewById(R.id.btnAddHome);
        newHomeName = findViewById(R.id.newHome);
        recyclerView = findViewById(R.id.homeList);
        noHomes = (TextView) findViewById(R.id.no_homes);
        updateView();
        if (addHomeBttn != null) {
            addHomeBttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    createHome();
                }
            });
        }
        updateView();
    }

    //Mostrar el overflow
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    //Asignar las funciones al overflow
    public boolean onOptionsItemSelected (MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.overflow_refresh) {
            updateView();
            Toast.makeText(getApplicationContext(), R.string.toast_Refresh, Toast.LENGTH_SHORT).show();
        } else if (id== R.id.overflow_settings) {
            Intent i= new Intent(this, SettingsActivity.class);
            startActivity(i);

            Toast.makeText(getApplicationContext(), R.string.toast_Settings, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateView() {
        getHomes();
    }

    public void getHomes() {
        ApiClient.getInstance().getHomes(new Callback<Result<List<Home>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Home>>> call, @NonNull Response<Result<List<Home>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Home>> result = response.body();
                    homesList = result.getResult();
                    if (homesList.size()!= 0) {
                        noHomes.setVisibility(View.GONE);
                    }
                    if(homesList != null){
                        HomesAdapter myAdapter = new HomesAdapter(context,homesList);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));}
                    //homesList.addAll(result.getResult());
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Home>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void createHome() {
        Toast.makeText(this, R.string.toast_createHome, Toast.LENGTH_LONG).show();
        Home home = new Home(newHomeName.getText().toString());
        ApiClient.getInstance().addHome(home, new Callback<Result<Home>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Home>> call, @NonNull Response<Result<Home>> response) {
                if (response.isSuccessful()) {
                    Result<Home> result = response.body();
                    if (result != null) {
                        updateView();
                        home.setId(result.getResult().getId());
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Home>> call, @NonNull Throwable t) {
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
        if ((t.toString().contains("connect")) || (t.toString().contains("Unable to resolve host"))){
            Toast.makeText(getApplicationContext(), R.string.toast_ConnectionProblems, Toast.LENGTH_SHORT).show();
        }
    }
}