package com.example.fyp;

import com.google.firebase.firestore.Exclude;

public class Summary {
    private String id;

    public Summary(){}

    public Summary(String id, String title,String summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;

    }

    private String summary;
    private String title;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
