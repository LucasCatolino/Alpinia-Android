package com.example.Alpinia.API;

import com.example.Alpinia.API.objects.Device;
import com.example.Alpinia.API.objects.Routine;
import com.example.Alpinia.API.objects.devices.AirConditioner;
import com.example.Alpinia.API.objects.devices.AirConditionerState;
import com.example.Alpinia.API.objects.devices.DoorState;
import com.example.Alpinia.API.objects.devices.FaucetState;
import com.example.Alpinia.API.objects.Home;
import com.example.Alpinia.API.objects.devices.LightsState;
import com.example.Alpinia.API.objects.devices.RefrigeratorState;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.API.objects.Room;
import com.example.Alpinia.API.objects.devices.Song;
import com.example.Alpinia.API.objects.devices.SpeakerState;
import com.example.Alpinia.API.objects.devices.VacuumState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    //--------------------------- HOMES -----------------------------

    @POST("homes")
    @Headers("Content-Type: application/json")
    Call<Result<Home>> addHome(@Body Home home);

    @PUT("homes/{homeId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> modifyRoom(@Path("homesId") String homeId, @Body Home home);

    @DELETE("homes/{homeId}")
    Call<Result<Boolean>> deleteHome(@Path("homeId") String homeId);

    @GET("homes/{homeId}")
    Call<Result<Room>> getHome(@Path("homeId") String homeId);

    @GET("homes")
    Call<Result<List<Home>>> getHomes();



    //--------------------------- ROOMS -----------------------------

    @POST("rooms")
    @Headers("Content-Type: application/json")
    Call<Result<Room>> addRoom(@Body Room room);

    @PUT("rooms/{roomId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> modifyRoom(@Path("roomId") String roomId, @Body Room room);

    @DELETE("rooms/{roomId}")
    Call<Result<Boolean>> deleteRoom(@Path("roomId") String roomId);

    @GET("rooms/{roomId}")
    Call<Result<Room>> getRoom(@Path("roomId") String roomId);

    @GET("rooms")
    Call<Result<List<Room>>> getRooms();

    @GET("homes/{homeId}/rooms")
    Call<Result<List<Room>>> getHomeRooms(@Path("homeId") String homeId);

    @POST("homes/{homeId}/rooms/{roomId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> addRoomToHome(@Path("homeId")String homeId,@Path("roomId") String roomId);



    //--------------------------- DEVICES -----------------------------

    @POST("devices")
    @Headers("Content-Type: application/json")
    Call<Result<Device>> addDevice(@Body Device device);

    @GET("rooms/{roomId}/devices")
    @Headers("Content-Type: application/json")
    Call<Result<List<Device>>> getRoomDevices(@Path("roomId") String roomId);

    @POST("rooms/{roomId}/devices/{deviceId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> addDeviceToRoom(@Path("roomId")String roomId,@Path("deviceId") String deviceId);

    @DELETE("devices/{deviceId}")
    Call<Result<Boolean>> deleteDevice(@Path("deviceId") String deviceId);


    //--------------------------- ROUTINES -----------------------------

    @GET("routines")
    Call<Result<List<Routine>>> getRoutines();

    @POST("routines")
    @Headers("Content-Type: application/json")
    Call<Result<Routine>> addRoutine(@Body Routine routine);

    @DELETE("routines/{routineId}")
    Call<Result<Boolean>> deleteRoutine(@Path("routineId") String routineId);

    @GET("routines/{routineId}")
    Call<Result<Routine>> getRoutine(@Path("routineId") String routineId);

    @PUT("routines/{routineId}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> modifyRoutine(@Path("routineId") String routineId, @Body Routine routine);

    @PUT("routines/{routineId}/execute")
    @Headers("Content-Type: application/json")
    Call<Result<List<String>>> executeRoutine(@Path("routineId") String routineId);





    //--------------------------- LIGHTS -----------------------------


    @GET("devices/{deviceId}/state")
    Call<Result<LightsState>> getLightState(@Path("deviceId") String deviceId);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> turnOnOrOffLight(@Path("deviceId") String deviceId, @Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Integer>> setLightIntensity(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body Integer [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<String>> setLightColor(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body String [] data);


    //--------------------------- DOOR -----------------------------

    @GET("devices/{deviceId}/state")
    Call<Result<DoorState>> getDoorState(@Path("deviceId") String deviceId);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> executeActionOnDoor(@Path("deviceId") String deviceId, @Path("actionName") String actionName);


    //--------------------------- FAUCET -----------------------------

    @GET("devices/{deviceId}/state")
    Call<Result<FaucetState>> getFaucetState(@Path("deviceId") String deviceId);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> openOrCloseFaucet(@Path("deviceId") String deviceId, @Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> dispenseExactAmount(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body ArrayList<Object> data);



    //--------------------------- REFRIGERATOR -----------------------------

    @GET("devices/{deviceId}/state")
    Call<Result<RefrigeratorState>> getRefrigeratorState(@Path("deviceId") String deviceId);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> changeRefrigeratorMode(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body String [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Integer>> setFridgeTemp(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body Integer [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Integer>> setFreezerTemp(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body Integer [] data);

    //---------------------SPEAKERS--------------------------------------------

    @GET("devices/{deviceId}/state")
    Call<Result<SpeakerState>> getSpeakerState(@Path("deviceId") String deviceId);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Integer>> setSpeakerVolume(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body Integer [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> play(@Path("deviceId") String deviceId, @Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> stop(@Path("deviceId") String deviceId,@Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> pause(@Path("deviceId") String deviceId,@Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> resume(@Path("deviceId") String deviceId,@Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> nextSong(@Path("deviceId") String deviceId,@Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> previousSong(@Path("deviceId") String deviceId,@Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<String>> setGenre(@Path("deviceId") String deviceId,@Path("actionName") String actionName,@Body String [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<List<Song>>> getPlaylist(@Path("deviceId") String deviceId,@Path("actionName") String actionName);
    //-----------------------------VACUUM---------------------

    @GET("devices/{deviceId}/state")
    Call<Result<VacuumState>> getVacuumState(@Path("deviceId") String deviceId);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> startVacuum(@Path("deviceId") String deviceId, @Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> pauseVacuum(@Path("deviceId") String deviceId, @Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> dockVacuum(@Path("deviceId") String deviceId, @Path("actionName") String actionName);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<String>> setVacuumMode(@Path("deviceId") String deviceId, @Path("actionName") String actionName,@Body String [] data);



    //---------------------AIR CONDITIONER-------------------------

    @GET("devices/{deviceId}/state")
    Call<Result<AirConditionerState>> getAirConditionerState(@Path("deviceId") String deviceId);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> changeAirConditionerMode(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body String [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Integer>> setAcTemp(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body Integer [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> setVerticalSwing(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body String [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> setHorizontalSwing(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body String [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> setFanSpeed(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body String [] data);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Boolean>> executeActionOnAc(@Path("deviceId") String deviceId, @Path("actionName") String actionName);

}
