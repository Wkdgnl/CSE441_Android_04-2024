package com.example.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText edta, edtb, edtkq;
    Button btntong, btnxoa;
    TextView txt_lichsu;
    String lichsu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edta = findViewById(R.id.edta);
        edtb = findViewById(R.id.edtb);
        edtkq = findViewById(R.id.edtkq);
        btntong = findViewById(R.id.btntong);
        btnxoa = findViewById(R.id.btnxoa);
        txt_lichsu = findViewById(R.id.txt_lichsu);

        // đọc lại dữ liệu đã lưu
        SharedPreferences mypref = getSharedPreferences("mysave", MODE_PRIVATE);
        lichsu = mypref.getString("ls","");
        txt_lichsu.setText(lichsu);

        btntong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(edta.getText().toString());
                int b = Integer.parseInt(edtb.getText().toString());
                int kq = a +b;
                edtkq.setText(kq+"");
                lichsu +=  a +" + "+b +" = "+ kq;
                txt_lichsu.setText(lichsu);
                lichsu += "\n";
            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lichsu ="";
                txt_lichsu.setText(lichsu);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 4 bước lưu trữ dữ liệu
        SharedPreferences mypref = getSharedPreferences("mysave", MODE_PRIVATE);
        SharedPreferences.Editor myedit = mypref.edit();
        myedit.putString("ls", lichsu);
        myedit.commit();
    }
}