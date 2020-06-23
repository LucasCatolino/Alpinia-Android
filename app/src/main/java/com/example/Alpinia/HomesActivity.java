package com.example.Alpinia;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;


public class HomesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Home> homesList;
    Context context = this;

    Button addHomeBttn;
    EditText newHomeName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addHomeBttn = findViewById(R.id.btnAddHome);
        newHomeName = findViewById(R.id.newHome);
        recyclerView = findViewById(R.id.homeList);
        if (addHomeBttn != null) {
            addHomeBttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    createHome();
                }
            });
        }
        getHomes();

    }

    public void getHomes() {
        ApiClient.getInstance().getHomes(new Callback<Result<List<Home>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Home>>> call, @NonNull Response<Result<List<Home>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Home>> result = response.body();
                    System.out.println(result.getResult().toString());
                    homesList = result.getResult();
                    System.out.println(homesList.toString());
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
        Toast.makeText(this, "Creating Home", Toast.LENGTH_LONG).show();
        Home home = new Home(newHomeName.getText().toString());
        ApiClient.getInstance().addHome(home, new Callback<Result<Home>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Home>> call, @NonNull Response<Result<Home>> response) {
                if (response.isSuccessful()) {
                    Result<Home> result = response.body();
                    if (result != null) {
                        getHomes();
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
    }
}
