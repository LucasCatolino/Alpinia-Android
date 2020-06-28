package com.example.Alpinia.API.objects.devices;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefrigeratorDialog extends AppCompatActivity {
    // creo las variables con las que voy a trabajar
    Context context;
    private ApiClient api;
    private String deviceId;
    private Spinner spinMode;
    private Spinner spinFridge;
    private Spinner spinFreezer;


    // sobreescribo el onCreate para relacionarlo con el layout del refrigerator
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = (Context) this;
        setContentView(R.layout.ref_card);
        // instancio la API
        api = ApiClient.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");
        // hago que las variables creadas referencian a los botones del layout
        spinMode = findViewById(R.id.ref_mode);
        spinFridge = findViewById(R.id.ref_fridge_temp);
        spinFreezer = findViewById(R.id.ref_freezer_temp);

        // obtengo el estado de la heladera
        api.getRefrigeratorState(deviceId, new Callback<Result<RefrigeratorState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<RefrigeratorState>> call, @NonNull Response<Result<RefrigeratorState>> response) {
                if(response.isSuccessful()) {
                    Result<RefrigeratorState> result = response.body();
                    if(result != null) {
                        RefrigeratorState refrigeratorState = result.getResult();
                        // freezer_aux = tempratura actual del freezer (en la API)
                        Integer freezer_aux = refrigeratorState.getFreezerTemperature();
                        // freezer_aux2 = índice de la tempratura en la que está el freezer (que índice en el arreglo ocupa) -> estamos trabajando con Spinners
                        // le paso la temperatura y me devuelve el índice que ocupa en el arreglo -> función getIndex
                        int freezer_aux2 = getIndex(spinFreezer, freezer_aux.toString());
                        // mismo procedimiento con la heladera
                        Integer fridge_aux = refrigeratorState.getTemperature();
                        int fridge_aux2 = getIndex(spinFridge, fridge_aux.toString());
                        // en la interfaz selecciono el índice que me devuelve la función
                        spinFreezer.setSelection(freezer_aux2);
                        spinFridge.setSelection(fridge_aux2);
                        // la API me devuelve el modo en el que está la heladera e itero sobre las 3 opciones posibles para seleccionar el índice adecuado
                        String mode_aux = refrigeratorState.getMode();
                        if(mode_aux.equals("default")){
                            spinMode.setSelection(0);
                        } else if(mode_aux.equals("vacation")){
                            spinMode.setSelection(1);
                        } else if(mode_aux.equals("party")){
                            spinMode.setSelection(2);
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



        // invoca a la función changeMode() cuando se selecciona un ítem del spinner de mode
        spinMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeMode();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no se selecciono ninguna opción -> no hace nada
            }
        });

        // invoca a la función changeFridgeTemp() cuando se selecciona un ítem del spinner del fridge
        spinFridge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeFridgeTemp();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no se selecciono ninguna opción -> no hace nada
            }
        });

        // invoca a la función changeFreezerTemp() cuando se selecciona un ítem del spinner del freezer
        spinFreezer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeFreezerTemp();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no se selecciono ninguna opción -> no hace nada
            }
        });

    }

    // CERRAMOS EL ON.CREATE, ARRANCAMOS CON LAS FUNCIONES QUE SE IMPLEMENTAN AL TOCAR LOS BOTONES


    private void changeMode() {
        api.changeRefrigeratorMode(deviceId, spinMode.getSelectedItem().toString().toLowerCase(), new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        updateStateOnce();
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
                        updateStateOnce();
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
                        updateStateOnce();
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

    private void updateStateOnce() {
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
                        //  ErrorHandler.handleError(response, requireView(), getString(R.string.error_1_string));
                    }
                } else {
                  //  ErrorHandler.handleError(response, requireView(), getString(R.string.error_1_string));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<RefrigeratorState>> call, @NonNull Throwable t) {
              //  ErrorHandler.handleUnexpectedError(t, requireView(), RefrigeratorControllerFragment.this);
            }
        });
    }

}

