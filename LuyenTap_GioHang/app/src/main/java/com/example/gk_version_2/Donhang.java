package com.example.gk_version_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Donhang extends AppCompatActivity {
ImageView img;
ImageButton btntang,btngiam;
TextView txtten,txtmota,txtgia,txtsoluong,txtthanhtien;
Button btnxacnhan,btn_trolai_1;
int soluong = 1;
int tien;
DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donhang);

        dbHelper = new DBHelper(this);
        img = findViewById(R.id.imggio);
        txtten = findViewById(R.id.txttenspgio);
        txtmota = findViewById(R.id.txtmotagio);
        txtgia = findViewById(R.id.txtgiagio);
        txtsoluong = findViewById(R.id.txtsl);
        txtthanhtien = findViewById(R.id.txttiensp);
        btngiam = findViewById(R.id.btngiam);
        btntang = findViewById(R.id.btntang);
        btnxacnhan = findViewById(R.id.btnxacnhan);
        btn_trolai_1 = findViewById(R.id.btnhome);

        Intent intent = getIntent();
        int imgsp = intent.getIntExtra("anh",1);
        String ten = intent.getStringExtra("ten");
        String mota = intent.getStringExtra("mota");
        int gia = intent.getIntExtra("gia",1);

        img.setImageResource(imgsp);
        txtten.setText(ten);
        txtmota.setText(mota);
        txtgia.setText("Giá "+gia+" $");
        txtsoluong.setText(soluong+"");
        tien = (soluong * gia);
        txtthanhtien.setText("Thành tiền "+tien+" $");

        btntang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong++;
                txtsoluong.setText(soluong+"");
                tien = (soluong * gia);
                txtthanhtien.setText("Thành tiền "+tien+" $");

            }
        });
        btngiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluong--;
                txtsoluong.setText(soluong+"");
                tien = (soluong * gia);
                txtthanhtien.setText("Thành tiền "+tien+" $");
                if(soluong <=1){
                    Toast.makeText(Donhang.this, "Số lượng không thể nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                    txtsoluong.setText("1");
                    soluong = 1;
                    tien = (soluong * gia);
                    txtthanhtien.setText("Thành tiền "+tien+" $");
                }
            }
        });

        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tensanpham, mota;
                int gia, soluong, thanhtien;
                // Chuyển đổi ảnh từ ImageView thành mảng byte[]
                byte[] imgsp = convertImageViewToByteArray(img);
//                byte[] imgsp = getIntent().getByteArrayExtra("anh"); // Lấy dữ liệu hình ảnh từ Intent


                tensanpham = txtten.getText().toString();
                mota = txtmota.getText().toString();
                gia = Integer.parseInt(txtgia.getText().toString().replace("Giá ", "").replace("$", "").replace(" ", ""));
                soluong = Integer.parseInt(txtsoluong.getText().toString());
                thanhtien = Integer.parseInt(txtthanhtien.getText().toString().replace("Thành tiền ", "").replace("$", "").replace(" ", ""));

                boolean addSuccess = dbHelper.insertData2(tensanpham, mota, gia, soluong, thanhtien, imgsp);
                if (addSuccess) {
                    Toast.makeText(Donhang.this, "Đơn hàng thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Donhang.this, "Đơn hàng thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_trolai_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private byte[] convertImageViewToByteArray(ImageView imageView) {
        //lấy đối tượng Bitmap từ ImageView bằng cách truy cập vào drawable hiện tại của ImageView
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap scaledBitmap = scaleBitmap(bitmap, 500); // Thay đổi 500 thành kích thước tối đa mong muốn
        ByteArrayOutputStream stream = new ByteArrayOutputStream(); //Khởi tạo để để lưu trữ dữ liệu ảnh dưới dạng byte.
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);//nén Bitmap thành định dạng PNG và ghi dữ liệu nén vào stream.
        return stream.toByteArray();
    }
    //nén ảnh
    private Bitmap scaleBitmap(Bitmap bitmap, int maxSize) {
        // lấy kích thước ban đầu của Bitmap.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //tính tỷ lệ giữa chiều rộng và chiều cao của Bitmap.
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

}