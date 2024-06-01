package com.example.book.Domain;

public class Comment {
    private String userID;
    private String storyID;
    private String content;
    private String timestamp;

    public Comment(String userID, String storyID, String content, String timestamp) {
        this.userID = userID;
        this.storyID = storyID;
        this.content = content;
        this.timestamp = timestamp;
    }

    // getters and setters

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStoryID() {
        return storyID;
    }

    public void setStoryID(String storyID) {
        this.storyID = storyID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
