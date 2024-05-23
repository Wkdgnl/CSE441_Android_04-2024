package com.example.intent2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class KetQua extends AppCompatActivity {

    TextView txtNghiem;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ket_qua);

        txtNghiem = findViewById(R.id.txtNghiem);
        btnBack = findViewById(R.id.btnBack);

        // nhận intent
        Intent myintent = getIntent();
        // lấy bundle ra khỏi intent
        Bundle mybundle = myintent.getBundleExtra("mypackage");
        // ly dữ liệu ra khỏi bundle
        int a = mybundle.getInt("soa");
        int b = mybundle.getInt("sob");

        // giải pt
        String nghiem = "";
        if (a ==0 && b ==0)
        {
            nghiem ="PT vô số nghiệm";
        }
        else if (a == 0 && b !=0 )
        {
            nghiem ="PT vô nghiệm";
        }
        else
        {
            nghiem =""+ (-1.0)*b/a;

        }
        txtNghiem.setText(nghiem);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}