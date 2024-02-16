package com.kamjer.AtiperaExc.app.controller;

import com.kamjer.AtiperaExc.app.dto.RepositoryDto;
import com.kamjer.AtiperaExc.app.dto.RepositoryResponse;
import com.kamjer.AtiperaExc.app.exception.ErrorResponseException;
import com.kamjer.AtiperaExc.app.service.GitHubService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Log
@RestController
public class GitHubController {

    private final GitHubService gitHubService;

    private final String acceptedHeaderValue;
    private final String notAcceptableMessage;

    public GitHubController(GitHubService service, @Value("${accepted.header.value}") String acceptedHeaderValue, @Value("${exception.message.not-acceptable}") String notAcceptableMessage) {
        this.gitHubService = service;
        this.acceptedHeaderValue = acceptedHeaderValue;
        this.notAcceptableMessage = notAcceptableMessage;
    }

    @GetMapping(path = "/auth/{owner}/repos")
        public ResponseEntity<List<RepositoryResponse>> getRepositoryFromOwner(
                @RequestHeader(value = HttpHeaders.ACCEPT) String headerValue,
                @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token,
                @PathVariable String owner)
            throws ErrorResponseException, RestClientException {
        if (!headerValue.equals(acceptedHeaderValue)) {
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE, notAcceptableMessage);
        }
        return new ResponseEntity<>(gitHubService.getGitHubRepository(token, owner), HttpStatus.OK);
    }

    @GetMapping(path = "/{owner}/repos")
    public ResponseEntity<List<RepositoryResponse>> getRepositoryFromOwner(
            @RequestHeader(value = HttpHeaders.ACCEPT) String headerValue,
            @PathVariable String owner)
            throws ErrorResponseException, RestClientException {
        if (!headerValue.equals(acceptedHeaderValue)) {
            throw new ErrorResponseException(HttpStatus.NOT_ACCEPTABLE, notAcceptableMessage);
        }
        return new ResponseEntity<>(gitHubService.getGitHubRepository(owner),HttpStatus.OK);
    }
}
