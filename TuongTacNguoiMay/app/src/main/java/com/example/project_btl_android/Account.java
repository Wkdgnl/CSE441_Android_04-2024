package com.example.project_btl_android;

import java.io.Serializable;

public class Account implements Serializable {
    private String id;
    private String UserName;
    public Account() {
    }

    public Account(String id, String userName) {
        this.id = id;
        UserName = userName;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }


}
