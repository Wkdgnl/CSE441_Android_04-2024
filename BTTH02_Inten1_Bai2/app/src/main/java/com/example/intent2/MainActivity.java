package com.example.intent2;

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

public class MainActivity extends AppCompatActivity {
    Button btnKQ;
    EditText edtA, edtB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
      // ánh xạ

        edtA = findViewById(R.id.edtA);
        edtB = findViewById(R.id.edtB);
        btnKQ = findViewById(R.id.btnKQ);

        // xử lý onclick

        btnKQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khai báo intent
                Intent myintent = new Intent(MainActivity.this, KetQua.class);
                //lấy dữ liệu
                int a  = Integer.parseInt(edtA.getText().toString());
                int b  = Integer.parseInt(edtB.getText().toString());
                // đóng gói dữ liệu vào bundle
                Bundle mybundle = new Bundle();
                // Đưa dữ liệu vào bundle
                mybundle.putInt("soa",a);
                mybundle.putInt("sob",b);
                // đưa bundle vào intent
                myintent.putExtra("mypackage",mybundle);
                // khởi động
                startActivity(myintent);
            }
        });
    }
}