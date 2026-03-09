
package com.arya.open_source_recommender.Controller;
import java.util.ArrayList;
// import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arya.open_source_recommender.Service.GithubService;
import org.springframework.web.bind.annotation.*;

import com.arya.open_source_recommender.Model.RecommendedRepo;
import com.arya.open_source_recommender.Model.Repository;
@CrossOrigin(origins = "http://localhost:8081")  // frontend runs on 8080 or adjust if needed
@RestController
@RequestMapping("/api")
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/profile/{username}")
    public String getProfile(@PathVariable String username) {
        return githubService.getGithubProfile(username);
    }

    @GetMapping("/repos/{username}")
public List<Repository> getRepos(@PathVariable String username) {
    return githubService.getUserRepositories(username);
}

@GetMapping("/skills/{username}")
public Map<String, Integer> getSkills(@PathVariable String username) {
    return githubService.detectSkills(username);
}
@GetMapping("/recommend/{username}")
public Map<String, List<RecommendedRepo>> recommend(@PathVariable String username) {
    return githubService.recommendRepos(username);
}
@GetMapping("/recommend/issues/{username}")
public Map<String, List<Map<String, Object>>> recommendWithIssues(@PathVariable String username) {
    return githubService.recommendReposWithIssues(username);
}
@GetMapping("/repos/links/{username}")
public List<String> getUserRepoLinks(@PathVariable String username) {
    List<Repository> repos = githubService.getUserRepositories(username);
    List<String> links = new ArrayList<>();
    for (Repository repo : repos) {
        links.add(repo.getHtml_url());
    }
    return links;
}
@GetMapping("/recommend/links/{username}")
public List<String> getRecommendedRepoLinks(@PathVariable String username) {
    return githubService.getRecommendedRepoLinks(username);
}
}