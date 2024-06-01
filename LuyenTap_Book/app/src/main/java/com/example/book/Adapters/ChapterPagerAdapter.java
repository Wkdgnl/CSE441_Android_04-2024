package com.example.book.Adapters;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.book.Fragments.ChapterFragment;

import java.lang.ref.WeakReference;
import java.util.List;


public class ChapterPagerAdapter extends FragmentStateAdapter {
    private List<String> chapterContents;
    private List<String> chapterTitles;
    private String storyId;
    private int currentReadingPosition;
    private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

    public ChapterPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> chapterContents, List<String> chapterTitles, String storyId, int currentReadingPosition) {
        super(fragmentActivity);
        this.chapterContents = chapterContents;
        this.chapterTitles = chapterTitles;
        this.storyId = storyId;
        this.currentReadingPosition = currentReadingPosition;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment existingFragment = fragmentSparseArray.get(position);
        if (existingFragment != null) {
            return existingFragment;
        } else {
            int readingPosition = currentReadingPosition;
            Log.e("ChapterPagerAdapter", "Reading position: " + readingPosition);
            ChapterFragment fragment = ChapterFragment.newInstance(chapterContents.get(position), storyId, readingPosition);
            Bundle args = new Bundle();
            args.putString(ChapterFragment.ARG_CONTENT, chapterContents.get(position));
            args.putString(ChapterFragment.ARG_STORY_ID, storyId);
            args.putInt(ChapterFragment.ARG_READING_POSITION, readingPosition);
            fragment.setArguments(args);
            fragmentSparseArray.put(position, fragment);
            return fragment;
        }
    }

    public ChapterFragment getFragment(int position) {
        Fragment fragment = fragmentSparseArray.get(position);
        if (fragment instanceof ChapterFragment) {
            return (ChapterFragment) fragment;
        }
        return null;
    }

    public String getChapterTitle(int position) {
        return chapterTitles.get(position);
    }

    public String getChapterContent(int position) {
        return chapterContents.get(position);
    }

    @Override
    public int getItemCount() {
        return chapterContents.size();
    }
}