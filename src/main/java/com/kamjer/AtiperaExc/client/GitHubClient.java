package com.kamjer.AtiperaExc.client;

import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.model.BranchDto;
import com.kamjer.AtiperaExc.model.RepositoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Component
public class GitHubClient {

    private final WebClient webClient;
    private final String githubAcceptValue;

    @Autowired
    public GitHubClient(WebClient webClient,
                        @Value("${github.accept.value}") String githubAcceptValue) {
        this.webClient = webClient;
        this.githubAcceptValue = githubAcceptValue;
    }

    public Flux<RepositoryDto> getGitHubRepositories(Optional<String> token, String owner) {
        HttpHeaders headers = new HttpHeaders();
        token.ifPresent(headers::setBearerAuth);
        headers.set(HttpHeaders.ACCEPT, githubAcceptValue);

        String urlBuilder = "/users" +
                "/" +
                owner +
                "/repos";
        return webClient
                .get()
                .uri(urlBuilder)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToFlux(RepositoryDto.class);
    }

    public Flux<BranchDto> getGitHubBranches(String owner, String repoName, Optional<String> token) {
        HttpHeaders headers = new HttpHeaders();
        token.ifPresent(headers::setBearerAuth);
        headers.set(HttpHeaders.ACCEPT, githubAcceptValue);

        String urlBuilder =
                "/repos" +
                "/" +
                owner +
                "/" +
                repoName +
                "/branches";
        return webClient
                .get()
                .uri(urlBuilder)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToFlux(BranchDto.class);
    }
}
