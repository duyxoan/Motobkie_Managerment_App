package com.example.demo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProductManageActivity extends AppCompatActivity {
    EditText edtMaXe, edtTenHangXe, edtTenXe, edtMauSac, edtDonGia, edtLuongBan, edtTonKho;
    Button btnInsert, btnDelete, btnUpdate, btnQuery,btnBack;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manage);

        edtMaXe = findViewById(R.id.edtMaXe);
        edtTenHangXe = findViewById(R.id.edtTenHangXe);
        edtTenXe = findViewById(R.id.edtTenXe);
        edtMauSac = findViewById(R.id.edtMauSac);
        edtDonGia = findViewById(R.id.edtDonGia);
        edtLuongBan = findViewById(R.id.edtLuongBan);
        edtTonKho = findViewById(R.id.edtTonKho);

        btnInsert = findViewById(R.id.btnInsert);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnQuery = findViewById(R.id.btnQuery);
        btnBack = findViewById(R.id.btnBack);


        lv = findViewById(R.id.lv);

        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        mydatabase = openOrCreateDatabase("qlxe.db", MODE_PRIVATE, null);

        try {
            String sql = "CREATE TABLE tblXe(MaXe TEXT primary key, TenHangXe TEXT, TenXe TEXT, MauSac TEXT, DonGia INTEGER, LuongBan INTEGER, TonKho INTEGER)";
            mydatabase.execSQL(sql);
        } catch (SQLException e) {
            Log.e("Error", "Không thể tạo bảng: " + e.getMessage());
            e.printStackTrace();
        }

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maXe = edtMaXe.getText().toString();
                String tenHangXe = edtTenHangXe.getText().toString();
                String tenXe = edtTenXe.getText().toString();
                String mauSac = edtMauSac.getText().toString();
                int donGia = 0;
                int luongBan = 0;
                int tonKho = 0;

                try {
                    donGia = Integer.parseInt(edtDonGia.getText().toString());
                    luongBan = Integer.parseInt(edtLuongBan.getText().toString());
                    tonKho = Integer.parseInt(edtTonKho.getText().toString());
                } catch (NumberFormatException e) {
                    // Xử lý nếu có lỗi khi chuyển đổi dữ liệu sang số nguyên
                    Toast.makeText(ProductManageActivity.this, "Vui lòng nhập đúng định dạng số cho Đơn giá, Lượng bán và Tồn kho.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra nếu mã xe đã tồn tại
                Cursor cursor = mydatabase.rawQuery("SELECT * FROM tblXe WHERE MaXe = ?", new String[]{maXe});
                if (cursor.getCount() > 0) {
                    Toast.makeText(ProductManageActivity.this, "Mã xe đã tồn tại.", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                // Kiểm tra nếu có trường nào chưa điền
                if (maXe.isEmpty() || tenHangXe.isEmpty() || tenXe.isEmpty() || mauSac.isEmpty()) {
                    Toast.makeText(ProductManageActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("MaXe", maXe);
                values.put("TenHangXe", tenHangXe);
                values.put("TenXe", tenXe);
                values.put("MauSac", mauSac);
                values.put("DonGia", donGia);
                values.put("LuongBan", luongBan);
                values.put("TonKho", tonKho);

                if (mydatabase.insert("tblXe", null, values) == -1) {
                    Toast.makeText(ProductManageActivity.this, "Fail to Insert Record!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductManageActivity.this, "Insert record Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý các sự kiện khác (btnDelete, btnUpdate, btnQuery) tương tự như trên
        // Bạn có thể sao chép mã trong các sự kiện khác và chỉ cần điều chỉnh một số giá trị

        // Xử lý sự kiện btnDelete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maXe = edtMaXe.getText().toString();
                int n = mydatabase.delete("tblXe", "MaXe = ?", new String[]{maXe});
                String msg = (n == 0) ? "No record to Delete" : n + " record is deleted";
                Toast.makeText(ProductManageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện btnUpdate
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maXe = edtMaXe.getText().toString();

                // Kiểm tra xem có thông tin mã xe không
                if (maXe.isEmpty()) {
                    Toast.makeText(ProductManageActivity.this, "Vui lòng nhập mã xe.", Toast.LENGTH_SHORT).show();
                    return; // Kết thúc sự kiện nếu không có mã xe
                }

                int donGia = 0;
                int luongBan = 0;

                // Kiểm tra liệu đơn giá có được nhập không
                if (!edtDonGia.getText().toString().isEmpty()) {
                    donGia = Integer.parseInt(edtDonGia.getText().toString());
                }

                // Kiểm tra liệu lượng bán có được nhập không
                if (!edtLuongBan.getText().toString().isEmpty()) {
                    luongBan = Integer.parseInt(edtLuongBan.getText().toString());
                }

                // Lấy giá trị tồn kho ban đầu từ cơ sở dữ liệu
                Cursor cursor = mydatabase.rawQuery("SELECT TonKho FROM tblXe WHERE MaXe = ?", new String[]{maXe});
                int tonKhoBanDau = 0;
                if (cursor.moveToFirst()) {
                    tonKhoBanDau = cursor.getInt(0);
                }
                cursor.close();

                // Tính toán tồn kho mới dựa trên lượng bán mới
                int tonKhoMoi = tonKhoBanDau - luongBan;

                // Kiểm tra liệu tồn kho mới có âm không
                if (tonKhoMoi < 0) {
                    Toast.makeText(ProductManageActivity.this, "Tồn kho không thể âm.", Toast.LENGTH_SHORT).show();
                    return; // Kết thúc sự kiện nếu tồn kho mới âm
                }

                ContentValues values = new ContentValues();
                if (donGia != 0) {
                    values.put("DonGia", donGia);
                }
                if (luongBan != 0) {
                    values.put("LuongBan", luongBan);
                    values.put("TonKho", tonKhoMoi);
                }

                int n = mydatabase.update("tblXe", values, "MaXe = ?", new String[]{maXe});
                String msg = (n == 0) ? "No record to Update" : n + " record is updated";
                Toast.makeText(ProductManageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện btnQuery
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo Intent để chuyển sang Activity mới
                Intent intent = new Intent(ProductManageActivity.this, ProductListActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to ProductManageActivity
                Intent intent = new Intent(ProductManageActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });


    }
}
