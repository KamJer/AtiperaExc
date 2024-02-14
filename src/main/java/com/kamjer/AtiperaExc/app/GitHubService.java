package com.kamjer.AtiperaExc.app;

import com.kamjer.AtiperaExc.app.DTO.BranchDTO;
import com.kamjer.AtiperaExc.app.DTO.RepositoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Service
public class GitHubService {

    @Value("${github.api.base-url}")
    private String githubApiBaseUrl;

    @Value("${github.api.base-url}")
    private String notFoundMassage;

    private Logger log = Logger.getLogger(GitHubService.class.getName());


    public ResponseEntity<List<RepositoryDTO>> getGitHubRepository(String owner) throws RestClientException {
//        creating template
        RestTemplate restTemplate = new RestTemplate();
//        building url for api
        StringBuilder bd = new StringBuilder();
        bd.append(githubApiBaseUrl)
                .append("/users")
                .append("/")
                .append(owner)
                .append("/repos");

        log.info(bd.toString());
//        fetching necceserry data from response
//        fetching owner and repository name
        ResponseEntity<List<RepositoryDTO>> responseStringRepositories = restTemplate
                .exchange(bd.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {} );

        List<RepositoryDTO> filteredRepos = Optional.ofNullable(responseStringRepositories.getBody()).orElse(new ArrayList<>())
                .stream()
                .filter(
                        repositoryDTO ->
                                !repositoryDTO.isFork()
                )
                .peek(repositoryDTO -> {
                    repositoryDTO.setBranchDTOList(getBranchesForRepo(owner, repositoryDTO.getName()).getBody());
                })
                .toList();

        return new ResponseEntity<>(filteredRepos, HttpStatus.OK);
    }

    public ResponseEntity<List<BranchDTO>> getBranchesForRepo(String owner, String repoName) {
//        https://api.github.com/repos/KamJer/EndianUtilities/branches
        RestTemplate restTemplate = new RestTemplate();

        StringBuilder bd = new StringBuilder();
        bd.append(githubApiBaseUrl)
                .append("/repos")
                .append("/")
                .append(owner)
                .append("/")
                .append(repoName)
                .append("/branches");

        log.info(bd.toString());

        return restTemplate.exchange(bd.toString(), HttpMethod.GET, null, new ParameterizedTypeReference<>() {} );
    }
}

