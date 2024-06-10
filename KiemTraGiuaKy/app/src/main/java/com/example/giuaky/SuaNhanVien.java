package com.example.giuaky;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SuaNhanVien extends AppCompatActivity {
    ImageView imgSuaAnhDaiDien;
    Button btnSuaAnhDaiDien, btnSuaNhanVien;
    EditText edtSuaHoTen, edtSuaChucVu, edtSuaEmail, edtSuaSoDienThoai;
    Spinner spnSuaDonVi;
    Bitmap selectedBitmap;
    SQLiteDatabase db;
    ArrayList<String> donViList = new ArrayList<>();
    ArrayAdapter<String> donViAdapter;
    String donVi;
    int idDonVi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sua_nhan_vien);

        db = openOrCreateDatabase("giuaky.db", MODE_PRIVATE, null);
        imgSuaAnhDaiDien = findViewById(R.id.imgSuaAnhDaiDien);
        imgSuaAnhDaiDien.setImageResource(R.drawable.avatar);
        btnSuaAnhDaiDien = findViewById(R.id.btnSuaAnhDaiDien);
        btnSuaNhanVien = findViewById(R.id.btnSuaNhanVien);
        edtSuaHoTen = findViewById(R.id.edtSuaHoTen);
        edtSuaChucVu = findViewById(R.id.edtSuaChucVu);
        edtSuaEmail = findViewById(R.id.edtSuaEmailNV);
        edtSuaSoDienThoai = findViewById(R.id.edtSuaSDTNV);
        spnSuaDonVi = findViewById(R.id.spnSuaDonVi);
        donViAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, donViList);
        spnSuaDonVi.setAdapter(donViAdapter);

        btnSuaAnhDaiDien.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn logo"), 1);
        });

        btnSuaNhanVien.setOnClickListener(v -> {
            saveData();
        });

        loadDonVi();
        loadData();
    }

    private void loadDonVi() {
        donViList.clear();
        donViList.add("-- Không có --");
        Cursor cursor = db.rawQuery("SELECT * FROM DonVi", null);
        while (cursor.moveToNext()) {
            int idDonVi = cursor.getInt(0);
            String tenDonVi = cursor.getString(1);
            donViList.add(idDonVi + " - " + tenDonVi);
        }
        cursor.close();
        donViAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        Cursor cursor = db.rawQuery("SELECT * FROM NhanVien WHERE ID = ?", new String[]{String.valueOf(getIntent().getIntExtra("IDNhanVien", 0))});
        if (cursor.moveToNext()) {
            byte[] anhDaiDien = cursor.getBlob(5);
            Bitmap bitmap = BitmapFactory.decodeByteArray(anhDaiDien, 0, anhDaiDien.length);
            imgSuaAnhDaiDien.setImageBitmap(bitmap);
            edtSuaHoTen.setText(cursor.getString(1));
            edtSuaChucVu.setText(cursor.getString(2));
            edtSuaEmail.setText(cursor.getString(3));
            edtSuaSoDienThoai.setText(cursor.getString(4));
//
            donViList.forEach(donVi -> {
                if (donVi.split(" - ")[0].equals(cursor.getString(6))) {
                    spnSuaDonVi.setSelection(((ArrayAdapter<String>) spnSuaDonVi.getAdapter()).getPosition(donVi));
                }
            });
            cursor.close();
        }
    }

    private void saveData(){
        String hoTen = edtSuaHoTen.getText().toString();
        String chucVu = edtSuaChucVu.getText().toString();
        String email = edtSuaEmail.getText().toString();
        String soDienThoai = edtSuaSoDienThoai.getText().toString();
        String donVi = spnSuaDonVi.getSelectedItem().toString();
        if (hoTen.isEmpty() || chucVu.isEmpty() || email.isEmpty() || soDienThoai.isEmpty() || donVi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        int idDonVi = Integer.parseInt(donVi.split(" - ")[0]);

        ContentValues contentValues = new ContentValues();
        contentValues.put("HoTen", hoTen);
        contentValues.put("ChucVu", chucVu);
        contentValues.put("Email", email);
        contentValues.put("SoDienThoai", soDienThoai);
        contentValues.put("MaDonVi", idDonVi);
        if(selectedBitmap != null) {
            contentValues.put("AnhDaiDien", getBitmapAsByteArray(selectedBitmap));
        }
        long result = db.update("NhanVien", contentValues, "ID = ?", new String[]{String.valueOf(getIntent().getIntExtra("IDNhanVien", 0))});
        if(result > 0) {
            Toast.makeText(this, "Sửa nhân viên thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, "Sửa nhân viên thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgSuaAnhDaiDien.setImageBitmap(bitmap);
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
}