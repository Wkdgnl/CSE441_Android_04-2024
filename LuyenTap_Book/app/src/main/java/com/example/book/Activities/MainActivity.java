package com.example.book.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.book.Adapters.BookListAdapter;
import com.example.book.Adapters.SliderAdapters;
import com.example.book.Domain.ListBook;
import com.example.book.Domain.SliderItems;
import com.example.book.Domain.SlowViewPager2Scroller;
import com.example.book.Domain.Stories;
import com.example.book.Domain.Story;
import com.example.book.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterBestStories;
    private RecyclerView recyclerViewBestStories, recyclerViewBestStories1, recyclerViewWebtoon;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar loading1, loading2, loading3;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    EditText searchMainEdt;
    LinearLayout categoryLayout, ProfileLayout, BookmarkLayout, MainLayout, DownloadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControl();
        initView();
        banner();
        sendRequest();
        sendRequestBest();
        sendRequestWebtoon();
        searchMainEdt = findViewById(R.id.searchMainEdt);
        categoryLayout = findViewById(R.id.categoryLayout);
        ProfileLayout = findViewById(R.id.ProfileLayout);
        BookmarkLayout = findViewById(R.id.BookmarkLayout);
        MainLayout = findViewById(R.id.MainLayout);
        DownloadLayout = findViewById(R.id.DownloadLayout);
        searchMainEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });
        ProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        BookmarkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
            }
        });
        MainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
        DownloadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DownloadActivity.class));
            }
        });
    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://book-e0e75-default-rtdb.firebaseio.com/stories.json", s -> {
            Gson gson = new Gson();
            loading1.setVisibility(View.GONE);
            Type type = new TypeToken<Map<String, Story>>(){}.getType();
            Map<String, Story> storyMap = gson.fromJson(s, type);

            // Limit to first 5 entries
            storyMap = storyMap.entrySet().stream()
                    .limit(8)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Stories stories = new Stories(storyMap);
            adapterBestStories = new BookListAdapter(stories, "Best");
            recyclerViewBestStories.setAdapter(adapterBestStories);
        }, volleyError -> {
            loading1.setVisibility(View.GONE);
            Log.i("NgaoPhongVan", "onErrorResponse: "+ volleyError.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private void sendRequestBest() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://book-e0e75-default-rtdb.firebaseio.com/stories.json", s -> {
            Gson gson = new Gson();
            loading2.setVisibility(View.GONE);
            Type type = new TypeToken<Map<String, Story>>(){}.getType();
            Map<String, Story> storyMap = gson.fromJson(s, type);

            // Skip the first 5 entries and limit to next 5 entries
            storyMap = storyMap.entrySet().stream()
                    .skip(8)
                    .limit(8)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            Stories stories = new Stories(storyMap);
            adapterBestStories = new BookListAdapter(stories,"Best");
            recyclerViewBestStories1.setAdapter(adapterBestStories);
        }, volleyError -> {
            loading1.setVisibility(View.GONE);
            Log.i("NgaoPhongVan", "onErrorResponse: "+ volleyError.toString());
        });
        mRequestQueue.add(mStringRequest);
    }
    private void sendRequestWebtoon() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading3.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://book-e0e75-default-rtdb.firebaseio.com/webtoon.json", s -> {
            Gson gson = new Gson();
            loading3.setVisibility(View.GONE);
            Type type = new TypeToken<Map<String, Story>>(){}.getType();
            Map<String, Story> storyMap = gson.fromJson(s, type);
            Stories stories = new Stories(storyMap);
            adapterBestStories = new BookListAdapter(stories, "webtoon");
            recyclerViewWebtoon.setAdapter(adapterBestStories);
        }, volleyError -> {
            loading1.setVisibility(View.GONE);
            Log.i("NgaoPhongVan", "onErrorResponse: "+ volleyError.toString());
        });
        mRequestQueue.add(mStringRequest);
    }
    private void banner() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.silde1));
        sliderItems.add(new SliderItems(R.drawable.slide2));
        sliderItems.add(new SliderItems(R.drawable.slide3));
        sliderItems.add(new SliderItems(R.drawable.slide4));
        sliderItems.add(new SliderItems(R.drawable.slide5));
        sliderItems.add(new SliderItems(R.drawable.slide6));
        sliderItems.add(new SliderItems(R.drawable.slide7));
        try {
            Field mScroller = ViewPager2.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            SlowViewPager2Scroller scroller = new SlowViewPager2Scroller(viewPager2.getContext());
            mScroller.set(viewPager2, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        viewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
                // Tạo một DecelerateInterpolator
                Interpolator interpolator = new DecelerateInterpolator();
                // Sử dụng Interpolator để thay đổi tốc độ của chuyển động
                page.setTranslationX(-position * page.getWidth() * interpolator.getInterpolation(Math.abs(position)));
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(slideRunnable);
            }
        });
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            slideHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunnable);
    }

    private void initView() {
        recyclerViewBestStories = findViewById(R.id.view1);
        recyclerViewBestStories1 = findViewById(R.id.view2);
        recyclerViewWebtoon = findViewById(R.id.view3);
        recyclerViewBestStories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewBestStories1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWebtoon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loading1 = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);
        loading3 = findViewById(R.id.progressBar3);
    }

    private void addControl() {
        viewPager2 = findViewById(R.id.viewpageSlider);

    }
}