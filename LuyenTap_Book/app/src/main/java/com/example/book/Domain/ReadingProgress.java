package com.example.book.Domain;

public class ReadingProgress {
    private int currentChapterIndex;
    private int currentReadingPosition;

    public ReadingProgress(int currentChapterIndex, int currentReadingPosition) {
        this.currentChapterIndex = currentChapterIndex;
        this.currentReadingPosition = currentReadingPosition;
    }

    public int getCurrentChapterIndex() {
        return currentChapterIndex;
    }

    public void setCurrentChapterIndex(int currentChapterIndex) {
        this.currentChapterIndex = currentChapterIndex;
    }

    public int getCurrentReadingPosition() {
        return currentReadingPosition;
    }

    public void setCurrentReadingPosition(int currentReadingPosition) {
        this.currentReadingPosition = currentReadingPosition;
    }
}