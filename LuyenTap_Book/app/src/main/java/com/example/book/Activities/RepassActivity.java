package com.example.book.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RepassActivity extends AppCompatActivity {
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
    EditText edtEmail, edtPass, edtRepass;
    Button btnRepass;
    TextView txtloginnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_repass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtEmail = findViewById(R.id.edtMail);
        edtPass = findViewById(R.id.edtPassword);
        edtRepass = findViewById(R.id.edtRePassword);
        btnRepass = findViewById(R.id.btnRepass);
        txtloginnow = findViewById(R.id.txtloginnow);
        ImageView imageMail = findViewById(R.id.imageMail);
        ImageView imagePassword = findViewById(R.id.imagePassword);
        ImageView imageconPassword = findViewById(R.id.imageconPassword);
        btnRepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật mật khẩu
                String newPassword = edtRepass.getText().toString();
                String email = edtEmail.getText().toString();
                DatabaseReference emailRef = usersRef.child(email);
                emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Email exists in the database, update the password
                            emailRef.child("password").setValue(newPassword)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Update successful
                                            Log.d("Firebase", "Cập nhật mật khẩu thành công");
                                            Toast.makeText(RepassActivity.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Update failed
                                            Log.d("Firebase", "Cập nhật mật khẩu thất bại", e);
                                            Toast.makeText(RepassActivity.this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Email does not exist in the database
                            Toast.makeText(RepassActivity.this, "Email không tồn tại trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
            }
        });
        txtloginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        edtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        edtRepass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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