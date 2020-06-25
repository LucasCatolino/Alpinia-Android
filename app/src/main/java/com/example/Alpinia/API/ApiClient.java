package com.example.Alpinia.API;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.devices.DoorState;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.ErrorResult;
import com.example.Alpinia.API.objects.devices.FaucetState;
import com.example.Alpinia.API.objects.Home;
import com.example.Alpinia.API.objects.devices.LightsState;
import com.example.Alpinia.API.objects.devices.RefrigeratorState;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.API.objects.Room;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private Retrofit retrofit = null;
    private ApiService service = null;
    private static ApiClient instance = null;
    // Use IP 10.0.2.2 instead of 127.0.0.1 when running Android emulator in the
    // same computer that runs the API.
    private String BaseURL = "http://10.0.2.2:8080/api/";

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.service = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public void setBaseURL(String newURL){
        String a= "http://";
        String b= ":8080/api/";
        BaseURL=a+newURL+b;
        retrofit = new Retrofit.Builder() .baseUrl(BaseURL) .addConverterFactory(GsonConverterFactory.create()).build();
        this.service= retrofit.create(ApiService.class);
    }
    public Error getError(ResponseBody response) {
        Converter<ResponseBody, ErrorResult> errorConverter =
                this.retrofit.responseBodyConverter(ErrorResult.class, new Annotation[0]);
        try {
            ErrorResult responseError = errorConverter.convert(response);
            return responseError.getError();
        } catch (IOException e) {
            return null;
        }
    }

    public Call<Result<Room>> addRoom(Room room, Callback<Result<Room>> callback) {
        Call<Result<Room>> call = this.service.addRoom(room);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> modifyRoom(Room room, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.modifyRoom(room.getId(), room);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> deleteRoom(String roomId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.deleteRoom(roomId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Room>> getRoom(String roomId, Callback<Result<Room>> callback) {
        Call<Result<Room>> call = this.service.getRoom(roomId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Room>>> getRooms(Callback<Result<List<Room>>> callback) {
        Call<Result<List<Room>>> call = this.service.getRooms();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Device>> addDevice(Device device, Callback<Result<Device>> callback) {
        Call<Result<Device>> call = this.service.addDevice(device);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Home>>> getHomes(Callback<Result<List<Home>>> callback) {
        Call<Result<List<Home>>> call = this.service.getHomes();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Home>> addHome(Home home, Callback<Result<Home>> callback) {
        Call<Result<Home>> call = this.service.addHome(home);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Room>>> getHomeRooms(String homeId, Callback<Result<List<Room>>> callback) {
        Call<Result<List<Room>>> call = this.service.getHomeRooms(homeId);
        call.enqueue(callback);
        return call;
    }


    public Call<Result<Boolean>> addRoomToHome(String homeId,  String roomId, Callback<Result<Boolean>> callback){

        Call<Result<Boolean>> call = this.service.addRoomToHome(homeId, roomId);
        call.enqueue(callback);
        return call;
    };

    //--------------------------DEVICES-------------------------------------------


    public Call<Result<Boolean>> addDeviceToRoom(String roomId,  String deviceId, Callback<Result<Boolean>> callback){

        Call<Result<Boolean>> call = this.service.addDeviceToRoom(roomId, deviceId);
        call.enqueue(callback);
        return call;
    };

    public Call<Result<List<Device>>> getRoomDevices(String roomId, Callback<Result<List<Device>>> callback) {
        Call<Result<List<Device>>> call = this.service.getRoomDevices(roomId);
        call.enqueue(callback);
        return call;
    }



    //------------------------- LIGHTS ----------------------------------------


    public Call<Result<LightsState>> getLightState(String deviceId, Callback<Result<LightsState>> callback) {
        Call<Result<LightsState>> call = this.service.getLightState(deviceId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> turnLightsOn(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.turnOnOrOffLight(deviceId, "turnOn");
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> turnLightsOff(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.turnOnOrOffLight(deviceId, "turnOff");
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Integer>> setLightIntensity(String deviceId, Integer newIntensity, Callback<Result<Integer>> callback) {
        Integer [] aux = new Integer[1];
        aux[0] = newIntensity;

        Call<Result<Integer>> call = this.service.setLightIntensity(deviceId, "setIntensity", aux);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<String>> setLightColor(String deviceId, String newColor, Callback<Result<String>> callback) {
        String [] aux = new String[1];
        aux[0] = newColor;

        Call<Result<String>> call = this.service.setLightColor(deviceId, "setColor", aux);
        call.enqueue(callback);
        return call;
    }


    //------------------------- DOOR ----------------------------------------

    public Call<Result<DoorState>> getDoorState(String deviceId, Callback<Result<DoorState>> callback) {
        Call<Result<DoorState>> call = this.service.getDoorState(deviceId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> openDoor(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.executeActionOnDoor(deviceId, "open");
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> closeDoor(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.executeActionOnDoor(deviceId, "close");
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> lockDoor(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.executeActionOnDoor(deviceId, "lock");
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> unlockDoor(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.executeActionOnDoor(deviceId, "unlock");
        call.enqueue(callback);
        return call;
    }

    //------------------------- FAUCET ----------------------------------------

    public Call<Result<FaucetState>> getFaucetState(String deviceId, Callback<Result<FaucetState>> callback) {
        Call<Result<FaucetState>> call = this.service.getFaucetState(deviceId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> openFaucet(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.openOrCloseFaucet(deviceId, "open");
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> closeFaucet(String deviceId, Callback<Result<Boolean>> callback) {
        Call<Result<Boolean>> call = this.service.openOrCloseFaucet(deviceId, "close");
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> dispenseExactAmount(String deviceId, Integer amount, String unit, Callback<Result<Boolean>> callback) {
        ArrayList<Object> aux = new ArrayList<>();
        aux.add(amount);
        aux.add(unit);
        Call<Result<Boolean>> call = this.service.dispenseExactAmount(deviceId, "setLevel", aux);
        call.enqueue(callback);
        return call;
    }

    public String getBaseURL() {
																						   
							   
        return BaseURL;
    }


    //------------------------- REFRIGERATOR ----------------------------------------

    public Call<Result<RefrigeratorState>> getRefrigeratorState(String deviceId, Callback<Result<RefrigeratorState>> callback) {
        Call<Result<RefrigeratorState>> call = this.service.getRefrigeratorState(deviceId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Boolean>> changeRefrigeratorMode(String deviceId, String newMode, Callback<Result<Boolean>> callback) {
        String [] aux = new String[1];
        aux[0] = newMode;

        Call<Result<Boolean>> call = this.service.changeRefrigeratorMode(deviceId, "setMode", aux);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Integer>> setFridgeTemp(String deviceId, Integer newTemp, Callback<Result<Integer>> callback) {
        Integer [] aux = new Integer[1];
        aux[0] = newTemp;

        Call<Result<Integer>> call = this.service.setFridgeTemp(deviceId, "setTemperature", aux);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Integer>> setFreezerTemp(String deviceId, Integer newTemp, Callback<Result<Integer>> callback) {
        Integer [] aux = new Integer[1];
        aux[0] = newTemp;

        Call<Result<Integer>> call = this.service.setFreezerTemp(deviceId, "setFreezerTemperature", aux);
        call.enqueue(callback);
        return call;
    }







}
