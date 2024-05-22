package com.example.bmi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Button btnBMI;
    EditText edtTen, edtChieucao, edtCannang, edtBMI, edtChuandoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ánh xạ
        btnBMI= findViewById(R.id.btnBMI);
        edtTen = findViewById(R.id.edtTen);
        edtChieucao = findViewById(R.id.edtChieucao);
        edtCannang = findViewById(R.id.edtCannang);
        edtBMI = findViewById(R.id.edtBMI);
        edtChuandoan = findViewById(R.id.edtChuandoan);

        // Onclick

        btnBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double H = Double.parseDouble(edtChieucao.getText()+"");
                double W = Double.parseDouble(edtCannang.getText()+"");
                double BMI = W/Math.pow(H,2);
                String chuandoan ="";
                if(BMI<18){
                    chuandoan = "Bạn gầy";
                } else if (BMI<=24.9) {
                    chuandoan = "Bạn bình thường";
                } else if (BMI<=29.9) {
                    chuandoan = "Bạn đang béo phì độ I";
                } else if (BMI <=34.9) {
                    chuandoan= "Bạn đang béo phì độ II";
                }
                else{
                    chuandoan = "Bạn đang béo phì độ III";
                }

                DecimalFormat dcf = new DecimalFormat("#.0");
                edtBMI.setText(dcf.format(BMI));
                edtChuandoan.setText(chuandoan);
            }
        });
    }
}