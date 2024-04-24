package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button buttonManageEmployees;
    private Button buttonManageProducts;
    private Button buttonManageRevenue;
    private Button buttonManageCustomers;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuql);

        // Initialize UI components
        buttonManageEmployees = findViewById(R.id.buttonManageEmployees);
        buttonManageProducts = findViewById(R.id.buttonManageProducts);
        buttonManageRevenue = findViewById(R.id.buttonManageRevenue);
        buttonManageCustomers = findViewById(R.id.buttonManageCustomers);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Set up the button listeners using lambda expressions
        buttonManageEmployees.setOnClickListener(v -> {
            showToastAndPerform("Manage Employees button clicked");
            // Chuyển sang trang ProductManageActivity
            Intent intent = new Intent(HomeActivity.this, EmployeeManageActivity.class);
            startActivity(intent);
        });
        buttonManageProducts.setOnClickListener(v -> {
            showToastAndPerform("Manage Products button clicked");
            // Chuyển sang trang ProductManageActivity
            Intent intent = new Intent(HomeActivity.this, ProductManageActivity.class);
            startActivity(intent);
        });

        buttonManageRevenue.setOnClickListener(v -> {
            showToastAndPerform("Manage Revenue button clicked");
            // Chuyển sang trang ProductManageActivity
            Intent intent = new Intent(HomeActivity.this, RevenueManageActivity.class);
            startActivity(intent);
        });
        buttonManageCustomers.setOnClickListener(v -> {
            showToastAndPerform("Customer Manage button clicked");
            // Chuyển sang trang ProductManageActivity
            Intent intent = new Intent(HomeActivity.this, CustomerManageActivity.class);
            startActivity(intent);
        });
        buttonLogout.setOnClickListener(v -> {
            showToastAndPerform("Logout button clicked");
            logoutUser();
        });
    }

    private void showToastAndPerform(String message) {
        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
        // Replace this method's implementation with actual code for each management operation
    }

    private void logoutUser() {
        // Here you can add code to handle logout operation, such as clearing session data or navigating to the login screen
        // Start LoginActivity
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the HomeActivity
    }
}
