package com.example.custom_gridview;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
public class SubActivity extends AppCompatActivity {
    TextView txt_subname, txt_subprice;
    ImageView img_sub;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        img_sub = findViewById(R.id.img_subitem);
        txt_subname = findViewById(R.id.txt_subtensp);
        txt_subprice= findViewById(R.id.txt_subgiasp);
        Intent intent1 = getIntent();
        String subname =
                intent1.getStringExtra("name");
        txt_subname.setText(subname);
        int subgia = intent1.getIntExtra("price",0);
        txt_subprice.setText("Giá: "+subgia);
        int img = intent1.getIntExtra("image",0);
        img_sub.setImageResource(img);
    }
}
