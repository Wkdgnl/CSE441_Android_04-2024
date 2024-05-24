package com.example.custom_listview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SubAcyivity extends AppCompatActivity {
    TextView txt_subphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub_acyivity);
        txt_subphone= findViewById(R.id.txt_subphone);
        // nhận intent
        Intent myintent = getIntent();
        // lấy dữ liệu
        String namephone = myintent.getStringExtra("name");
        txt_subphone.setText(namephone);
    }
}