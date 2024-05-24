package com.example.custom_listview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int image[] = {R.drawable.telephone,R.drawable.phone_call,R.drawable.smartphone,R.drawable.smartphone1,R.drawable.smartphone2};
    String name[] ={"Samsung","Iphone","Vivo","Redmi","Oppo"};
    // khai báo listview
    ArrayList<Phone> mylist;
    MyArrayAdapter myadapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>(); // tạo mới mảng rỗng
        for (int i = 0;i<name.length;i++){
            mylist.add(new Phone(image[i],name[i]));
        }
        myadapter= new MyArrayAdapter(MainActivity.this, R.layout.layout_item, mylist);
        lv.setAdapter(myadapter);

        // xử lý click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent myintent = new Intent(MainActivity.this, SubAcyivity.class);
                myintent.putExtra("name",name[i]);
                startActivity(myintent);
            }
        });
    }
}