package com.example.intent3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class XuLyKetQua extends AppCompatActivity {
    EditText edtNhan;
    Button btnGoc, btnBinhphuong;
    Intent myintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xu_ly_ket_qua);

        edtNhan = findViewById(R.id.edtNhan);
        btnGoc = findViewById(R.id.btnGoc);
        btnBinhphuong = findViewById(R.id.btnBinhphuong);

        // nận intent
        myintent = getIntent();
        // lấy dữ liệu khỏi Intent
        int a = myintent.getIntExtra("soa",0);
        edtNhan.setText(a+"");

        // xử lý onclick
        btnGoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myintent.putExtra("kq",a);
                setResult(33,myintent);
                finish();
            }
        });

        btnBinhphuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myintent.putExtra("kq", a*a);
                setResult(34,myintent);
                finish();
            }
        });
    }
}