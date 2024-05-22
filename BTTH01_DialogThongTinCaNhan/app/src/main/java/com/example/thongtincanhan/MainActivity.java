package com.example.thongtincanhan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // khai báo biến giao diện
    EditText edtHoten, edtCCCD, edtBosung;
    Button btnGui;
    RadioGroup idgroup;
    CheckBox chkBA, chkDev, chkTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ánh xạ id
        edtHoten = findViewById(R.id.edtHoten);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtBosung = findViewById(R.id.edtBosung);
        btnGui = findViewById(R.id.btnGui);
        idgroup = findViewById(R.id.idgroup);
        chkBA = findViewById(R.id.chkBA);
        chkDev = findViewById(R.id.chkDev);
        chkTest = findViewById(R.id.chkTest);


        // xử lý onclick button Gửi
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lấy thông tin họ tên
                String hoten = edtHoten.getText().toString();
                if (hoten.length()<3)
                {
                    Toast.makeText(MainActivity.this, "Họ tên lớn hơn 3 ký tự", Toast.LENGTH_LONG).show();
                    edtHoten.requestFocus();
                    edtHoten.selectAll();
                    return;
                }

                // lấy CCCD
                String CCCD = edtCCCD.getText().toString();
                if (CCCD.length()!=9)
                {
                    Toast.makeText(MainActivity.this, "CCCD phải có 9 ký tự", Toast.LENGTH_LONG).show();
                    edtCCCD.requestFocus();
                    edtCCCD.selectAll();
                    return;
                }

                // Lấy thông tin trình độ
                int idselect = idgroup.getCheckedRadioButtonId();
                RadioButton radselect = findViewById(idselect);
                String trinhdo = radselect.getText().toString();

                // Lấy thông tin Vị trí
                String vitri ="";
                if (chkBA.isChecked())
                {
                    vitri += chkBA.getText().toString()+"-";
                }
                if (chkDev.isChecked())
                {
                    vitri += chkDev.getText().toString()+"-";
                }
                if (chkTest.isChecked())
                {
                    vitri += chkTest.getText().toString()+"-";
                }

                // lấy thông tin bo sung
                String bosung = edtBosung.getText().toString();
                String tonghop = hoten+"\n" +CCCD +"\n"+trinhdo+"\n"+vitri+"\n";
                tonghop += "-------------Thông tin khác--------------\n";
                tonghop += bosung+"\n";
                tonghop +="-----------------------------------------";


                // Tạo Dialog và hiển thị thông tin
                AlertDialog.Builder mydialog = new AlertDialog.Builder(MainActivity.this);
                mydialog.setTitle("Thông tin ứng tuyển");
                mydialog.setMessage(tonghop);
                mydialog.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                mydialog.create().show();
            }
        });
    }
}