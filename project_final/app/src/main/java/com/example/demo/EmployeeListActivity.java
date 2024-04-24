package com.example.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmployeeListActivity extends AppCompatActivity {
    ListView lv;
    Button btnBack;
    SearchView searchView;
    ArrayList<String> employeeList;
    ArrayAdapter<String> employeeAdapter;
    SQLiteDatabase myDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        lv = findViewById(R.id.lv);
        btnBack = findViewById(R.id.btnBack);
        searchView = findViewById(R.id.searchView);

        employeeList = new ArrayList<>();
        employeeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeList);
        lv.setAdapter(employeeAdapter);

        myDatabase = openOrCreateDatabase("qlnv.db", MODE_PRIVATE, null);

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM tblNhanVien", null);
        if (cursor.moveToFirst()) {
            do {
                String employeeData = "Mã nhân viên: " + cursor.getString(0) + "\nHọ và tên: " +
                        cursor.getString(1) + "\nNgày sinh: " + cursor.getString(2) + "\nLương: " +
                        cursor.getInt(3) +"triệu"+ "\nChức vụ: " + cursor.getString(4) + "\nĐịa chỉ: " +
                        cursor.getString(5) + "\nSố điện thoại: " + cursor.getString(6);
                employeeList.add(employeeData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        employeeAdapter.notifyDataSetChanged();

        // Set onClickListener for the Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to EmployeeManageActivity
                Intent intent = new Intent(EmployeeListActivity.this, EmployeeManageActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });

        // Xử lý sự kiện tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                employeeAdapter.getFilter().filter(newText); // Lọc danh sách nhân viên dựa trên từ khóa tìm kiếm mới
                return false;
            }
        });
    }
}

