package com.kamjer.AtiperaExc.service;

import com.kamjer.AtiperaExc.client.GitHubClient;
import com.kamjer.AtiperaExc.model.BranchDto;
import com.kamjer.AtiperaExc.model.RepositoryDto;
import com.kamjer.AtiperaExc.model.RepositoryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Service
@AllArgsConstructor
public class GitHubService {

    private final GitHubClient gitHubClient;

    public Flux<RepositoryResponse> getGitHubRepository(String owner, String token) throws RestClientException {
        return getGitHubRepositoryToken(owner, Optional.of(token));
    }

    public Flux<RepositoryResponse> getGitHubRepository(String owner) throws RestClientException {
        return getGitHubRepositoryToken(owner, Optional.empty());
    }

    private Flux<RepositoryResponse> getGitHubRepositoryToken(String owner, Optional<String> token) throws RestClientException {
        return gitHubClient.getGitHubRepositories(token, owner)
                .filter( repositoryDto -> !repositoryDto.isFork())
                .map(repositoryDto -> new RepositoryResponse(repositoryDto, getBranchesForRepo(owner, repositoryDto.getName(), token)));
    }

    private Flux<BranchDto> getBranchesForRepo(String owner, String repoName, Optional<String> token) {
        return gitHubClient.getGitHubBranches(owner, repoName, token);
    }
}

