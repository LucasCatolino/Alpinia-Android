package com.example.Alpinia;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotifications;
    EditText newIPAddress;
    private Button buttonApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);//refactor fragment a activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switchNotifications = (Switch) findViewById(R.id.switch1);
        newIPAddress = (EditText) findViewById(R.id.settings_IPAddress);
        buttonApply = (Button) findViewById(R.id.btnSaveChangesOnIP);

        if (buttonApply != null) {
            buttonApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeIPAddress();
                }
            });
        }

        if (switchNotifications != null) {
            switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        switchNotifications(isChecked);
                    } else {
                        switchNotifications(isChecked);
                    }
                }
            });
        }
    }
/*
    //Mostrar el overflow
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    //Asignar las funciones al overflow
    public boolean onOptionsItemSelected (MenuItem item) {
        int id= item.getItemId();
        if (id== R.id.overflow_refresh) {
            Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_SHORT).show();
        } else if (id== R.id.overflow_settings) {

            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
*/
        private void switchNotifications(Boolean isChecked) {
            //TODO
        }

        private void changeIPAddress() {
            //TODO
        }

}