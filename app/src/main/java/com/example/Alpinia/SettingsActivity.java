package com.example.Alpinia;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Alpinia.API.ApiClient;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    EditText newIPAddress;
    TextView textViewIP;
    TextView settings_notifications;
    private Button buttonApply;
    private Button buttonManageNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonManageNotifications = (Button) findViewById(R.id.btnSaveChangesOnNotifications);
        newIPAddress = (EditText) findViewById(R.id.settings_IPAddress);
        buttonApply = (Button) findViewById(R.id.btnSaveChangesOnIP);
        textViewIP = (TextView) findViewById((R.id.textViewIP));
        settings_notifications = (TextView) findViewById(((R.id.settings_notifications)));


        textViewIP.setText(ApiClient.getInstance().getBaseURL());

        if (buttonManageNotifications != null) {
            buttonManageNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageNotifications();
                }
            });
        }

        if(newIPAddress != null) {
            newIPAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonApply.setClickable(true);
                }
            });
        }

        if (buttonApply != null) {
            buttonApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeIPAddress();
                }
            });
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            buttonManageNotifications.setVisibility(View.GONE);
            settings_notifications.setVisibility(View.GONE);
        }

    }

        private void manageNotifications() {
            Intent i = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            i.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(i);
        }

        private void changeIPAddress() {
            if (validateIP(newIPAddress.getText().toString())) {
                ApiClient.getInstance().setBaseURL(newIPAddress.getText().toString());
                textViewIP.setText(ApiClient.getInstance().getBaseURL());
                Toast.makeText(getApplicationContext(), R.string.toast_IPAddress, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_IPAddressMissing, Toast.LENGTH_LONG).show();
            }
        }

    private boolean validateIP(String IPAddress) {
        try {
            if ( IPAddress == null || IPAddress.isEmpty() ) {
                return false;
            }

            String[] parts = IPAddress.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( IPAddress.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
