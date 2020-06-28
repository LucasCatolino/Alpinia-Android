package com.example.Alpinia;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Alpinia.API.ApiClient;
import com.example.Alpinia.API.objects.Error;
import com.example.Alpinia.API.objects.Home;
import com.example.Alpinia.API.objects.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomesAdapter extends RecyclerView.Adapter<HomesAdapter.HomeViewHolder>{
    Context context;
    List<Home> homes;

    public HomesAdapter(Context ct, List<Home> homeList){
        context = ct;
        homes = homeList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.name.setText(homes.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("homeId",homes.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(context.getResources().getString(R.string.alertwarn)).setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ApiClient.getInstance().deleteHome(homes.get(position).getId(), new Callback<Result<Boolean>>() {
                                    @Override
                                    public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                                        if(response.isSuccessful())
                                        {
                                            context.startActivity(new Intent(context, context.getClass()));
                                        }
                                        else{
                                            handleError(response);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                                        handleUnexpectedError(t);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog myAlert = alert.create();
                alert.setTitle("Delete item");
                alert.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return homes.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView homeIcon;
        ImageView trash;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            trash = itemView.findViewById(R.id.go_to_rooms);
            homeIcon = itemView.findViewById(R.id.homeImg);
            name = itemView.findViewById(R.id.tvName);
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
