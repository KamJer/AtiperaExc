package com.kamjer.AtiperaExc.config;

import com.kamjer.AtiperaExc.client.GitHubClient;
import com.kamjer.AtiperaExc.controller.GitHubController;
import com.kamjer.AtiperaExc.service.GitHubService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestConfig {
    @Bean
    public GitHubClient gitHubClient(RestTemplate restTemplate, @Value("${github.api.base-url}") String baseUrl) {
        String acceptedValue = "application/vnd.github+json";
        return new GitHubClient(restTemplate, acceptedValue, baseUrl);
    }

    @Bean
    public GitHubService gitHubService(GitHubClient gitHubClient) {
        return new GitHubService(gitHubClient);
    }

    @Bean
    public GitHubController gitHubController(GitHubService gitHubService) {
        String headerValue = "application/json";
        String message = "test";
        return new GitHubController(gitHubService, headerValue, message);
    }
}
