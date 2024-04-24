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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerManageActivity extends AppCompatActivity {
    EditText edtMaKH, edtTenKH, edtXedaMua, edtMauSac, edtDiaChi, edtSDT, edtDonGia, edtNgayMua;
    Button btnInsertC, btnDeleteC, btnUpdateC, btnQueryC, btnBackC;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manage);

        edtMaKH = findViewById(R.id.edtMaKH);
        edtTenKH = findViewById(R.id.edtTenKH);
        edtXedaMua = findViewById(R.id.edtXedaMua);
        edtMauSac = findViewById(R.id.edtMauSac);
        edtDonGia = findViewById(R.id.edtDonGia);
        edtNgayMua = findViewById(R.id.edtNgayMua);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtSDT = findViewById(R.id.edtSDT);

        btnInsertC = findViewById(R.id.btnInsert);
        btnDeleteC = findViewById(R.id.btnDelete);
        btnUpdateC = findViewById(R.id.btnUpdate);
        btnQueryC = findViewById(R.id.btnQuery);
        btnBackC = findViewById(R.id.btnBack);


        lv = findViewById(R.id.lv);

        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        mydatabase = openOrCreateDatabase("qlkh.db", MODE_PRIVATE, null);

        try {
            String sql = "CREATE TABLE IF NOT EXISTS tblKhachHang(MaKH TEXT PRIMARY KEY, TenKH TEXT,DiaChi TEXT, SDT TEXT, XeDaMua TEXT, MauSac TEXT, DonGia INTEGER, NgayMua TEXT )";
            mydatabase.execSQL(sql);
        } catch (SQLException e) {
            Log.e("Error", "Không thể tạo bảng: " + e.getMessage());
            e.printStackTrace();
        }

        btnInsertC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maKH = edtMaKH.getText().toString();
                String tenKH = edtTenKH.getText().toString();
                String xeDaMua = edtXedaMua.getText().toString();
                String mauSac = edtMauSac.getText().toString();
                String ngayMua = edtNgayMua.getText().toString();
                String diaChi = edtDiaChi.getText().toString();
                String sdt = edtSDT.getText().toString();
                int donGia = 0;

                // Kiểm tra định dạng đơn giá
                if (!edtDonGia.getText().toString().isEmpty()) {
                    try {
                        donGia = Integer.parseInt(edtDonGia.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(CustomerManageActivity.this, "Đơn giá phải là một số nguyên.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Kiểm tra nếu mã khách hàng đã tồn tại
                Cursor cursor = mydatabase.rawQuery("SELECT * FROM tblKhachHang WHERE MaKH = ?", new String[]{maKH});
                if (cursor.getCount() > 0) {
                    Toast.makeText(CustomerManageActivity.this, "Mã khách hàng đã tồn tại.", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                // Kiểm tra nếu có trường nào bị bỏ trống
                if (maKH.isEmpty() || tenKH.isEmpty() || xeDaMua.isEmpty() || mauSac.isEmpty() || ngayMua.isEmpty() || diaChi.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(CustomerManageActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("MaKH", maKH);
                values.put("TenKH", tenKH);
                values.put("DiaChi", diaChi);
                values.put("SDT", sdt);
                values.put("XeDaMua", xeDaMua);
                values.put("MauSac", mauSac);
                values.put("DonGia", donGia);
                values.put("NgayMua", ngayMua);

                if (mydatabase.insert("tblKhachHang", null, values) == -1) {
                    Toast.makeText(CustomerManageActivity.this, "Fail to Insert Record!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CustomerManageActivity.this, "Insert record Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện btnDelete
        btnDeleteC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maKH = edtMaKH.getText().toString();
                int n = mydatabase.delete("tblKhachHang", "MaKH = ?", new String[]{maKH});
                String msg = (n == 0) ? "No record to Delete" : n + " record is deleted";
                Toast.makeText(CustomerManageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện btnUpdate
        btnUpdateC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maKH = edtMaKH.getText().toString();

                // Kiểm tra xem có thông tin mã khách hàng không
                if (maKH.isEmpty()) {
                    Toast.makeText(CustomerManageActivity.this, "Vui lòng nhập mã khách hàng.", Toast.LENGTH_SHORT).show();
                    return; // Kết thúc sự kiện nếu không có mã khách hàng
                }

                ContentValues values = new ContentValues();

                // Kiểm tra và cập nhật thông tin đơn giá
                if (!TextUtils.isEmpty(edtDonGia.getText().toString())) {
                    int donGia = Integer.parseInt(edtDonGia.getText().toString());
                    values.put("DonGia", donGia);
                }

                // Tiếp tục với các trường cần cập nhật khác nếu có
                // Ví dụ: Cập nhật trường "TenKH"
                if (!TextUtils.isEmpty(edtTenKH.getText().toString())) {
                    String tenKH = edtTenKH.getText().toString();
                    values.put("TenKH", tenKH);
                }

                int n = mydatabase.update("tblKhachHang", values, "MaKH = ?", new String[]{maKH});
                String msg = (n == 0) ? "No record to Update" : n + " record is updated";
                Toast.makeText(CustomerManageActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });





// Xử lý sự kiện btnQuery
        btnQueryC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo Intent để chuyển sang Activity mới để hiển thị danh sách khách hàng
                Intent intent = new Intent(CustomerManageActivity.this, CustomerListActivity.class);
                startActivity(intent); // Bắt đầu Activity mới
            }
        });

        btnBackC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to HomeActivity
                Intent intent = new Intent(CustomerManageActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity
            }
        });




    }
}
