package com.example.Alpinia;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.Alpinia.ui.home.HomeViewModel;

import java.io.Console;
import java.sql.SQLOutput;
import java.util.List;

import static java.lang.Thread.sleep;

public class FragmentHome extends Fragment {


    private Button createRoomButton;
    private Button modifyRoomButton;
    private Button deleteRoomButton;
    private Button getRoomButton;
    private Button getRoomsButton;
    private TextView resultTextView;
    private Room room;
    private int index = 1;

    RecyclerView recyclerView;
    List<Room> roomsList;
    Context context;
    Button addRoomBttn;
    EditText newRoomName;
    private String homeId;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);
        context = (Context) getActivity();

        MainActivity activity = (MainActivity) getActivity();

        homeId = activity.getHomeId();

        addRoomBttn = (Button) vista.findViewById(R.id.btnAddRoom);
        newRoomName = (EditText) vista.findViewById(R.id.newRoom);
        recyclerView = (RecyclerView) vista.findViewById(R.id.roomList);

        if (addRoomBttn != null) {
            addRoomBttn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    createRoom();
                }
            });
        }
        getHomeRooms();
        return vista;
    }

    private void createRoom() {
        Toast.makeText(getContext(), "Creating Room", Toast.LENGTH_LONG).show();

        room = new Room(newRoomName.getText().toString(), new RoomMeta("9m2"));
        ApiClient.getInstance().addRoom(room, new Callback<Result<Room>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Room>> call, @NonNull Response<Result<Room>> response) {
                if (response.isSuccessful()) {

                    Result<Room> result = response.body();
                    if (result != null) {
                        room.setId(result.getResult().getId());

                        ApiClient.getInstance().addRoomToHome(homeId,room.getId(), new Callback<Result<Boolean>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {

                                if (response.isSuccessful()){
                                    getHomeRooms();
                                } else {
                                    handleError(response);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                                handleUnexpectedError(t);
                            }
                        });

                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Room>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });

   }




    private void getHomeRooms() {

        ApiClient.getInstance().getHomeRooms(homeId, new Callback<Result<List<Room>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Room>>> call, @NonNull Response<Result<List<Room>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Room>> result = response.body();
                    roomsList = result.getResult();
                    System.out.println(result.getResult().toString());
                    if(roomsList != null){
                        RoomsAdapter myAdapter = new RoomsAdapter(context,roomsList);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));}

                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Room>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }


    private void showResult(String result) {
        String format = getResources().getString(R.string.result);
        resultTextView.setText(String.format(format, result));
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
}