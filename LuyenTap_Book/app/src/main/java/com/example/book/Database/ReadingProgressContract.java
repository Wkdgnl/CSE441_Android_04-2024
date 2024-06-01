package com.example.book.Database;

import android.provider.BaseColumns;

public final class ReadingProgressContract {
    private ReadingProgressContract() {}

    public static class ReadingProgressEntry implements BaseColumns {
        public static final String TABLE_NAME = "reading_progress";
        public static final String COLUMN_NAME_STORY_ID = "story_id";
        public static final String COLUMN_NAME_CHAPTER_INDEX = "chapter_index";
        public static final String COLUMN_NAME_READING_PROGRESS = "reading_progress";
    }
}