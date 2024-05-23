package com.example.intent4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btncallphone, btnsendsms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ánh xạ
        btncallphone= findViewById(R.id.btncallphone);
        btnsendsms = findViewById(R.id.btnsendsms);

        // onclick
        btncallphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(MainActivity.this, CallPhoneActivity.class);
                startActivity(myintent);
            }
        });

        btnsendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent2 = new Intent(MainActivity.this, SendSmsActivity.class);
                startActivity(myintent2);
            }
        });
    }
}