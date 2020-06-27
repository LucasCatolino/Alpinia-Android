package com.example.Alpinia.API.objects.devices;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaucetDialog extends AppCompatActivity {
    EditText toDispense;
    Button startButton;
    TextView faucetName;
    Spinner unitSpinner;
    Switch onOffSwitch;

    FaucetState state;
    ApiClient api;
    String deviceId;
    String deviceName;
    ArrayAdapter<CharSequence> units;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faucet_card);

        faucetName = findViewById(R.id.fauce_name);
        startButton = findViewById(R.id.start_dispensing);
        toDispense = findViewById(R.id.amount_to_dispense);
        unitSpinner = findViewById(R.id.unit_spinner);
        onOffSwitch = findViewById(R.id.switch_button);

        units = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_list_item_1);
        units.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(units);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInteger(toDispense.getText().toString())
                        && Integer.valueOf(toDispense.getText().toString()) > 0
                        && Integer.valueOf(toDispense.getText().toString()) < 101){

                    startDispensing(Integer.valueOf(toDispense.getText().toString()));
                }
                else{
                    toDispense.setError("Must be a number between 1 and 100!");
                }

            }
        });

        onOffSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.isOpen()){
                    closeFaucet();
                }
                else
                    openFaucet();
            }
        });

        api = ApiClient.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");
        faucetName.setText( getIntent().getStringExtra("deviceName"));

        updateState();
    }

    public void openFaucet() {
        api.openFaucet(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (response.isSuccessful())
                    updateState();
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void closeFaucet() {
        api.closeFaucet(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (response.isSuccessful())
                    updateState();
                else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void startDispensing(Integer amount) {
        api.dispenseExactAmount(deviceId, amount, unitSpinner.getSelectedItem().toString(), new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                if (response.isSuccessful()) {
                    updateState();
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    public void updateState() {
        api.getFaucetState(deviceId, new Callback<Result<FaucetState>>() {
            @Override
            public void onResponse(Call<Result<FaucetState>> call, Response<Result<FaucetState>> response) {
                if (response.isSuccessful()) {
                    Result<FaucetState> result = response.body();
                    state = result.getResult();

                    onOffSwitch.setChecked(state.isOpen());
                    toggleButtons(state.isOpen());
                } else
                    handleError(response);
            }

            @Override
            public void onFailure(Call<Result<FaucetState>> call, Throwable t) {
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

    private void toggleButtons(boolean enabled) {
        unitSpinner.setEnabled(enabled);
        startButton.setEnabled(enabled);
        toDispense.setEnabled(enabled);
    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}