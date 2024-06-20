package com.example.project_btl_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class CategoryManagementActivity extends AppCompatActivity {
    ListView lvCategory;
    TextView txtBackToManagementHomepage, txtVAddCategory;
    ArrayAdapterCategoryManagement myAdapterCategoryManagement;
    ArrayList<Category> myListCategory;
    ArrayList<String> myListCategoryName;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    boolean checkCategoryName = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        txtVAddCategory = findViewById(R.id.txtVAddCategory);
        txtBackToManagementHomepage = findViewById(R.id.txtBackToManagementHomepage);
        lvCategory = findViewById(R.id.lvCategory);
        myListCategory = new ArrayList<>();
        myListCategoryName = new ArrayList<>();

        db.getReference("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myListCategory.clear();
                myListCategoryName.clear();
                for (DataSnapshot data: snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    if (category != null) {
                        myListCategory.add(category);
                        myListCategoryName.add(category.getName());
                    }

                }
                myAdapterCategoryManagement = new ArrayAdapterCategoryManagement(CategoryManagementActivity.this, R.layout.layout_category, myListCategory);
                lvCategory.setAdapter(myAdapterCategoryManagement);
                txtBackToManagementHomepage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                //Show Dialog nhập tên thể lọại khi thêm
                txtVAddCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInputDialog();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Load data failed "+error.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Dialog nhập tên
    public void showInputDialog() {
        // Tạo một AlertDialog.Builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CategoryManagementActivity.this);

        // Thiết lập tiêu đề của Dialog
        dialogBuilder.setTitle("Nhập tên thể loại mới");

        // Tạo một EditText để người dùng nhập giá trị
        final EditText input = new EditText(CategoryManagementActivity.this);

        // Đặt EditText vào Dialog
        dialogBuilder.setView(input);

        // Thiết lập nút Positive Button (OK)
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCategoryName = true;
                // Lấy giá trị từ EditText
                String categoryName = input.getText().toString().trim();
                if (categoryName.isEmpty()) {
                    Toast.makeText(CategoryManagementActivity.this, "Không để trống tên thể loại", Toast.LENGTH_SHORT).show();
                }
                //Nếu trùng tên -> xác nhận trước khi thêm
                else if (myListCategoryName.contains(categoryName)) {
                    submitInsert(categoryName);
                }
                else {
                    insertCategory(categoryName).thenAccept(newCategory -> {
                        // Thực hiện các hành động tiếp theo sau khi category được thêm thành công
                        Toast.makeText(CategoryManagementActivity.this, "Thêm thể loại mới thành công", Toast.LENGTH_SHORT).show();
//                        myListCategory.add(newCategory);
//                        myAdapterCategoryManagement.notifyDataSetChanged();
                    }).exceptionally(ex -> {
                        // Xử lý lỗi
                        Toast.makeText(getApplicationContext(), "Failed to add category: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        return null;
                    });
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Không có hành động cụ thể nếu người dùng chọn Cancel
                dialog.cancel();
            }
        });

        // Thiết lập nút Negative Button (Cancel)

        // Tạo và hiển thị Dialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    //Xác nhận khi trùng tên
    public void submitInsert(String categoryName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CategoryManagementActivity.this);

        // Thiết lập tiêu đề của Dialog
        dialogBuilder.setTitle("Tên thể loại đã tồn tại, bạn chắc chắn muốn thêm?");
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertCategory(categoryName).thenAccept(newCategory -> {
                    // Thực hiện các hành động tiếp theo sau khi category được thêm thành công
                    Toast.makeText(CategoryManagementActivity.this, "Thêm thể loại mới thành công", Toast.LENGTH_SHORT).show();
//                    myListCategory.add(newCategory);
//                    myAdapterCategoryManagement.notifyDataSetChanged();
                }).exceptionally(ex -> {
                    // Xử lý lỗi
                    Toast.makeText(getApplicationContext(), "Failed to add category: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    // all
//    public CompletableFuture<List<Category>> getAllCategories() {
//        CompletableFuture<List<Category>> future = new CompletableFuture<>();
//
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Category> categoryList = new ArrayList<>();
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    Category category = data.getValue(Category.class);
//                    if (category != null) {
//                        categoryList.add(category);
//                    }
//                }
//                future.complete(categoryList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                future.completeExceptionally(error.toException());
//            }
//        });
//
//        return future;
//    }

    //Thêm
    public CompletableFuture<Category> insertCategory(String name) {
        CompletableFuture<Category> future = new CompletableFuture<>();
        DatabaseReference categoryRef = db.getReference("Category");

        // Tạo một key mới cho category
        String id = categoryRef.push().getKey();

        // Tạo category mới với id và name
        Category newCategory = new Category(id, name);

        // Đưa category mới lên Firebase
        categoryRef.child(id).setValue(newCategory).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(newCategory);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

}