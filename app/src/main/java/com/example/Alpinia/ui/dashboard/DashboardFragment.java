package com.example.Alpinia.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.Error;

import com.example.Alpinia.API.objects.Routine;
import com.example.Alpinia.R;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.RoutineAdapter;
import com.example.Alpinia.SettingsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Button createDeviceButton;
    private Device lamp;
    private TextView resultTextView;
    ApiClient api;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = root.findViewById(R.id.recycler_routines);
        api = ApiClient.getInstance();

        updateView();
        return root;

    }



    //Mostrar el overflow
    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    //Asignar las funciones al overflow
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.overflow_refresh) {
            updateView();
            Toast.makeText(getActivity(), R.string.toast_Refresh, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.overflow_settings) {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);

            Toast.makeText(getActivity(), R.string.toast_Settings, Toast.LENGTH_SHORT).show();
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
                        RoutineAdapter myAdapter = new RoutineAdapter(getActivity(),routineList);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));}
                } else {
                    handleError(response);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Routine>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "App";
        Log.e(LOG_TAG, t.toString());
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

}