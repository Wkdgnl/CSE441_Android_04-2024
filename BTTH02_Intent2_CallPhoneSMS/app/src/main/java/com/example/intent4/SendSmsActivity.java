package com.example.intent4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SendSmsActivity extends AppCompatActivity {
    EditText edtsms;
    ImageButton btnsms;
    Button btnback2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_sms);

        edtsms.findViewById(R.id.edtsms);
        btnsms.findViewById(R.id.btnsms);
        btnback2.findViewById(R.id.btnback2);

        // onclick

        btnsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsintent = new Intent(Intent.ACTION_SEND, Uri.parse("smsto:"+edtsms.getText().toString()));
                startActivity(smsintent);
            }
        });
        btnback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}