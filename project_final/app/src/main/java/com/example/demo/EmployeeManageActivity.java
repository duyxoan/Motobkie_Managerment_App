package com.example.demo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmployeeManageActivity extends AppCompatActivity {
    EditText edtMaNV, edtHoTen, edtNgaySinh, edtLuong, edtChucVu, edtDiaChi, edtSDT;
    Button btnInsertE, btnDeleteE, btnUpdateE, btnQueryE,btnBackE;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manage);

        edtMaNV = findViewById(R.id.edtMaNV);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtLuong = findViewById(R.id.edtLuong);
        edtChucVu = findViewById(R.id.edtChucVu);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtSDT = findViewById(R.id.edtSDT);

        btnInsertE = findViewById(R.id.btnInsertE);
        btnDeleteE = findViewById(R.id.btnDeleteE);
        btnUpdateE = findViewById(R.id.btnUpdateE);
        btnQueryE = findViewById(R.id.btnQueryE);
        btnBackE =  findViewById(R.id.btnBackE);

        lv = findViewById(R.id.lv);

        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        mydatabase = openOrCreateDatabase("qlnv.db", MODE_PRIVATE, null);

        try {
            String sql = "CREATE TABLE tblNhanVien(MaNV TEXT primary key, HoTen TEXT, NgaySinh TEXT, Luong INTEGER, ChucVu TEXT, DiaChi TEXT, SDT TEXT)";
            mydatabase.execSQL(sql);
        } catch (SQLException e) {
            Log.e("Error", "Không thể tạo bảng: " + e.getMessage());
            e.printStackTrace();
        }

        // Xử lý sự kiện btnInsert
        btnInsertE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maNV = edtMaNV.getText().toString();
                String hoTen = edtHoTen.getText().toString();
                String ngaySinh = edtNgaySinh.getText().toString();
                int luong = 0;
                String chucVu = edtChucVu.getText().toString();
                String diaChi = edtDiaChi.getText().toString();
                String sdt = edtSDT.getText().toString();

                try {
                    luong = Integer.parseInt(edtLuong.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(EmployeeManageActivity.this, "Vui lòng nhập số cho lương.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra nếu mã nhân viên đã tồn tại
                Cursor cursor = mydatabase.rawQuery("SELECT * FROM tblNhanVien WHERE MaNV = ?", new String[]{maNV});
                if (cursor.getCount() > 0) {
                    Toast.makeText(EmployeeManageActivity.this, "Mã nhân viên đã tồn tại.", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                // Kiểm tra nếu có trường nào chưa điền
                if (maNV.isEmpty() || hoTen.isEmpty() || ngaySinh.isEmpty() || chucVu.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(EmployeeManageActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("MaNV", maNV);
                values.put("HoTen", hoTen);
                values.put("NgaySinh", ngaySinh);
                values.put("Luong", luong);
                values.put("ChucVu", chucVu);
                values.put("DiaChi", diaChi);
                values.put("SDT", sdt);

                if (mydatabase.insert("tblNhanVien", null, values) == -1) {
                    Toast.makeText(EmployeeManageActivity.this, "Fail to Insert Record!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EmployeeManageActivity.this, "Insert record Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Xử lý sự kiện btnDelete
        btnDeleteE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maNV = edtMaNV.getText().toString();
                int n = mydatabase.delete("tblNhanVien", "MaNV = ?", new String[]{maNV});
                String msg = (n == 0) ? "No record to Delete" : n + " record is deleted";
                Toast.makeText(EmployeeManageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

// Xử lý sự kiện btnUpdate
        // Xử lý sự kiện btnUpdate
        btnUpdateE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maNV = edtMaNV.getText().toString();

                // Kiểm tra xem có thông tin mã nhân viên không
                if (maNV.isEmpty()) {
                    Toast.makeText(EmployeeManageActivity.this, "Vui lòng nhập mã nhân viên.", Toast.LENGTH_SHORT).show();
                    return; // Kết thúc sự kiện nếu không có mã nhân viên
                }

                ContentValues values = new ContentValues();

                // Kiểm tra và cập nhật thông tin lương
                if (!TextUtils.isEmpty(edtLuong.getText().toString())) {
                    int luong = Integer.parseInt(edtLuong.getText().toString());
                    values.put("Luong", luong);
                }

                // Tiếp tục với các trường cần cập nhật khác nếu có

                int n = mydatabase.update("tblNhanVien", values, "MaNV = ?", new String[]{maNV});
                String msg = (n == 0) ? "No record to Update" : n + " record is updated";
                Toast.makeText(EmployeeManageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });


// Xử lý sự kiện btnQuery
        btnQueryE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo Intent để chuyển sang Activity mới để hiển thị danh sách nhân viên
                Intent intent = new Intent(EmployeeManageActivity.this, EmployeeListActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });
        btnBackE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to ProductManageActivity
                Intent intent = new Intent(EmployeeManageActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });


    }
}
