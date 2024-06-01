package com.example.book.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.book.Adapters.ChapterWebtoonAdapter;
import com.example.book.Adapters.ImageAdapter;
import com.example.book.Database.ReadingProgressDbHelper;
import com.example.book.R;

import java.util.ArrayList;
import java.util.List;

public class ReadWebtoonActivity extends AppCompatActivity {
    private List<String> chapters; // Danh sách tất cả các chương
    private int currentChapterIndex; // Vị trí chương hiện tại
    private ImageAdapter adapter;
    private RecyclerView viewPager;
    private TextView titleChapterTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read_webtoon);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleChapterTxt = findViewById(R.id.titleChapterTxt);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        Intent callerIntent = getIntent();
        currentChapterIndex = callerIntent.getIntExtra("CHAPTER_INDEX", 0);
        chapters = callerIntent.getStringArrayListExtra("CHAPTERS");
        int storyId = callerIntent.getIntExtra("STORY_ID", -1);
        String read = callerIntent.getStringExtra("READ_TYPE");

        ArrayList<String> chapterTitles = callerIntent.getStringArrayListExtra("CHAPTER_TITLES");
        String currentChapterTitle = "";
        if(chapterTitles != null && currentChapterIndex < chapterTitles.size()) {
            currentChapterTitle = chapterTitles.get(currentChapterIndex);
        }
        titleChapterTxt.setText(currentChapterTitle);

        List<String[]> chapterImages = new ArrayList<>();
        for (String chapter : chapters) {
            String[] imageUrls = chapter.split(", ");
            chapterImages.add(imageUrls);
        }
        RecyclerView chapterRecyclerView = findViewById(R.id.chapterRecyclerView);

        ChapterWebtoonAdapter chapterAdapter = new ChapterWebtoonAdapter(this, chapterImages, screenWidth);
        chapterRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        chapterRecyclerView.setAdapter(chapterAdapter);
        chapterRecyclerView.scrollToPosition(currentChapterIndex); // Cuộn đến chương tương ứng
        // ...
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(chapterRecyclerView);

        chapterRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                    currentChapterIndex = firstVisibleItemPosition;
                    ReadingProgressDbHelper dbHelper = new ReadingProgressDbHelper(ReadWebtoonActivity.this);
                    // Cập nhật tiến trình đọc trong cơ sở dữ liệu
                    if (dbHelper.isReadingProgressExist(read + storyId)) {
                        dbHelper.updateCurrentChapterIndex(read + storyId, currentChapterIndex);
                    } else {
                        dbHelper.insertReadingProgress(read + storyId, currentChapterIndex, 0);
                    }
                    if(chapterTitles != null && currentChapterIndex < chapterTitles.size()) {
                        String currentChapterTitle = chapterTitles.get(currentChapterIndex);
                        titleChapterTxt.setText(currentChapterTitle);
                    }
                }
            }
        });
    }
}