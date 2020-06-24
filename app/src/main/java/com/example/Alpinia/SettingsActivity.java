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

        private void switchNotifications(Boolean isChecked) {
            //TODO
        }

        private void changeIPAddress() {
            ApiClient.getInstance().setBaseURL(newIPAddress.getText().toString());
            Toast.makeText(getApplicationContext(), "IP Address: "+ ApiClient.getInstance().getBaseURL(), Toast.LENGTH_LONG).show();
            //TODO
        }

}
