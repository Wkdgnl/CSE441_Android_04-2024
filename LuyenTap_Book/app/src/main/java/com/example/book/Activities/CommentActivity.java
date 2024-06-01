package com.example.book.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.Adapters.CommentAdapter;
import com.example.book.Domain.Comment;
import com.example.book.Domain.User;
import com.example.book.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    EditText commentEdt;
    Context context;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    ImageView sendComment, imageBack;
    TextView totalCommnetsTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        int storyId = intent.getIntExtra("STORY_ID", -1);
        String read = intent.getStringExtra("READ_TYPE"); // lấy giá trị của biến read
        context = this;
        List<Comment> comments = new ArrayList<>();
        Map<String, User> users = new HashMap<>();
        commentEdt = findViewById(R.id.commentEdt);
        sendComment = findViewById(R.id.sendComment);
        RecyclerView commentView = findViewById(R.id.commnentView);
        totalCommnetsTxt = findViewById(R.id.totalCommnetsTxt);
        imageBack = findViewById(R.id.imageBack);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentView.setLayoutManager(layoutManager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        CommentAdapter adaptercomment = new CommentAdapter(comments, users);
        commentView.setAdapter(adaptercomment);
        DatabaseReference usersRef = database.getReference("users");
        DatabaseReference commentRef = database.getReference("comments");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kiểm tra xem dataSnapshot có tồn tại không
                if (dataSnapshot.exists()) {
                    // Lấy ra user dưới dạng Map
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();
                    // Duyệt qua tất cả các user
                    for (Map.Entry<String, Object> entry : userMap.entrySet()) {
                        // Lấy ra thông tin của user
                        Map singleUser = (Map) entry.getValue();
                        String password = (String) singleUser.get("password");
                        String name = (String) singleUser.get("name");
                        String avatar = (String) singleUser.get("avatar");
                        if (avatar != null) {
                            Log.e("avatar", avatar);
                        }
                        if (name != null) {
                            Log.e("name", name);
                        }
                        if (password != null) {
                            Log.e("password", password);
                        }
                        User user = new User(password, avatar, name);
                        users.put(name, user);
                    }
                } else {
                    // User không tồn tại
                    Toast.makeText(context, "User không tồn tại", Toast.LENGTH_SHORT).show();
                }
                adaptercomment.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(context, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kiểm tra xem dataSnapshot có tồn tại không
                if (dataSnapshot.exists()) {
                    long totalCommnets = 0;
                    // Duyệt qua tất cả các comment
                    for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                        String content = commentSnapshot.child("content").getValue(String.class);
                        String timestamp = commentSnapshot.child("timestamp").getValue(String.class);
                        String userID = commentSnapshot.child("userID").getValue(String.class);
                        String idStoryComment = commentSnapshot.child("storyID").getValue(String.class);
                        // Kiểm tra xem StoryID của comment có khớp với StoryID của truyện hiện tại hay không
                        if (idStoryComment != null && idStoryComment.equals(String.valueOf(read + storyId))) {
                            Comment comment = new Comment(userID, idStoryComment, content, timestamp);
                            Log.e("content", content);
                            Log.e("timestamp", timestamp);
                            Log.e("userID", userID);
                            Log.e("idStory", idStoryComment);
                            totalCommnets++;
                            totalCommnetsTxt.setText(totalCommnets + " bình luận");
                            comments.add(comment);
                        }
                    }
                } else {
                    // Comment không tồn tại
                    Toast.makeText(context, "Comment không tồn tại", Toast.LENGTH_SHORT).show();
                }
                adaptercomment.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(context, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = commentEdt.getText().toString();
                if (username == null || username.isEmpty() || commentContent.isEmpty()) {
                    Toast.makeText(context, "Đã xảy ra lỗi. Vui lòng khởi động lại ứng dụng và thử lại.", Toast.LENGTH_SHORT).show();
                } else {
                    usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Tạo một chuỗi ngày tháng năm hiện tại
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                // Tạo một đối tượng Comment mới
                                Comment newComment = new Comment(username, String.valueOf(read + storyId), commentContent, currentDate);

                                // Thêm comment mới vào Firebase
                                DatabaseReference newCommentRef = commentRef.push(); // Tạo một tham chiếu mới cho comment
                                newCommentRef.setValue(newComment); // Đặt giá trị cho tham chiếu mới

                                comments.add(newComment);
                                adaptercomment.notifyDataSetChanged();

                                commentEdt.setText(""); // Xóa nội dung trong commentEdt
                            } else {
                                // Người dùng không tồn tại
                                Toast.makeText(context, "Đã xảy ra lỗi. Người dùng không tồn tại.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi
                            Toast.makeText(context, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}