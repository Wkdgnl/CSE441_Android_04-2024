package com.example.book.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
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

import com.example.book.Adapters.SoftInputAssist;
import com.example.book.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://book-e0e75-default-rtdb.firebaseio.com/");
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String IsLogin = "IsLoginKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (sharedPreferences.getBoolean(IsLogin, false)) {
            // Nếu đã đăng nhập, chuyển hướng đến MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        addControll();
        final EditText mail = findViewById(R.id.edtMail);
        final EditText password = findViewById(R.id.edtPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final TextView registernow = findViewById(R.id.txtregisternow);
        TextView txtRepass = findViewById(R.id.txtRepass);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mailTxt = String.valueOf(mail.getText());
                final String passwordTxt = String.valueOf(password.getText());
                final String emailKey = mailTxt.replace('.', '_');
                if(mailTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin của bạn!", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(emailKey)) {
                                final String getpasswordTxt = snapshot.child(emailKey).child("password").getValue(String.class);
                                if(getpasswordTxt.equals(passwordTxt)) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(IsLogin, true);
                                    editor.putString("Username", mailTxt); // Lưu tên đăng nhập
                                    editor.apply();
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        txtRepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RepassActivity.class));
            }
        });
    }

    private void addControll() {
        EditText edtMail = findViewById(R.id.edtMail);
        EditText edtPassword = findViewById(R.id.edtPassword);
        ImageView imageMail = findViewById(R.id.imageMail);
        ImageView imagePassword = findViewById(R.id.imagePassword);

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
    }
}