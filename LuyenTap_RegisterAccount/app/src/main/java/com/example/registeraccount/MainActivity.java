package com.example.registeraccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtUsername, txtPass, txtRePass, txtFullName, txtPhone;
    Button btnRegis;

    void noti(String mes) {
        Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPass);
        txtRePass = findViewById(R.id.txtRePass);
        txtFullName = findViewById(R.id.txtFullName);
        txtPhone = findViewById(R.id.txtPhone);
        btnRegis = findViewById(R.id.btnRegis);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String pass = txtPass.getText().toString();
                String rePass = txtRePass.getText().toString();
                String fullName = txtFullName.getText().toString();
                String phone = txtPhone.getText().toString();

                if (username.equals("") || pass.equals("") || rePass.equals("") || fullName.equals("") || phone.equals("")) {
                    noti("Vui lòng điền đủ thông tin");
                    return;
                }
                if (!pass.equals(rePass)) {
                    noti("Không khớp mật khẩu");
                    return;
                }

                Intent intent = new Intent(MainActivity.this, AfterRegis.class);
                intent.putExtra("username",username);
                intent.putExtra("pass", pass);
                intent.putExtra("fullname", fullName);
                intent.putExtra("phone", phone);
                startActivity(intent);

            }
        });


    }
}