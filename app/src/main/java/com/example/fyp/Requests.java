package com.example.fyp;


//Requests File is an object to send and receive data for Summarization. This is used in the SummarizeActivity
public class Requests {
    private int num_sentences;
    private String summarised_text;

    public Requests(int num_sentences, String scanned_text) {
        this.num_sentences = num_sentences;
        this.summarised_text = scanned_text;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "num_sentences=" + num_sentences +
                ", summarised_text='" + summarised_text + '\'' +
                '}';
    }

    public int getNum_sentences() {
        return num_sentences;
    }

    public String getSummarised_text() {
        return summarised_text;
    }
}
