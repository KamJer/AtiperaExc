package com.kamjer.AtiperaExc.app.service;

import com.kamjer.AtiperaExc.app.client.GitHubClient;
import com.kamjer.AtiperaExc.app.dto.BranchDto;
import com.kamjer.AtiperaExc.app.dto.RepositoryDto;
import com.kamjer.AtiperaExc.app.dto.RepositoryResponse;
import lombok.extern.java.Log;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
@Service
public class GitHubService {

    private final GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<RepositoryResponse> getGitHubRepository(String token, String owner) throws RestClientException {
//        fetching necceserry data from response
//        fetching owner and repository name
        ResponseEntity<List<RepositoryDto>> responseRepositories = gitHubClient.getGitHubRepositories(token, owner);

        return Optional.ofNullable(responseRepositories.getBody()).orElse(new ArrayList<>())
                .stream()
                .filter( repositoryDTO -> !repositoryDTO.isFork())
                .map(repositoryDto -> {
                    RepositoryResponse response = new RepositoryResponse();
                    response.setBranchDTOList(getBranchesForRepo(owner, repositoryDto.getName()));
                    return response;
                })
                .toList();
    }

    protected List<BranchDto> getBranchesForRepo(String owner, String repoName) {
        ResponseEntity<List<BranchDto>> branchDtoResponseEntity = gitHubClient.getGitHubBranches(owner, repoName);
        return Optional.ofNullable(branchDtoResponseEntity.getBody()).orElse(new ArrayList<>());
    }
}

