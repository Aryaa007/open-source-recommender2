package com.arya.open_source_recommender.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arya.open_source_recommender.Model.Issue;
import com.arya.open_source_recommender.Model.RecommendedRepo;
import com.arya.open_source_recommender.Model.Repository;

@Service
public class GithubService {

    RestTemplate restTemplate = new RestTemplate();

    public String getGithubProfile(String username) {
        String url = "https://api.github.com/users/" + username;
        return restTemplate.getForObject(url, String.class);
    }

    public List<Repository> getUserRepositories(String username) {

        String url = "https://api.github.com/users/" + username + "/repos";

        Repository[] repos = restTemplate.getForObject(url, Repository[].class);

        return Arrays.asList(repos);
    }
    public Map<String, Integer> detectSkills(String username) {

    List<Repository> repos = getUserRepositories(username);

    Map<String, Integer> skills = new HashMap<>();

    for (Repository repo : repos) {

        String lang = repo.getLanguage();

        if (lang != null) {
            skills.put(lang, skills.getOrDefault(lang, 0) + 1);
        }
    }

    return skills;
}
@SuppressWarnings("unchecked")
public List<RecommendedRepo> searchRepositories(String language) {

    String url = "https://api.github.com/search/repositories?q=language:" + language + "+stars:>100&sort=stars";

    Map<String, Object> response =
            (Map<String, Object>) restTemplate.getForObject(url, Map.class);

    if (response == null) {
        return new ArrayList<>();
    }

    List<Map<String, Object>> items =
            (List<Map<String, Object>>) response.get("items");

    if (items == null) {
        return new ArrayList<>();
    }

    List<RecommendedRepo> repos = new ArrayList<>();

    for (Map<String, Object> item : items) {

        RecommendedRepo repo = new RecommendedRepo();

        repo.setName((String) item.get("name"));
        repo.setHtml_url((String) item.get("html_url"));
        repo.setStargazers_count(((Number) item.get("stargazers_count")).intValue());

        repos.add(repo);
    }

    return repos;
}

 public Map<String, List<RecommendedRepo>> recommendRepos(String username) {

    Map<String, Integer> skills = detectSkills(username);

    Map<String, List<RecommendedRepo>> recommendations = new HashMap<>();

    for (String language : skills.keySet()) {

        List<RecommendedRepo> repos = searchRepositories(language);

        recommendations.put(language, repos.subList(0, Math.min(5, repos.size())));
    }

    return recommendations;
}
public Map<String, List<Map<String, Object>>> recommendReposWithIssues(String username) {

    Map<String, Integer> skills = detectSkills(username);
    Map<String, List<Map<String, Object>>> recommendations = new HashMap<>();

    for (String language : skills.keySet()) {
        List<RecommendedRepo> repos = searchRepositories(language);

        List<Map<String, Object>> repoDataList = new ArrayList<>();

        for (RecommendedRepo repo : repos.subList(0, Math.min(5, repos.size()))) {
            Map<String, Object> repoData = new HashMap<>();
            repoData.put("name", repo.getName());
            repoData.put("html_url", repo.getHtml_url());
            repoData.put("stars", repo.getStargazers_count());

            String owner = repo.getHtml_url().split("/")[3];
            String repoName = repo.getName();
            List<Issue> issues = getBeginnerIssues(owner, repoName);

            repoData.put("beginner_issues", issues);
            repoDataList.add(repoData);
        }

        recommendations.put(language, repoDataList);
    }

    return recommendations;
}
public List<Issue> getBeginnerIssues(String owner, String repoName) {
    String url = "https://api.github.com/repos/" + owner + "/" + repoName +
                 "/issues?labels=good-first-issue,help-wanted&state=open";

    Issue[] issues = restTemplate.getForObject(url, Issue[].class);

    return issues != null ? Arrays.asList(issues) : new ArrayList<>();
}
public List<String> getRecommendedRepoLinks(String username) {
    Map<String, Integer> skills = detectSkills(username); // languages the user knows
    List<String> links = new ArrayList<>();

    for (String language : skills.keySet()) {
        List<RecommendedRepo> repos = searchRepositories(language); // search top repos in that language

        // Take top 5 per language
        for (RecommendedRepo repo : repos.subList(0, Math.min(5, repos.size()))) {
            links.add(repo.getHtml_url()); // only link
        }
    }

    return links;
}
}