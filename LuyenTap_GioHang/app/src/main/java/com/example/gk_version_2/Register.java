package com.example.gk_version_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Register extends AppCompatActivity {
    EditText edthoten, edttendangnhap, edtmatkhau,edtngaytao;
    Button btndangky,btnthoat;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Ánh xạ id
        edthoten = findViewById(R.id.edt_r_name);
        edttendangnhap = findViewById(R.id.edt_r_username);
        edtmatkhau = findViewById(R.id.edt_r_password);
        edtngaytao = findViewById(R.id.edt_r_ngaytao);
        btndangky = findViewById(R.id.btn_r_register);
        btnthoat = findViewById(R.id.btn_r_thoat);
        // edt ngày tạo

        // Lấy ngày hiện tại từ lớp Calendar
        Calendar calendar = Calendar.getInstance();

        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Format ngày hiện tại và gán vào EditText
        String currentDate = dateFormat.format(calendar.getTime());
        edtngaytao.setText(currentDate);

        dbHelper = new DBHelper(this);
        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten,tendn,matkhau,ngaytao;
                hoten = edthoten.getText().toString();
                tendn = edttendangnhap.getText().toString();
                matkhau = edtmatkhau.getText().toString();
                ngaytao = edtngaytao.getText().toString();
                if(hoten.equals("") || tendn.equals("") ||matkhau.equals("")){
                    Toast.makeText(Register.this, "Vui lòng nhập đủ tất cả thông tin ! ", Toast.LENGTH_SHORT).show();
                }else{
                    if(dbHelper.kt_tendn(tendn)){
                        Toast.makeText(Register.this,"Tên tài khoản đã tồn tại !",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean registeredSuccess = dbHelper.insertData(hoten,tendn,matkhau,ngaytao);
                    if(registeredSuccess){
                        Toast.makeText(Register.this,"Đăng ký thành công !",Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(Register.this,"Đăng ký không thành công !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}