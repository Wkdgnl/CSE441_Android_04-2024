package com.example.custom_gridview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //khai báo 2 mảng con
    String name[] = {"Gậy bẻ lò xo","Ấm đun siêu tốc",
            "Tai nghe siêu cấp","Sữa rửa mặt Simple",
            "Tất Nam cao cấp","Quần Legging Nam",
            "Tai nghe Bluetooth","Bơ đậu cao cấp"};
    int image[] = {R.drawable.hinh1,
            R.drawable.hinh2,R.drawable.hinh3,
            R.drawable.hinh4,
            R.drawable.hinh5,R.drawable.hinh6,
            R.drawable.hinh7, R.drawable.hinh8};
    //Khai báo Listview
    int price[] = {120000,100000,1700000,
            20000,50000,150000,300000,110000};
    ArrayList<Phone> mylist; //Khai báo mảng chính
    GridView gv; //Khai báo ListView
    MyArrayAdapter myadapter; //Khai báo Adapter
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//Tạo mảng chính và add 2 mảng con vào
        mylist = new ArrayList<>();
        for (int i = 0; i<name.length; i++)
        {
            mylist.add(new
                    Phone(image[i],name[i],price[i]));
        }
//Tạo mới Adapter
        myadapter = new
                MyArrayAdapter(this,R.layout.layout_item,mylist);
//Tạo Listview và setAdapter
        gv = findViewById(R.id.gv);
        gv.setAdapter(myadapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(MainActivity.this, SubActivity.class);
                intent1.putExtra("image",image[i]);
                intent1.putExtra("name",name[i]);
                intent1.putExtra("price",price[i]);
                startActivity(intent1);
            }
        });
    }
}