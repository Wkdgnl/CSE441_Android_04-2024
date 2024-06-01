package com.example.book.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.Adapters.BookmarkAdapter;
import com.example.book.Adapters.SearchAdapter;
import com.example.book.Domain.Story;
import com.example.book.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class BookmarkActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<Story> stories;
    BookmarkAdapter bookmarkAdapter;
    ImageView imageBackBookmark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookmark);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Trong BookmarkActivity
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String readStories = sharedPreferences.getString("ReadStories", "");
        String[] storyIds = readStories.split(",");

        RecyclerView bookmarkview = findViewById(R.id.bookmarkview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bookmarkview.setLayoutManager(layoutManager);
        stories = new ArrayList<>();
        bookmarkAdapter = new BookmarkAdapter(stories);
        bookmarkview.setAdapter(bookmarkAdapter);
        imageBackBookmark = findViewById(R.id.imageBackBookmark);
        imageBackBookmark.setOnClickListener(v -> finish());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for (String storyId : storyIds) {
            DatabaseReference storyRef = database.getReference("stories/story" + storyId);
            storyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Kiểm tra xem dataSnapshot có tồn tại không
                    if (dataSnapshot.exists()) {
                        // Lấy ra story dưới dạng Map
                        Map<String, Object> storyMap = (Map<String, Object>) dataSnapshot.getValue();

                        // Tạo một đối tượng Story từ storyMap
                        Story story = new Story();
                        story.setId(((Long) storyMap.get("id")).intValue());
                        story.setTitle((String) storyMap.get("title"));
                        story.setAuthor((String) storyMap.get("author"));
                        story.setCategory((String) storyMap.get("category"));
                        story.setDescription((String) storyMap.get("description"));
                        story.setImage((String) storyMap.get("image"));

                        // Thêm story vào danh sách và cập nhật RecyclerView
                        stories.add(story);
                        bookmarkAdapter.notifyDataSetChanged();
                    } else {
                        // Story không tồn tại
                        Log.e("BookmarkActivity", "Story không tồn tại: " + storyId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi
                    Log.e("BookmarkActivity", "Lỗi: " + databaseError.getMessage());
                }
            });
        }
    }
}