package com.example.project_btl_android;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="qlSP.db";
    EditText edtUserName, edtPassword;
    TextView txtToRegisterActivity, txtForgetPassword;
    Button btnLogin;
    FirebaseDatabase db;
    DatabaseReference myRef;
    String cartId;
    String idUser = "";

    @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                EdgeToEdge.enable(this);
                setContentView(R.layout.activity_main);
                //Xóa CSDL trong device
        //        deleteDatabase("qlSP.db");
                //Copy CSDL từ asset vào device
                processCopy();
                edtUserName = findViewById(R.id.UserName);
                edtPassword = findViewById(R.id.PassWord);
                txtForgetPassword = findViewById(R.id.txtForgetPassword);
                edtUserName.setText("");
                edtPassword.setText("");
                btnLogin = findViewById(R.id.btnLogin);
                txtToRegisterActivity = findViewById(R.id.txtToRegisterActivity);
                database = openOrCreateDatabase("qlSP.db", MODE_PRIVATE, null);
                db = new FirebaseDataBaseHelper().getFirebaseDatabase();
                myRef = db.getReference("Account");
                //Xử lý sự kiện khi người dùng yêu cầu đăng ký -> chuyển đến activity đăng ký
                txtToRegisterActivity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        intent.setAction("FromMain");
                        startActivity(intent);
                    }
                });

                txtForgetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage();
                    }
                });

                //Khi người dùng đăng nhập
                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userName = edtUserName.getText().toString().trim();
                        String passWord = edtPassword.getText().toString();
                        //Xác định là người dùng khi đã đăng nhập thành công
        //                boolean isUser = false;
        //                //Phân quyền người dùng
        //                int role = -1;
                        //Khi người dùng không nhập đủ các trường thông tin
                        if(userName.isEmpty()){
                            edtUserName.setError("Vui lòng nhập tên đăng nhập/Email");
                            edtUserName.requestFocus();
                        } else if(passWord.isEmpty()){
                            edtPassword.setError("Vui lòng nhập mật khẩu");
                            edtPassword.requestFocus();
                        }
                        //Nhập đủ
                        else{
                            myRef.orderByChild("UserName").equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean userFound = false;
                                    int role = -1;
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        String dbPassword = userSnapshot.child("Password").getValue(String.class);
                                        if (dbPassword != null && dbPassword.equals(passWord)) {
                                            idUser = userSnapshot.getKey();
                                            userFound = true;
                                            role = userSnapshot.child("Role").getValue(Integer.class);
                                            break;
                                        }
                                    }
                                    if (!userFound) {
                                        Toast.makeText(MainActivity.this, "Thông tin đăng nhập không hợp lệ", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        if (role == 1) {
                                            db.getReference("Cart").orderByChild("userId").equalTo(idUser).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            cartId = dataSnapshot.getValue(Cart.class).getId();
                                                        }
                                                        Account user = new Account(idUser, userName);
                                                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                                        intent.putExtra("idUser", idUser);
                                                        intent.putExtra("idUser", idUser);
                                                        intent.putExtra("user", user);
                                                        intent.putExtra("cartId", cartId);
                                                        startActivity(intent);
                                                    } else {
                                                        CartService cartService = new CartService();
                                                        cartService.addCart(idUser).thenAccept(cart -> {
                                                            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                                            intent.putExtra("idUser", idUser);
                                                            intent.putExtra("cartId", cart.getId());
                                                            startActivity(intent);
                                                        }).exceptionally(ex -> {
                                                            // Xử lý trường hợp ngoại lệ (nếu có)
                                                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                                            return null;
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(getApplicationContext(), "Load data failed "+error.toString(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                        else {
                                            Intent intent = new Intent(MainActivity.this, ManagementHomepageActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MainActivity.this, "Error getting data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

    public void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_forget_password, null);
        builder.setView(dialogView);
        TextView txtCancelInForgetPassword = dialogView.findViewById(R.id.txtCancelInForgetPassword);
        TextView txtSubmitInForgetPassword = dialogView.findViewById(R.id.txtSubmitInForgetPassword);
        EditText edtEmailInForgetPassword = dialogView.findViewById(R.id.edtEmailInForgetPassword);
        AlertDialog dialog2 = builder.create();
        txtCancelInForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {dialog2.dismiss();}
        });
        txtSubmitInForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailInForgetPassword.getText().toString().trim();
                if(email.isEmpty()){
                    edtEmailInForgetPassword.setError("Vui lòng nhập email");
                    edtEmailInForgetPassword.requestFocus();
                    return;
                }
                else{
                    try {
                        Cursor cursor = database.query("Account", null, "Email = ?", new String[]{email}, null, null, null);
                        if(!cursor.moveToFirst()){
                            edtEmailInForgetPassword.setError("Email không tồn tại");
                            edtEmailInForgetPassword.requestFocus();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Đã gửi mã OTP đến địa chỉ email của bạn", Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                dialog2.dismiss();
            }
        });
        dialog2.show();

    }


    //Copy cơ sở dữ liệu từ thư mục assets vào project
    private void processCopy() {
//private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
// Path to the just created empty db
            String outFileName = getDatabasePath();
// if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
// Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
// transfer bytes from the inputfile to the outputfile
// Truyền bytes dữ liệu từ input đến output
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
// Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}