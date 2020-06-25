package com.example.Alpinia.API.objects.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefrigeratorDialog extends Fragment {

    private String deviceId;
    private Spinner spinMode, spinFridge, spinFreezer;
    private ApiClient api;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.refrigerator_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readBundle(getArguments());

        init(view);
    }

    private void init(@NonNull View view) {
        spinMode = view.findViewById(R.id.ref_mode);
        spinFridge = view.findViewById(R.id.ref_fridge_temp);
        spinFreezer = view.findViewById(R.id.ref_freezer_temp);

        api = ApiClient.getInstance();

        api.getRefrigeratorState(deviceId, new Callback<Result<RefrigeratorState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<RefrigeratorState>> call, @NonNull Response<Result<RefrigeratorState>> response) {
                if(response.isSuccessful()) {
                    Result<RefrigeratorState> result = response.body();
                    if(result != null) {
                        RefrigeratorState refrigeratorState = result.getResult();
                        Integer freezerTemp = refrigeratorState.getFreezerTemperature();
                        int freezerTempIndex = getIndex(spinFreezer, freezerTemp.toString());
                        Integer fridgeTemp = refrigeratorState.getTemperature();
                        int fridgeTempIndex = getIndex(spinFridge, fridgeTemp.toString());
                        spinFreezer.setSelection(freezerTempIndex);
                        spinFridge.setSelection(fridgeTempIndex);


                        String aux = refrigeratorState.getMode();
                        switch (aux) {
                            case "default":
                                spinMode.setSelection(0);
                                break;
                            case "vacation":
                                spinMode.setSelection(1);
                                break;
                            case "party":
                                spinMode.setSelection(2);
                                break;
                        }

                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<RefrigeratorState>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });

        spinMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeMode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        spinFridge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeFridgeTemp();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        spinFreezer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeFreezerTemp();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            deviceId = bundle.getString("deviceId");
        }
    }

    @NonNull
    public static RefrigeratorDialog newInstance(String deviceId) {
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);

        RefrigeratorDialog fragment = new RefrigeratorDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void changeMode() {
        api.changeRefrigeratorMode(deviceId, spinMode.getSelectedItem().toString().toLowerCase(), new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        updateSpinners();
                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
            }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });
    }

    private void changeFridgeTemp() {
        api.setFridgeTemp(deviceId, Integer.parseInt(spinFridge.getSelectedItem().toString()), new Callback<Result<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Integer>> call, @NonNull Response<Result<Integer>> response) {
                if(response.isSuccessful()) {
                    Result<Integer> result = response.body();
                    if(result != null) {
                        updateSpinners();
                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Integer>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });
    }

    private void changeFreezerTemp() {
        api.setFreezerTemp(deviceId, Integer.parseInt(spinFreezer.getSelectedItem().toString()), new Callback<Result<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Integer>> call, @NonNull Response<Result<Integer>> response) {
                if(response.isSuccessful()) {
                    Result<Integer> result = response.body();
                    if(result != null) {
                        updateSpinners();
                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Integer>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return -1;
    }

    private void updateSpinners() {
        api.getRefrigeratorState(deviceId, new Callback<Result<RefrigeratorState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<RefrigeratorState>> call, @NonNull Response<Result<RefrigeratorState>> response) {
                if(response.isSuccessful()) {
                    Result<RefrigeratorState> result = response.body();
                    if(result != null) {
                        RefrigeratorState refrigeratorState = result.getResult();
                        Integer freezerTemp = refrigeratorState.getFreezerTemperature();
                        int freezerTempIndex = getIndex(spinFreezer, freezerTemp.toString());
                        Integer fridgeTemp = refrigeratorState.getTemperature();
                        int fridgeTempIndex = getIndex(spinFridge, fridgeTemp.toString());
                        String aux = refrigeratorState.getMode();
                        spinFreezer.setSelection(freezerTempIndex);
                        spinFridge.setSelection(fridgeTempIndex);
                        switch (aux) {
                            case "default":
                                spinMode.setSelection(0);
                                break;
                            case "vacation":
                                spinMode.setSelection(1);
                                break;
                            case "party":
                                spinMode.setSelection(2);
                                break;
                        }

                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<RefrigeratorState>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });
    }
}

