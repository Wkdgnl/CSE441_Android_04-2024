package com.example.intent6_google;

import android.content.Intent;
import android.net.Uri;
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
    EditText edtlink;
    Button btnopen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edtlink = findViewById(R.id.edtlink);
        btnopen = findViewById(R.id.btnopen);

        btnopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khai báo intent ẩn để mở tới ứng dụng web
                Intent myintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+edtlink.getText().toString()));
                startActivity(myintent);

            }
        });
    }
}