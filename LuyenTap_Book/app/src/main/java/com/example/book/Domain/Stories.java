package com.example.book.Domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Stories {

    private Map<String, Story> stories;

    public Map<String, Story> getStories() {
        return stories;
    }

    public void setStories(Map<String, Story> stories) {
        this.stories = stories;
    }

    public Stories(Map<String, Story> stories) {
        this.stories = stories;
    }


}

