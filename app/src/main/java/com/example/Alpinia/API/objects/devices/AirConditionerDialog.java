package com.example.Alpinia.API.objects.devices;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirConditionerDialog extends AppCompatActivity {
    // creo las variables con las que voy a trabajar
    Context context;
    private ApiClient api;
    private String deviceId;
    private Spinner spinMode;
    private Spinner spinTemperature;
    private Spinner spinVerticalSwing;
    private Spinner spinHorizontalSwing;
    private Spinner spinSpeed;
    private Switch switchOpenClose;
    AirConditionerState myState;



    // sobreescribo el onCreate para relacionarlo con el layout del aire acondicionado
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = (Context) this;
        setContentView(R.layout.ac_card);
        // instancio la API
        api = ApiClient.getInstance();
        deviceId = getIntent().getStringExtra("deviceId");
        // hago que las variables creadas referencian a los botones del layout
        spinMode = findViewById(R.id.spin_mode_ac);
        spinTemperature = findViewById(R.id.spin_temperature_ac);
        spinVerticalSwing = findViewById(R.id.spin_vertical_ac);
        spinHorizontalSwing = findViewById(R.id.spin_horizontal_ac);
        spinSpeed = findViewById(R.id.spin_speed_ac);
        switchOpenClose = findViewById(R.id.open_close_ac);

        /*

     VER TEMA DEL SWITCH ON Y OFF


        switchOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myState.isOpen()){
                    closeAc();
                }
                else{
                    openAc();
                }
            }
        });

         */

        // obtengo el estado del aire acondicionado
        api.getAirConditionerState(deviceId, new Callback<Result<AirConditionerState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<AirConditionerState>> call, @NonNull Response<Result<AirConditionerState>> response) {
                if(response.isSuccessful()) {
                    Result<AirConditionerState> result = response.body();
                    if(result != null) {
                        AirConditionerState acState = result.getResult();
                        //temperature_aux = tempratura actual del aire acondicionado (en la API)
                        Integer temperature_aux = acState.getTemperature();
                        // temperature_aux2 = índice de la tempratura en la que está el aire acondicionado (que índice en el arreglo ocupa) -> estamos trabajando con Spinners
                        // le paso la temperatura y me devuelve el índice que ocupa en el arreglo -> función getIndex
                        int temperature_aux2 = getIndex(spinTemperature, temperature_aux.toString());
                        // en la interfaz selecciono el índice que me devuelve la función
                        spinTemperature.setSelection(temperature_aux2);
                        // la API me devuelve el modo en el que está el aire acondicionado e itero sobre las 3 opciones posibles para seleccionar el índice adecuado
                        String mode_aux = acState.getMode();
                        if(mode_aux.equals("ventilation")){
                            spinMode.setSelection(0);
                        } else if(mode_aux.equals("cold")){
                            spinMode.setSelection(1);
                        } else if(mode_aux.equals("hot")){
                            spinMode.setSelection(2);
                        }
                        // MISMO PROCEDIMIENTO PARA EL ALA HORIZONTAL, VERTICAL Y LA VELOCIDAD
                        // helice vertical
                        String vertical_aux = acState.getVerticalSwing();
                        if(vertical_aux.equals("default")){
                            spinVerticalSwing.setSelection(0);
                        } else if(vertical_aux.equals("22")){
                            spinVerticalSwing.setSelection(1);
                        } else if(vertical_aux.equals("45")){
                            spinVerticalSwing.setSelection(2);
                        } else if(vertical_aux.equals("67")){
                            spinVerticalSwing.setSelection(3);
                        } else if(vertical_aux.equals("90")){
                            spinVerticalSwing.setSelection(4);
                        }
                        // helice horizontal
                        String horizontal_aux = acState.getHorizontalSwing();
                        if(horizontal_aux.equals("default")){
                            spinHorizontalSwing.setSelection(0);
                        } else if(horizontal_aux.equals("-90")){
                            spinHorizontalSwing.setSelection(1);
                        } else if(horizontal_aux.equals("-45")){
                            spinHorizontalSwing.setSelection(2);
                        } else if(horizontal_aux.equals("0")){
                            spinHorizontalSwing.setSelection(3);
                        } else if(horizontal_aux.equals("45")){
                            spinHorizontalSwing.setSelection(4);
                        } else if(horizontal_aux.equals("90")){
                            spinHorizontalSwing.setSelection(5);
                        }
                        // velocidad del aire acondicionado
                        String speed_aux = acState.getFanSpeed();
                        if(speed_aux.equals("default")){
                            spinSpeed.setSelection(0);
                        } else if(speed_aux.equals("25")){
                            spinSpeed.setSelection(1);
                        } else if(speed_aux.equals("50")){
                            spinSpeed.setSelection(2);
                        } else if(speed_aux.equals("75")){
                            spinSpeed.setSelection(3);
                        } else if(speed_aux.equals("100")){
                            spinSpeed.setSelection(4);
                        }

                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<AirConditionerState>> call, @NonNull Throwable t) {
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

        spinTemperature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeTemp();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no se selecciono ninguna opción -> no hace nada
            }
        });

        // invoca a la función changeVerticalSwing() cuando se selecciona un ítem del spinner de la helice vertical
        spinVerticalSwing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeVerticalSwing();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no se selecciono ninguna opción -> no hace nada
            }
        });

        // invoca a la función changeHorizontalSwing() cuando se selecciona un ítem del spinner de la helice horizontal
        spinHorizontalSwing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeHorizontalSwing();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no se selecciono ninguna opción -> no hace nada
            }
        });
        // invoca a la función  changeSpeed() cuando se selecciona un ítem del spinner de la velocidad del aire
        spinSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeSpeed();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // no se selecciono ninguna opción -> no hace nada
            }
        });
    }

    // CERRAMOS EL ON.CREATE, ARRANCAMOS CON LAS FUNCIONES QUE SE IMPLEMENTAN AL TOCAR LOS BOTONES


    private void changeMode() {
        api.changeAirConditionerMode(deviceId, spinMode.getSelectedItem().toString().toLowerCase(), new Callback<Result<Boolean>>() {
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

    private void changeTemp() {
        api.setAcTemp(deviceId, Integer.parseInt(spinTemperature.getSelectedItem().toString()), new Callback<Result<Integer>>() {
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

    private void changeVerticalSwing() {
        api.setVerticalSwing(deviceId, spinVerticalSwing.getSelectedItem().toString().toLowerCase(), new Callback<Result<Boolean>>() {
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

    private void changeHorizontalSwing() {
        api.setHorizontalSwing(deviceId, spinHorizontalSwing.getSelectedItem().toString().toLowerCase(), new Callback<Result<Boolean>>() {
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

    private void changeSpeed() {
        api.setFanSpeed(deviceId, spinSpeed.getSelectedItem().toString().toLowerCase(), new Callback<Result<Boolean>>() {
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

    // FUNCION AUXILIAR -> me indica la posición en el arreglo
    private int getIndex(Spinner spinner, String myString){
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return -1;
    }

    private void updateStateOnce() {
        api.getAirConditionerState(deviceId, new Callback<Result<AirConditionerState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<AirConditionerState>> call, @NonNull Response<Result<AirConditionerState>> response) {
                if(response.isSuccessful()) {
                    Result<AirConditionerState> result = response.body();
                    if(result != null) {
                        AirConditionerState acState = result.getResult();
                        //temperature_aux = tempratura actual del aire acondicionado (en la API)
                        Integer temperature_aux = acState.getTemperature();
                        // temperature_aux2 = índice de la tempratura en la que está el aire acondicionado (que índice en el arreglo ocupa) -> estamos trabajando con Spinners
                        // le paso la temperatura y me devuelve el índice que ocupa en el arreglo -> función getIndex
                        int temperature_aux2 = getIndex(spinTemperature, temperature_aux.toString());
                        // en la interfaz selecciono el índice que me devuelve la función
                        spinTemperature.setSelection(temperature_aux2);
                        // la API me devuelve el modo en el que está el aire acondicionado e itero sobre las 3 opciones posibles para seleccionar el índice adecuado
                        String mode_aux = acState.getMode();
                        if(mode_aux.equals("ventilation")){
                            spinMode.setSelection(0);
                        } else if(mode_aux.equals("cold")){
                            spinMode.setSelection(1);
                        } else if(mode_aux.equals("hot")){
                            spinMode.setSelection(2);
                        }
                        // MISMO PROCEDIMIENTO PARA EL ALA HORIZONTAL, VERTICAL Y LA VELOCIDAD
                        // helice vertical
                        String vertical_aux = acState.getVerticalSwing();
                        if(vertical_aux.equals("default")){
                            spinVerticalSwing.setSelection(0);
                        } else if(vertical_aux.equals("22")){
                            spinVerticalSwing.setSelection(1);
                        } else if(vertical_aux.equals("45")){
                            spinVerticalSwing.setSelection(2);
                        } else if(vertical_aux.equals("67")){
                            spinVerticalSwing.setSelection(3);
                        } else if(vertical_aux.equals("90")){
                            spinVerticalSwing.setSelection(4);
                        }
                        // helice horizontal
                        String horizontal_aux = acState.getHorizontalSwing();
                        if(horizontal_aux.equals("default")){
                            spinHorizontalSwing.setSelection(0);
                        } else if(horizontal_aux.equals("-90")){
                            spinHorizontalSwing.setSelection(1);
                        } else if(horizontal_aux.equals("-45")){
                            spinHorizontalSwing.setSelection(2);
                        } else if(horizontal_aux.equals("0")){
                            spinHorizontalSwing.setSelection(3);
                        } else if(horizontal_aux.equals("45")){
                            spinHorizontalSwing.setSelection(4);
                        } else if(horizontal_aux.equals("90")){
                            spinHorizontalSwing.setSelection(5);
                        }
                        // velocidad del aire acondicionado
                        String speed_aux = acState.getFanSpeed();
                        if(speed_aux.equals("default")){
                            spinSpeed.setSelection(0);
                        } else if(speed_aux.equals("25")){
                            spinSpeed.setSelection(1);
                        } else if(speed_aux.equals("50")){
                            spinSpeed.setSelection(2);
                        } else if(speed_aux.equals("75")){
                            spinSpeed.setSelection(3);
                        } else if(speed_aux.equals("100")){
                            spinSpeed.setSelection(4);
                        }

                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<AirConditionerState>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });
    }

    /*

    FALTA IMPLEMENTAR EL BOTON DE ON Y OFF

    private void openAc() {
        api.openAc(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    spinTemperature.setVisibility(View.VISIBLE);
                    spinHorizontalSwing.setVisibility(View.VISIBLE);
                    spinMode.setVisibility(View.VISIBLE);
                    spinVerticalSwing.setVisibility(View.VISIBLE);
                    spinSpeed.setVisibility(View.VISIBLE);
                    switchOpenClose.setChecked(myState.isOpen());
                }
                else {
                    //
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                //
            }
        });
    }


    private void closeAc() {
        api.closeDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    updateState();
                    spinTemperature.setVisibility(View.INVISIBLE);
                    spinHorizontalSwing.setVisibility(View.INVISIBLE);
                    spinMode.setVisibility(View.INVISIBLE);
                    spinVerticalSwing.setVisibility(View.INVISIBLE);
                    spinSpeed.setVisibility(View.INVISIBLE);
                    switchOpenClose.setChecked(myState.isOpen());
                }
                else {
                  //  handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
              //  handleUnexpectedError(t);
            }
        });
    }

    private void updateState() {
        api.getAirConditionerState(deviceId, new Callback<Result<AirConditionerState>>() {
            @Override
            public void onResponse(Call<Result<AirConditionerState>> call, Response<Result<AirConditionerState>> response) {
                if(response.isSuccessful()){
                    Result<AirConditionerState> result = response.body();
                    myState = result.getResult();
                    switchOpenClose.setChecked(myState.isOpen());
                }
                else{
                  //  handleError(response);
                }

            }

            @Override
            public void onFailure(Call<Result<AirConditionerState>> call, Throwable t) {
               // handleUnexpectedError(t);
            }
        });
    }

     */

}

