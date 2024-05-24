package com.example.listview_basic;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    String myphone[]={"Samsung","Oppo","Redmi","Vivo","Iphone"};
    ArrayAdapter<String> myadapter;
    ListView lv1;
    TextView txtSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // ánh xạ
        txtSelect = findViewById(R.id.txtSelect);
        lv1 = findViewById(R.id.lv1);
        myadapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,myphone);
        lv1.setAdapter(myadapter);

        // Bắt sự kiện click vào item listview
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                txtSelect.setText("Vị trí: "+i+" "+myphone[i]);
            }
        });
    }
}