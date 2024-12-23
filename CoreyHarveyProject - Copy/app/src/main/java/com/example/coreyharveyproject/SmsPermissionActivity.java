package com.example.coreyharveyproject;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsPermissionActivity extends AppCompatActivity {

    TextView smsPermissionStatus;
    Button grantPermissionButton;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        smsPermissionStatus = findViewById(R.id.smsPermissionStatus);
        grantPermissionButton = findViewById(R.id.grantPermissionButton);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            smsPermissionStatus.setText("SMS Permission Granted");
        } else {
            smsPermissionStatus.setText("SMS Permission Required");
        }

        grantPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(SmsPermissionActivity.this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        SMS_PERMISSION_REQUEST_CODE);
            }
        });
    }
}