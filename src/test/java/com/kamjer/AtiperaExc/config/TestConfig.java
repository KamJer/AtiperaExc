package com.kamjer.AtiperaExc.config;

import com.kamjer.AtiperaExc.client.GitHubClient;
import com.kamjer.AtiperaExc.controller.GitHubController;
import com.kamjer.AtiperaExc.integration.GitHubAtiperaIntegrationTest;
import com.kamjer.AtiperaExc.service.GitHubService;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

@TestConfiguration
public class TestConfig {


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(String.format("http://localhost:%s", GitHubAtiperaIntegrationTest.mockBackEnd.getPort()))
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(10))))
                .build();
    }

    @Bean
    public GitHubClient gitHubClient(WebClient webClient) {
        String acceptedValue = "application/vnd.github+json";
        return new GitHubClient(webClient, acceptedValue);
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
