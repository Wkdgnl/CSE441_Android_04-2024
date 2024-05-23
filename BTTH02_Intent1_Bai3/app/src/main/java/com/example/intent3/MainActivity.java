package com.example.intent3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText edt1, edt2;
    Button btnYeuCau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        btnYeuCau = findViewById(R.id.btnYeuCau);

        // xử lý click

        btnYeuCau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khai báo intent
                Intent myintent = new Intent(MainActivity.this, XuLyKetQua.class);
                int a = Integer.parseInt(edt1.getText().toString());
                // đưa dữ liệu vào Intent
                myintent.putExtra("soa",a);
                startActivityForResult(myintent,99);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == 33)
        {
            int kq = data.getIntExtra("kq",0);
            edt2.setText("Gía trị gốc là: "+kq);
        }
        if (requestCode == 99 && resultCode ==34)
        {
            int kq = data.getIntExtra("kq",0);
            edt2.setText("Gía trị bình phương là: "+kq);
        }
    }
}