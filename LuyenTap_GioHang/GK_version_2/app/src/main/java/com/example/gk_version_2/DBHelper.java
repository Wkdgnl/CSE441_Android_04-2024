package com.example.gk_version_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.gk_version_2.Cart.Cart;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBName = "ql.db";

    private Context context; // Thêm trường Context
    public DBHelper(@Nullable Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table taikhoan(tennhanvien TEXT, tendangnhap TEXT primary key, matkhau TEXT, ngaytao TEXT)");
        db.execSQL("create table hoadon(tensanpham TEXT,mota TEXT, gia INTEGER, soluong INTEGER, thanhtien INTEGER,img BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists taikhoan");
        db.execSQL("drop table if exists hoadon");

    }



    // các function liên quan đến LOGIN, REGISTER
    public boolean insertData(String tennhanvien, String tendangnhap, String matkhau, String ngaytao){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tennhanvien", tennhanvien);
        contentValues.put("tendangnhap",tendangnhap);
        contentValues.put("matkhau",matkhau);
        contentValues.put("ngaytao",ngaytao);
        long result = myDB.insert("taikhoan",null,contentValues);
        if(result==-1)return false;
        else return true;
    }
    public boolean kt_tendn(String tendn){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from taikhoan where tendangnhap = ?", new String[]{tendn});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public boolean checkLogin(String tendangnhap,String matkhau){
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from taikhoan where tendangnhap = ? and matkhau = ?", new String[]{tendangnhap,matkhau});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    //Các function liên quan đến Hoadon
    public boolean insertData2(String tensanpham, String mota, int gia, int soluong, int thanhtien,byte[] imgData){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tensanpham", tensanpham);
        contentValues.put("mota", mota);
        contentValues.put("gia",gia);
        contentValues.put("soluong",soluong);
        contentValues.put("thanhtien",thanhtien);
        contentValues.put("img", imgData);
        long result = myDB.insert("hoadon",null,contentValues);
        if(result==-1)return false;
        else return true;
    }

    // Phương thức truy vấn dữ liệu từ bảng "hoadon"
    public List<Cart> getAllCartItems() {
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery("select * from hoadon", null);
        if (cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu từ các cột của mỗi hàng trong kết quả truy vấn
                String tensanpham = cursor.getString(0);
                String mota = cursor.getString(1);
                int gia = cursor.getInt(2);
                int soluong = cursor.getInt(3);
                int thanhtien = cursor.getInt(4);
                byte[] img = cursor.getBlob(5); // Lấy dữ liệu của cột ảnh (img)

                // Tạo đối tượng Cart từ dữ liệu lấy được và thêm vào danh sách
                cartList.add(new Cart(tensanpham, mota, gia, soluong, thanhtien,img));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }
}
