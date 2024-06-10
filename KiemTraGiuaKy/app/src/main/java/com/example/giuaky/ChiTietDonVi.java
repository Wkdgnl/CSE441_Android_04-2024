package com.example.giuaky;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChiTietDonVi extends AppCompatActivity {
    ImageView imgDetailLogo;
    SQLiteDatabase db;
    TextView txtSua;
    EditText edtDetailTenDonVi, edtDetailDiaChi, edtDetailSDT, edtDetailEmail, edtDetailWebsite, edtDetailDonViCha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_don_vi);

        db = openOrCreateDatabase("giuaky.db", MODE_PRIVATE, null);

        imgDetailLogo = findViewById(R.id.imgDetailLogo);
        imgDetailLogo.setImageResource(R.drawable.avatar);
        txtSua = findViewById(R.id.txtSuaDonVi);
        edtDetailTenDonVi = findViewById(R.id.edtDetailTenDonVi);
        edtDetailDiaChi = findViewById(R.id.edtDetailDiaChi);
        edtDetailSDT = findViewById(R.id.edtDetailSDT);
        edtDetailEmail = findViewById(R.id.edtDetailEmail);
        edtDetailWebsite = findViewById(R.id.edtDetailWebsite);
        edtDetailDonViCha = findViewById(R.id.edtDetailDonViCha);

        loadData();

        txtSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(ChiTietDonVi.this, SuaDonVi.class);
                intent.putExtra("IDDonVi", getIntent().getIntExtra("IDDonVi", 0));
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        int id = getIntent().getIntExtra("IDDonVi", 0);
        Cursor cursor = db.rawQuery("SELECT * FROM DonVi WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            edtDetailTenDonVi.setText(cursor.getString(1));
            edtDetailDiaChi.setText(cursor.getString(5));
            edtDetailSDT.setText(cursor.getString(6));
            edtDetailEmail.setText(cursor.getString(2));
            edtDetailWebsite.setText(cursor.getString(3));
            byte[] img = cursor.getBlob(4);
            if(img != null){
                imgDetailLogo.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            }
            else {
                imgDetailLogo.setImageResource(R.drawable.avatar);
            }
        }

        // Get name DonViCha
        if(cursor.getString(7) == null || cursor.getString(7).equals("0")){
            edtDetailDonViCha.setText("Không có");
            return;
        }
        Cursor cursorDonViCha = db.rawQuery("SELECT * FROM DonVi WHERE id = ?", new String[]{cursor.getString(7)});
        if (cursorDonViCha.moveToNext()) {
            edtDetailDonViCha.setText(cursorDonViCha.getString(1));
        }
        cursor.close();
        cursorDonViCha.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}