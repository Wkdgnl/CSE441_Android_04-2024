package com.example.chuyendoinhietdo;

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
// khai báo biến giao diện
    EditText edtF, edtC;
    Button btnFC, btnCF, btnXoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // ánh xạ ID
        edtC = findViewById(R.id.edtC);
        edtF = findViewById(R.id.edtF);
        edtF = findViewById(R.id.edtF);
        btnFC = findViewById(R.id.btnFC);
        btnCF = findViewById(R.id.btnCF);
        btnXoa = findViewById(R.id.btnXoa);

        // xử lý click

        // Từ C -> F
        btnCF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int c = Integer.parseInt(edtC.getText().toString());
                double f = c*1.8 + 32;
                edtF.setText(f+"");
            }
        });

        // Từ F -> C
        btnFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int f = Integer.parseInt(edtF.getText().toString());
                double c = (f-32)/1.8;
                edtC.setText(c+"");
            }
        });


        // Xóa
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtC.setText("");
                edtF.setText("");
            }
        });
    }
}