package com.kamjer.AtiperaExc.client;

import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.model.BranchDto;
import com.kamjer.AtiperaExc.model.RepositoryDto;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Log
@Component
public class GitHubClient {

    private final String baseUrl;

    private final RestTemplate restTamplate;
    private final String githubAcceptValue;

    @Autowired
    public GitHubClient(RestTemplate restTemplate,
                        @Value("${github.accept.value}") String githubAcceptValue,
                        @Value("${github.api.base-url}") String baseUrl) {
        this.restTamplate = restTemplate;
        this.githubAcceptValue = githubAcceptValue;
        this.baseUrl = baseUrl;
    }

    public List<RepositoryDto> getGitHubRepositories(Optional<String> token, String owner) throws ErrorResponseException {
        HttpHeaders headers = new HttpHeaders();
        token.ifPresent(headers::setBearerAuth);
        headers.set(HttpHeaders.ACCEPT, githubAcceptValue);

        String urlBuilder = baseUrl + "/users" +
                "/" +
                owner +
                "/repos";
        try {
            return Optional.ofNullable(restTamplate.getForEntity(urlBuilder, RepositoryDto[].class).getBody())
                    .map(List::of)
                    .orElseThrow();
        } catch (HttpClientErrorException ex) {
            throw new ErrorResponseException(ex.getStatusCode(), ex.getMessage());
        }
    }

    public List<BranchDto> getGitHubBranches(String owner, String repoName, Optional<String> token) {
        HttpHeaders headers = new HttpHeaders();
        token.ifPresent(headers::setBearerAuth);
        headers.set(HttpHeaders.ACCEPT, githubAcceptValue);

        String urlBuilder = baseUrl +
                "/repos" +
                "/" +
                owner +
                "/" +
                repoName +
                "/branches";
        return Optional.ofNullable(restTamplate.getForEntity(urlBuilder, BranchDto[].class).getBody())
                .map(List::of)
                .orElseThrow();
    }
}
