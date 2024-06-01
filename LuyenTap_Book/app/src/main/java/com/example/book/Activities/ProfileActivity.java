package com.example.book.Activities;

import static com.example.book.Activities.LoginActivity.IsLogin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.book.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameProfileTxt, LogoutTxt, deleteUserTxt;
    ImageView userImageProfile, imageLogout, imageBackProfile, imageDeleteUser;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userNameProfileTxt = findViewById(R.id.userNameProfileTxt);
        userImageProfile = findViewById(R.id.userImageProfile);
        LogoutTxt = findViewById(R.id.LogoutTxt);
        imageLogout = findViewById(R.id.imageLogout);
        imageBackProfile = findViewById(R.id.imageBackProfile);
        deleteUserTxt = findViewById(R.id.deleteUserTxt);
        imageDeleteUser = findViewById(R.id.imageDeleteUser);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "thien");
        userNameProfileTxt.setText(username);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String avatarUrl = dataSnapshot.child("avatar").getValue(String.class);
                Glide.with(ProfileActivity.this)
                        .load(avatarUrl)
                        .circleCrop()
                        .into(userImageProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        LogoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(ProfileActivity.this)
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Người dùng xác nhận đăng xuất
                                // Đặt IsLogin thành false
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(IsLogin, false);
                                editor.apply();

                                // Chuyển hướng đến LoginActivity
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        imageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(ProfileActivity.this)
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Người dùng xác nhận đăng xuất
                                // Đặt IsLogin thành false
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(IsLogin, false);
                                editor.apply();

                                // Chuyển hướng đến LoginActivity
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        deleteUserTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(ProfileActivity.this)
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn có chắc chắn muốn xóa tài khoản?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Người dùng xác nhận xóa tài khoản
                                // Xóa người dùng từ Firebase Database
                                usersRef.child(username).removeValue();

                                // Xóa tất cả các bình luận của người dùng
                                DatabaseReference commentsRef = database.getReference("comments");
                                commentsRef.orderByChild("userID").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                                            commentSnapshot.getRef().removeValue();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                // Đặt IsLogin thành false
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(IsLogin, false);
                                editor.apply();

                                // Chuyển hướng đến LoginActivity
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        imageDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(ProfileActivity.this)
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn có chắc chắn muốn xóa tài khoản?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Người dùng xác nhận xóa tài khoản
                                // Xóa người dùng từ Firebase Database
                                usersRef.child(username).removeValue();

                                // Xóa tất cả các bình luận của người dùng
                                DatabaseReference commentsRef = database.getReference("comments");
                                commentsRef.orderByChild("userID").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                                            commentSnapshot.getRef().removeValue();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                // Đặt IsLogin thành false
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(IsLogin, false);
                                editor.apply();

                                // Chuyển hướng đến LoginActivity
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        imageBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}