package com.example.gk_version_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText edt_l_tendn;
    EditText edt_l_makhau;
    TextView txt_l_dangky;
    Button btn_l_dangnhap;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_l_tendn = findViewById(R.id.edt_l_username);
        edt_l_makhau = findViewById(R.id.edt_l_password);
        txt_l_dangky = findViewById(R.id.txt_l_dangky);
        btn_l_dangnhap = findViewById(R.id.btn_l_login);

        dbHelper = new DBHelper(this);
        btn_l_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_l_tendn.getText().toString().equals("")|| edt_l_makhau.getText().toString().equals("")){
                    Toast.makeText(Login.this, "Vui lòng nhập đủ tất cả thông tin ! ", Toast.LENGTH_SHORT).show();
                }else{
                    boolean isLoggedID = dbHelper.checkLogin(edt_l_tendn.getText().toString(),edt_l_makhau.getText().toString());
                    if(isLoggedID){
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(Login.this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txt_l_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

    }
}