package com.example.tipcaculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText edtAmount;
    TextView txvPercent, edtTip, edtTotal;
    Button btnSub, btnSum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtAmount = findViewById(R.id.txtBillAmount);
        edtTip = findViewById(R.id.lbTipResult);
        edtTotal = findViewById(R.id.lbResult);
        txvPercent = findViewById(R.id.lbPercentResult);
        btnSub = findViewById(R.id.btnTru);
        btnSum = findViewById(R.id.btnCong);
        btnSub.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(txvPercent.getText().toString().replace("%", "")) - 1;
                txvPercent.setText(temp+"%");
                int billAmount = edtAmount.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(edtAmount.getText().toString());
                double tip = (double) billAmount*temp/100;
                edtTip.setText(tip+"");
                edtTotal.setText(billAmount + tip + "");
            }
        });
        btnSum.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(txvPercent.getText().toString().replace("%", "")) + 1;
                txvPercent.setText(temp+"%");
                int billAmount = edtAmount.getText().toString().trim().isEmpty() ? 0 : Integer.parseInt(edtAmount.getText().toString());
                double tip = (double) billAmount*temp/100;
                edtTip.setText(tip+"");
                edtTotal.setText(billAmount + tip + "");
            }
        });
    }
}