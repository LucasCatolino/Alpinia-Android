package com.example.Alpinia.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.Alpinia.ApiClient;
import com.example.Alpinia.Device;
import com.example.Alpinia.Error;

import com.example.Alpinia.Lamp;
import com.example.Alpinia.LampState;
import com.example.Alpinia.R;
import com.example.Alpinia.Result;
import com.example.Alpinia.Room;
import com.example.Alpinia.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Button createDeviceButton;
    private Device lamp;
    private TextView resultTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        resultTextView = root.findViewById(R.id.result);

        createDeviceButton = (Button) root.findViewById(R.id.create_device);
        if (createDeviceButton != null) {
            createDeviceButton.setOnClickListener(v -> createDevice());
        }

        return root;
    }

    private void createDevice() {
        Toast.makeText(getContext(), "Creating Device", Toast.LENGTH_LONG).show();
        lamp = new Lamp("lamparsaa");

        ApiClient.getInstance().addDevice(lamp, new Callback<Result<Device>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Device>> call, @NonNull Response<Result<Device>> response) {
                if (response.isSuccessful()) {
                    Result<Device> result = response.body();
                    if (result != null) {
                        lamp.setId(result.getResult().getId());
                        showResult(lamp.toString());
                        toggleButtons(true);
                    }

                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void toggleButtons(boolean enabled) {
        createDeviceButton.setEnabled(enabled);
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "App";
        Log.e(LOG_TAG, t.toString());
    }

    private void showResult(String result) {
        String format = getResources().getString(R.string.result);
        resultTextView.setText(String.format(format, result));
    }


}