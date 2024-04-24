package com.example.demo;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class RevenueManageActivity extends AppCompatActivity {

    private EditText editTextMonth;
    private EditText editTextYear;
    private EditText editTextAmount;
    private Button buttonSave;
    private Button buttonShowStoredData;
    private Button buttonDrawChart;
    private Button buttonDeleteData; // New button for deleting entered data
    private LineChart lineChart;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revenue_manage_activity);

        // Initialize UI components
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextYear = findViewById(R.id.editTextYear);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonSave = findViewById(R.id.buttonSave);
        buttonShowStoredData = findViewById(R.id.buttonShowStoredData);
        buttonDrawChart = findViewById(R.id.buttonDrawChart);
        buttonDeleteData = findViewById(R.id.buttonDeleteData); // Initialize the new button
        lineChart = findViewById(R.id.lineChart);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set up button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRevenue();
            }
        });

        buttonShowStoredData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStoredData();
            }
        });

        buttonDrawChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRevenueChart();
            }
        });

        buttonDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEnteredData();
            }
        });
    }

    private void saveRevenue() {
        // Code to save revenue to database
        int month = Integer.parseInt(editTextMonth.getText().toString());
        int year = Integer.parseInt(editTextYear.getText().toString());
        float amount = Float.parseFloat(editTextAmount.getText().toString());

        long id = databaseHelper.insertRevenue(month, year, amount);
        if (id != -1) {
            Toast.makeText(RevenueManageActivity.this, "Revenue saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RevenueManageActivity.this, "Failed to save revenue", Toast.LENGTH_SHORT).show();
        }

        // Clear input fields
        editTextMonth.getText().clear();
        editTextYear.getText().clear();
        editTextAmount.getText().clear();
    }

    private void displayRevenueChart() {
        List<Entry> entries = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllRevenue();
        int monthIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MONTH);
        int revenueIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REVENUE);
        while (cursor.moveToNext()) {
            int month = cursor.getInt(monthIndex);
            float revenue = cursor.getFloat(revenueIndex) / 1000; // Chia cho 1000 để chuyển đổi sang nghìn đồng
            entries.add(new Entry(month, revenue));
        }
        cursor.close();

        LineDataSet dataSet = new LineDataSet(entries, "Monthly Revenue (Thousand VND)"); // Đổi tiêu đề của biểu đồ
        dataSet.setValueFormatter(new DefaultValueFormatter(0)); // Đặt định dạng giá trị để không hiển thị thêm số thập phân

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        Description description = new Description();
        description.setText("Monthly Revenue Chart");
        lineChart.setDescription(description);

        // Chỉnh tỉ lệ trục y
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return super.getFormattedValue(value * 1000); // Nhân lại với 1000 để hiển thị giá trị ban đầu
            }
        });

        lineChart.invalidate(); // Refresh chart
    }

    private void showStoredData() {
        // Code to retrieve stored data from DatabaseHelper and display it
        // For example:
        Cursor cursor = databaseHelper.getAllRevenue();
        StringBuilder data = new StringBuilder();
        while (cursor.moveToNext()) {
            int month = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MONTH));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_YEAR));
            float revenue = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REVENUE));
            data.append("Month: ").append(month).append(", Year: ").append(year).append(", Revenue: ").append(revenue).append("\n");
        }
        cursor.close();
        Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
    }

    private void deleteEnteredData() {
        // Code to delete entered data from DatabaseHelper
        databaseHelper.deleteEnteredData();
        Toast.makeText(RevenueManageActivity.this, "Entered data deleted successfully", Toast.LENGTH_SHORT).show();
    }
}
