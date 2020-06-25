package com.example.Alpinia.API.objects.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;




import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaucetDialog extends Fragment {

    private String deviceId;
    private Switch switchOpenClose;
    private Button buttonDispenseAuto, buttonStop, buttonCancel, buttonStart;
    private EditText amountToDispense;
    private Spinner spinnerQuantity;

    private ApiClient api;

    private boolean isOpen;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.faucet_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readBundle(getArguments());

        init(requireView());
    }

    private void init(View view) {
        switchOpenClose = view.findViewById(R.id.open_close_switch_faucet);
        buttonDispenseAuto = view.findViewById(R.id.dispense_exactly);
        buttonStop = view.findViewById(R.id.stop_button);
        buttonStart = view.findViewById(R.id.start_dispensing);
        buttonCancel = view.findViewById(R.id.cancel_dispensing);
        amountToDispense = view.findViewById(R.id.amount_to_dispense);
        spinnerQuantity = view.findViewById(R.id.unit_spinner);

        api = ApiClient.getInstance();

        api.getFaucetState(deviceId, new Callback<Result<FaucetState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<FaucetState>> call, @NonNull Response<Result<FaucetState>> response) {
                if(response.isSuccessful()) {
                    Result<FaucetState> result = response.body();
                    if(result != null) {
                        FaucetState faucetState = result.getResult();
                        isOpen = faucetState.isOpen();
                        switchOpenClose.setChecked(isOpen);
                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
            }
            }

            @Override
            public void onFailure(@NonNull Call<Result<FaucetState>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });

        if(isOpen)
            buttonDispenseAuto.setVisibility(View.INVISIBLE);

        buttonStop.setVisibility(View.INVISIBLE);
        buttonStart.setVisibility(View.INVISIBLE);
        buttonCancel.setVisibility(View.INVISIBLE);
        amountToDispense.setVisibility(View.INVISIBLE);
        spinnerQuantity.setVisibility(View.INVISIBLE);

        buttonStop.setOnClickListener(this::stopDispensing);
        buttonStart.setOnClickListener(this::startDispensing);
        buttonDispenseAuto.setOnClickListener(this::dispenseAutomatically);
        buttonCancel.setOnClickListener(this::cancelButtonPressed);


        if(switchOpenClose != null) {
            switchOpenClose.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    openFaucet();
                } else {
                    closeFaucet();
                }
            });
        }
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            deviceId = bundle.getString("deviceId");
        }
    }

    @NonNull
    public static FaucetDialog newInstance(String deviceId) {
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);

        FaucetDialog fragment = new FaucetDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void openFaucet() {
        if(isOpen) {
            Toast.makeText(getContext(), "THE FAUCET WAS ALREADY OPENED !!!!", Toast.LENGTH_LONG).show();
            return;
        }

        api.openFaucet(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        Toast.makeText(getContext(), "OPENING FAUCET....", Toast.LENGTH_LONG).show();
                        isOpen = true;
                        buttonDispenseAuto.setVisibility(View.INVISIBLE);
                        buttonStop.setVisibility(View.INVISIBLE);
                        buttonStart.setVisibility(View.INVISIBLE);
                        buttonCancel.setVisibility(View.INVISIBLE);
                        amountToDispense.setVisibility(View.INVISIBLE);
                        spinnerQuantity.setVisibility(View.INVISIBLE);
                    } else {
                        //manejo error
                    }
                } else {
                    //manejo error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                //manejo error
        }
        });
    }

    private void closeFaucet() {
        if(!isOpen) {
            Toast.makeText(getContext(), "THE FAUCET WAS ALREADY CLOSED", Toast.LENGTH_LONG).show();
            return;
        }

        api.closeFaucet(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        Toast.makeText(getContext(), "CLOSING FAUCET....", Toast.LENGTH_LONG).show();
                        isOpen = false;
                        buttonDispenseAuto.setVisibility(View.INVISIBLE);
                        buttonStop.setVisibility(View.INVISIBLE);
                        buttonStart.setVisibility(View.INVISIBLE);
                        buttonCancel.setVisibility(View.INVISIBLE);
                        amountToDispense.setVisibility(View.INVISIBLE);
                        spinnerQuantity.setVisibility(View.INVISIBLE);
                    } else {
                        //manejo error
                    }
                } else {
                    //manejo error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                //manejo error
            }
        });
    }

    private void dispenseAutomatically(View view) {
        buttonDispenseAuto.setVisibility(View.INVISIBLE);
        buttonStart.setVisibility(View.VISIBLE);
        buttonCancel.setVisibility(View.VISIBLE);
        amountToDispense.setVisibility(View.VISIBLE);
        spinnerQuantity.setVisibility(View.VISIBLE);
    }

    private void cancelButtonPressed(View view) {
        buttonDispenseAuto.setVisibility(View.VISIBLE);
        buttonStart.setVisibility(View.INVISIBLE);
        buttonCancel.setVisibility(View.INVISIBLE);
        amountToDispense.setVisibility(View.INVISIBLE);
        spinnerQuantity.setVisibility(View.INVISIBLE);
    }

    private void stopDispensing(View view) {
        closeFaucet();
    }

    private void startDispensing(View view) {
        try{
            Integer.parseInt(amountToDispense.getText().toString());
        } catch (Exception e) {
            Toast.makeText(getContext(), "PLEASE, ENTER AN AMOUNT", Toast.LENGTH_LONG).show();
            amountToDispense.setText("");
            return;
        }

        int currentAmount = Integer.parseInt(amountToDispense.getText().toString());
        String unit = spinnerQuantity.getSelectedItem().toString();

        if(currentAmount == 0) {
            Toast.makeText(getContext(), "YOU HAVE TO ENTER A NUMBER GREATER THAN ZERO", Toast.LENGTH_LONG).show();
            amountToDispense.setText("");
            return;
        }


        api.dispenseExactAmount(deviceId, currentAmount, unit, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        Toast.makeText(getContext(), "DISPENSING " + amountToDispense + unit, Toast.LENGTH_LONG).show();
                        switchOpenClose.setChecked(true);
                    } else {
                        //manejo error
                    }
                } else {
                    //manejo error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                //manejo error
            }
        });
    }
}