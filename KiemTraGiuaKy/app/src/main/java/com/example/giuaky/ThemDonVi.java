package com.example.giuaky;

import android.content.ContentValues;
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

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ThemDonVi extends AppCompatActivity {
    ImageView imgLogo;
    Button btnThemLogo, btnAddDonVi;
    EditText edtThemTenDonVi, edtThemDiaChi, edtThemSDT, edtThemEmail, edtThemWebsite;
    Spinner spnDonViCha;

    ArrayList<String> donViList;
    ArrayAdapter<String> donViAdapter;
    SQLiteDatabase db;
    Bitmap selectedBitmap;
    String donViCha;
    int idDonViCha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_don_vi);

        db = openOrCreateDatabase("giuaky.db", MODE_PRIVATE, null);

        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setImageResource(R.drawable.avatar);
        btnThemLogo = findViewById(R.id.btnThemLogo);
        btnAddDonVi = findViewById(R.id.btnAddDonVi);
        edtThemTenDonVi = findViewById(R.id.edtThemTenDonVi);
        edtThemDiaChi = findViewById(R.id.edtThemDiaChi);
        edtThemSDT = findViewById(R.id.edtDetailSDTNV);
        edtThemEmail = findViewById(R.id.edtDetailEmailNV);
        edtThemWebsite = findViewById(R.id.edtThemWebsite);
        spnDonViCha = findViewById(R.id.spnDonViCha);

        donViList = new ArrayList<>();
        donViAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, donViList);
        spnDonViCha.setAdapter(donViAdapter);
        loadDonViCha();

        btnThemLogo.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn logo"), 1);
        });

        btnAddDonVi.setOnClickListener(v -> {
            addDonVi();
        });
    }

    private void loadDonViCha() {
        donViList.clear();
        donViList.add("-- Vui lòng chọn đơn vị cha --");
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
                imgLogo.setImageBitmap(bitmap);
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

    private void addDonVi() {
        String tenDonVi = edtThemTenDonVi.getText().toString();
        String diaChi = edtThemDiaChi.getText().toString();
        String sdt = edtThemSDT.getText().toString();
        String email = edtThemEmail.getText().toString();
        String website = edtThemWebsite.getText().toString();


        if(spnDonViCha.getSelectedItemPosition() != 0) {
            donViCha  = spnDonViCha.getSelectedItem().toString();
            idDonViCha = Integer.parseInt(donViCha.split(" - ")[0]);
        }

        if(tenDonVi.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || email.isEmpty() || website.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedBitmap != null) {
            byte[] logo = getBitmapAsByteArray(selectedBitmap);

            ContentValues contentValues = new ContentValues();
            contentValues.put("TenDonVi", tenDonVi);
            contentValues.put("DiaChi", diaChi);
            contentValues.put("SoDienThoai", sdt);
            contentValues.put("Email", email);
            contentValues.put("Website", website);
            contentValues.put("Logo", logo);
            contentValues.put("DonViCha", idDonViCha);

            long result = db.insert("DonVi", null, contentValues);

            if (result != -1) {
                Toast.makeText(this, "Đơn vị đã được thêm", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm đơn vị thất bại", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn logo", Toast.LENGTH_SHORT).show();
        }
    }
}
