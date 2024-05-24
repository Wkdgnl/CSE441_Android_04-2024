package com.example.tabselect;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edta, edtb;
    Button btntong;
    TabHost mytab;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        addEvent();

    }

    private void addEvent() {
        btntong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(edta.getText().toString());
                int b = Integer.parseInt(edtb.getText().toString());
                int c = a + b;
                mylist.add(a +"+"+b +" = "+ c);
                myadapter.notifyDataSetChanged();// cập nhật lại adapter
            }
        });
    }

    private void addControl() {
        edta = findViewById(R.id.edta);
        edtb = findViewById(R.id.edtb);
        btntong = findViewById(R.id.btntong);

        // xử lý listview
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>(); // tạo mảng
        myadapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,mylist);
        lv.setAdapter(myadapter);

        // xử lý tab
        mytab = findViewById(R.id.mytab);
        mytab.setup();
        TabHost.TabSpec spec1, spec2;
        // ứng với mỗi tab con, thực hiện 4 bước
        // tab1
        spec1 = mytab.newTabSpec("t1"); // tạo mới tab
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("",getResources().getDrawable(R.drawable.plus));
        mytab.addTab(spec1);

        // tab2
        spec2 = mytab.newTabSpec("t2"); // tạo mới tab
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("",getResources().getDrawable(R.drawable.clock));
        mytab.addTab(spec2);

    }
}