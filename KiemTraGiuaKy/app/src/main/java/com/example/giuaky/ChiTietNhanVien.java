package com.example.giuaky;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChiTietNhanVien extends AppCompatActivity {
    ImageView imgDetailLogoNV;
    EditText edtDetailTenNV, edtDetailChucVu, edtDetailEmail, edtDetailSDT, edtDetailDonViNV;
    SQLiteDatabase db;
    TextView txtSuaNhanVien;
    int idNhanVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_nhan_vien);
        db = openOrCreateDatabase("giuaky.db", MODE_PRIVATE, null);

        imgDetailLogoNV = findViewById(R.id.imgDetailAnhDaiDien);
        edtDetailTenNV = findViewById(R.id.edtDetailHoTen);
        edtDetailChucVu = findViewById(R.id.edtDetailChucVu);
        edtDetailEmail = findViewById(R.id.edtDetailEmailNV);
        edtDetailSDT = findViewById(R.id.edtDetailSDTNV);
        edtDetailDonViNV = findViewById(R.id.edtDetailDonViNV);
        txtSuaNhanVien = findViewById(R.id.txtSuaNhanVien);

        loadData();

        txtSuaNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(ChiTietNhanVien.this, SuaNhanVien.class);
                intent.putExtra("IDNhanVien", idNhanVien);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        idNhanVien = getIntent().getIntExtra("IDNhanVien", 0);
        Cursor cursor = db.rawQuery("SELECT * FROM NhanVien WHERE id = ?", new String[]{String.valueOf(idNhanVien)});
        if (cursor.moveToNext()) {
            edtDetailTenNV.setText(cursor.getString(1));
            edtDetailChucVu.setText(cursor.getString(2));
            edtDetailEmail.setText(cursor.getString(3));
            edtDetailSDT.setText(cursor.getString(4));
            byte[] anh = cursor.getBlob(5);
            imgDetailLogoNV.setImageBitmap(BitmapFactory.decodeByteArray(anh, 0, anh.length));
        }

        // Get name DonVi from IDDonVi
        Cursor cursorDonVi = db.rawQuery("SELECT * FROM DonVi WHERE id = ?", new String[]{String.valueOf(cursor.getInt(6))});
        if (cursorDonVi.moveToNext()) {
            edtDetailDonViNV.setText(cursorDonVi.getString(1));
        }
        cursorDonVi.close();
        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}