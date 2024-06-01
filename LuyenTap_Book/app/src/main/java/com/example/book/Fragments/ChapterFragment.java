package com.example.book.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.Database.ReadingProgressDbHelper;
import com.example.book.R;

public class ChapterFragment extends Fragment {
    private ScrollView scrollView;
    private TextView textView;
    public static final String ARG_CONTENT = "content";
    public static final String ARG_STORY_ID = "storyId";
    public static final String ARG_READING_POSITION = "currentReadingPosition";
    private OnReadingProgressListener progressListener;
    private ViewTreeObserver.OnScrollChangedListener scrollChangedListener;
    private int percentage;
    private String storyId;
    private int currentReadingPosition;

    public static ChapterFragment newInstance(String content, String storyId, int currentReadingPosition) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTENT, content);
        args.putString(ARG_STORY_ID, storyId);
        args.putInt(ARG_READING_POSITION, currentReadingPosition);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateReadingProgress() {
        if (scrollView != null && isAdded()) {
            int scrollY = scrollView.getScrollY();
            int totalHeight = scrollView.getChildAt(0).getHeight();
            int visibleHeight = scrollView.getHeight();
            percentage = (int) ((double) scrollY / (totalHeight - visibleHeight) * 100);

//            Log.d("ChapterFragment", "ScrollY: " + scrollY);
//            Log.d("ChapterFragment", "TotalHeight: " + totalHeight);
//            Log.d("ChapterFragment", "VisibleHeight: " + visibleHeight);
//            Log.d("ChapterFragment", "Percentage: " + percentage);

            if (progressListener != null) {
                progressListener.onReadingProgressUpdate(percentage);
            }

            TextView percentReadTxt = getActivity().findViewById(R.id.percentReadTxt);
            percentReadTxt.setText(percentage + "% đã đọc");
        } else {
//            Log.d("ChapterFragment", "ScrollView is null or Fragment is not added.");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        textView = view.findViewById(R.id.chapter_content);
        scrollView = view.findViewById(R.id.scrollView);
        Bundle args = getArguments();
        if (args != null) {
            String content = args.getString(ARG_CONTENT);
            storyId = args.getString(ARG_STORY_ID);
            currentReadingPosition = args.getInt(ARG_READING_POSITION);
            Log.e("storyId", storyId);
            Log.e("currentReadingPosition", String.valueOf(currentReadingPosition));
            content = content.replace("\\n", "\n");
            textView.setText(content);
            Log.e("currentReadingPosition", String.valueOf(currentReadingPosition));
        } else {
            Toast.makeText(getContext(), "No content", Toast.LENGTH_SHORT).show();
        }
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int totalHeight = scrollView.getChildAt(0).getHeight();
                int scrollFinal = (int)(totalHeight*currentReadingPosition)/100;
                Log.e("scrollFinal", String.valueOf(scrollFinal));
                scrollView.scrollTo(0, scrollFinal);
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (isVisible() && isResumed()) { // chỉ cập nhật tiến trình đọc nếu ChapterFragment này đang được hiển thị
                    updateReadingProgress();

                    ReadingProgressDbHelper dbHelper = new ReadingProgressDbHelper(getActivity());
                    dbHelper.updateReadingProgressPercentage(storyId, percentage);
                }
            }
        });
        // Thêm listener vào scrollView
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Cập nhật tiến độ đọc lần đầu khi layout đã sẵn sàng
                updateReadingProgress();

                // Loại bỏ OnGlobalLayoutListener này để không gọi lại nhiều lần
                scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return view;
    }

    public static String getArgContentKey() {
        return ARG_CONTENT;
    }

    public interface OnReadingProgressListener {
        void onReadingProgressUpdate(int percentage);
    }

    public void setOnReadingProgressListener(OnReadingProgressListener listener) {
        this.progressListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateReadingProgress();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Xóa listener khi Fragment không còn hiển thị
        scrollView.getViewTreeObserver().removeOnScrollChangedListener(scrollChangedListener);
    }
    public interface OnChapterChangeListener {
        void onChapterChange(int newChapterIndex);
    }
}