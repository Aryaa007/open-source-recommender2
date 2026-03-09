package com.arya.open_source_recommender.Model;

public class RecommendedRepo {

    private String name;
    private String html_url;
    private int stargazers_count;

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

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }
    private String owner_login; // add this

    // getters and setters
    public String getOwner_login() { return owner_login; }
    public void setOwner_login(String owner_login) { this.owner_login = owner_login; }
}
