package com.kamjer.AtiperaExc.client;

import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.model.BranchDto;
import com.kamjer.AtiperaExc.model.RepositoryDto;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Component
public class GitHubClient {

    private final RestTemplate restTemplate;

    private final String githubApiBaseUrl;

    public GitHubClient(RestTemplate restTemplate,
                        @Value("${github.api.base-url}") String githubApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.githubApiBaseUrl = githubApiBaseUrl;
    }

    public List<RepositoryDto> getGitHubRepositories(Optional<String> token, String owner) {
        //        building url for api
        StringBuilder urlBuilder = new StringBuilder(githubApiBaseUrl);
        urlBuilder.append("/users")
                .append("/")
                .append(owner)
                .append("/repos");

        HttpHeaders headers = new HttpHeaders();
        token.ifPresent(headers::setBearerAuth);

        RequestEntity<Void> requestEntity = RequestEntity.get(urlBuilder.toString()).headers(headers).build();
        ResponseEntity<List<RepositoryDto>> response = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {} );

        log.info(urlBuilder.toString());

        return Optional.of(response.getBody()).orElse(new ArrayList<>());
    }

    public List<BranchDto> getGitHubBranches(String owner, String repoName) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(githubApiBaseUrl)
                .append("/repos")
                .append("/")
                .append(owner)
                .append("/")
                .append(repoName)
                .append("/branches");
        ResponseEntity<List<BranchDto>> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {} );

        log.info(urlBuilder.toString());

        return Optional.of(response.getBody()).orElse(new ArrayList<>());
    }
}
