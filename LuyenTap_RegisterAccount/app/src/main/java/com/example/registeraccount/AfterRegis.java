package com.example.registeraccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AfterRegis extends AppCompatActivity {

    TextView txtFullName, txtPhone, txtUsername, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_regis);

        txtFullName = findViewById(R.id.txtFullName);
        txtPhone = findViewById(R.id.txtPhone);
        txtUsername = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPass);

        txtFullName.setText("Full name: " + getIntent().getStringExtra("fullname"));
        txtPhone.setText("Phone number: " + getIntent().getStringExtra("phone"));
        txtUsername.setText("Username: " + getIntent().getStringExtra("username"));
        txtPass.setText("Password: " + getIntent().getStringExtra("pass"));

    }
}