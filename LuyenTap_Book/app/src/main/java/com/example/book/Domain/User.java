package com.example.book.Domain;

public class User {
    private String password;
    private String avatar;
    private String name;

    public User(String password, String avatar, String name) {
        this.password = password;
        this.avatar = avatar;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getters and setters

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}