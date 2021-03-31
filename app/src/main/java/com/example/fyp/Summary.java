package com.example.fyp;

public class Summary {

    private String summary;
    private String title;

    public Summary() {
    }

    public Summary(String title, String summary) {
        this.title = title;
        this.summary = summary;
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
