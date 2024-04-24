package com.example.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Revenue.db";
    public static final String TABLE_NAME = "revenue";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_REVENUE = "revenue";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_MONTH + " INTEGER," +
                    COLUMN_YEAR + " INTEGER," +
                    COLUMN_REVENUE + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public Cursor getAllRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_MONTH,
                COLUMN_YEAR,
                COLUMN_REVENUE
        };

        return db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

    public long insertRevenue(int month, int year, float amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra năm và tháng không âm
        if (month < 0 || year < 0) {
            return -1; // Trả về -1 nếu năm hoặc tháng không hợp lệ
        }

        // Kiểm tra số tiền không âm
        if (amount < 0) {
            return -1; // Trả về -1 nếu số tiền không hợp lệ
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTH, month);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_REVENUE, amount);

        // Chèn dữ liệu vào cơ sở dữ liệu và trả về ID của hàng mới được chèn, hoặc -1 nếu xảy ra lỗi
        return db.insert(TABLE_NAME, null, values);
    }

    public void deleteEnteredData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
