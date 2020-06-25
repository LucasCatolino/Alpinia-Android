package com.example.Alpinia.API.objects.devices;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.R;
import com.example.Alpinia.API.objects.Result;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoorDialog extends Fragment {

    private ApiClient api;
    private String deviceId;
    private Switch switchOpenClose, switckLockUnlock;
    private boolean isLocked, isOpen;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.door_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        readBundle(getArguments());
        init(requireView());
    }

    private void init(View view) {
        api = ApiClient.getInstance();
        switchOpenClose = view.findViewById(R.id.open_close_door);
        switckLockUnlock = view.findViewById(R.id.lock_unlock_switch);

        api.getDoorState(deviceId, new Callback<Result<DoorState>>() {
            @Override
            public void onResponse(@NonNull Call<Result<DoorState>> call, @NonNull Response<Result<DoorState>> response) {
                if(response.isSuccessful()) {
                    Result<DoorState> result = response.body();
                    if(result != null) {
                        DoorState doorState = result.getResult();
                        isOpen = doorState.isOpen();
                        isLocked = doorState.isLocked();
                        System.out.println("isOpen: " + isOpen);
                        System.out.println("isLocked: " + isLocked);
                        if(isOpen)
                            switchOpenClose.setEnabled(false);
                        if(isLocked)
                            switckLockUnlock.setEnabled(false);
                        switckLockUnlock.setChecked(isLocked);
                        switchOpenClose.setChecked(!isOpen);
                        if(switchOpenClose != null && switckLockUnlock != null) {
                            switchOpenClose.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    closeDoor();
                                } else {
                                    openDoor();
                                }
                            });
                            switckLockUnlock.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    lockDoor();
                                } else {
                                    unlockDoor();
                                }
                            });
                        }
                    } else {
                        //manejo de error
                    }
                } else {
                    //manejo de error
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<DoorState>> call, @NonNull Throwable t) {
                //manejo de error
            }
        });
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            deviceId = bundle.getString("deviceId");
        }
    }

    @NonNull
    public static DoorDialog newInstance(String deviceId) {
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);

        DoorDialog fragment = new DoorDialog();
        fragment.setArguments(bundle);

        return fragment;
    }


    // ABRO LA PUERTA
    private void openDoor() {
        if(isOpen) {
            Toast.makeText(getContext(), "THE DOOR WAS ALREADY OPENED !!!!", Toast.LENGTH_LONG).show();
            return;
        }
        if(isLocked) {
            Toast.makeText(getContext(), "THE DOOR IS LOCKED, WE CANNOT OPENED IT", Toast.LENGTH_LONG).show();
            return;
        }

        api.openDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        Toast.makeText(getContext(), "OPENING DOOR....", Toast.LENGTH_LONG).show();
                        isOpen = true;
                        switchOpenClose.setEnabled(false);
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

    //CIERRO LA PUERTA
    private void closeDoor() {
        if(!isOpen || isLocked) {
            Toast.makeText(getContext(), "THE DOOR WAS ALREADY CLOSED !!!!", Toast.LENGTH_LONG).show();
            return;
        }
        api.closeDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        Toast.makeText(getContext(), "CLOSING DOOR....", Toast.LENGTH_LONG).show();
                        isOpen = false;
                        switchOpenClose.setEnabled(true);
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


    //BLOQUEO LA PUERTA
    private void lockDoor() {
        if(isLocked) {
            Toast.makeText(getContext(), "THE DOOR WAS ALREADY LOCKED !!!!", Toast.LENGTH_LONG).show();
            return;
        }
        if(isOpen) {
            Toast.makeText(getContext(), "THE DOOR IS OPEN", Toast.LENGTH_LONG).show();
            return;
        }

        api.lockDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        Toast.makeText(getContext(), "LOCKING DOOR....", Toast.LENGTH_LONG).show();
                        isLocked = true;
                        switckLockUnlock.setEnabled(false);
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


    //DESBLOQUEO LA PUERTA
    private void unlockDoor() {
        if(isOpen || !isLocked) {
            Toast.makeText(getContext(), "THE DOOR WAS ALREADY UNLOCKED !!!!", Toast.LENGTH_LONG).show();
            return;
        }

        api.unlockDoor(deviceId, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if(response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    if(result != null) {
                        Toast.makeText(getContext(), "UNLOCKING DOOR....", Toast.LENGTH_LONG).show();
                        isLocked = false;
                        switckLockUnlock.setEnabled(true);
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
}
