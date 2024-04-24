package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonExit = findViewById(R.id.buttonExit);

        // Set up the button listeners
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login logic
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Dummy login logic for demonstration
                if (username.equals("admin") && password.equals("1")) {
                    // Login successful
                    Toast.makeText(MainActivity.this, "Manager logged in successfully", Toast.LENGTH_SHORT).show();
                    // Start HomeActivity
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    // Finish current activity to prevent user from going back to login screen
                    finish();
                } else {
                    // Login failed
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    // You can show an error message or clear the input fields here
                }
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will close the app. You could also just finish the current activity with finish();
                System.exit(0);
            }
        });
    }
}