package com.example.Alpinia;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Result;
import com.example.Alpinia.API.objects.Routine;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>{
    Context context;
    List<Routine> routines;

    public RoutineAdapter(Context ct, List<Routine> routineList){
        context = ct;
        routines = routineList;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_routine,parent,false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        holder.name.setText(routines.get(position).getName());
        holder.executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.getInstance().executeRoutine(routines.get(position).getId(), new Callback<Result<List<String>>>() {
                    @Override
                    public void onResponse(Call<Result<List<String>>> call, Response<Result<List<String>>> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context,"Routine executed",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            handleError(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<List<String>>> call, Throwable t) {
                        handleUnexpectedError(t);
                    }
                });
            }
        });
        holder.description.setText(routines.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public class RoutineViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView routineIcon;
        Button executeButton;
        TextView description;
        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            routineIcon = itemView.findViewById(R.id.roomImg);
            name = itemView.findViewById(R.id.routine_name);
            executeButton = itemView.findViewById(R.id.execute);
            description =  itemView.findViewById(R.id.routine_description);
        }

    }
    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = context.getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "App";
        Log.e(LOG_TAG, t.toString());
    }

}