package com.example.book.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.example.book.Adapters.BlurBuilder;
import com.example.book.Adapters.CommentAdapter;
import com.example.book.Adapters.SoftInputAssist;
import com.example.book.Database.ReadingProgressDbHelper;
import com.example.book.Domain.Comment;
import com.example.book.Domain.ReadingProgress;
import com.example.book.Domain.User;
import com.example.book.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;

public class DetailActivity extends AppCompatActivity {
    Context context;
    ImageView image, imageDetail, imageback, imageAddBookmark;
    TextView categoryTxt, storynameTxt, authornameTxt, decriptionTxt, readTxt;
    String read = "";
    TabHost tabdetail;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    private SoftInputAssist softInputAssist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        softInputAssist = new SoftInputAssist(this);
        context = this;
        categoryTxt = findViewById(R.id.categoryTxt);
        authornameTxt = findViewById(R.id.authornameTxt);
        storynameTxt = findViewById(R.id.storynameTxt);
        decriptionTxt = findViewById(R.id.decriptionTxt);
        image = findViewById(R.id.imageStory);
        imageDetail = findViewById(R.id.imageDetail);
        imageback = findViewById(R.id.imageback);
        tabdetail = findViewById(R.id.tabdetail);
        readTxt = findViewById(R.id.readTxt);
        imageAddBookmark = findViewById(R.id.imageAddBookmark);

        ListView listView = findViewById(R.id.listView);


        Intent callerIntent = getIntent();
        int idStory = callerIntent.getIntExtra("id", -1); // -1 là giá trị mặc định
        String readType = callerIntent.getStringExtra("readType");
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        tabdetail.setup();
        TabHost.TabSpec spec1, spec2, spec3, spec4;

        spec1 = tabdetail.newTabSpec("t1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Giới Thiệu");
        tabdetail.addTab(spec1);

        spec3 = tabdetail.newTabSpec("t3");
        spec3.setIndicator("Bình Luận");
        spec3.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return new View(context);
            }
        });
        tabdetail.addTab(spec3);

        tabdetail.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("t3")) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("STORY_ID", idStory);
                    intent.putExtra("READ_TYPE", read);
                    startActivity(intent);
                    tabdetail.setCurrentTabByTag("t1"); // switch back to the first tab
                }
            }
        });

        spec4 = tabdetail.newTabSpec("t4");
        spec4.setContent(R.id.tab4);
        spec4.setIndicator("D.S Chương");
        tabdetail.addTab(spec4);

        if (readType.equals("Best")) read = "stories";
        if (readType.equals("Bests")) read = "beststories";
        if (readType.equals("webtoon")) read = "webtoon";
        ArrayAdapter<String> adapter;
        ArrayList<String> listItems = new ArrayList<>();
        HashMap<String, String> chapterContents = new HashMap<>();
        ArrayList<String> chapters = new ArrayList<>();
        ArrayList<String> chapterTitlesWebtoon = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        DatabaseReference storyRef = database.getReference(read + "/story" + idStory);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(read + "/story" + idStory + "/chapters");


        storyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Kiểm tra xem dataSnapshot có tồn tại không
                if (dataSnapshot.exists()) {
                    // Lấy ra story dưới dạng Map
                    Map<String, Object> storyMap = (Map<String, Object>) dataSnapshot.getValue();

                    // Lấy ra các thuộc tính của story
                    String title = (String) storyMap.get("title");
                    String author = (String) storyMap.get("author");
                    String category = (String) storyMap.get("category");
                    String description = (String) storyMap.get("description");
                    String imageUrl = (String) storyMap.get("image");

                    // Cập nhật giao diện người dùng với các thuộc tính của story
                    categoryTxt.setText(category);
                    authornameTxt.setText("bởi " + author);
                    decriptionTxt.setText(description);
                    storynameTxt.setText(title);

                    // Sử dụng Glide để tải hình ảnh từ URL và hiển thị nó trong ImageView
                    Glide.with(context)
                            .load(imageUrl)
                            .transform(new RoundedCorners(40)) // 40 là bán kính góc bo tròn
                            .into(image);
                    Glide.with(context)
                            .load(imageUrl)
                            .apply(new RequestOptions().transform(new BlurTransformation(30, 3), new ColorFilterTransformation(Color.argb(80, 0, 0, 0))))
                            .into(imageDetail);
                } else {
                    // Story không tồn tại
                    Toast.makeText(context, "Story không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(context, "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Xóa danh sách cũ
                listItems.clear();
                chapterContents.clear();
                int i = 1;
                // Duyệt qua tất cả các chapter
                for (DataSnapshot chapterSnapshot : dataSnapshot.getChildren()) {
                    String chapterTitle = chapterSnapshot.child("title").getValue(String.class);
                    String chapterContent = chapterSnapshot.child("content").getValue(String.class);
                    listItems.add("Chương " + i + ": " + chapterTitle);
                    chapterContents.put("Chương " + i + ": " + chapterTitle, chapterContent);
                    i++;
                    chapters.add(chapterContent);
                    chapterTitlesWebtoon.add(chapterTitle);
                }

                // Cập nhật ListView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        readTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadingProgressDbHelper dbHelper = new ReadingProgressDbHelper(context);
                ReadingProgress readingProgress = dbHelper.getReadingProgress(read + idStory);
                if (readingProgress != null) {
                    int currentChapterIndex = readingProgress.getCurrentChapterIndex();
                    int currentReadingPosition = readingProgress.getCurrentReadingPosition();
                    Intent intent;
                    if (!read.equals("webtoon")) {
                        intent = new Intent(getBaseContext(), ReadActivity.class);
                        intent.putExtra("STORY_ID", idStory);
                        intent.putExtra("CURRENT_CHAPTER_INDEX", currentChapterIndex);
                        intent.putExtra("CURRENT_READING_POSITION", currentReadingPosition);
                        intent.putExtra("READ_TYPE", read);
                    } else {
                        intent = new Intent(getBaseContext(), ReadWebtoonActivity.class);
                        intent.putExtra("STORY_ID", idStory);
                        intent.putExtra("CHAPTERS", chapters);
                        intent.putExtra("CHAPTER_INDEX", currentChapterIndex);
                        intent.putExtra("CHAPTER_TITLES", chapterTitlesWebtoon);
                        intent.putExtra("READ_TYPE", read);
                    }
                    startActivity(intent);
                } else {
                    dbHelper.insertReadingProgress(read + idStory, 0, 0);
                }
            }
        });
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageAddBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String readStories = sharedPreferences.getString("ReadStories", "");
                String[] storyIds = readStories.split(",");
                boolean isExist = false;
                for (String storyId : storyIds) {
                    if (storyId.equals(String.valueOf(idStory))) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("ReadStories", readStories + idStory + ",");
                    editor.apply();
                    Toast.makeText(context, "Đã thêm vào tủ truyện", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Truyện đã có trong tủ truyện", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!read.equals("webtoon")) {
                    Intent intent = new Intent(getBaseContext(), ReadActivity.class);
                    intent.putExtra("STORY_ID", idStory);
                    intent.putExtra("CURRENT_CHAPTER_INDEX", position);
                    intent.putExtra("READ_TYPE", read);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), ReadWebtoonActivity.class);
                    intent.putExtra("STORY_ID", idStory);
                    intent.putExtra("CHAPTERS", chapters);
                    intent.putExtra("CHAPTER_INDEX", position);
                    intent.putExtra("CHAPTER_TITLES", chapterTitlesWebtoon);
                    intent.putExtra("READ_TYPE", read);
                    startActivity(intent);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void addTab() {

    }
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//    }
    protected void onPause() {
        super.onPause();
        softInputAssist.onPause();
    }
    protected void onResume() {
        super.onResume();
        softInputAssist.onResume();
    }
    protected void onDestroy() {
        super.onDestroy();
        softInputAssist.onDestroy();
    }

}