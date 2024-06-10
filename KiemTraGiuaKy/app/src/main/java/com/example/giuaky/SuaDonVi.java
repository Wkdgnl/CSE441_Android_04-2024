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

public class SuaDonVi extends AppCompatActivity {
    ImageView imgSualLogo;
    Button btnSuaLogo, btnEditDonVi;
    EditText edtSuaTenDonVi, edtSuaDiaChi, edtSuaSDT, edtSuaEmail, edtSuaWebsite;
    Spinner spnSuaDonViCha;
    Bitmap selectedBitmap;
    SQLiteDatabase db;
    ArrayList<String> donViList = new ArrayList<>();
    ArrayAdapter<String> donViAdapter;
    String donViCha;
    int idDonViCha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sua_don_vi);

        db = openOrCreateDatabase("giuaky.db", MODE_PRIVATE, null);

        imgSualLogo = findViewById(R.id.imgSuaLogo);
        btnSuaLogo = findViewById(R.id.btnSuaLogo);
        btnEditDonVi = findViewById(R.id.btnEditDonVi);
        edtSuaTenDonVi = findViewById(R.id.edtSuaTenDonVi);
        edtSuaDiaChi = findViewById(R.id.edtSuaDiaChi);
        edtSuaSDT = findViewById(R.id.edtSuaSDT);
        edtSuaEmail = findViewById(R.id.edtSuaEmail);
        edtSuaWebsite = findViewById(R.id.edtSuaWebsite);
        spnSuaDonViCha = findViewById(R.id.spnSuaDonViCha);

        donViAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, donViList);
        spnSuaDonViCha.setAdapter(donViAdapter);

        loadDonViCha();
        loadData();

        btnSuaLogo.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn logo"), 1);
        });

        btnEditDonVi.setOnClickListener(v -> {
            saveData();
        });
    }

    private void loadDonViCha() {
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
        int id = getIntent().getIntExtra("IDDonVi", 0);
        Cursor cursor = db.rawQuery("SELECT * FROM DonVi WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            edtSuaTenDonVi.setText(cursor.getString(1));
            edtSuaDiaChi.setText(cursor.getString(5));
            edtSuaSDT.setText(cursor.getString(6));
            edtSuaEmail.setText(cursor.getString(2));
            edtSuaWebsite.setText(cursor.getString(3));
            byte[] logo = cursor.getBlob(4);
            if (logo != null) {
                imgSualLogo.setImageBitmap(BitmapFactory.decodeByteArray(logo, 0, logo.length));
            }
            else {
                imgSualLogo.setImageResource(R.drawable.avatar);
            }
            donViList.forEach(donVi -> {
                if (donVi.split(" - ")[0].equals(cursor.getString(7))) {
                    spnSuaDonViCha.setSelection(((ArrayAdapter<String>) spnSuaDonViCha.getAdapter()).getPosition(donVi));
                }
            });
        }
        cursor.close();
    }

    private void saveData() {
        int id = getIntent().getIntExtra("IDDonVi", 0);
        String tenDonVi = edtSuaTenDonVi.getText().toString();
        String diaChi = edtSuaDiaChi.getText().toString();
        String sdt = edtSuaSDT.getText().toString();
        String email = edtSuaEmail.getText().toString();
        String website = edtSuaWebsite.getText().toString();

        if(tenDonVi.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || email.isEmpty() || website.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put("TenDonVi", tenDonVi);
        contentValues.put("DiaChi", diaChi);
        contentValues.put("SoDienThoai", sdt);
        contentValues.put("Email", email);
        contentValues.put("Website", website);
        if(selectedBitmap != null){
            byte[] logo = getBitmapAsByteArray(selectedBitmap);
            contentValues.put("Logo", logo);
        }
        if(spnSuaDonViCha.getSelectedItemPosition() != 0) {
            donViCha  = spnSuaDonViCha.getSelectedItem().toString();
            idDonViCha = Integer.parseInt(donViCha.split(" - ")[0]);
        }
        contentValues.put("DonViCha", idDonViCha);

        long result = db.update("DonVi", contentValues, "id = ?", new String[]{String.valueOf(id)});
        if (result > 0) {
            Toast.makeText(this, "Sửa đơn vị thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, "Sửa đơn vị thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgSualLogo.setImageBitmap(bitmap);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}