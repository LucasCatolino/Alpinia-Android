package com.example.Alpinia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    private Button createRoomButton;
    private Button modifyRoomButton;
    private Button deleteRoomButton;
    private Button getRoomButton;
    private Button getRoomsButton;
    private TextView resultTextView;
    private Room room;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    //Mostrar el overflow
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    //Asignar las funciones al overflow
    public boolean onOptionsItemSelected (MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.overflow_refresh) {
            Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_SHORT).show();
        } else if (id== R.id.overflow_settings) {
            Intent i= new Intent(this, SettingsActivity.class);
            startActivity(i);
/*
            FragmentSettings fragmentSettings= new FragmentSettings();
            FragmentTransaction transition= getSupportFragmentManager().beginTransaction();
            transition.replace(R.id.nav_host_fragment, fragmentSettings);
            transition.commit();
*/
            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getHomeId(){
        if(getIntent().hasExtra("homeId"))
            return getIntent().getExtras().get("homeId").toString();
        return null;
    }
    @Override
    protected void onStop() {
        super.onStop();

        //TODO: cancel Call<T>
    }

}