package com.example.coreyharveyproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the EditText views
        usernameInput = findViewById(R.id.editTextUsername);
        passwordInput = findViewById(R.id.editTextPassword);
    }

    // Called when the login button is clicked
    public void login(View view) {
        // Get input from username and password fields
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Retrieve saved credentials from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", MODE_PRIVATE);
        String registeredUsername = sharedPreferences.getString("username", null);
        String registeredPassword = sharedPreferences.getString("password", null);

        // Check if the entered credentials match the saved ones
        if (username.equals(registeredUsername) && password.equals(registeredPassword)) {
            // Successful login, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Finish LoginActivity to prevent going back to it with the back button
        } else {
            // Invalid credentials, show a toast
            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    // Called when the register button is clicked
    public void navigateToRegister(View view) {
        // Navigate to RegisterActivity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
