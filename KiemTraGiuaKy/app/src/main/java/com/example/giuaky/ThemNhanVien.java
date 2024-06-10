package com.example.giuaky;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ThemNhanVien extends AppCompatActivity {
    SQLiteDatabase db;
    ImageView imgAnhDaiDien;
    Button btnThemAnhDaiDien, btnThemNhanVien;
    EditText edtThemHoTen, edtThemChucVu, edtThemSDT, edtThemEmail;
    ArrayList<String> donViList;
    Bitmap selectedBitmap = null;
    byte[] anhDaiDien;
    ArrayAdapter<String> donViAdapter;
    Spinner spnDonVi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_nhan_vien);
        db = openOrCreateDatabase("giuaky.db", MODE_PRIVATE, null);
        donViList = new ArrayList<>();
        donViAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, donViList);
        spnDonVi = findViewById(R.id.spnDonVi);
        spnDonVi.setAdapter(donViAdapter);
        imgAnhDaiDien = findViewById(R.id.imgDetailAnhDaiDien);
        btnThemAnhDaiDien = findViewById(R.id.btnThemAnhDaiDien);
        btnThemNhanVien = findViewById(R.id.btnAddNhanVien);
        edtThemHoTen = findViewById(R.id.edtDetailHoTen);
        edtThemChucVu = findViewById(R.id.edtDetailChucVu);
        edtThemSDT = findViewById(R.id.edtDetailSDTNV);
        edtThemEmail = findViewById(R.id.edtDetailEmailNV);

        imgAnhDaiDien.setImageResource(R.drawable.avatar);
        loadDonViCha();

        btnThemAnhDaiDien.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn logo"), 1);
        });

        btnThemNhanVien.setOnClickListener(v -> {
            try {
                addNhanVien();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadDonViCha() {
        donViList.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM DonVi", null);
        while (cursor.moveToNext()) {
            int idDonVi = cursor.getInt(0);
            String tenDonVi = cursor.getString(1);
            donViList.add(idDonVi + " - " + tenDonVi);
        }
        cursor.close();
        donViAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgAnhDaiDien.setImageBitmap(bitmap);
                selectedBitmap = bitmap; // Lưu lại bitmap đã chọn
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    private void addNhanVien() throws IOException {
        String hoTen = edtThemHoTen.getText().toString();
        String chucVu = edtThemChucVu.getText().toString();
        String sdt = edtThemSDT.getText().toString();
        String email = edtThemEmail.getText().toString();
        String donVi = spnDonVi.getSelectedItem().toString();
        if (hoTen.isEmpty() || chucVu.isEmpty() || sdt.isEmpty() || email.isEmpty() || donVi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] donViArr = donVi.split(" - ");
        int idDonVi = Integer.parseInt(donViArr[0]);
        if(selectedBitmap != null) {
            anhDaiDien = getBitmapAsByteArray(selectedBitmap);
        }
        else {
            selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("android.resource://com.example.giuaky/drawable/avatar"));
            anhDaiDien = getBitmapAsByteArray(selectedBitmap);
        }
        db.execSQL("INSERT INTO NhanVien VALUES (null, ?, ?, ?, ?, ?, ?)", new Object[]{hoTen, chucVu, email, sdt, anhDaiDien, idDonVi});
        Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}