package com.example.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.demo.ProductManageActivity;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {
    ListView lv;
    Button btnBack;
    SearchView searchView;
    ArrayList<String> productList;
    ArrayAdapter<String> productAdapter;
    SQLiteDatabase myDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        lv = findViewById(R.id.lv);
        btnBack = findViewById(R.id.btnBack);
        searchView = findViewById(R.id.searchView);
        if (searchView != null) {
            // Đã tìm thấy searchView, vì vậy bạn có thể gán OnQueryTextListener
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    productAdapter.getFilter().filter(newText); // Lọc danh sách sản phẩm dựa trên từ khóa tìm kiếm mới
                    return false;
                }
            });
        } else {
            // Nếu searchView là null, in ra thông báo cảnh báo hoặc log để kiểm tra
            Log.e("ProductListActivity", "searchView is null!");
        }

        productList = new ArrayList<>();
        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        lv.setAdapter(productAdapter);

        myDatabase = openOrCreateDatabase("qlxe.db", MODE_PRIVATE, null);

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM tblXe", null);
        if (cursor.moveToFirst()) {
            do {
                String productData = cursor.getString(0) + " -:" + cursor.getString(1) + " \n-Tên xe: " +
                        cursor.getString(2) + " \n-Màu: " + cursor.getString(3) + " \n-Đơn giá " +
                        cursor.getInt(4) +"triệu"+ " \n-Lượng bán " + cursor.getInt(5) + " \n-Tồn kho " + cursor.getInt(6) +
                        " \n-Doanh thu: " + calculateRevenue(cursor.getInt(4), cursor.getInt(5))+"triệu";
                productList.add(productData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        productAdapter.notifyDataSetChanged();

        // Set onClickListener for the Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to ProductManageActivity
                Intent intent = new Intent(ProductListActivity.this, ProductManageActivity.class);
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
                productAdapter.getFilter().filter(newText); // Lọc danh sách sản phẩm dựa trên từ khóa tìm kiếm mới
                return false;
            }
        });
    }



    // Hàm tính toán doanh thu
    private int calculateRevenue(int donGia, int luongBan) {
        return donGia * luongBan;
    }

}
