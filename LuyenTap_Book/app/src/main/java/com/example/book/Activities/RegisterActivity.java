package com.example.book.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.book.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://book-e0e75-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControll();
        final EditText email = findViewById(R.id.edtMail);
        final EditText password = findViewById(R.id.edtPassword);
        final EditText conPassword = findViewById(R.id.edtconPassword);
        final Button btnRegister = findViewById(R.id.btnRegister);
        final TextView txtloginnow = findViewById(R.id.txtloginnow);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();
                final String emailKey = emailTxt.replace('.', '_');
                if (emailTxt.isEmpty() || passwordTxt.isEmpty() || conPasswordTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin của bạn!", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(conPasswordTxt)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu nhập lại không chính xác!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(emailKey)) {
                                Toast.makeText(RegisterActivity.this, "Email đã tồn tại.", Toast.LENGTH_SHORT).show();
                            } else {
                                DatabaseReference newUserRef = databaseReference.child("users").child(emailKey);
                                newUserRef.child("password").setValue(passwordTxt);
                                newUserRef.child("avatar").setValue("data:image/png;base64,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");
                                newUserRef.child("name").setValue(emailTxt);
                                Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        txtloginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void addControll() {
        EditText edtMail = findViewById(R.id.edtMail);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtconPassword = findViewById(R.id.edtconPassword);
        ImageView imageMail = findViewById(R.id.imageMail);
        ImageView imagePassword = findViewById(R.id.imagePassword);
        ImageView imageconPassword = findViewById(R.id.imageconPassword);
        edtMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Đặt hình ảnh mới khi EditText được nhấn
                    imageMail.setImageResource(R.drawable.maildam);
                } else {
                    // Đặt lại hình ảnh ban đầu khi EditText mất focus
                    imageMail.setImageResource(R.drawable.mailnhat);
                }
            }
        });
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Đặt hình ảnh mới khi EditText được nhấn
                    imagePassword.setImageResource(R.drawable.lockdam);
                } else {
                    // Đặt lại hình ảnh ban đầu khi EditText mất focus
                    imagePassword.setImageResource(R.drawable.locknhat);
                }
            }
        });
        edtconPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Đặt hình ảnh mới khi EditText được nhấn
                    imageconPassword.setImageResource(R.drawable.lockdam);
                } else {
                    // Đặt lại hình ảnh ban đầu khi EditText mất focus
                    imageconPassword.setImageResource(R.drawable.locknhat);
                }
            }
        });
    }
}