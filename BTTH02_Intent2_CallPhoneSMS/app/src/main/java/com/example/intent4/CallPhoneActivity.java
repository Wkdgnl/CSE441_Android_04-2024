package com.example.intent4;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CallPhoneActivity extends AppCompatActivity {

    EditText edtPhone;
    ImageButton btnCall;
    Button btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_call_phone);


        //
        edtPhone = findViewById(R.id.edtPhone);
        btnCall = findViewById(R.id.btnCall);
        btnback = findViewById(R.id.btnback);

        // onclick
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khai báo intent ẩn
                Intent callintent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+edtPhone.getText().toString()));
                // yêu cầu sự đồng ý cuả người dùng
                if (ActivityCompat.checkSelfPermission(CallPhoneActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CallPhoneActivity.this, new String[]{android.Manifest.permission.CALL_PHONE},1);
                    return;
                }

                startActivity(callintent);
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}