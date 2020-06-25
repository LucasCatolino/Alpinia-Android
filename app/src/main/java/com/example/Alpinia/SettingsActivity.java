package com.example.Alpinia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Alpinia.API.ApiClient;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotifications;
    EditText newIPAddress;
    TextView textViewIP;
    private Button buttonApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switchNotifications = (Switch) findViewById(R.id.switch1);
        newIPAddress = (EditText) findViewById(R.id.settings_IPAddress);
        buttonApply = (Button) findViewById(R.id.btnSaveChangesOnIP);
        textViewIP = (TextView) findViewById((R.id.textViewIP));

        textViewIP.setText(ApiClient.getInstance().getBaseURL());

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
            textViewIP.setText(ApiClient.getInstance().getBaseURL());
            Toast.makeText(getApplicationContext(), "IP Address: "+ ApiClient.getInstance().getBaseURL(), Toast.LENGTH_LONG).show();
        }

}
