package com.arya.open_source_recommender.Model;

public class Repository {
    private String name;
    private String html_url; 
    private String language; 

    public Repository() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getHtml_url() {
        return html_url;
    }
    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
}