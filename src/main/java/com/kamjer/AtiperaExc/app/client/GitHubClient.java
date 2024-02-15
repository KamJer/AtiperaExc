package com.kamjer.AtiperaExc.app.client;

import com.kamjer.AtiperaExc.app.dto.BranchDto;
import com.kamjer.AtiperaExc.app.dto.RepositoryDto;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Log
@Component
public class GitHubClient {

    private final RestTemplate restTemplate;

    private final String acceptValueGitHubApi;

    private final String githubApiBaseUrl;

    public GitHubClient(RestTemplate restTemplate,
                        @Value("${github.accept.value}") String acceptValueGitHubApi,
                        @Value("${github.api.base-url}") String githubApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.acceptValueGitHubApi = acceptValueGitHubApi;
        this.githubApiBaseUrl = githubApiBaseUrl;
    }

    public ResponseEntity<List<RepositoryDto>> getGitHubRepositories(String token, String owner) {
        //        building url for api
        StringBuilder urlBuilder = new StringBuilder(githubApiBaseUrl);
        urlBuilder.append("/users")
                .append("/")
                .append(owner)
                .append("/repos");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(MediaType.parseMediaTypes(acceptValueGitHubApi));
        if (!token.isEmpty()) {
            headers.setBearerAuth(token);
        }
        RequestEntity<Void> requestEntity = RequestEntity.get(urlBuilder.toString()).headers(headers).build();

        log.info(urlBuilder.toString());
        return restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {} );
    }

    public ResponseEntity<List<BranchDto>> getGitHubBranches(String owner, String repoName) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(githubApiBaseUrl)
                .append("/repos")
                .append("/")
                .append(owner)
                .append("/")
                .append(repoName)
                .append("/branches");

        log.info(urlBuilder.toString());

        return restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {} );
    }
}
