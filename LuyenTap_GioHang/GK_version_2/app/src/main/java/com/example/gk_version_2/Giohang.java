package com.example.gk_version_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gk_version_2.Cart.Cart;
import com.example.gk_version_2.Cart.CartAdapter;

import java.util.ArrayList;
import java.util.List;

public class Giohang extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView txt_tongtien;
    Button btn_trolai;
    CartAdapter cartAdapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        txt_tongtien = findViewById(R.id.txt_tongtien);
        btn_trolai = findViewById(R.id.btn_back);



        // Khởi tạo RecyclerView và DBHelper
        recyclerView = findViewById(R.id.recy2);
        dbHelper = new DBHelper(this);

        // Truy vấn dữ liệu từ cơ sở dữ liệu và chuyển thành danh sách Cart
        List<Cart> cartList = dbHelper.getAllCartItems();


        // Khởi tạo và đặt Adapter cho RecyclerView
        cartAdapter = new CartAdapter(this, cartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        // Tính tổng số tiền
        int total = calculateTotalPrice(cartList);

        // Hiển thị tổng số tiền
        txt_tongtien.setText("Thành tiền: " + total + " $");

        btn_trolai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    private List<Cart> getCartDataFromDatabase() {
//        List<Cart> cartList = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM hoadon", null);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                String tensanpham = cursor.getString(0);
//                String mota = cursor.getString(1);
//                int gia = cursor.getInt(2);
//                int soluong = cursor.getInt(3);
//                int thanhtien = cursor.getInt(4);
//                byte[] imgData = cursor.getBlob(5);
//                // Tạo đối tượng Cart và thêm vào danh sách
//                cartList.add(new Cart(tensanpham, mota, gia, soluong, thanhtien,imgData));
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//        return cartList;
//    }

    private int calculateTotalPrice(List<Cart> cartList) {
        int totalPrice = 0;
        for (Cart cart : cartList) {
            totalPrice += cart.getThanhtien();
        }
        return totalPrice;
    }

}