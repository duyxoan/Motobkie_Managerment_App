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

public class CustomerListActivity extends AppCompatActivity {
    ListView lv;
    Button btnBackC;
    SearchView searchView;
    ArrayList<String> customerList;
    ArrayAdapter<String> customerAdapter;
    SQLiteDatabase myDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        lv = findViewById(R.id.lv);
        btnBackC = findViewById(R.id.btnBackC);
        searchView = findViewById(R.id.searchView);

        customerList = new ArrayList<>();
        customerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerList);
        lv.setAdapter(customerAdapter);

        myDatabase = openOrCreateDatabase("qlkh.db", MODE_PRIVATE, null);

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM tblKhachHang", null);
        if (cursor.moveToFirst()) {
            do {
                String customerData = "Mã khách hàng: " + cursor.getString(0) + "\nHọ và tên: " +
                        cursor.getString(1) + "\nĐịa chỉ: " + cursor.getString(2) + "\nSĐT: " +
                        cursor.getString(3) + "\nXe đã mua: " + cursor.getString(4) + "\nMàu: " +
                        cursor.getString(5) + "\nĐơn giá: " + cursor.getString(6) + "triệu" + "\nNgày mua:" + cursor.getString(7);
                customerList.add(customerData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        customerAdapter.notifyDataSetChanged();

        // Set onClickListener for the Back button
        btnBackC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to EmployeeManageActivity
                Intent intent = new Intent(CustomerListActivity.this, CustomerManageActivity.class);
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
                customerAdapter.getFilter().filter(newText); // Lọc danh sách khách hàng dựa trên từ khóa tìm kiếm mới
                return false;
            }
        });
    }
}
