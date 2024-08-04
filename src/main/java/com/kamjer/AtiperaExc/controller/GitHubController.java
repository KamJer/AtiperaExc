package com.kamjer.AtiperaExc.controller;

import com.kamjer.AtiperaExc.model.RepositoryResponse;
import com.kamjer.AtiperaExc.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.service.GitHubService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import reactor.core.publisher.Flux;

import java.util.List;

@Log
@RestController
public class GitHubController {

    private final GitHubService gitHubService;
    private final String acceptedHeaderValue;
    private final String notAcceptableMessage;

    public GitHubController(GitHubService gitHubService,
                            @Value("${accepted.header.value}") String acceptedHeaderValue,
                            @Value("${exception.message.not-acceptable}") String notAcceptableMessage) {
        this.gitHubService = gitHubService;
        this.acceptedHeaderValue = acceptedHeaderValue;
        this.notAcceptableMessage = notAcceptableMessage;
    }

    @GetMapping(path = "/auth/{owner}/repos")
    public Flux<RepositoryResponse> getRepositoryFromOwner(
            @RequestHeader(value = HttpHeaders.ACCEPT) String headerValue,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
            @PathVariable String owner)
            throws ErrorResponseException {
        if (!headerValue.equals(acceptedHeaderValue)) {
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE, notAcceptableMessage);
        }
        return gitHubService.getGitHubRepository(owner, token);
    }

    @GetMapping(path = "/{owner}/repos")
    public Flux<RepositoryResponse> getRepositoryFromOwner(
            @RequestHeader(value = HttpHeaders.ACCEPT) String headerValue,
            @PathVariable String owner)
            throws ErrorResponseException, RestClientException {
        if (!headerValue.equals(acceptedHeaderValue)) {
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE, notAcceptableMessage);
        }
        return gitHubService.getGitHubRepository(owner);
    }
}
