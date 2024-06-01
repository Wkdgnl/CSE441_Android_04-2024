package com.example.book.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.book.R;

public class CategoryActivity extends AppCompatActivity {
    Button ngontinhBtn, nguocBtn, haihuocBtn, dothiBtn, hiendaiBtn, xuyenkhongBtn, xuyennhanhBtn, truyenteenBtn,
            kiemhiepBtn, tienhiepBtn, mattheBtn, cungdauBtn, giadauBtn, niendaiBtn, codaiBtn, dienvanBtn, trinhthamBtn,
            dinangBtn, digioiBtn, vongduBtn, linhdiBtn, trongsinhBtn, quansuBtn, lichsuBtn, thamhiemBtn, huyenhuyenBtn, khoahuyenBtn, hethongBtn,
            phuongtayBtn, vietnamBtn, doanvanBtn, haogiathemonBtn, tieuthuyetBtn;

    ImageView imageBackSearchCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ngontinhBtn = findViewById(R.id.ngontinhBtn);
        nguocBtn = findViewById(R.id.nguocBtn);
        haihuocBtn = findViewById(R.id.haihuocBtn);
        dothiBtn = findViewById(R.id.dothiBtn);
        hiendaiBtn = findViewById(R.id.hiendaiBtn);
        xuyenkhongBtn = findViewById(R.id.xuyenkhongBtn);
        xuyennhanhBtn = findViewById(R.id.xuyennhanhBtn);
        truyenteenBtn = findViewById(R.id.truyenteenBtn);
        kiemhiepBtn = findViewById(R.id.kiemhiepBtn);
        tienhiepBtn = findViewById(R.id.tienhiepBtn);
        mattheBtn = findViewById(R.id.mattheBtn);
        cungdauBtn = findViewById(R.id.cungdauBtn);
        giadauBtn = findViewById(R.id.giadauBtn);
        niendaiBtn = findViewById(R.id.niendaiBtn);
        codaiBtn = findViewById(R.id.codaiBtn);
        dienvanBtn = findViewById(R.id.dienvanBtn);
        trinhthamBtn = findViewById(R.id.trinhthamBtn);
        dinangBtn = findViewById(R.id.dinangBtn);
        digioiBtn = findViewById(R.id.digioiBtn);
        vongduBtn = findViewById(R.id.vongduBtn);
        linhdiBtn = findViewById(R.id.linhdiBtn);
        trongsinhBtn = findViewById(R.id.trongsinhBtn);
        quansuBtn = findViewById(R.id.quansuBtn);
        lichsuBtn = findViewById(R.id.lichsuBtn);
        thamhiemBtn = findViewById(R.id.thamhiemBtn);
        huyenhuyenBtn = findViewById(R.id.huyenhuyenBtn);
        khoahuyenBtn = findViewById(R.id.khoahuyenBtn);
        hethongBtn = findViewById(R.id.hethongBtn);
        phuongtayBtn = findViewById(R.id.phuongtayBtn);
        vietnamBtn = findViewById(R.id.vietnamBtn);
        doanvanBtn = findViewById(R.id.doanvanBtn);
        haogiathemonBtn = findViewById(R.id.haogiathemonBtn);
        tieuthuyetBtn = findViewById(R.id.tieuthuyetBtn);
        imageBackSearchCategory = findViewById(R.id.imageBackSearchCategory);

        imageBackSearchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ngontinhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Ngôn Tình");
                startActivity(intent);
            }
        });
        nguocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Ngược");
                startActivity(intent);
            }
        });
        haihuocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Hài Hước");
                startActivity(intent);
            }
        });
        dothiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Đô Thị");
                startActivity(intent);
            }
        });
        hiendaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Hiện Đại");
                startActivity(intent);
            }
        });
        xuyenkhongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Xuyên Không");
                startActivity(intent);
            }
        });
        xuyennhanhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Xuyên Nhanh");
                startActivity(intent);
            }
        });
        truyenteenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Truyện Teen");
                startActivity(intent);
            }
        });
        kiemhiepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Kiếm Hiệp");
                startActivity(intent);
            }
        });
        tienhiepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Tiên Hiệp");
                startActivity(intent);
            }
        });
        mattheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Mạt Thế");
                startActivity(intent);
            }
        });
        cungdauBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Cung Đấu");
                startActivity(intent);
            }
        });
        giadauBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Gia Đấu");
                startActivity(intent);
            }
        });
        niendaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Niên Đại");
                startActivity(intent);
            }
        });
        codaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Cổ Đại");
                startActivity(intent);
            }
        });
        dienvanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Điền Văn");
                startActivity(intent);
            }
        });
        trinhthamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Trinh Thám");
                startActivity(intent);
            }
        });
        dinangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Dị Năng");
                startActivity(intent);
            }
        });
        digioiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Dị Giới");
                startActivity(intent);
            }
        });
        vongduBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Võng Du");
                startActivity(intent);
            }
        });
        linhdiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Linh Dị");
                startActivity(intent);
            }
        });
        trongsinhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Trọng Sinh");
                startActivity(intent);
            }
        });
        quansuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Quân Sự");
                startActivity(intent);
            }
        });
        lichsuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Lịch Sử");
                startActivity(intent);
            }
        });
        thamhiemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Thám Hiểm");
                startActivity(intent);
            }
        });
        huyenhuyenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Huyền Huyễn");
                startActivity(intent);
            }
        });
        khoahuyenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Khoa Huyễn");
                startActivity(intent);
            }
        });
        hethongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Hệ Thống");
                startActivity(intent);
            }
        });
        phuongtayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Phương Tây");
                startActivity(intent);
            }
        });
        vietnamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Việt Nam");
                startActivity(intent);
            }
        });
        doanvanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Đoản Văn");
                startActivity(intent);
            }
        });
        haogiathemonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Hào Giả Thế Môn");
                startActivity(intent);
            }
        });
        tieuthuyetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                intent.putExtra("category", "Tiểu Thuyết");
                startActivity(intent);
            }
        });

    }
}