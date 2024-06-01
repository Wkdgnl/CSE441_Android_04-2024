package com.example.book.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.example.book.Domain.ReadingProgress;

public class ReadingProgressDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ReadingProgress.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReadingProgressContract.ReadingProgressEntry.TABLE_NAME + " (" +
                    ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_STORY_ID + " INTEGER," +
                    ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_CHAPTER_INDEX + " INTEGER," +
                    ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_READING_PROGRESS + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ReadingProgressContract.ReadingProgressEntry.TABLE_NAME;

    public ReadingProgressDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public long insertReadingProgress(String storyId, int chapterIndex, float readingProgress) {
        // Get the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_STORY_ID, storyId);
        values.put(ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_CHAPTER_INDEX, chapterIndex);
        values.put(ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_READING_PROGRESS, readingProgress);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ReadingProgressContract.ReadingProgressEntry.TABLE_NAME, null, values);
        return newRowId;
    }
    public ReadingProgress getReadingProgress(String storyId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_CHAPTER_INDEX,
                ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_READING_PROGRESS
        };

        String selection = ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_STORY_ID + " = ?";
        String[] selectionArgs = { storyId };

        Cursor cursor = db.query(
                ReadingProgressContract.ReadingProgressEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int currentChapterIndex = cursor.getInt(cursor.getColumnIndexOrThrow(ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_CHAPTER_INDEX));
            int currentReadingPosition = cursor.getInt(cursor.getColumnIndexOrThrow(ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_READING_PROGRESS));
            cursor.close();
            return new ReadingProgress(currentChapterIndex, currentReadingPosition);
        } else {
            cursor.close();
            return null;
        }
    }

    public void updateCurrentChapterIndex(String storyId, int currentChapterIndex) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_CHAPTER_INDEX, currentChapterIndex);

        String selection = ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_STORY_ID + " = ?";
        String[] selectionArgs = { storyId };

        db.update(
                ReadingProgressContract.ReadingProgressEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void updateReadingProgressPercentage(String storyId, float percentage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_READING_PROGRESS, percentage);

        String selection = ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_STORY_ID + " = ?";
        String[] selectionArgs = { storyId };

        db.update(
                ReadingProgressContract.ReadingProgressEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public boolean isReadingProgressExist(String storyId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ReadingProgressContract.ReadingProgressEntry.COLUMN_NAME_STORY_ID + " = ?";
        String[] selectionArgs = { storyId };

        Cursor cursor = db.query(
                ReadingProgressContract.ReadingProgressEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exist = (cursor.getCount() > 0);
        cursor.close();

        return exist;
    }
}
